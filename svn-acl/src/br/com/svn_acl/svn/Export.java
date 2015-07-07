package br.com.svn_acl.svn;

import java.io.File;

import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import br.com.svn_acl.util.Util;

public class Export {

	public boolean exportando(String url, String user, String password) throws SVNException {
		
		String destPath = Util.getNomeArquivoURL(url);

		SVNRepository repository = null;

		// initiate the reporitory from the url
		repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
		// create authentication data
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user,
				password.toCharArray());
		repository.setAuthenticationManager(authManager);

		SVNNodeKind nodeKind = null;

		try {
			nodeKind = repository.checkPath("", -1);
		} catch (SVNAuthenticationException e) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "User or password invalid", url);
			throw new SVNAuthenticationException(err);
		}

		if (nodeKind == SVNNodeKind.DIR) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
					"Entry at URL ''{0}'' is a directory while file was expected", url);
			throw new SVNException(err);
		}

		// need to identify latest revision
		long latestRevision = repository.getLatestRevision();

		// create client manager and set authentication
		SVNClientManager ourClientManager = SVNClientManager.newInstance();
		ourClientManager.setAuthenticationManager(authManager);
		// use SVNUpdateClient to do the export
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		updateClient.setIgnoreExternals(false);
		// TODO Arquivo baixado ira ser renomeado caso exista
		updateClient.doExport(repository.getLocation(), new File(destPath), SVNRevision.create(latestRevision),
				SVNRevision.create(latestRevision), null, true, SVNDepth.INFINITY);
		return true;

	}

	public static void main(String[] args) {
		String url = "http://mizar/svn/brad2011/dn1/svn_acl/svn.acl";
		String user = "lucas.fernandes";
		String password = "P@ssw0rd1997";
		try {
			new Export().exportando(url, user, password);
		} catch (SVNAuthenticationException e) {
			System.out.println("Usuario ou senha invalidos");
		} catch (SVNException e) {
			System.out.println("URL Invalida, passe o caminho de um arquivo");
		} catch (Exception e) {
			System.out.println("Error");
		}
	}

}
