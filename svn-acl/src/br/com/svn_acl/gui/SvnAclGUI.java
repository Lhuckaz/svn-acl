package br.com.svn_acl.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;

import br.com.svn_acl.controler.GerenciadorDeGrupos;
import br.com.svn_acl.controler.GerenciadorDePermissoes;
import br.com.svn_acl.listener.ListaDiretoriosListener;
import br.com.svn_acl.listener.ListaGrupoListener;

public class SvnAclGUI {

	private JFrame frame;
	private JTabbedPane tabPainel;

	private JPanel jPanelPrincipalGrupos;
	private JPanel jPanelPrincipalListGrupos;
	private JPanel jPanelPrincipalPermissoes;

	private JList<String> listaGrupos;
	private DefaultListModel<String> modeloGrupos;
	private JScrollPane jScrollGrupos;
	private JPanel jPanelGrupos;

	private JList<String> listaUsuarios;
	private DefaultListModel<String> modeloUsuarios;
	private JScrollPane jScrollUsuarios;
	private JPanel jPanelUsuarios;

	private JPanel jPanelPrincipalListPermissoes;

	private JList<String> listaDiretorios;
	private DefaultListModel<String> modeloDiretorios;
	private JScrollPane jScrollDiretorios;
	private JPanel jPanelDiretorios;

	private JList<String> listaPermissoes;
	private DefaultListModel<String> modeloPermissoes;
	private JScrollPane jScrollPermissoes;
	private JPanel jPanelPermissoes;

	private ListaGrupoListener listaGrupoListener;
	private ListaDiretoriosListener listaDiretoriosListener;

	private String grupoSelecionado;
	private String diretorioSelecionado;

	private GerenciadorDeGrupos gerenciadorDeGrupos;
	private GerenciadorDePermissoes gerenciadorDePermissoes;
	private List<String> listaUsuariosGrupo;
	private List<String> listaPermissaoDiretorio;

	public SvnAclGUI() {
		gerenciadorDeGrupos = new GerenciadorDeGrupos("svn.acl");
		gerenciadorDePermissoes = new GerenciadorDePermissoes("svn.acl");
		prepareGUI();
	}

	private void prepareGUI() {
		frame = new JFrame("Lista de Controle de Acesso do Subversion");
		tabPainel = new JTabbedPane();
		jPanelPrincipalGrupos = new JPanel(new BorderLayout());
		jPanelPrincipalPermissoes = new JPanel(new BorderLayout());

		listaGrupoListener = new ListaGrupoListener(this);
		listaDiretoriosListener = new ListaDiretoriosListener(this);

		jPanelPrincipalListGrupos = new JPanel();
		jPanelPrincipalListGrupos.setLayout(new GridLayout(1, 2));

		jPanelPrincipalListPermissoes = new JPanel();
		jPanelPrincipalListPermissoes.setLayout(new GridLayout(1, 2));

		adicionarListaDeGrupos();

		adicionarGrupos();

		adicionarListaDeUsuariosEmGrupos();

		adicionaBotoesEmGrupos();

		adicionarListaDeDiretorios();

		adicionarDiretorios();

		adicionarListaDePermissoesEmDiretorios();

		adicionaPainelsATabPainel();

		frame.add(tabPainel);

		// TODO frame.setPreferredSize(new Dimension(750, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void adicionaPainelsATabPainel() {
		jPanelPrincipalGrupos.add(jPanelPrincipalListGrupos);
		tabPainel.addTab("Grupos", jPanelPrincipalGrupos);
		jPanelPrincipalPermissoes.add(jPanelPrincipalListPermissoes);
		tabPainel.addTab("Permiss�es", jPanelPrincipalPermissoes);
	}

