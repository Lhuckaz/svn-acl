package br.com.svn_acl.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DirEPermDoGrupoOuUser extends JDialog implements ActionListener {

	private JCheckBox jCheckBoxUsuario;
	private JComboBox<String> comboGrupos;
	private JComboBox<String> comboUsuarios;

	public DirEPermDoGrupoOuUser(final SvnAclGUI svnAclGUI) {
		super(svnAclGUI.getFrame(), "Diretórios e permissões do grupo ou usuário", true);

		JPanel painelAdiciona = new JPanel(new GridLayout(2, 1));
		JPanel painelCheckUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel painelOpcoes = new JPanel(new FlowLayout());

		final JLabel grupo = new JLabel("Grupo: ");
		Vector<String> listarGrupos = new Vector<String>(svnAclGUI.getGerenciadorDeGrupos().listarGrupos());
		comboGrupos = new JComboBox<>(listarGrupos);
		comboGrupos.addActionListener(this);

		final JLabel usuario = new JLabel("Usuario: ");
		Vector<String> listaUsuarios = new Vector<String>(svnAclGUI.getGerenciadorDeGrupos().listarUsuarios());
		comboUsuarios = new JComboBox<>(listaUsuarios);
		comboUsuarios.addActionListener(this);

		jCheckBoxUsuario = new JCheckBox("Usuario");
		jCheckBoxUsuario.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					adicionaUsuario();
				} else {
					adicionaGrupo();
				}
			}

			/**
			 * 
			 * Adiciona usuário no {@link javax.swing.JList JList} da interface
			 * principal {@link br.com.svn_acl.gui.SvnAclGUI SvnAclGUI}
			 * 
			 * @return retorna algum valor int para o método
			 *         {@link ActionListener#itemStateChanged itemStateChanged}
			 */
			private int adicionaUsuario() {
				painelOpcoes.setVisible(false);
				painelOpcoes.removeAll();
				painelOpcoes.add(usuario);
				painelOpcoes.add(comboUsuarios);
				painelOpcoes.setVisible(true);
				return 0;
			}

			/**
			 * 
			 * Adiciona grupo no {@link javax.swing.JList JList} da interface
			 * principal {@link br.com.svn_acl.gui.SvnAclGUI SvnAclGUI}
			 * 
			 * @return retorna algum valor int para o método
			 *         {@link ActionListener#itemStateChanged itemStateChanged}
			 */
			private int adicionaGrupo() {
				painelOpcoes.setVisible(false);
				painelOpcoes.removeAll();
				painelOpcoes.add(grupo);
				painelOpcoes.add(comboGrupos);
				painelOpcoes.setVisible(true);
				return 0;
			}
		});
		
		DefaultListModel<String> modeloGrupos = new DefaultListModel<>();
		final JList<String> listaGrupos = new JList<>(modeloGrupos);

		painelCheckUsuario.add(jCheckBoxUsuario);
		painelOpcoes.add(grupo);
		painelOpcoes.add(comboGrupos);
		painelAdiciona.add(painelCheckUsuario);
		painelAdiciona.add(painelOpcoes);

		getContentPane().add(painelAdiciona);
		pack();
		setLocationRelativeTo(svnAclGUI.getFrame());
		setModal(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!jCheckBoxUsuario.isSelected()) {
			String grupoSelecionado = (String) comboGrupos.getSelectedItem();
			System.out.println(grupoSelecionado);
		} else {
			String usuarioSelecionado = (String) comboUsuarios.getSelectedItem();
			System.out.println(usuarioSelecionado);
		}
	}
}
