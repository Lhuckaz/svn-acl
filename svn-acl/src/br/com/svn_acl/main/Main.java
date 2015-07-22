package br.com.svn_acl.main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.com.svn_acl.gui.SvnAclGUI;

/**
 * 
 * Classe principal
 * 
 * @author Lhuckaz
 *
 */
public class Main {

	public static void main(String[] args) {
		final String[] argumentos = args;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new SvnAclGUI(argumentos);
				}
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao iniciar", "Erro", JOptionPane.ERROR_MESSAGE);
		}

		// Verifica os usuarios no AD apos a tela ter aberto
		boolean verificaUsuariosAD = SvnAclGUI.verificaUsuariosAD();
		// Se nao foi possivel conectar no AD usar os usuarios do arquivo
		// allusers
		if (!verificaUsuariosAD) {
			SvnAclGUI.allUser = SvnAclGUI.addAllUserByFile();
		}

	}
}
