package br.com.svn_acl.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdicionarEmDiretorio extends JDialog implements ActionListener {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;

	public AdicionarEmDiretorio(SvnAclGUI owner) {
		super(owner.getFrame(), "Adicionar em Diretorio", true);
		JPanel painelAdiciona = new JPanel(new GridLayout(2, 1));
		JPanel painelCheckUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel painelOpcoes = new JPanel(new FlowLayout());

		final JLabel grupo = new JLabel("Grupo");
		Vector<String> listarGrupos = new Vector<String>(owner.getGerenciadorDeGrupos().listarGrupos());
		final JComboBox<String> comboGrupos = new JComboBox<>(listarGrupos);

		final JLabel usuario = new JLabel("Usuario");
		Vector<String> listaUsuarios = new Vector<String>(owner.getGerenciadorDeGrupos().listarUsuarios());
		final JComboBox<String> comboUsuarios = new JComboBox<>(listaUsuarios);

		String[] permissoes = { "LEITURA", "LEITURA\\ESCRITA", "ESCRITA" };
		final JComboBox<String> comboPermissoes = new JComboBox<>(permissoes);

		final JButton botaoAdiciona = new JButton("Adicionar");
		botaoAdiciona.addActionListener(this);

		final JLabel jLabelPermissoes = new JLabel("Permissões");

		JCheckBox jCheckBoxUsuario = new JCheckBox("Adicionar Usuario");
		jCheckBoxUsuario.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					adicionaUsuario();
				} else {
					adicionaGrupo();
				}
			}

			private int adicionaUsuario() {
				painelOpcoes.setVisible(false);
				painelOpcoes.removeAll();
				painelOpcoes.add(usuario);
				painelOpcoes.add(comboUsuarios);
				painelOpcoes.add(jLabelPermissoes);
				painelOpcoes.add(comboPermissoes);
				painelOpcoes.add(botaoAdiciona);
				painelOpcoes.setVisible(true);
				return 0;
			}

			private int adicionaGrupo() {
				painelOpcoes.setVisible(false);
				painelOpcoes.removeAll();
				painelOpcoes.add(grupo);
				painelOpcoes.add(comboGrupos);
				painelOpcoes.add(jLabelPermissoes);
				painelOpcoes.add(comboPermissoes);
				painelOpcoes.add(botaoAdiciona);
				painelOpcoes.setVisible(true);
				return 0;
			}
		});

		painelCheckUsuario.add(jCheckBoxUsuario);
		painelOpcoes.add(grupo);
		painelOpcoes.add(comboGrupos);
		painelOpcoes.add(jLabelPermissoes);
		painelOpcoes.add(comboPermissoes);
		painelOpcoes.add(botaoAdiciona);
		painelAdiciona.add(painelCheckUsuario);
		painelAdiciona.add(painelOpcoes);
		getContentPane().add(painelAdiciona);
		pack();
		setLocationRelativeTo(owner.getFrame());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		setVisible(false);
	}

}
