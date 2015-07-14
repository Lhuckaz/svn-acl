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

import br.com.svn_acl.gui.AdConfigura;
import br.com.svn_acl.util.Util;

/**
 * Classe responsável por acessar o AD
 * 
 * @author Lhuckaz
 *
 */
public class ActiveDirectory {

	private DirContext dirContext;
	private String domainBase;
	SearchControls searchCtls;
	private final String filter = "(&(objectclass=user)(objectcategory=person))";
	private String[] returnAttributes = { "sAMAccountName" };
	private boolean comErros;

	/**
	 * 
	 * @return retorna se existiu erro na conexão com AD
	 */
	public boolean isComErros() {
		return comErros;
	}

	/**
	 * 
	 * Construtor da classe {@link ActiveDirectory}
	 * 
	 * @throws NamingException
	 *             erro
	 * @throws ConnectException
	 *             conexão não efetuada
	 * @throws FileNotFoundException
	 *             arquivo properties não encontrado
	 */
	public ActiveDirectory() throws NamingException, ConnectException, FileNotFoundException {
		Properties propertiesSystem = new Properties();
		try {
			propertiesSystem.load(new FileInputStream(Util.ARQUIVO_PROPERTIES));
		} catch (Exception e) {
			throw new FileNotFoundException();
		}
		String domainController = propertiesSystem.getProperty("domain.ldap");
		String username = propertiesSystem.getProperty("username.ldap");
		String password = AdConfigura.recuperaSenha();

		if (domainController.equals("") || username.equals("") || password.equals("")) {
			throw new NullPointerException();
		}

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

	/**
	 * 
	 * Realiza a conexão e retorna todos os usuarios da LDAP no AD
	 * 
	 * @return retorna todos os usuários do LDAP
	 * @throws ConnectException
	 *             erro de conexão
	 */
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

	/**
	 * 
	 * Retorna nome do domínio formatado <i>DC=dominio,DC=corporacao</i>
	 * 
	 * @param base
	 *            nome do domínio
	 * @return retorna string com o nome do domínio formatado
	 */
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

	/**
	 * Fechar conexao com LDAP
	 */
	public void closeLdapConnection() {
		try {
			if (dirContext != null)
				dirContext.close();
		} catch (NamingException e) {
		}
	}

	/**
	 * 
	 * Faz comparação com os usuários do AD e verifica se existe
	 * 
	 * @param usuario
	 *            nome do usuário
	 * @return retorna <code>true</code> se usuário existe no AD
	 */
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
}
