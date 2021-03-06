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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.swing.Action;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import br.com.svn_acl.action.RedoAction;
import br.com.svn_acl.action.UndoAction;
import br.com.svn_acl.ad.ActiveDirectory;
import br.com.svn_acl.controler.Gerenciador;
import br.com.svn_acl.controler.GerenciadorDeGrupos;
import br.com.svn_acl.controler.GerenciadorDePermissoes;
import br.com.svn_acl.listener.AdItemMenuListener;
import br.com.svn_acl.listener.ArquivoItemMenuListener;
import br.com.svn_acl.listener.ListaDiretoriosListener;
import br.com.svn_acl.listener.ListaGrupoListener;
import br.com.svn_acl.listener.ListaPermissoesListener;
import br.com.svn_acl.listener.ListaUsuariosListener;
import br.com.svn_acl.listener.PesquisaMenuItem;
import br.com.svn_acl.listener.SshItemMenuListener;
import br.com.svn_acl.listener.SubversionItemMenuListener;
import br.com.svn_acl.util.DocumentTamanhoJTextField;
import br.com.svn_acl.util.NaturalOrderComparatorStringInsensitive;
import br.com.svn_acl.util.Util;

/**
 * 
 * Interface gr�fica principal, composta de {@link JFrame}, exibe na
 * {@link JTabbedPane} "Grupos" os grupos e usu�rios e em "Permiss�es" os
 * diret�rios e as permiss�es dos grupos e usu�rios
 * 
 * @author Lhuckaz
 *
 */
public class SvnAclGUI {

	private JFrame frame;
	private JTabbedPane tabPainel;
	private Dimension dimensao;

	private JMenuBar jMenuBar;
	private JMenu jMenuArquivos;
	private JMenu jMenuSubversion;
	private JMenu jMenuSsh;
	private JMenu jMenuAd;
	private JMenu jMenuPesquisa;
	private JMenuItem jMenuItemNovo;
	private JMenuItem jMenuItemAbrir;
	private JMenuItem jMenuItemSalvar;
	private JMenuItem jMenuItemSair;
	private JMenuItem jMenuItemCheckout;
	private JMenuItem jMenuItemCommit;
	private JMenuItem jMenuItemImportar;
	private JMenuItem jMenuItemTransferir;
	private JMenuItem jMenuItemAdSettings;
	private JMenuItem jMenuItemGruposDoUser;
	private JMenuItem jMenuItemPermDoGrupo;

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
	// Remover usu�rios em lote
	private ArrayList<String> usuariosSelecionados;
	private String diretorioSelecionado = "";
	private String permissoesSelecionada = "";

	private JTextField usuarioParaAdicionar;

	private Gerenciador gerenciador;
	private List<String> listarGrupos;
	private List<String> listarDiretorios;
	private List<String> listaUsuariosGrupo;
	private List<String> listaPermissaoDiretorio;

	private JButton botaoRemGroup;
	private JButton botaoAddGroup;
	private JButton botaoAdicionarUserLotes;
	private JButton botaoRemoverUser;
	private JButton botaoRemoverUserAllGroups;
	private JButton botaoAdicionarUser;
	private JButton botaoAddDir;
	private JButton botaoRemDir;
	private JButton botaoAlterar;
	private JButton botaoAdicionar;
	private JButton botaoRemover;

	public static List<String> allUser;
	// Ao iniciar com true para poder fechar o arquivo sem mensagem de
	// fechamento quando ainda nao se abriu nenhum arquivo
	public static boolean arquivoSalvo = true;

	private static UndoAction undo;
	private static RedoAction redo;

