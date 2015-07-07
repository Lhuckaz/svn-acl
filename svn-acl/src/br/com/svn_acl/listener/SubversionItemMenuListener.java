package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.com.svn_acl.gui.SubversionArquivo;
import br.com.svn_acl.gui.SvnAclGUI;

public class SubversionItemMenuListener implements ActionListener {

	private SvnAclGUI svnAclGUI;
	private SubversionArquivo subversionArquivo;

	public SubversionItemMenuListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object open = e.getSource();
		// MenuItem exportar
		if (open == svnAclGUI.getJMenuItemExport()) {
			export();
		}

		// MenuItem commit
		if (open == svnAclGUI.getJMenuItemCommit()) {
			commit();
		}
	}

	private void export() {
		subversionArquivo = new SubversionArquivo(svnAclGUI, "Exportar");
		subversionArquivo.setVisible(true);
	}

	private void commit() {
		subversionArquivo = new SubversionArquivo(svnAclGUI, "Commit");
		subversionArquivo.setVisible(true);
	}

}
