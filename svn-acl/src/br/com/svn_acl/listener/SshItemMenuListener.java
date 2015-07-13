package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.com.svn_acl.gui.SshGui;
import br.com.svn_acl.gui.SvnAclGUI;

public class SshItemMenuListener implements ActionListener {

	private SvnAclGUI svnAclGUI;

	public SshItemMenuListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object open = e.getSource();
		// MenuItem importar
		if (open == svnAclGUI.getJMenuItemImportar()) {
			importar();
		}

		// MenuItem exportar
		if (open == svnAclGUI.getJMenuItemTransferir()) {
			exportar();
		}
	}

	private void importar() {
		new SshGui(svnAclGUI, "Importar");
	}

	private void exportar() {
		new SshGui(svnAclGUI, "Transferir");
	}

}
