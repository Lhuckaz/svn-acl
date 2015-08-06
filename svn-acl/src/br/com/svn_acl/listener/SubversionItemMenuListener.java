package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import br.com.svn_acl.gui.SubversionArquivo;
import br.com.svn_acl.gui.SvnAclGUI;

/**
 * 
 * Classe ouvinte {@link JMenuItem} "Checkout" e "Commit" do {@link JMenu}
 * "Subversion" da classe {@link SvnAclGUI} para abrir a classe
 * {@link SubversionArquivo} com determinadas configurações
 * 
 * @author Lhuckaz
 *
 */
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
		if (open == svnAclGUI.getJMenuItemCheckout()) {
			export();
		}

		// MenuItem commit
		if (open == svnAclGUI.getJMenuItemCommit()) {
			commit();
		}
	}

	/**
	 * Abre a classe {@link SubversionArquivo} com o titulo "Checkout"
	 */
	private void export() {
		subversionArquivo = new SubversionArquivo(svnAclGUI, "Checkout");
		subversionArquivo.setVisible(true);
	}

	/**
	 * Abre a classe {@link SubversionArquivo} com o titulo "Commit"
	 */
	private void commit() {
		subversionArquivo = new SubversionArquivo(svnAclGUI, "Commit");
		subversionArquivo.setVisible(true);
	}

}