	public SvnAclGUI(String... argumentos) {
		String arquivo = null;
		try {
			arquivo = argumentos[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			// abrir arquivo vazio
		}
		prepareGUI();
		carregaArquivo(arquivo);
	}

	/**
	 * Monta a interface gr�fica
	 */
	private void prepareGUI() {
		frame = new JFrame("Lista de Controle de Acesso do Subversion");

		adicionandoIcon();

		adicionaMenu();

		redimensionando();

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

		adicionaAcoesUndoRedo();

		frame.add(tabPainel);

		adicionarEventoFinal();

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		// Tela iniciada maximilizada
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Iniciado na classe Main
		// verificaUsuariosAD();
	}

	private boolean carregadoArquivo = false;

	/**
	 * 
	 * M�todo principal para carregar o arquivo que ser� apresentado pelo
	 * programa
	 * 
	 * @param arquivo
	 *            nome do arquivo a carregar
	 */
	public void carregaArquivo(String arquivo) {
		// Nao executar quando parametro for nulo
		if (arquivo != null) {
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

			botaoRemGroup.setEnabled(true);
			botaoAddGroup.setEnabled(true);
			botaoAdicionarUserLotes.setEnabled(true);
			botaoRemoverUser.setEnabled(true);
			botaoRemoverUserAllGroups.setEnabled(true);
			botaoAdicionarUser.setEnabled(true);
			botaoAddDir.setEnabled(true);
			botaoRemDir.setEnabled(true);
			botaoAlterar.setEnabled(true);
			botaoAdicionar.setEnabled(true);
			botaoRemover.setEnabled(true);
			jMenuItemPermDoGrupo.setEnabled(true);
			jMenuItemGruposDoUser.setEnabled(true);
			// Abrir, Checkout, Importar e altera��es
			arquivoSalvo = false;

			atualizaGrupos();
			atualizaDiretorios();
		}
	}

	/**
	 * Adiciona um icone a janela
	 */
	private void adicionandoIcon() {
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/ico.png"));
		frame.setIconImage(image);
	}

	/**
	 * Adiciona o {@link JMenuBar}
	 */
	private void adicionaMenu() {
		ArquivoItemMenuListener arquivoItemMenuListener = new ArquivoItemMenuListener(this);
		SubversionItemMenuListener subversionItemMenuListener = new SubversionItemMenuListener(this);
		SshItemMenuListener sshItemMenuListener = new SshItemMenuListener(this);
		AdItemMenuListener adItemMenuListener = new AdItemMenuListener(this);
		PesquisaMenuItem pesquisaMenuItem = new PesquisaMenuItem(this);

		jMenuBar = new JMenuBar();
		jMenuArquivos = new JMenu("Arquivo");
		jMenuItemNovo = new JMenuItem("Novo");
		jMenuItemNovo.addActionListener(arquivoItemMenuListener);
		jMenuItemNovo.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemAbrir = new JMenuItem("Abrir");
		jMenuItemAbrir.addActionListener(arquivoItemMenuListener);
		jMenuItemAbrir.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemSalvar = new JMenuItem("Salvar");
		jMenuItemSalvar.addActionListener(arquivoItemMenuListener);
		jMenuItemSalvar.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemSalvar.setEnabled(false);
		jMenuItemSair = new JMenuItem("Sair");
		jMenuItemSair.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO (Repeat WindowListener) Quando arquivo for modificado ou
				// aberto por
				// algum meio ela dar um alerta se deseja realmente fechar
				boolean arquivoSalvo = SvnAclGUI.arquivoSalvo;
				if (arquivoSalvo) {
					apagaArquivosEFecha();
				} else {
					if (JOptionPane.showConfirmDialog(frame, "Deseja sair ?\nAltera��es n�o salvas ser�o perdidas!",
							"Saindo", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						apagaArquivosEFecha();
					}
				}
			}

			/**
			 * Apaga e fecha o programa
			 */
			private void apagaArquivosEFecha() {
				if (gerenciador != null)
					gerenciador.apagaArquivosDeGerenciamento();
				System.exit(0);

			}

		});
		jMenuItemSair.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuArquivos.add(jMenuItemNovo);
		jMenuArquivos.add(jMenuItemAbrir);
		jMenuArquivos.add(jMenuItemSalvar);
		jMenuArquivos.addSeparator();
		jMenuArquivos.add(jMenuItemSair);

		jMenuSubversion = new JMenu("Subversion");
		jMenuItemCheckout = new JMenuItem("Checkout");
		jMenuItemCheckout.addActionListener(subversionItemMenuListener);
		jMenuItemCheckout.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemCommit = new JMenuItem("Commit");
		jMenuItemCommit.addActionListener(subversionItemMenuListener);
		jMenuItemCommit.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemCommit.setEnabled(false);
		jMenuSubversion.add(jMenuItemCheckout);
		jMenuSubversion.add(jMenuItemCommit);

		jMenuSsh = new JMenu("SSH");
		jMenuItemImportar = new JMenuItem("Importar");
		jMenuItemImportar.addActionListener(sshItemMenuListener);
		jMenuItemImportar.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemTransferir = new JMenuItem("Transferir");
		jMenuItemTransferir.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemTransferir.setEnabled(false);
		jMenuItemTransferir.addActionListener(sshItemMenuListener);
		jMenuSsh.add(jMenuItemImportar);
		jMenuSsh.add(jMenuItemTransferir);

		jMenuAd = new JMenu("AD");
		jMenuItemAdSettings = new JMenuItem("Configurar LDAP");
		jMenuItemAdSettings.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemAdSettings.addActionListener(adItemMenuListener);
		jMenuAd.add(jMenuItemAdSettings);

		jMenuPesquisa = new JMenu("Pesquisa");
		jMenuItemGruposDoUser = new JMenuItem("Grupos do usu�rio");
		jMenuItemGruposDoUser.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_G, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemGruposDoUser.setToolTipText("Exibe os grupos que o usu�rio participa");
		jMenuItemGruposDoUser.addActionListener(pesquisaMenuItem);
		jMenuItemGruposDoUser.setEnabled(false);
		jMenuItemPermDoGrupo = new JMenuItem("Diret�rios e permiss�es do grupo ou usu�rio");
		jMenuItemPermDoGrupo.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_U, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		jMenuItemPermDoGrupo.setToolTipText("Exibe os diret�rios e as permiss�es do grupo ou usu�rio");
		jMenuItemPermDoGrupo.addActionListener(pesquisaMenuItem);
		jMenuItemPermDoGrupo.setEnabled(false);
		jMenuPesquisa.add(jMenuItemGruposDoUser);
		jMenuPesquisa.add(jMenuItemPermDoGrupo);

