package br.com.svn_acl.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;

import br.com.svn_acl.controler.GerenciadorDeGrupos;
import br.com.svn_acl.listener.ListaGrupoListener;

public class SvnAclGUI {

	private JFrame frame;
	private JPanel jPanelPrincipal;

	private JList<String> listaGrupos;
	private DefaultListModel<String> modeloGrupos;
	private JScrollPane jScrollGrupos;
	private JPanel jPanelGrupos;

	private JList<String> listaUsuarios;
	private DefaultListModel<String> modeloUsuarios;
	private JScrollPane jScrollUsuarios;
	private JPanel jPanelUsuarios;

	ListaGrupoListener listaGrupoListener;

	private String grupoSelecionado;

	GerenciadorDeGrupos gerenciadorDeGrupos;
	private List<String> listaUsuariosGrupo;

	public SvnAclGUI() {
		gerenciadorDeGrupos = new GerenciadorDeGrupos("svn.acl");
		prepareGUI();
	}

	private void prepareGUI() {
		frame = new JFrame("Lista de Controle de Acesso do Subversion");

		listaGrupoListener = new ListaGrupoListener(this);

		jPanelPrincipal = new JPanel();
		jPanelPrincipal.setLayout(new GridLayout(1, 2));

		adicionarListaDeGrupos();

		adicionarGrupos();

		adicionarListaDeUsuarios();

		frame.add(jPanelPrincipal);

		// TODO frame.setPreferredSize(new Dimension(750, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void adicionarGrupos() {
		List<String> listarGrupos = gerenciadorDeGrupos.listarGrupos();
		for (String usuarios : listarGrupos) {
			((DefaultListModel<String>) listaGrupos.getModel()).addElement(usuarios);
		}
	}

	private void adicionarListaDeGrupos() {
		jPanelGrupos = new JPanel(new FlowLayout(FlowLayout.CENTER));
		modeloGrupos = new DefaultListModel<>();
		listaGrupos = new JList<>(modeloGrupos);
		listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaGrupos.addListSelectionListener(listaGrupoListener);
		jScrollGrupos = new JScrollPane(listaGrupos);
		// Definir tamanho to JScrollPane
		jScrollGrupos.setPreferredSize(new Dimension(250, 250));
		jPanelGrupos.add(jScrollGrupos);
		jPanelPrincipal.add(jPanelGrupos);
	}

	private void adicionarListaDeUsuarios() {
		jPanelUsuarios = new JPanel(new FlowLayout(FlowLayout.CENTER));
		modeloUsuarios = new DefaultListModel<>();
		listaUsuarios = new JList<>(modeloUsuarios);
		listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jScrollUsuarios = new JScrollPane(listaUsuarios);
		// Definir tamanho to JScrollPane
		jScrollUsuarios.setPreferredSize(new Dimension(250, 250));
		jPanelUsuarios.add(jScrollUsuarios);
		jPanelPrincipal.add(jPanelUsuarios);
	}

	public GerenciadorDeGrupos getGerenciadorDeGrupos() {
		return gerenciadorDeGrupos;
	}

	public void setGrupoSelecionado(String grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public void setUsuariosDoGrupo(List<String> listaUsuariosGrupo) {
		this.listaUsuariosGrupo = listaUsuariosGrupo;
	}

	public void atualizaUsuarios() {
		((DefaultListModel<String>) listaUsuarios.getModel()).removeAllElements();
		for (String usuarios : listaUsuariosGrupo) {
			((DefaultListModel<String>) listaUsuarios.getModel()).addElement(usuarios);
		}
	}

}
