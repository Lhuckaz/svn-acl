/*
 * ====================================================================
 * Copyright (c) 2004-2010 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package br.com.svn_acl.svn;

import java.io.ByteArrayInputStream;

import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class Commit {

	public Commit() {
		setupLibrary();
	}

	private void commitando(String endereco, String user, String password, String arquivo, byte[] conteudo,
			String commit, boolean begin) throws SVNException {
		/*
		 * URL that points to repository.
		 */
		SVNURL url = SVNURL.parseURIEncoded(endereco);
		/*
		 * Credentials to use for authentication.
		 */
		String userName = user;
		char[] userPassword = password.toCharArray();

		byte[] modifiedContents = conteudo;

		/*
		 * Create an instance of SVNRepository class. This class is the main
		 * entry point for all "low-level" Subversion operations supported by
		 * Subversion protocol.
		 * 
		 * These operations includes browsing, update and commit operations. See
		 * SVNRepository methods javadoc for more details.
		 */
		SVNRepository repository = SVNRepositoryFactory.create(url);

		/*
		 * User's authentication information (name/password) is provided via an
		 * ISVNAuthenticationManager instance. SVNWCUtil creates a default
		 * authentication manager given user's name and password.
		 * 
		 * Default authentication manager first attempts to use provided user
		 * name and password and then falls back to the credentials stored in
		 * the default Subversion credentials storage that is located in
		 * Subversion configuration area. If you'd like to use provided user
		 * name and password only you may use BasicAuthenticationManager class
		 * instead of default authentication manager:
		 * 
		 * authManager = new BasicAuthenticationsManager(userName,
		 * userPassword);
		 * 
		 * You may also skip this point - anonymous access will be used.
		 */
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, userPassword);
		repository.setAuthenticationManager(authManager);

		/*
		 * Get type of the node located at URL we used to create SVNRepository.
		 * 
		 * "" (empty string) is path relative to that URL, -1 is value that may
		 * be used to specify HEAD (latest) revision.
		 */
		SVNNodeKind nodeKind = repository.checkPath("", -1);

		/*
		 * Checks up if the current path really corresponds to a directory. If
		 * it doesn't, the program exits. SVNNodeKind is that one who says what
		 * is located at a path in a revision.
		 */
		if (nodeKind == SVNNodeKind.NONE) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "No entry at URL ''{0}''", url);
			throw new SVNException(err);
		} else if (nodeKind == SVNNodeKind.FILE) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
					"Entry at URL ''{0}'' is a file while directory was expected", url);
			throw new SVNException(err);
		}

		/*
		 * Gets an editor for committing the changes to the repository. NOTE:
		 * you must not invoke methods of the SVNRepository until you close the
		 * editor with the ISVNEditor.closeEdit() method.
		 * 
		 * commitMessage will be applied as a log message of the commit.
		 * 
		 * ISVNWorkspaceMediator instance will be used to store temporary files,
		 * when 'null' is passed, then default system temporary directory will
		 * be used to create temporary files.
		 */
		ISVNEditor editor = repository.getCommitEditor(commit, null);
		
		if (begin) {
			addFile(editor, arquivo, modifiedContents);
		} else {
			modifyFile(editor, arquivo, modifiedContents);
		}
	}

	/*
	 * Initializes the library to work with a repository via different
	 * protocols.
	 */
	private static void setupLibrary() {
		/*
		 * For using over http:// and https://
		 */
		DAVRepositoryFactory.setup();
		/*
		 * For using over svn:// and svn+xxx://
		 */
		SVNRepositoryFactoryImpl.setup();
	
		/*
		 * For using over file:///
		 */
		FSRepositoryFactory.setup();
	}

	/*
	 * This method performs commiting an addition of a file.
	 */
	private static SVNCommitInfo addFile(ISVNEditor editor, String file, byte[] data) throws SVNException {
		/*
		 * Always called first. Opens the current root directory. It means all
		 * modifications will be applied to this directory until a next entry
		 * (located inside the root) is opened/added.
		 * 
		 * -1 - revision is HEAD (actually, for a comit editor this number is
		 * irrelevant)
		 */
		editor.openRoot(-1);
		/*
		 * Adds a new directory (in this case - to the root directory for which
		 * the SVNRepository was created). Since this moment all changes will be
		 * applied to this new directory.
		 * 
		 * dirPath is relative to the root directory.
		 * 
		 * copyFromPath (the 2nd parameter) is set to null and copyFromRevision
		 * (the 3rd) parameter is set to -1 since the directory is not added
		 * with history (is not copied, in other words).
		 */
		// editor.addDir(dirPath, null, -1);
		/*
		 * Adds a new file to the just added directory. The file path is also
		 * defined as relative to the root directory.
		 * 
		 * copyFromPath (the 2nd parameter) is set to null and copyFromRevision
		 * (the 3rd parameter) is set to -1 since the file is not added with
		 * history.
		 */
		editor.addFile(file, null, -1);
		/*
		 * The next steps are directed to applying delta to the file (that is
		 * the full contents of the file in this case).
		 */
		editor.applyTextDelta(file, null);
		/*
		 * Use delta generator utility class to generate and send delta
		 * 
		 * Note that you may use only 'target' data to generate delta when there
		 * is no access to the 'base' (previous) version of the file. However,
		 * using 'base' data will result in smaller network overhead.
		 * 
		 * SVNDeltaGenerator will call editor.textDeltaChunk(...) method for
		 * each generated "diff window" and then editor.textDeltaEnd(...) in the
		 * end of delta transmission. Number of diff windows depends on the file
		 * size.
		 */
		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(file, new ByteArrayInputStream(data), editor, true);

		/*
		 * Closes the new added file.
		 */
		editor.closeFile(file, checksum);
		/*
		 * Closes the root directory.
		 */
		editor.closeDir();
		/*
		 * This is the final point in all editor handling. Only now all that new
		 * information previously described with the editor's methods is sent to
		 * the server for committing. As a result the server sends the new
		 * commit information.
		 */
		return editor.closeEdit();
	}

	/*
	 * This method performs committing file modifications.
	 */
	private static SVNCommitInfo modifyFile(ISVNEditor editor, String filePath, byte[] newData) throws SVNException {
		/*
		 * Always called first. Opens the current root directory. It means all
		 * modifications will be applied to this directory until a next entry
		 * (located inside the root) is opened/added.
		 * 
		 * -1 - revision is HEAD
		 */
		editor.openRoot(-1);
		/*
		 * Opens the file added in the previous commit.
		 * 
		 * filePath is also defined as a relative path to the root directory.
		 */
		try {
			editor.openFile(filePath, -1);
		} catch (SVNException e) {
			throw new SVNCancelException();
		}

		/*
		 * The next steps are directed to applying and writing the file delta.
		 */
		editor.applyTextDelta(filePath, null);

		/*
		 * Use delta generator utility class to generate and send delta
		 * 
		 * Note that you may use only 'target' data to generate delta when there
		 * is no access to the 'base' (previous) version of the file. However,
		 * here we've got 'base' data, what in case of larger files results in
		 * smaller network overhead.
		 * 
		 * SVNDeltaGenerator will call editor.textDeltaChunk(...) method for
		 * each generated "diff window" and then editor.textDeltaEnd(...) in the
		 * end of delta transmission. Number of diff windows depends on the file
		 * size.
		 */
		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(filePath, new ByteArrayInputStream(newData), editor, true);
		/*
		 * Closes the file.
		 */
		editor.closeFile(filePath, checksum);

		/*
		 * Closes the directory.
		 */
		editor.closeDir();

		/*
		 * This is the final point in all editor handling. Only now all that new
		 * information previously described with the editor's methods is sent to
		 * the server for committing. As a result the server sends the new
		 * commit information.
		 */
		return editor.closeEdit();
	}

	public static void main(String[] args) {

		Commit cCommit = new Commit();

		String endereco = "http://mizar/svn/brad2011/dn1/svn_acl";
		String user = "lucas.fernandes";
		String password = "P@ssw0rd1997";
		String arquivo = "svn.acl";
		byte[] conteudo = "teste".getBytes();

		String commit = "Alteracao";

		try {
			cCommit.commitando(endereco, user, password, arquivo, conteudo, commit, false);
		} catch (SVNAuthenticationException e) {
			System.out.println("Usuário ou senha inválidos");
		} catch (SVNCancelException e) {
			System.out.println("Arquivo não existe");
		} catch (SVNException e) {
			System.out.println("URL Invalida");
		} catch (Exception e) {
			System.out.println("Error");
		}

		try {
			// cCommit.commitando(endereco, user, password, arquivo, conteudo, commit, true);
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
}