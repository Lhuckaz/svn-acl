package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class AdicionarUsuarioEmLotes extends JDialog implements ListSelectionListener {

	private ArrayList<String> usuarios;
	protected boolean add;
	private SvnAclGUI svnAclGUI;

	public AdicionarUsuarioEmLotes(SvnAclGUI svnAclGUI) {
		super(svnAclGUI.getFrame());
		this.svnAclGUI = svnAclGUI;
		this.setModal(true);
		Dimension dimensao = new Dimension(300, 300);

		JPanel jPanelPrincipalListGrupos = new JPanel(new BorderLayout());
		JPanel jPanelDeUsuarios = new JPanel();
		DefaultListModel<String> modeloUsuariosLotes = new DefaultListModel<>();
		JList<String> listaDeUsuarios = new JList<>(modeloUsuariosLotes);
		listaDeUsuarios.addListSelectionListener(this);
		JScrollPane jScrollDeUsuarios = new JScrollPane(listaDeUsuarios);
		// Definir tamanho to JScrollPane
		jScrollDeUsuarios.setPreferredSize(dimensao);
		jPanelDeUsuarios.add(jScrollDeUsuarios);
		jPanelPrincipalListGrupos.add(jPanelDeUsuarios);

		JPanel jPanelDeBotoes = new JPanel();
		JButton button = new JButton("Adicionar");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				add = true;
				setVisible(false);
			}
		});
		jPanelDeBotoes.add(button);
		jPanelPrincipalListGrupos.add(jPanelDeBotoes, BorderLayout.SOUTH);

		carregaListaDeUsuarios(modeloUsuariosLotes);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().add(jPanelPrincipalListGrupos);
		pack();
		setLocationRelativeTo(svnAclGUI.getFrame());
		setModal(true);
		setVisible(true);
	}

	private void carregaListaDeUsuarios(DefaultListModel<String> modeloUsuariosLotes) {
		// Se caso nao existir usuarios no AD adicionar ja existentes
		if (SvnAclGUI.allUser.size() != 0) {
			ArrayList<String> todosUsuarios = (ArrayList<String>) SvnAclGUI.allUser;
			Collections.sort(todosUsuarios, String.CASE_INSENSITIVE_ORDER);
			for (String usuarios : todosUsuarios) {
				modeloUsuariosLotes.addElement(usuarios);
			}
		} else {
			ArrayList<String> todosUsuarios = (ArrayList<String>) svnAclGUI.getGerenciadorDeGrupos().listarUsuarios();
			Collections.sort(todosUsuarios, String.CASE_INSENSITIVE_ORDER);
			for (String usuarios : todosUsuarios) {
				modeloUsuariosLotes.addElement(usuarios);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		JList<?> lista = (JList<?>) e.getSource();
		if ((String) lista.getSelectedValue() != null) {
			@SuppressWarnings("unchecked")
			ArrayList<String> usuariosSelecionados = (ArrayList<String>) lista.getSelectedValuesList();
			usuarios = new ArrayList<>();
			usuarios.addAll(usuariosSelecionados);
		}
	}

	public ArrayList<String> getUsuariosSelecionados() {
		return usuarios;

	}
}