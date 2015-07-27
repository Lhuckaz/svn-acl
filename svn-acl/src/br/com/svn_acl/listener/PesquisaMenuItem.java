package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.com.svn_acl.gui.DirEPermDoGrupoOuUser;
import br.com.svn_acl.gui.GruposDoUser;
import br.com.svn_acl.gui.SvnAclGUI;

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

	private void pesquisaDirEPermDoGrupoOuUser() {
		new DirEPermDoGrupoOuUser(svnAclGUI);
	}

	private void pesquisaGruposDoUser() {
		new GruposDoUser(svnAclGUI);
	}

}
