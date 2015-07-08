package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import br.com.svn_acl.controler.Gerenciador;
import br.com.svn_acl.controler.GerenciadorDeGrupos;
import br.com.svn_acl.controler.GerenciadorDePermissoes;
import br.com.svn_acl.listener.ListaDiretoriosListener;
import br.com.svn_acl.listener.ListaGrupoListener;
import br.com.svn_acl.listener.ListaPermissoesListener;
import br.com.svn_acl.listener.ListaUsuariosListener;
import br.com.svn_acl.listener.ArquivoItemMenuListener;
import br.com.svn_acl.listener.SshItemMenuListener;
import br.com.svn_acl.listener.SubversionItemMenuListener;
import br.com.svn_acl.svn.ActiveDirectory;
import br.com.svn_acl.util.DefineTamanhoJTextField;
import br.com.svn_acl.util.Util;

public class SvnAclGUI {

	private JFrame frame;
	private JTabbedPane tabPainel;
	private Dimension dimensao;

	private JMenuBar jMenuBar;
	private JMenu jMenuArquivos;
	private JMenu jMenuSubversion;
	private JMenu jMenuSsh;
	private JMenuItem jMenuItemAbrir;
	private JMenuItem jMenuItemSalvar;
	private JMenuItem jMenuItemExport;
	private JMenuItem jMenuItemCommit;
	private JMenuItem jMenuItemTransferir;

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

	private JTextField usuarioParaAdicionar;

	private Gerenciador gerenciador;
	private List<String> listarGrupos;
	private List<String> listarDiretorios;
	private List<String> listaUsuariosGrupo;
	private List<String> listaPermissaoDiretorio;

	private List<String> allUser;

	public SvnAclGUI() {
		prepareGUI();
	}

	private void prepareGUI() {
		frame = new JFrame("Lista de Controle de Acesso do Subversion");

		adicionandoIcon();

		adicionaMenu();

		dimensao = new Dimension(600, 550);

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

		adicionarListaDeUsuariosEmGrupos();

		adicionaBotoesEmGrupos();

		adicionarListaDeDiretorios();

		adicionarListaDePermissoesEmDiretorios();

		adicionaOpcoesEmPermissoes();

		adicionaPainelsATabPainel();

		frame.add(tabPainel);

		adicionarEventoFinal();

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		verificaUsuariosAD();
	}

	private boolean carregadoArquivo = false;

	public void carregaArquivo(String arquivo) {
		gerenciador = new Gerenciador(arquivo);

		if (!carregadoArquivo) {
			adicionarGrupos();
			adicionarDiretorios();
			carregadoArquivo = true;
		} else {
			atualizaGrupos();
			atualizaDiretorios();
		}
		jMenuItemCommit.setEnabled(true);
		jMenuItemTransferir.setEnabled(true);
		jMenuItemSalvar.setEnabled(true);
	}

	private void adicionandoIcon() {
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/ico.png"));
		frame.setIconImage(image);
	}

	private void adicionaMenu() {
		ArquivoItemMenuListener arquivoItemMenuListener = new ArquivoItemMenuListener(this);
		SubversionItemMenuListener subversionItemMenuListener = new SubversionItemMenuListener(this);
		SshItemMenuListener sshItemMenuListener = new SshItemMenuListener(this);

		jMenuBar = new JMenuBar();
		jMenuArquivos = new JMenu("Arquivo");
		jMenuItemAbrir = new JMenuItem("Abrir");
		jMenuItemAbrir.addActionListener(arquivoItemMenuListener);
		jMenuItemAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		jMenuItemSalvar = new JMenuItem("Salvar");
		jMenuItemSalvar.addActionListener(arquivoItemMenuListener);
		jMenuItemSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		jMenuItemSalvar.setEnabled(false);
		jMenuArquivos.add(jMenuItemAbrir);
		jMenuArquivos.add(jMenuItemSalvar);

		jMenuSubversion = new JMenu("Subversion");
		jMenuItemExport = new JMenuItem("Export");
		jMenuItemExport.addActionListener(subversionItemMenuListener);
		jMenuItemExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		jMenuItemCommit = new JMenuItem("Commit");
		jMenuItemCommit.addActionListener(subversionItemMenuListener);
		jMenuItemCommit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		jMenuItemCommit.setEnabled(false);
		jMenuSubversion.add(jMenuItemExport);
		jMenuSubversion.add(jMenuItemCommit);

		jMenuSsh = new JMenu("SSH");
		jMenuItemTransferir = new JMenuItem("Transferir");
		jMenuItemTransferir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		jMenuItemTransferir.setEnabled(false);
		jMenuItemTransferir.addActionListener(sshItemMenuListener);
		jMenuSsh.add(jMenuItemTransferir);

		jMenuBar.add(jMenuArquivos);
		jMenuBar.add(jMenuSubversion);
		jMenuBar.add(jMenuSsh);

		frame.setJMenuBar(jMenuBar);
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
		listarGrupos = getGerenciadorDeGrupos().listarGrupos();
		for (String grupos : listarGrupos) {
			((DefaultListModel<String>) listaGrupos.getModel()).addElement(grupos);
		}
	}

