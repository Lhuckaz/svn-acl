package br.com.svn_acl.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.com.svn_acl.gui.SvnAclGUI;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new SvnAclGUI();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean verificaUsuariosAD = SvnAclGUI.verificaUsuariosAD();
		if (!verificaUsuariosAD) {
			SvnAclGUI.allUser = SvnAclGUI.addAllUserByFile();
		}

	}
}