		jMenuBar.add(jMenuArquivos);
		jMenuBar.add(jMenuSubversion);
		jMenuBar.add(jMenuSsh);
		jMenuBar.add(jMenuAd);
		jMenuBar.add(jMenuPesquisa);

		frame.setJMenuBar(jMenuBar);
	}

	/**
	 * Redimenciona componentes da tela para diferentes resolu��es
	 */
	private void redimensionando() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dimensao = new Dimension(650, 600);

		if (screenSize.width <= 800) {
			dimensao.setSize(380, 450);
		} else if (screenSize.width <= 1024) {
			dimensao.setSize(500, 600);
		} else if (screenSize.width <= 1280) {
			dimensao.setSize(620, 850);
		} else if (screenSize.width >= 1920) {
			dimensao.setSize(950, 850);
		}
	}

	/**
	 * Adiciona a lista de grupos
	 */
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

	/**
	 * Adiciona lista de usu�rios em Grupos
	 */
	private void adicionarListaDeUsuariosEmGrupos() {
		jPanelUsuarios = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modeloUsuarios = new DefaultListModel<>();
		listaUsuarios = new JList<>(modeloUsuarios);
		// Remover em lote v.2.0
		// listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUsuarios.addListSelectionListener(listaUsuariosListener);
		jScrollUsuarios = new JScrollPane(listaUsuarios);
		// Definir tamanho to JScrollPane
		jScrollUsuarios.setPreferredSize(dimensao);
		jPanelUsuarios.add(jScrollUsuarios);
		jPanelPrincipalListGrupos.add(jPanelUsuarios);
	}

	/**
	 * Adiciona os grupos
	 */
	private void adicionarGrupos() {
		listarGrupos = getGerenciadorDeGrupos().listarGrupos();
		for (String grupos : listarGrupos) {
			((DefaultListModel<String>) listaGrupos.getModel()).addElement(grupos);
		}
	}

	/**
	 * Adiciona {@link JButton}'s em {@link JTabbedPane} "Grupos"
	 */
	private void adicionaBotoesEmGrupos() {
		// Painel dos bot�es
		JPanel painelBotoesGrupos = new JPanel(new BorderLayout());

		// Painel dos bot�es gerenciadores de Grupos
		JPanel painelBotoesGruposGerGrupos = new JPanel();
		painelBotoesGruposGerGrupos.setLayout(new FlowLayout(FlowLayout.LEFT));
		botaoAddGroup = new JButton("+");
		botaoAddGroup.setEnabled(false);
		botaoAddGroup.setToolTipText("Adicionar Grupo");
		botaoAddGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String grupo = JOptionPane.showInputDialog(SvnAclGUI.this.getFrame(), "Grupo", "Adicionar Grupo",
						JOptionPane.PLAIN_MESSAGE);
				try {
					boolean adicionaGrupo = getGerenciadorDeGrupos().adicionaGrupo(Util.removeSinaisDiacriticos(grupo));
					if (adicionaGrupo) {
						gerenciador.atualizaArquivo();
						listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
					} else {
						JOptionPane
								.showMessageDialog(SvnAclGUI.this.getFrame(),
										"N�o foi poss�vel adicionar grupo: " + "\"" + grupo
												+ "\"\nVerfique se o grupo j� existe",
										"Erro", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NullPointerException ex) {
					return;
				}

			}
		});
		botaoRemGroup = new JButton("-");
		botaoRemGroup.setToolTipText("Remover Grupo");
		botaoRemGroup.setEnabled(false);
		botaoRemGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getGrupoSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else {
					getGerenciadorDeGrupos().removeGrupoEPermissoes(getGrupoSelecionado());
					gerenciador.atualizaArquivo();
					listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
					setGrupoSelecionado("");
					atualizaDiretorios();
				}
			}
		});

		// Painel dos bot�es gerenciadores de Usu�rios
		JPanel painelBotoesGruposGerUsers = new JPanel();
		painelBotoesGruposGerUsers.setLayout(new FlowLayout(FlowLayout.RIGHT));
		usuarioParaAdicionar = new JTextField(25);
		usuarioParaAdicionar.setDocument(new DocumentTamanhoJTextField(50));
		usuarioParaAdicionar.setPreferredSize(new Dimension(0, 20));
		botaoAdicionarUser = new JButton("Adicionar");
		botaoAdicionarUser.setEnabled(false);
		botaoAdicionarUser.addActionListener(new ActionListener() {

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
					// Realizando a verifica��o por cada usuario.
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

			// FIXME Metodo funcional, porem confuso
			private void verificaUsuario(String usuarioParaAdicionar) {
				boolean usuarioExiste = getGerenciadorDeGrupos().usuarioExiste(usuarioParaAdicionar);
				boolean contains = true;
				boolean allUserEquals0 = false;
				if (allUser != null) {
					contains = allUser.contains(usuarioParaAdicionar);
					allUserEquals0 = allUser.size() == 0;
				} else {
					allUserEquals0 = true;
				}
				// Verifica se usuario existe no contexto atual
				if (allUserEquals0) {
					if (!usuarioExiste) {
						if (allUserEquals0) {
							if (!usuarioExiste) {
								int confirmar = JOptionPane.showConfirmDialog(getFrame(),
										"Usu�rio \"" + usuarioParaAdicionar
												+ "\" ainda nao existe\nDeseja adicionar assim mesmo ?",
										"Adicionar", JOptionPane.YES_NO_OPTION);
								if (confirmar == 0)
									adicionaUsuario(usuarioParaAdicionar);
								return;
							} else {
								adicionaUsuario(usuarioParaAdicionar);
								return;
							}
						}
						if (contains) {
							int confirmar = JOptionPane.showConfirmDialog(getFrame(),
									"Usu�rio \"" + usuarioParaAdicionar
											+ "\" ainda nao existe\nUsuario existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						} else {
							int confirmar = JOptionPane.showConfirmDialog(getFrame(),
									"Usu�rio \"" + usuarioParaAdicionar
											+ "\" ainda nao existe\nUsuario N�O existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						}
					} else {
						adicionaUsuario(usuarioParaAdicionar);
					}
				} else {
					if (!usuarioExiste) {
						if (contains) {
							int confirmar = JOptionPane.showConfirmDialog(getFrame(),
									"Usu�rio \"" + usuarioParaAdicionar
											+ "\" ainda nao existe\nUsuario existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						} else {
							int confirmar = JOptionPane.showConfirmDialog(getFrame(),
									"Usu�rio \"" + usuarioParaAdicionar
											+ "\" ainda nao existe\nUsuario N�O existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						}
					} else {
						if (contains) {
							adicionaUsuario(usuarioParaAdicionar);
						} else {
							int confirmar = JOptionPane.showConfirmDialog(getFrame(),
									"Usu�rio: \"" + usuarioParaAdicionar
											+ "\" N�O existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						}
					}
				}
			}

			/**
			 * 
			 * Adiciona o usu�rio ao {@link JList JList}
			 * 
			 * @param usuarioParaAdicionar
			 *            nome do usu�rio
			 */
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
							"N�o foi possivel adicionar\nVerifique se o usu�rio j� participa do grupo", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				}
				gerenciador.atualizaArquivo();
				listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
			}
		});
		botaoAdicionarUserLotes = new JButton("Adicionar usu�rios em lotes");
		botaoAdicionarUserLotes.setEnabled(false);
		botaoAdicionarUserLotes.addActionListener(new ActionListener() {

			StringBuilder naoAdicionados;
			boolean contenNaoAdicionados;

			@Override
			public void actionPerformed(ActionEvent e) {
				naoAdicionados = new StringBuilder();
				if (getGrupoSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo", "Adicionar em Lotes",
							JOptionPane.ERROR_MESSAGE);
				} else {
					AdicionarUsuarioEmLotes adicionarUsuarioEmLotes = new AdicionarUsuarioEmLotes(SvnAclGUI.this,
							getGrupoSelecionado());
					if (adicionarUsuarioEmLotes.add && adicionarUsuarioEmLotes.getUsuariosSelecionados() != null) {
						ArrayList<String> usuariosSelecionados = adicionarUsuarioEmLotes.getUsuariosSelecionados();
						for (String usuario : usuariosSelecionados) {
							adicionaUsuarioLotes(usuario);
						}

						// Se existe usuarios que n�o foram adicionados exibe um
						// alerta
						if (contenNaoAdicionados) {
							JTextArea jTextArea = new JTextArea("N�o foi possivel adicionar usu�rios abaixo ao grupo "
									+ getGrupoSelecionado() + ": \n\n" + naoAdicionados
									+ "\nVerifique se o usu�rios j� participam do grupo!");
							jTextArea.setEditable(false);
							// Quebra de linha automatica
							jTextArea.setLineWrap(true);
							// Nao quebra linha no meio da palvra
							jTextArea.setWrapStyleWord(true);
							JScrollPane usuariosNaoAdicionados = new JScrollPane(jTextArea);
							usuariosNaoAdicionados.setPreferredSize(new Dimension(500, 300));
							JOptionPane.showMessageDialog(getFrame(), usuariosNaoAdicionados, "N�o adicionados",
									JOptionPane.ERROR_MESSAGE);
						}
						// Setar valor com false para quando for adicionar
						// outros usuarios se nao conter n�o adicionados nao
						// apresentar o JOptionPane
						contenNaoAdicionados = false;

					}
				}
			}

			/**
			 * 
			 * Adiciona o usu�rio ao {@link JList JList}
			 * 
			 * @param usuarioParaAdicionar
			 *            nome do usu�rio
			 */
			private void adicionaUsuarioLotes(String usuarioParaAdicionar) {
				boolean adicionou = getGerenciadorDeGrupos().adicionaUsuarioNoGrupo(getGrupoSelecionado(),
						usuarioParaAdicionar);
				if (!adicionou) {
					naoAdicionados.append(usuarioParaAdicionar + "\n");
					contenNaoAdicionados = true;
				}
				gerenciador.atualizaArquivo();
				listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
			}

		});
		botaoRemoverUser = new JButton("Remover");
		botaoRemoverUser.setEnabled(false);
		botaoRemoverUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getGrupoSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else if (getUsuariosSelecionados().size() == 0) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um usu�rio", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else {
					for (String usuarios : usuariosSelecionados) {
						getGerenciadorDeGrupos().removeUsuarioDoGrupo(getGrupoSelecionado(), usuarios);
						gerenciador.atualizaArquivo();
					}
					listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
				}

			}
		});

		botaoRemoverUserAllGroups = new JButton("Remover de todos os grupos");
		botaoRemoverUserAllGroups.setEnabled(false);
		botaoRemoverUserAllGroups.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RemoverTodosUsuarioDoGrupo removerTodosUsuarioDoGrupo = new RemoverTodosUsuarioDoGrupo(SvnAclGUI.this);
				if (removerTodosUsuarioDoGrupo.remove && removerTodosUsuarioDoGrupo.getUsuariosSelecionados() != null) {
					ArrayList<String> usuariosSelecionados = removerTodosUsuarioDoGrupo.getUsuariosSelecionados();
					for (String usuario : usuariosSelecionados) {
						removeUsuariosDoGrupo(usuario);
					}
				}
			}

			private void removeUsuariosDoGrupo(String usuario) {
				getGerenciadorDeGrupos().removeUsuarioDeTodosOsGrupos(usuario);
				gerenciador.atualizaArquivo();
				if (!getGrupoSelecionado().equals("") || !getDiretorioSelecionado().equals("")) {
					listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
					listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
				}

			}

		});

		painelBotoesGruposGerGrupos.add(botaoAddGroup);
		painelBotoesGruposGerGrupos.add(botaoRemGroup);

		painelBotoesGruposGerUsers.add(usuarioParaAdicionar);
		painelBotoesGruposGerUsers.add(botaoAdicionarUser);
		painelBotoesGruposGerUsers.add(botaoRemoverUser);
		painelBotoesGruposGerUsers.add(botaoAdicionarUserLotes);
		painelBotoesGruposGerUsers.add(botaoRemoverUserAllGroups);
		painelBotoesGrupos.add(painelBotoesGruposGerUsers, BorderLayout.EAST);
		painelBotoesGrupos.add(painelBotoesGruposGerGrupos, BorderLayout.WEST);
		jPanelPrincipalGrupos.add(painelBotoesGrupos, BorderLayout.SOUTH);
	}

	/**
	 * Adiciona lista de diret�rios
	 */
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

	/**
	 * Adiciona lista de permiss�es em Diret�rios
	 */
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

	/**
	 * Adiciona os diret�rios
	 */
	private void adicionarDiretorios() {
		listarDiretorios = getGerenciadorDePermissoes().listaDiretorios();
		for (String diretorios : listarDiretorios) {
			((DefaultListModel<String>) listaDiretorios.getModel()).addElement(diretorios);
		}
	}

	/**
	 * Adiciona os {@link JButton}'s em {@link JTabbedPane} "Permiss�es"
	 */
	private void adicionaOpcoesEmPermissoes() {
		JPanel painelComboBoxPermissoes = new JPanel(new BorderLayout());

		// Painel dos bot�es gerenciadores de Diret�riosF
		JPanel painelBotoesGruposGerDir = new JPanel();
		painelBotoesGruposGerDir.setLayout(new FlowLayout(FlowLayout.LEFT));
		botaoAddDir = new JButton("+");
		botaoAddDir.setToolTipText("Adicionar Diret�rio");
		botaoAddDir.setEnabled(false);
		botaoAddDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String diretorio;
				String message = "";
				try {
					diretorio = JOptionPane.showInputDialog(SvnAclGUI.this.getFrame(), "Diret�rio",
							"Adicionar Diret�rio", JOptionPane.PLAIN_MESSAGE);
					if (diretorio == null)
						return;
				} catch (NullPointerException ex) {
					return;
				}
				boolean adicionaDir = false;
				try {
					adicionaDir = getGerenciadorDePermissoes().adicionaDir(diretorio);
				} catch (ArrayIndexOutOfBoundsException ex) {
					message = "Diretorio j� existe";
				} catch (NullPointerException ex) {
					message = "Diret�rio inv�lido";
				} catch (IOException ex) {
					message = "Erro ao ler o arquivo";
				} catch (Exception ex) {
					message = "Erro";
				}
				if (adicionaDir) {
					gerenciador.atualizaArquivo();
					listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
				} else {
					JOptionPane.showMessageDialog(getFrame(),
							"N�o foi poss�vel adicionar diret�rio: " + "\"" + diretorio + "\"\n" + message, "Erro",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		botaoRemDir = new JButton("-");
		botaoRemDir.setToolTipText("Remover Diret�rio");
		botaoRemDir.setEnabled(false);
		botaoRemDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um diret�rio", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else {
					getGerenciadorDePermissoes().removeDir(getDiretorioSelecionado());
					gerenciador.atualizaArquivo();
					listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
					setDiretorioSelecionado("");
				}
			}
		});

		JPanel painelComboBoxPermissoesGerPerm = new JPanel();
		painelComboBoxPermissoesGerPerm.setLayout(new FlowLayout(FlowLayout.RIGHT));
		botaoAdicionar = new JButton("Adicionar");
		botaoAdicionar.setEnabled(false);
		botaoAdicionar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um diret�rio", "Adicionar",
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
		botaoAlterar = new JButton("Alterar Permiss�es");
		botaoAlterar.setEnabled(false);
		botaoAlterar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um diretorio", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
				} else if (getPermissoesSelecionada().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo ou usu�rio", "Adicionar",
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
		botaoRemover = new JButton("Remover");
		botaoRemover.setEnabled(false);
		botaoRemover.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um diretorio", "Remover",
							JOptionPane.ERROR_MESSAGE);
				} else if (getPermissoesSelecionada().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um grupo ou usu�rio", "Remover",
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

		painelBotoesGruposGerDir.add(botaoAddDir);
		painelBotoesGruposGerDir.add(botaoRemDir);

		painelComboBoxPermissoesGerPerm.add(botaoAdicionar);
		painelComboBoxPermissoesGerPerm.add(botaoAlterar);
		painelComboBoxPermissoesGerPerm.add(botaoRemover);
		painelComboBoxPermissoes.add(painelBotoesGruposGerDir, BorderLayout.WEST);
		painelComboBoxPermissoes.add(painelComboBoxPermissoesGerPerm, BorderLayout.EAST);
		jPanelPrincipalPermissoes.add(painelComboBoxPermissoes, BorderLayout.SOUTH);
	}

	/**
	 * Adiciona as a��es dos bot�es control Z e control Y ao frame principal
	 */
	private void adicionaAcoesUndoRedo() {
		undo = new UndoAction(this, "Undo", "control Z");
		redo = new RedoAction(this, "Redo", "control Y");

		tabPainel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"),
				undo.getValue(Action.NAME));
		tabPainel.getActionMap().put(undo.getValue(Action.NAME), undo);

		tabPainel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"),
				redo.getValue(Action.NAME));
		tabPainel.getActionMap().put(redo.getValue(Action.NAME), redo);
	}

	/**
	 * Adiciona os {@link JPanel}'s "Grupos" e "Permiss�es" na
	 * {@link JTabbedPane}
	 */
	private void adicionaPainelsATabPainel() {
		jPanelPrincipalGrupos.add(jPanelPrincipalListGrupos);
		tabPainel.addTab("Grupos", jPanelPrincipalGrupos);
		jPanelPrincipalPermissoes.add(jPanelPrincipalListPermissoes);
		tabPainel.addTab("Permiss�es", jPanelPrincipalPermissoes);
	}

	/**
	 * 
	 * Realiza conex�o e verifica usu�rios no AD
	 * 
	 * @return retorna true se conex�o foi bem sucedida
	 */
	public static boolean verificaUsuariosAD() {
		ActiveDirectory ad = null;
		String message = "";
		boolean ret = false;
		int option = JOptionPane.INFORMATION_MESSAGE;
		try {
			ad = new ActiveDirectory();
			allUser = ad.allUser();
			ret = true;
			adicionaTodosOsUsuariosNoArquivo(ad.allUser());
		} catch (AuthenticationException e) {
			message = "Usuario ou senhas invalidos";
		} catch (ConnectException e) {
			message = "Conex�o com AD falhou.\nVerifique o Dom�nio";
			option = JOptionPane.INFORMATION_MESSAGE;
		} catch (FileNotFoundException e) {
			message = "Verifique arquivo system.properties";
		} catch (NullPointerException e) {
		} catch (Exception e) {
			message = "Erro com conexao LDAP";
			option = JOptionPane.ERROR_MESSAGE;
		} finally {
			if (ad != null) {
				ad.closeLdapConnection();
			}
		}

		if (message != "") {
			JOptionPane.showMessageDialog(null, message, "LDAP", option);
		}

		// Finalizando
		ad = null;
		return ret;
	}

	/**
	 * 
	 * Adiciona todos os usu�rios encontrados no AD no arquivo
	 * <i>"allusers.txt"</i>
	 * 
	 * @param allUser
	 *            todos os usu�rios encontrados no AD
	 */
	private static void adicionaTodosOsUsuariosNoArquivo(List<String> allUser) {
		File file = new File("allusers.txt");
		FileReader fileReader = null;
		BufferedReader leitor = null;
		FileWriter fileWriter = null;
		if (file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// Falha ao criar novo arquivo
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// Falha ao criar novo arquivo
			}
		}
		try {
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(file, false);
			leitor = new BufferedReader(fileReader);
			boolean finale = false;
			while (!finale) {
				for (String user : allUser) {
					fileWriter.write(user + "\n");
				}
				finale = true;
			}

		} catch (Exception e) {
			// Falha escrever arquivo
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				// Falha ao fechar arquivo
			}
		}

	}

	/**
	 * 
	 * Caso a conex�o com o AD n�o for bem sucedida adiciona a varival
	 * <code>allUser</code> os usuarios que est�o no arquivo
	 * <i>"allusers.txt"</i> preenchido na ultima conex�o com o AD
	 * 
	 * @return retorna todos os usuarios encontrados no arquivo
	 */
	public static List<String> addAllUserByFile() {
		ArrayList<String> allUser = new ArrayList<>();
		FileReader fileReader = null;
		BufferedReader leitor = null;
		try {
			File file = new File("allusers.txt");
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				allUser.add(line);
			}
			return allUser;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				fileReader.close();
				leitor.close();
			} catch (Exception e) {
				// Falha ao fechar arquivo
			}
		}
	}

	/**
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @return jMenuItemNovo
	 */
	public JMenuItem getJMenuItemNovo() {
		return jMenuItemNovo;
	}

	/**
	 * @return jMenuItemAbrir
	 */
	public JMenuItem getJMenuItemAbrir() {
		return jMenuItemAbrir;
	}

	/**
	 * @return jMenuItemSalvar
	 */
	public JMenuItem getJMenuItemSalvar() {
		return jMenuItemSalvar;
	}

	/**
	 * @return jMenuItemImportar
	 */
	public JMenuItem getJMenuItemImportar() {
		return jMenuItemImportar;
	}

	/**
	 * @return jMenuItemTransferir
	 */
	public JMenuItem getJMenuItemTransferir() {
		return jMenuItemTransferir;
	}

	/**
	 * @return jMenuItemCheckout
	 */
	public JMenuItem getJMenuItemCheckout() {
		return jMenuItemCheckout;
	}

	/**
	 * @return jMenuItemCommit
	 */
	public JMenuItem getJMenuItemCommit() {
		return jMenuItemCommit;
	}

	/**
	 * @return jMenuItemGruposDoUser
	 */
	public JMenuItem getJMenuItemGruposDoUser() {
		return jMenuItemGruposDoUser;
	}

	/**
	 * @return jMenuItemPermDoGrupo
	 */
	public JMenuItem getjMenuItemPermDoGrupo() {
		return jMenuItemPermDoGrupo;
	}

	/**
	 * @return grupoSelecionado
	 */
	public String getGrupoSelecionado() {
		return grupoSelecionado;
	}

	/**
	 * 
	 * Setar grupo selecionado
	 * 
	 * @param grupoSelecionado
	 *            grupo selecionado
	 */
	public void setGrupoSelecionado(String grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	/**
	 * @return diretorioSelecionado
	 */
	public String getDiretorioSelecionado() {
		return diretorioSelecionado;
	}

	/**
	 * 
	 * Setar diret�rio selecionado
	 * 
	 * @param diretorioSelecionado
	 *            diretorio selecionado
	 */
	public void setDiretorioSelecionado(String diretorioSelecionado) {
		this.diretorioSelecionado = diretorioSelecionado;
	}

	/**
	 * @return permissoesSelecionada
	 */
	public String getPermissoesSelecionada() {
		return permissoesSelecionada;
	}

	/**
	 * 
	 * Setar permiss�es selecionada
	 * 
	 * @param permissoesSelecionada
	 *            permissoes selecionada
	 */
	public void setPermissoesSelecionada(String permissoesSelecionada) {
		this.permissoesSelecionada = permissoesSelecionada;
	}

	/**
	 * @return usuariosSelecionados
	 */
	public ArrayList<String> getUsuariosSelecionados() {
		return usuariosSelecionados;
	}

	/**
	 * 
	 * Setar usu�rios selecionados
	 * 
	 * @param usuariosSelecionados
	 *            usu�rios selecionados
	 */
	public void setUsuariosSelecionados(ArrayList<String> usuariosSelecionados) {
		this.usuariosSelecionados = usuariosSelecionados;
	}

	/**
	 * @return listarGrupos
	 */
	public List<String> getListarGrupos() {
		// Organiza o nome dos grupos em ordem alfabetica
		Collections.sort(listarGrupos, new NaturalOrderComparatorStringInsensitive());
		return listarGrupos;
	}

	/**
	 * @return listarDiretorios
	 */
	public List<String> getListarDiretorios() {
		// Organiza o nome dos grupos em ordem alfabetic
		Collections.sort(listarDiretorios, new NaturalOrderComparatorStringInsensitive());
		return listarDiretorios;
	}

	/**
	 * @return {@link Gerenciador#getGerenciadorDeGrupos()}
	 */
	public GerenciadorDeGrupos getGerenciadorDeGrupos() {
		return gerenciador.getGerenciadorDeGrupos();
	}

	/**
	 * @return {@link Gerenciador#getGerenciadorDePermissoes()}
	 */
	public GerenciadorDePermissoes getGerenciadorDePermissoes() {
		return gerenciador.getGerenciadorDePermissoes();
	}

	/**
	 * 
	 * Setar lista de usu�rios do Grupo
	 * 
	 * @param listaUsuariosGrupo
	 *            lista usuarios grupo
	 */
	public void setUsuariosDoGrupo(List<String> listaUsuariosGrupo) {
		this.listaUsuariosGrupo = listaUsuariosGrupo;
	}

	/**
	 * 
	 * Setar lista de permiss�o do Diret�rio
	 * 
	 * @param listaPermissaoDiretorio
	 *            lista permiss�o diretorio
	 */
	public void setPermissoesDoDiretorio(List<String> listaPermissaoDiretorio) {
		this.listaPermissaoDiretorio = listaPermissaoDiretorio;
	}

	/**
	 * Atualiza Grupos
	 */
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

	/**
	 * Atualiza lista de grupos
	 */
	public void atualizaListaGrupos() {
		listarGrupos = getGerenciadorDeGrupos().listarGrupos();
	}

	/**
	 * Atualiza diret�rios
	 */
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

	/**
	 * Atualiza lista de diret�rios
	 */
	public void atualizaListaDiretorios() {
		listarDiretorios = getGerenciadorDePermissoes().listaDiretorios();
	}

	/**
	 * Atualiza usu�rios
	 */
	public void atualizaUsuarios() {
		((DefaultListModel<String>) listaUsuarios.getModel()).removeAllElements();
		Collections.sort(listaUsuariosGrupo, new NaturalOrderComparatorStringInsensitive());
		for (String usuarios : listaUsuariosGrupo) {
			((DefaultListModel<String>) listaUsuarios.getModel()).addElement(usuarios);
		}
	}

	/**
	 * Atualiza permiss�es
	 */
	public void atualizaPermissoes() {
		((DefaultListModel<String>) listaPermissoes.getModel()).removeAllElements();
		Collections.sort(listaPermissaoDiretorio, new NaturalOrderComparatorStringInsensitive());
		for (String usuarios : listaPermissaoDiretorio) {
			((DefaultListModel<String>) listaPermissoes.getModel()).addElement(usuarios);
		}
	}

	/**
	 * Retorna a a��o acionada por control Z e atualiza
	 */
	public void retornaArquivo() {
		gerenciador.alterarArquivo();
		gerenciador.atualizaArquivo();
		atualizaGrupos();
		atualizaDiretorios();
		if (!getGrupoSelecionado().equals(""))
			listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
		if (!getDiretorioSelecionado().equals(""))
			listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
	}

	/**
	 * Retorna a a��o do control Z sendo acionada por control Y e atualiza
	 */
	public void avancaArquivo() {
		gerenciador.alterarArquivo();
		gerenciador.atualizaArquivo();
		atualizaGrupos();
		atualizaDiretorios();
		if (!getGrupoSelecionado().equals(""))
			listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
		if (!getDiretorioSelecionado().equals(""))
			listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
	}

	/**
	 * Habilita a combina��o das teclas control + Z
	 */
	public static void habilitaRetorno() {
		undo.setEnabled(true);
	}

	/**
	 * Habilita a combina��o das teclas control + Y
	 */
	public static void habilitaAvanco() {
		redo.setEnabled(true);
	}

	/**
	 * Adiciona evento para quando fechar a janela apagar os arquivos de
	 * sincroniza��o e sa�da
	 */
	private void adicionarEventoFinal() {
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				// Quando arquivo for modificado ou aberto por algum meio ela
				// dar um alerta se deseja realmente fechar
				boolean arquivoSalvo = SvnAclGUI.arquivoSalvo;
				if (arquivoSalvo) {
					apagaArquivosEFecha();
				} else {
					if (JOptionPane.showConfirmDialog(frame, "Deseja sair ?\nAltera��es n�o salvas ser�o perdidas!",
							"Saindo", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						apagaArquivosEFecha();
					}
				}
			}

			/**
			 * Apaga e fecha o programa
			 */
			private void apagaArquivosEFecha() {
				if (gerenciador != null)
					gerenciador.apagaArquivosDeGerenciamento();
				System.exit(0);
			}
		});
	}

}
