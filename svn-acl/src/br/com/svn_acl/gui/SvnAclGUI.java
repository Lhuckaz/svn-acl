package br.com.svn_acl.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;

import br.com.svn_acl.controler.GerenciadorDeGrupos;
import br.com.svn_acl.controler.GerenciadorDePermissoes;
import br.com.svn_acl.listener.ListaDiretoriosListener;
import br.com.svn_acl.listener.ListaGrupoListener;
import br.com.svn_acl.listener.ListaPermissoesListener;
import br.com.svn_acl.listener.ListaUsuariosListener;

public class SvnAclGUI {

	private JFrame frame;
	private JTabbedPane tabPainel;
	private Dimension dimensao;

	private JPanel jPanelPrincipalGrupos;
	private JPanel jPanelPrincipalListGrupos;

	private JList<String> listaGrupos;
	private DefaultListModel<String> modeloGrupos;
	private JScrollPane jScrollGrupos;
	private JPanel jPanelGrupos;

	private JList<String> listaUsuarios;
	private DefaultListModel<String> modeloUsuarios;
	private JScrollPane jScrollUsuarios;
	private JPanel jPanelUsuarios;

	private JPanel jPanelPrincipalPermissoes;
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
	private ListaUsuariosListener listaUsuariosListener;
	private ListaDiretoriosListener listaDiretoriosListener;
	private ListaPermissoesListener listaPermissoesListener;

	private String grupoSelecionado = "";
	private String usuarioSelecionado = "";
	private String diretorioSelecionado = "";
	private String permissoesSelecionada = "";

	private GerenciadorDeGrupos gerenciadorDeGrupos;
	private GerenciadorDePermissoes gerenciadorDePermissoes;
	private List<String> listaUsuariosGrupo;
	private List<String> listaPermissaoDiretorio;

	public SvnAclGUI() {
		prepareGUI();
	}

	private void prepareGUI() {
		frame = new JFrame("Lista de Controle de Acesso do Subversion");

		gerenciadorDeGrupos = new GerenciadorDeGrupos("svn.acl");
		gerenciadorDePermissoes = new GerenciadorDePermissoes("svn.acl");
		dimensao = new Dimension(300, 250);

		tabPainel = new JTabbedPane();
		jPanelPrincipalGrupos = new JPanel(new BorderLayout());
		jPanelPrincipalPermissoes = new JPanel(new BorderLayout());

		listaGrupoListener = new ListaGrupoListener(this);
		listaUsuariosListener = new ListaUsuariosListener(this);
		listaDiretoriosListener = new ListaDiretoriosListener(this);
		listaPermissoesListener = new ListaPermissoesListener(this);

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

		adicionaOpcoesEmPermissoes();

		adicionaPainelsATabPainel();

		frame.add(tabPainel);

		// TODO frame.setPreferredSize(new Dimension(750, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void adicionarListaDeGrupos() {
		jPanelGrupos = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		modeloGrupos = new DefaultListModel<>();
		listaGrupos = new JList<>(modeloGrupos);
		listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaGrupos.addListSelectionListener(listaGrupoListener);
		jScrollGrupos = new JScrollPane(listaGrupos);
		// Definir tamanho to JScrollPane
		jScrollGrupos.setPreferredSize(dimensao);
		jPanelGrupos.add(jScrollGrupos);
		jPanelPrincipalListGrupos.add(jPanelGrupos);
	}

	private void adicionarListaDeUsuariosEmGrupos() {
		jPanelUsuarios = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modeloUsuarios = new DefaultListModel<>();
		listaUsuarios = new JList<>(modeloUsuarios);
		listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUsuarios.addListSelectionListener(listaUsuariosListener);
		jScrollUsuarios = new JScrollPane(listaUsuarios);
		// Definir tamanho to JScrollPane
		jScrollUsuarios.setPreferredSize(dimensao);
		jPanelUsuarios.add(jScrollUsuarios);
		jPanelPrincipalListGrupos.add(jPanelUsuarios);
	}

	private void adicionarGrupos() {
		List<String> listarGrupos = gerenciadorDeGrupos.listarGrupos();
		for (String usuarios : listarGrupos) {
			((DefaultListModel<String>) listaGrupos.getModel()).addElement(usuarios);
		}
	}

