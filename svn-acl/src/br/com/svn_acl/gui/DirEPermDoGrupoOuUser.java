package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.util.Util;

@SuppressWarnings("serial")
public class DirEPermDoGrupoOuUser extends JDialog implements ActionListener {

	private SvnAclGUI svnAclGUI;
	private JCheckBox jCheckBoxUsuario;
	private JComboBox<String> comboGrupos;
	private JComboBox<String> comboUsuarios;
	private JList<String> listaGrupos;
	private JList<String> listaPermissoes;
	private JPanel painelOpcoes;
	private JLabel grupo;
	private JLabel usuario;

	public DirEPermDoGrupoOuUser(SvnAclGUI svnAclGUI) {
		super(svnAclGUI.getFrame(), "Diretórios e permissões do grupo ou usuário", true);
		this.svnAclGUI = svnAclGUI;

		JPanel principal = new JPanel(new BorderLayout());

		JPanel painelSeleciona = new JPanel(new FlowLayout(FlowLayout.LEFT));
		painelOpcoes = new JPanel(new FlowLayout());

		grupo = new JLabel("Grupo: ");
		Vector<String> listarGrupos = new Vector<String>(svnAclGUI.getGerenciadorDeGrupos().listarGrupos());
		comboGrupos = new JComboBox<>(listarGrupos);
		comboGrupos.addActionListener(this);

		usuario = new JLabel("Usuário: ");
		Vector<String> listaUsuarios = new Vector<String>(svnAclGUI.getGerenciadorDeGrupos().listarUsuarios());
		comboUsuarios = new JComboBox<>(listaUsuarios);
		comboUsuarios.addActionListener(this);

		jCheckBoxUsuario = new JCheckBox("Usuário");
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

		JPanel painelListas = new JPanel(new FlowLayout());
		DefaultListModel<String> modeloGrupos = new DefaultListModel<>();
		listaGrupos = new JList<>(modeloGrupos);
		listaGrupos.addListSelectionListener(new ListaGrupos());
		listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		DefaultListModel<String> modeloPermissoes = new DefaultListModel<>();
		listaPermissoes = new JList<>(modeloPermissoes);
		listaPermissoes.addListSelectionListener(new ListaPermissoes());
		listaPermissoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane jScrollPaneGrupos = new JScrollPane(listaGrupos);
		JScrollPane jScrollPanePermissoes = new JScrollPane(listaPermissoes);
		Dimension dimension = new Dimension(300, 200);
		jScrollPaneGrupos.setPreferredSize(dimension);
		jScrollPanePermissoes.setPreferredSize(dimension);
		// Sincronizar os JScrollPanes
		jScrollPaneGrupos.getVerticalScrollBar().setModel(jScrollPanePermissoes.getVerticalScrollBar().getModel());
		// Retira a Scroll do segundo JList
		// jScrollPanePermissoes.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		painelListas.add(jScrollPaneGrupos);
		painelListas.add(jScrollPanePermissoes);

		painelOpcoes.add(grupo);
		painelOpcoes.add(comboGrupos);
		painelSeleciona.add(jCheckBoxUsuario);
		painelSeleciona.add(painelOpcoes);

		principal.add(painelSeleciona);
		principal.add(painelListas, BorderLayout.SOUTH);

		getContentPane().add(principal);
		pack();
		setLocationRelativeTo(svnAclGUI.getFrame());
		setModal(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!jCheckBoxUsuario.isSelected()) {
			String grupoSelecionado = (String) comboGrupos.getSelectedItem();
			atualizaListas(grupoSelecionado);
		} else {
			String usuarioSelecionado = (String) comboUsuarios.getSelectedItem();
			atualizaListas(usuarioSelecionado);
		}
	}

	void atualizaListas(String selecionado) {
		((DefaultListModel<String>) listaGrupos.getModel()).removeAllElements();
		((DefaultListModel<String>) listaPermissoes.getModel()).removeAllElements();

		Map<String, String> lista = svnAclGUI.getGerenciadorDePermissoes()
				.listaQuaisDiretoriosUmGrupoOuUserTemAcessoEQuaisPermissoes(selecionado);
		for (Map.Entry<String, String> diretorios : lista.entrySet()) {
			String diretorio = diretorios.getKey();
			((DefaultListModel<String>) listaGrupos.getModel()).addElement(diretorio);
			String permissao = diretorios.getValue();
			((DefaultListModel<String>) listaPermissoes.getModel()).addElement(Util.getPermissaoNomeadas(permissao));
		}
	}

	private class ListaGrupos implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return;
			listaPermissoes.setSelectedIndex(listaGrupos.getSelectedIndex());
		}
	}

	private class ListaPermissoes implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return;
			listaGrupos.setSelectedIndex(listaPermissoes.getSelectedIndex());
		}
	}
}
