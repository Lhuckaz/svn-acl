package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import br.com.svn_acl.gui.SshGUI;
import br.com.svn_acl.gui.SvnAclGUI;

/**
 * 
 * Classe ouvinte do {@link JMenuItem} "Importar" e "Transferir" do
 * {@link JMenu} "SSH" da classe {@link SvnAclGUI} para abrir a classe
 * {@link SshGUI} com determinadas configurações
 * 
 * @author Lhuckaz
 *
 */
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

	/**
	 * Abre a classe {@link SshGUI} com o titulo "Importar"
	 */
	private void importar() {
		new SshGUI(svnAclGUI, "Importar");
	}

	/**
	 * Abre a classe {@link SshGUI} com o titulo "Transferir"
	 */
	private void exportar() {
		new SshGUI(svnAclGUI, "Transferir");
	}

}