	private void adicionaBotoesEmGrupos() {
		JPanel painelBotoesGrupos = new JPanel();
		painelBotoesGrupos.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton botaoAdicionar = new JButton("Adicionar");
		botaoAdicionar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getGrupoSelecionado().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um grupo", "Adicionar", JOptionPane.ERROR_MESSAGE);
				} else {
					//gerenciadorDeGrupos.adicionaUsuarioNoGrupo(getGrupoSelecionado(), getUsuarioSelecionado());
					atualizaUsuarios();
				}

			}
		});
		JButton botaoRemover = new JButton("Remover");
		botaoRemover.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getGrupoSelecionado().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um grupo", "Remover", JOptionPane.ERROR_MESSAGE);
				} else if (getUsuarioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um usuário", "Remover", JOptionPane.ERROR_MESSAGE);
				} else {
					gerenciadorDeGrupos.removeUsuarioDoGrupo(getGrupoSelecionado(), getUsuarioSelecionado());
					atualizaUsuarios();
				}

			}
		});
		painelBotoesGrupos.add(botaoAdicionar);
		painelBotoesGrupos.add(botaoRemover);
		jPanelPrincipalGrupos.add(painelBotoesGrupos, BorderLayout.SOUTH);
	}

	private void adicionarListaDeDiretorios() {
		jPanelDiretorios = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		modeloDiretorios = new DefaultListModel<>();
		listaDiretorios = new JList<>(modeloDiretorios);
		listaDiretorios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaDiretorios.addListSelectionListener(listaDiretoriosListener);
		jScrollDiretorios = new JScrollPane(listaDiretorios);
		// Definir tamanho to JScrollPane
		jScrollDiretorios.setPreferredSize(dimensao);
		jPanelDiretorios.add(jScrollDiretorios);
		jPanelPrincipalListPermissoes.add(jPanelDiretorios);
	}

	private void adicionarListaDePermissoesEmDiretorios() {
		jPanelPermissoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modeloPermissoes = new DefaultListModel<>();
		listaPermissoes = new JList<>(modeloPermissoes);
		listaPermissoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaPermissoes.addListSelectionListener(listaPermissoesListener);
		jScrollPermissoes = new JScrollPane(listaPermissoes);
		// Definir tamanho to JScrollPane
		jScrollPermissoes.setPreferredSize(dimensao);
		jPanelPermissoes.add(jScrollPermissoes);
		jPanelPrincipalListPermissoes.add(jPanelPermissoes);
	}

	private void adicionarDiretorios() {
		List<String> listarDiretorios = gerenciadorDePermissoes.listaDiretorios();
		for (String diretorios : listarDiretorios) {
			((DefaultListModel<String>) listaDiretorios.getModel()).addElement(diretorios);
		}
	}

	private void adicionaOpcoesEmPermissoes() {
		JPanel painelComboBoxPermissoes = new JPanel();
		painelComboBoxPermissoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton botaoAdicionar = new JButton("Adicionar");
		botaoAdicionar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um diretório", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				} else {
					AdicionarEmDiretorio dialog = new AdicionarEmDiretorio(SvnAclGUI.this, gerenciadorDeGrupos);
					dialog.setModal(true);
					dialog.setVisible(true);
				}

			}
		});
		JButton botaoAlterar = new JButton("Alterar Permissões");
		botaoAlterar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um diretorio", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				} else if (getPermissoesSelecionada().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um grupo ou usuário", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				} else {
					AlteraPermissoes dialog = new AlteraPermissoes(SvnAclGUI.this, gerenciadorDeGrupos);
					dialog.setModal(true);
					dialog.setVisible(true);
				}
			}
		});
		JButton botaoRemover = new JButton("Remover");
		botaoRemover.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um diretorio", "Remover", JOptionPane.ERROR_MESSAGE);
				} else if (getPermissoesSelecionada().equals("")) {
					JOptionPane.showMessageDialog(null, "Selecione um grupo ou usuário", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else {
					// TODO Excluir um grupo ou diretorio
				}
			}
		});
		painelComboBoxPermissoes.add(botaoAdicionar);
		painelComboBoxPermissoes.add(botaoAlterar);
		painelComboBoxPermissoes.add(botaoRemover);
		jPanelPrincipalPermissoes.add(painelComboBoxPermissoes, BorderLayout.SOUTH);
	}

	private void adicionaPainelsATabPainel() {
		jPanelPrincipalGrupos.add(jPanelPrincipalListGrupos);
		tabPainel.addTab("Grupos", jPanelPrincipalGrupos);
		jPanelPrincipalPermissoes.add(jPanelPrincipalListPermissoes);
		tabPainel.addTab("Permissões", jPanelPrincipalPermissoes);
	}

	public JFrame getFrame() {
		return frame;
	}

	public String getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public String getDiretorioSelecionado() {
		return diretorioSelecionado;
	}

	public String getPermissoesSelecionada() {
		return permissoesSelecionada;
	}

	public String getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(String usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
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

	public void setPermissoesSelecionada(String permissoesSelecionada) {
		this.permissoesSelecionada = permissoesSelecionada;
	}

	public void atualizaPermissoes() {
		((DefaultListModel<String>) listaPermissoes.getModel()).removeAllElements();
		for (String usuarios : listaPermissaoDiretorio) {
			((DefaultListModel<String>) listaPermissoes.getModel()).addElement(usuarios);
		}
	}

}
