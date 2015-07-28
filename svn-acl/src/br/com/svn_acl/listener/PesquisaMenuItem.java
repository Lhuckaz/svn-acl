package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import br.com.svn_acl.gui.DirEPermDoGrupoOuUser;
import br.com.svn_acl.gui.GruposDoUser;
import br.com.svn_acl.gui.SvnAclGUI;

/**
 * 
 * Classe ouvinte dos {@link javax.swing.JMenuItem JMenuItem}
 * "Grupos do usuário" e
 * "Exibe os diretórios e as permissões do grupo ou usuário" da classe
 * {@link SvnAclGUI}
 * 
 * @author Lhuckaz
 *
 */
public class PesquisaMenuItem implements ActionListener {

	private SvnAclGUI svnAclGUI;

	public PesquisaMenuItem(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object open = e.getSource();

		// MenuItem grupos do usuarios
		if (open == svnAclGUI.getJMenuItemGruposDoUser()) {
			pesquisaGruposDoUser();
		}

		// MenuItem grupos do usuarios
		if (open == svnAclGUI.getjMenuItemPermDoGrupo()) {
			pesquisaDirEPermDoGrupoOuUser();
		}
	}

	/**
	 * Abrir o {@link JDialog} da classe {@link DirEPermDoGrupoOuUser}
	 */
	private void pesquisaDirEPermDoGrupoOuUser() {
		new DirEPermDoGrupoOuUser(svnAclGUI);
	}

	/**
	 * Abrir o {@link JDialog} da classe {@link GruposDoUser}
	 */
	private void pesquisaGruposDoUser() {
		new GruposDoUser(svnAclGUI);
	}

}
