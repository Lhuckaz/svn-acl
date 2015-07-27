package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class GruposDoUser extends JDialog {

	public GruposDoUser(final SvnAclGUI svnAclGUI) {
		super(svnAclGUI.getFrame(), "Grupos do usuário", true);

		JPanel painelGruposDoUser = new JPanel(new BorderLayout());

		JPanel painelComboBox = new JPanel();
		final JLabel usuario = new JLabel("Usuarios: ");
		Vector<String> listaUsuarios = null;
		try {
			listaUsuarios = new Vector<String>(svnAclGUI.getGerenciadorDeGrupos().listarUsuarios());
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(svnAclGUI.getFrame(), "Sem grupos", "Sem grupos",
					JOptionPane.INFORMATION_MESSAGE);
		}
		
		DefaultListModel<String> modeloGrupos = new DefaultListModel<>();
		final JList<String> listaGrupos = new JList<>(modeloGrupos);

		final JComboBox<String> comboUsuarios = new JComboBox<>(listaUsuarios);
		comboUsuarios.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				((DefaultListModel<String>) listaGrupos.getModel()).removeAllElements();
				List<String> listaGruposDoUsuario = svnAclGUI.getGerenciadorDeGrupos().listaGruposDoUsuario(
						(String) comboUsuarios.getSelectedItem());
				for (String usuarios : listaGruposDoUsuario) {
					((DefaultListModel<String>) listaGrupos.getModel()).addElement(usuarios);
				}
			}
		});

		JScrollPane jScrollPane = new JScrollPane(listaGrupos);
		jScrollPane.setPreferredSize(new Dimension(300, 300));

		painelComboBox.add(usuario);
		painelComboBox.add(comboUsuarios);
		painelGruposDoUser.add(painelComboBox, BorderLayout.NORTH);
		painelGruposDoUser.add(jScrollPane);

		getContentPane().add(painelGruposDoUser);
		pack();
		setLocationRelativeTo(svnAclGUI.getFrame());
		setModal(true);
		setVisible(true);
	}
}