	private void adicionaBotoesEmGrupos() {
		JPanel painelBotoesGrupos = new JPanel();
		painelBotoesGrupos.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton botaoRemover = new JButton("Remover");
		JButton botaoAdicionar = new JButton("Adicionar");
		painelBotoesGrupos.add(botaoAdicionar);
		painelBotoesGrupos.add(botaoRemover);
		jPanelPrincipalGrupos.add(painelBotoesGrupos, BorderLayout.SOUTH);
	}

	private void adicionarListaDeGrupos() {
		jPanelGrupos = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		modeloGrupos = new DefaultListModel<>();
		listaGrupos = new JList<>(modeloGrupos);
		listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaGrupos.addListSelectionListener(listaGrupoListener);
		jScrollGrupos = new JScrollPane(listaGrupos);
		// Definir tamanho to JScrollPane
		jScrollGrupos.setPreferredSize(new Dimension(250, 250));
		jPanelGrupos.add(jScrollGrupos);
		jPanelPrincipalListGrupos.add(jPanelGrupos);
	}

	private void adicionarListaDeUsuariosEmGrupos() {
		jPanelUsuarios = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modeloUsuarios = new DefaultListModel<>();
		listaUsuarios = new JList<>(modeloUsuarios);
		listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jScrollUsuarios = new JScrollPane(listaUsuarios);
		// Definir tamanho to JScrollPane
		jScrollUsuarios.setPreferredSize(new Dimension(250, 250));
		jPanelUsuarios.add(jScrollUsuarios);
		jPanelPrincipalListGrupos.add(jPanelUsuarios);
	}

	private void adicionarGrupos() {
		List<String> listarGrupos = gerenciadorDeGrupos.listarGrupos();
		for (String usuarios : listarGrupos) {
			((DefaultListModel<String>) listaGrupos.getModel()).addElement(usuarios);
		}
	}

	private void adicionarListaDeDiretorios() {
		jPanelDiretorios = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		modeloDiretorios = new DefaultListModel<>();
		listaDiretorios = new JList<>(modeloDiretorios);
		listaDiretorios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaDiretorios.addListSelectionListener(listaDiretoriosListener);
		jScrollDiretorios = new JScrollPane(listaDiretorios);
		// Definir tamanho to JScrollPane
		jScrollDiretorios.setPreferredSize(new Dimension(250, 250));
		jPanelDiretorios.add(jScrollDiretorios);
		jPanelPrincipalListPermissoes.add(jPanelDiretorios);
	}

	private void adicionarListaDePermissoesEmDiretorios() {
		jPanelPermissoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modeloPermissoes = new DefaultListModel<>();
		listaPermissoes = new JList<>(modeloPermissoes);
		listaPermissoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jScrollPermissoes = new JScrollPane(listaPermissoes);
		// Definir tamanho to JScrollPane
		jScrollPermissoes.setPreferredSize(new Dimension(250, 250));
		jPanelPermissoes.add(jScrollPermissoes);
		jPanelPrincipalListPermissoes.add(jPanelPermissoes);
	}

	private void adicionarDiretorios() {
		List<String> listarDiretorios = gerenciadorDePermissoes.listaDiretorios();
		for (String diretorios : listarDiretorios) {
			((DefaultListModel<String>) listaDiretorios.getModel()).addElement(diretorios);
		}
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

	public GerenciadorDePermissoes getGerenciadorDePermissoes() {
		return gerenciadorDePermissoes;
	}

	public void setDiretorioSelecionado(String diretorioSelecionado) {
		this.diretorioSelecionado = diretorioSelecionado;
	}

	public void setPermissoesDoDiretorio(List<String> listaPermissaoDiretorio) {
		this.listaPermissaoDiretorio = listaPermissaoDiretorio;
	}

	public void atualizaPermissoes() {
		((DefaultListModel<String>) listaPermissoes.getModel()).removeAllElements();
		for (String usuarios : listaPermissaoDiretorio) {
			((DefaultListModel<String>) listaPermissoes.getModel()).addElement(usuarios);
		}
	}

}
