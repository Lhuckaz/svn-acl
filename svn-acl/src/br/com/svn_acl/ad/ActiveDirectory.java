package br.com.svn_acl.ad;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class ActiveDirectory {

	private DirContext dirContext;
	private String domainBase;
	SearchControls searchCtls;
	private final String filter = "(&(objectclass=user)(objectcategory=person))";
	private String[] returnAttributes = { "sAMAccountName" };
	private boolean comErros;

	public ActiveDirectory() throws NamingException, ConnectException, FileNotFoundException {
		Properties propertiesSystem = new Properties();
		try {
			propertiesSystem.load(new FileInputStream("system.properties"));
		} catch (Exception e) {
			throw new FileNotFoundException();
		}
		String domainController = propertiesSystem.getProperty("domain.ldap");
		String username = propertiesSystem.getProperty("username.ldap");
		String password = propertiesSystem.getProperty("password.ldap");

		Properties properties = new Properties();

		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.PROVIDER_URL, "LDAP://" + domainController);
		properties.put(Context.SECURITY_PRINCIPAL, username + "@" + domainController);
		properties.put(Context.SECURITY_CREDENTIALS, password);

		// initializing active directory LDAP connection
		try {
			dirContext = new InitialDirContext(properties);
		} catch (CommunicationException e) {
			throw new ConnectException();
		} catch (AuthenticationException e) {
			throw new AuthenticationException();
		}

		// default domain base for search
		domainBase = getDomainBase(domainController);

		// initializing search controls
		searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		searchCtls.setReturningAttributes(returnAttributes);
	}

	public List<String> allUser() throws ConnectException {
		NamingEnumeration<SearchResult> result = null;
		try {
			result = this.dirContext.search(domainBase, filter, this.searchCtls);
		} catch (NamingException e) {
			throw new ConnectException();
		}
		ArrayList<String> allUsers = new ArrayList<>();
		try {
			while (result.hasMore()) {
				SearchResult rs = (SearchResult) result.next();
				Attributes attrs = rs.getAttributes();
				String temp = attrs.get("samaccountname").toString();
				String user = temp.substring(temp.indexOf(":") + 1).trim();
				allUsers.add(user);
			}
		} catch (NamingException e) {
			comErros = true;
		}
		return allUsers;
	}

	private static String getDomainBase(String base) {
		char[] namePair = base.toUpperCase().toCharArray();
		String dn = "DC=";
		for (int i = 0; i < namePair.length; i++) {
			if (namePair[i] == '.') {
				dn += ",DC=" + namePair[++i];
			} else {
				dn += namePair[i];
			}
		}
		return dn;
	}

	public void closeLdapConnection() {
		try {
			if (dirContext != null)
				dirContext.close();
		} catch (NamingException e) {
		}
	}

	public boolean existeUsuario(String usuario) {
		List<String> allUser;
		try {
			allUser = this.allUser();
		} catch (ConnectException e) {
			return false;
		}
		if (allUser != null) {
			if (allUser.contains(usuario)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		List<String> allUser = null;
		ActiveDirectory ad = null;
		try {
			ad = new ActiveDirectory();
			allUser = ad.allUser();
			System.out.println(ad.existeUsuario("lucas.fernandes"));
		} catch (AuthenticationException e) {
			System.out.println("Usuario ou senhas do AD inválidos");
		} catch (ConnectException e) {
			System.out.println("Conexao falhou");
		} catch (FileNotFoundException e) {
			System.out.println("Verifique arquivo");
		} catch (NamingException e) {
			System.out.println("Erro");
		} finally {
			if (ad != null) {
				ad.closeLdapConnection();
			}
		}
		if (allUser != null) {
			for (String user : allUser) {
				System.out.println(user);
			}
			System.out.println();
			if (ad.comErros) {
				System.out.println("Com erros");
			}
		}
	}
}