	private void adicionaBotoesEmGrupos() {
		JPanel painelBotoesGrupos = new JPanel();
		painelBotoesGrupos.setLayout(new FlowLayout(FlowLayout.RIGHT));
		usuarioParaAdicionar = new JTextField(25);
		usuarioParaAdicionar.setDocument(new DefineTamanhoJTextField(50));
		usuarioParaAdicionar.setPreferredSize(new Dimension(0, 20));
		JButton botaoAdicionar = new JButton("Adicionar");
		botaoAdicionar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getGrupoSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				} else if (usuarioParaAdicionar.getText().trim().replaceAll(" ", "").equals("")) {
					// Evento para deixar a borda vermelha ao usuario nao
					// digitar nenhum valor para adicionar
					Border border = BorderFactory.createLineBorder(Color.RED);
					usuarioParaAdicionar.setBorder(border);
					usuarioParaAdicionar.addFocusListener(new FocusListener() {

						@Override
						public void focusLost(FocusEvent e) {
						}

						@Override
						public void focusGained(FocusEvent e) {
							Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
							usuarioParaAdicionar.setBorder(border);
						}
					});
				} else {
					// Adicionando diversos usuarios separados por ",".
					// Realizando a verificação por cada usuario.
					if (usuarioParaAdicionar.getText().contains(",")) {
						String[] usuarios = usuarioParaAdicionar.getText().replaceAll(" ", "").trim().split(",");
						for (String usuario : usuarios) {
							verificaUsuario(usuario);
						}
					} else {
						verificaUsuario(usuarioParaAdicionar.getText());
					}
				}

			}

			private void verificaUsuario(String usuarioParaAdicionar) {
				boolean usuarioExiste = getGerenciadorDeGrupos().usuarioExiste(usuarioParaAdicionar);
				boolean contains = true;
				if(allUser != null) {
					contains = allUser.contains(usuarioParaAdicionar);
				}
				if (!usuarioExiste || !contains) {
					int confirmar = JOptionPane.showConfirmDialog(getFrame(), "Usuário \"" + usuarioParaAdicionar
							+ "\" ainda nao existe\nDeseja adicionar assim mesmo ?", "Adicionar",
							JOptionPane.YES_NO_OPTION);
					if (confirmar == 0)
						adicionaUsuario(usuarioParaAdicionar);
				} else {
					adicionaUsuario(usuarioParaAdicionar);
				}
			}

			private void adicionaUsuario(String usuarioParaAdicionar) {
				boolean adicionou = getGerenciadorDeGrupos().adicionaUsuarioNoGrupo(getGrupoSelecionado(),
						usuarioParaAdicionar);
				if (adicionou) {
					// JOptionPane.showMessageDialog(getFrame(), "Adicionado " +
					// usuarioParaAdicionar.getText() + " ao grupo " +
					// getGrupoSelecionado(), "Adicionar",
					// JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(getFrame(),
							"Não foi possivel adicionar\nVerifique se o usuário já participa do grupo", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				}
				gerenciador.atualizaArquivo();
				listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
			}
		});
		JButton botaoRemover = new JButton("Remover");
		botaoRemover.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getGrupoSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else if (getUsuarioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um usuário", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else {
					getGerenciadorDeGrupos().removeUsuarioDoGrupo(getGrupoSelecionado(), getUsuarioSelecionado());
					gerenciador.atualizaArquivo();
					listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
				}

			}
		});
		painelBotoesGrupos.add(usuarioParaAdicionar);
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
		listarDiretorios = getGerenciadorDePermissoes().listaDiretorios();
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
					JOptionPane.showMessageDialog(getFrame(), "Selecione um diretório", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				} else {
					AdicionarEmDiretorio dialog = new AdicionarEmDiretorio(SvnAclGUI.this, getDiretorioSelecionado());
					dialog.setVisible(true);
					// Descartando dialog
					gerenciador.atualizaArquivo();
					listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
					dialog = null;
				}

			}
		});
		JButton botaoAlterar = new JButton("Alterar Permissões");
		botaoAlterar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um diretorio", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				} else if (getPermissoesSelecionada().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo ou usuário", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String grupoOuUser = Util.getGrupoOuUser(getPermissoesSelecionada());
					AlteraPermissoes dialog = new AlteraPermissoes(SvnAclGUI.this, getDiretorioSelecionado(),
							grupoOuUser);
					dialog.setVisible(true);
					gerenciador.atualizaArquivo();
					listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
					// Descartando dialog
					dialog = null;
				}
			}
		});
		JButton botaoRemover = new JButton("Remover");
		botaoRemover.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um diretorio", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else if (getPermissoesSelecionada().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo ou usuário", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String grupoOuUser = Util.getGrupoOuUser(getPermissoesSelecionada());
					if (getPermissoesSelecionada().startsWith("@")) {
						getGerenciadorDePermissoes().removeGrupoDoDir(getDiretorioSelecionado(), grupoOuUser);
					} else {
						getGerenciadorDePermissoes().removeUserDoDir(getDiretorioSelecionado(), grupoOuUser);
					}

					gerenciador.atualizaArquivo();
					listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
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

	private void verificaUsuariosAD() {
		ActiveDirectory ad = null;
		String message = "";
		try {
			ad = new ActiveDirectory();
			allUser = ad.allUser();
		} catch (AuthenticationException e) {
			message = "Usuario ou senhas invalidos";
		} catch (ConnectException e) {
			message = "Conexão com AD falhou";
		} catch (FileNotFoundException e) {
			message = "Verifique arquivo system.properties";
		} catch (Exception e) {
			message = "Erro com conexao LDAP";
		} finally {
			if (ad != null) {
				ad.closeLdapConnection();
			}
		}

		if (message != "") {
			JOptionPane.showMessageDialog(null, message);
		}

		// Finalizando
		ad = null;
	}

	public JFrame getFrame() {
		return frame;
	}

	public JMenuItem getJMenuItemAbrir() {
		return jMenuItemAbrir;
	}

	public JMenuItem getJMenuItemSalvar() {
		return jMenuItemSalvar;
	}

	public JMenuItem getJMenuItemExport() {
		return jMenuItemExport;
	}

	public JMenuItem getJMenuItemCommit() {
		return jMenuItemCommit;
	}

	public String getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(String grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public String getDiretorioSelecionado() {
		return diretorioSelecionado;
	}

	public void setDiretorioSelecionado(String diretorioSelecionado) {
		this.diretorioSelecionado = diretorioSelecionado;
	}

	public String getPermissoesSelecionada() {
		return permissoesSelecionada;
	}

	public void setPermissoesSelecionada(String permissoesSelecionada) {
		this.permissoesSelecionada = permissoesSelecionada;
	}

	public String getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(String usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<String> getListarGrupos() {
		return listarGrupos;
	}

	public List<String> getListarDiretorios() {
		return listarDiretorios;
	}

	public GerenciadorDeGrupos getGerenciadorDeGrupos() {
		return gerenciador.getGerenciadorDeGrupos();
	}

	public GerenciadorDePermissoes getGerenciadorDePermissoes() {
		return gerenciador.getGerenciadorDePermissoes();
	}

	public void setUsuariosDoGrupo(List<String> listaUsuariosGrupo) {
		this.listaUsuariosGrupo = listaUsuariosGrupo;
	}

	public void setPermissoesDoDiretorio(List<String> listaPermissaoDiretorio) {
		this.listaPermissaoDiretorio = listaPermissaoDiretorio;
	}

	public void atualizaGrupos() {
		((DefaultListModel<String>) listaGrupos.getModel()).removeAllElements();
		List<String> listarGrupos = getListarGrupos();
		for (String usuarios : listarGrupos) {
			((DefaultListModel<String>) listaGrupos.getModel()).addElement(usuarios);
		}
		// atualiza lista de usuarios se caso mudar de arquivo
		if (listarGrupos.size() != 0)
			listaGrupoListener.atualizaUsuarios(listarGrupos.get(0));
	}

	public void atulizaListaGrupos() {
		listarGrupos = getGerenciadorDeGrupos().listarGrupos();
	}

	public void atualizaDiretorios() {
		((DefaultListModel<String>) listaDiretorios.getModel()).removeAllElements();
		List<String> listarDiretorios = getListarDiretorios();
		for (String diretorios : listarDiretorios) {
			((DefaultListModel<String>) listaDiretorios.getModel()).addElement(diretorios);
		}
		// atualiza lista de permissoes se caso mudar de arquivo
		if (listarDiretorios.size() != 0)
			listaDiretoriosListener.atualizaPermissoes(listarDiretorios.get(0));
	}

	public void atulizaListaDiretorios() {
		listarDiretorios = getGerenciadorDePermissoes().listaDiretorios();
	}

	public void atualizaUsuarios() {
		((DefaultListModel<String>) listaUsuarios.getModel()).removeAllElements();
		for (String usuarios : listaUsuariosGrupo) {
			((DefaultListModel<String>) listaUsuarios.getModel()).addElement(usuarios);
		}
	}

	public void atualizaPermissoes() {
		((DefaultListModel<String>) listaPermissoes.getModel()).removeAllElements();
		for (String usuarios : listaPermissaoDiretorio) {
			((DefaultListModel<String>) listaPermissoes.getModel()).addElement(usuarios);
		}
	}

	private void adicionarEventoFinal() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				// Adicionado if para fechar programa mesmo quando nenhum
				// arquivo for aberto
				if (gerenciador != null)
					gerenciador.apagaArquivosDeGerenciamento();
				System.exit(0);
			}
		});
	}

}
