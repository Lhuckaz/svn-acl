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
import br.com.svn_acl.listener.SshItemMenuListener;
import br.com.svn_acl.listener.SubversionItemMenuListener;
import br.com.svn_acl.util.DocumentTamanhoJTextField;
import br.com.svn_acl.util.Util;

/**
 * 
 * Interface gráfica principal, composta de {@link JFrame}, exibe na
 * {@link JTabbedPane} "Grupos" os grupos e usuários e em "Permissões" os
 * diretórios e as permissões dos grupos e usuários
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
	private JMenuItem jMenuItemAbrir;
	private JMenuItem jMenuItemSalvar;
	private JMenuItem jMenuItemExport;
	private JMenuItem jMenuItemCommit;
	private JMenuItem jMenuItemImportar;
	private JMenuItem jMenuItemTransferir;
	private JMenuItem jMenuItemAdSettings;

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

	private JButton botaoRemGroup;
	private JButton botaoAddGroup;
	private JButton botaoRemoverUser;
	private JButton botaoAdicionarUser;
	private JButton botaoAddDir;
	private JButton botaoRemDir;
	private JButton botaoAlterar;
	private JButton botaoAdicionar;
	private JButton botaoRemover;

	public static List<String> allUser;

	public SvnAclGUI() {
		prepareGUI();
	}

	/**
	 * Monta a interface gráfica
	 */
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
		// Tela iniciada maximilizada
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Iniciado na classe Main
		// verificaUsuariosAD();
	}

	private boolean carregadoArquivo = false;

	/**
	 * 
	 * Método principal para carregar o arquivo que será apresentado pelo
	 * programa
	 * 
	 * @param arquivo
	 *            nome do arquivo a carregar
	 */
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

		botaoRemGroup.setEnabled(true);
		botaoAddGroup.setEnabled(true);
		botaoRemoverUser.setEnabled(true);
		botaoAdicionarUser.setEnabled(true);
		botaoAddDir.setEnabled(true);
		botaoRemDir.setEnabled(true);
		botaoAlterar.setEnabled(true);
		botaoAdicionar.setEnabled(true);
		botaoRemover.setEnabled(true);
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
		jMenuItemImportar = new JMenuItem("Importar");
		jMenuItemImportar.addActionListener(sshItemMenuListener);
		jMenuItemImportar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		jMenuItemTransferir = new JMenuItem("Transferir");
		jMenuItemTransferir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		jMenuItemTransferir.setEnabled(false);
		jMenuItemTransferir.addActionListener(sshItemMenuListener);
		jMenuSsh.add(jMenuItemImportar);
		jMenuSsh.add(jMenuItemTransferir);

		jMenuAd = new JMenu("AD");
		jMenuItemAdSettings = new JMenuItem("Configurar LDAP");
		jMenuItemAdSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		jMenuItemAdSettings.addActionListener(adItemMenuListener);
		jMenuAd.add(jMenuItemAdSettings);

		jMenuBar.add(jMenuArquivos);
		jMenuBar.add(jMenuSubversion);
		jMenuBar.add(jMenuSsh);
		jMenuBar.add(jMenuAd);

		frame.setJMenuBar(jMenuBar);
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
	 * Adiciona lista de usuários em Grupos
	 */
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
		// Painel dos botões
		JPanel painelBotoesGrupos = new JPanel(new BorderLayout());

		// Painel dos botões gerenciadores de Grupos
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
					boolean adicionaGrupo = getGerenciadorDeGrupos().adicionaGrupo(grupo);
					if (adicionaGrupo) {
						gerenciador.atualizaArquivo();
						listaGrupoListener.atualizaUsuarios(getGrupoSelecionado());
					} else {
						JOptionPane.showMessageDialog(SvnAclGUI.this.getFrame(), "Não foi possível adicionar grupo: "
								+ "\"" + grupo + "\"\nVerfique se o grupo já existe", "Erro", JOptionPane.ERROR_MESSAGE);
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
				}
			}
		});

		// Painel dos botões gerenciadores de Usuários
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
								int confirmar = JOptionPane.showConfirmDialog(getFrame(), "Usuário \""
										+ usuarioParaAdicionar + "\" ainda nao existe\nDeseja adicionar assim mesmo ?",
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
							int confirmar = JOptionPane.showConfirmDialog(getFrame(), "Usuário \""
									+ usuarioParaAdicionar
									+ "\" ainda nao existe\nUsuario existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						} else {
							int confirmar = JOptionPane.showConfirmDialog(getFrame(), "Usuário \""
									+ usuarioParaAdicionar
									+ "\" ainda nao existe\nUsuario NÃO existe no AD\nDeseja adicionar assim mesmo ?",
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
							int confirmar = JOptionPane.showConfirmDialog(getFrame(), "Usuário \""
									+ usuarioParaAdicionar
									+ "\" ainda nao existe\nUsuario existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						} else {
							int confirmar = JOptionPane.showConfirmDialog(getFrame(), "Usuário \""
									+ usuarioParaAdicionar
									+ "\" ainda nao existe\nUsuario NÃO existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						}
					} else {
						if (contains) {
							adicionaUsuario(usuarioParaAdicionar);
						} else {
							int confirmar = JOptionPane.showConfirmDialog(getFrame(), "Usuário: \""
									+ usuarioParaAdicionar + "\" NÃO existe no AD\nDeseja adicionar assim mesmo ?",
									"Adicionar", JOptionPane.YES_NO_OPTION);
							if (confirmar == 0)
								adicionaUsuario(usuarioParaAdicionar);
						}
					}
				}
			}

			/**
			 * 
			 * Adiciona o usuário ao {@link JList JList}
			 * 
			 * @param usuarioParaAdicionar
			 *            nome do usuário
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
							"Não foi possivel adicionar\nVerifique se o usuário já participa do grupo", "Adicionar",
							JOptionPane.ERROR_MESSAGE);
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

		painelBotoesGruposGerGrupos.add(botaoAddGroup);
		painelBotoesGruposGerGrupos.add(botaoRemGroup);

		painelBotoesGruposGerUsers.add(usuarioParaAdicionar);
		painelBotoesGruposGerUsers.add(botaoAdicionarUser);
		painelBotoesGruposGerUsers.add(botaoRemoverUser);
		painelBotoesGrupos.add(painelBotoesGruposGerUsers, BorderLayout.EAST);
		painelBotoesGrupos.add(painelBotoesGruposGerGrupos, BorderLayout.WEST);
		jPanelPrincipalGrupos.add(painelBotoesGrupos, BorderLayout.SOUTH);
	}

	/**
	 * Adiciona lista de diretórios
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
	 * Adiciona lista de permissões em Diretórios
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
	 * Adiciona os diretórios
	 */
	private void adicionarDiretorios() {
		listarDiretorios = getGerenciadorDePermissoes().listaDiretorios();
		for (String diretorios : listarDiretorios) {
			((DefaultListModel<String>) listaDiretorios.getModel()).addElement(diretorios);
		}
	}

	/**
	 * Adiciona os {@link JButton}'s em {@link JTabbedPane} "Permissões"
	 */
	private void adicionaOpcoesEmPermissoes() {
		JPanel painelComboBoxPermissoes = new JPanel(new BorderLayout());

		// Painel dos botões gerenciadores de DiretóriosF
		JPanel painelBotoesGruposGerDir = new JPanel();
		painelBotoesGruposGerDir.setLayout(new FlowLayout(FlowLayout.LEFT));
		botaoAddDir = new JButton("+");
		botaoAddDir.setToolTipText("Adicionar Diretório");
		botaoAddDir.setEnabled(false);
		botaoAddDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String diretorio;
				try {
					diretorio = JOptionPane.showInputDialog(SvnAclGUI.this.getFrame(), "Diretório",
							"Adicionar Diretório", JOptionPane.PLAIN_MESSAGE);
					if (diretorio == null)
						return;
				} catch (NullPointerException ex) {
					return;
				}
				boolean adicionaDir = getGerenciadorDePermissoes().adicionaDir(diretorio);
				if (adicionaDir) {
					gerenciador.atualizaArquivo();
					listaDiretoriosListener.atualizaPermissoes(getDiretorioSelecionado());
				} else {
					JOptionPane.showMessageDialog(getFrame(), "Não foi possível adicionar diretório: " + "\""
							+ diretorio + "\"\nVerfique se o diretório já existe", "Erro", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		botaoRemDir = new JButton("-");
		botaoRemDir.setToolTipText("Remover Diretório");
		botaoRemDir.setEnabled(false);
		botaoRemDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getDiretorioSelecionado().equals("")) {
					JOptionPane.showMessageDialog(getFrame(), "Selecione um diretório", "Remover",
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
		botaoAlterar = new JButton("Alterar Permissões");
		botaoAlterar.setEnabled(false);
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
		botaoRemover = new JButton("Remover");
		botaoRemover.setEnabled(false);
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
	 * Adiciona os {@link JPanel}'s "Grupos" e "Permissões" na
	 * {@link JTabbedPane}
	 */
	private void adicionaPainelsATabPainel() {
		jPanelPrincipalGrupos.add(jPanelPrincipalListGrupos);
		tabPainel.addTab("Grupos", jPanelPrincipalGrupos);
		jPanelPrincipalPermissoes.add(jPanelPrincipalListPermissoes);
		tabPainel.addTab("Permissões", jPanelPrincipalPermissoes);
	}

	/**
	 * 
	 * Realiza conexão e verifica usuários no AD
	 * 
	 * @return retorna true se conexão foi bem sucedida
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
			message = "Conexão com AD falhou.\nVerifique o Domínio";
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
	 * Adiciona todos os usuários encontrados no AD no arquivo
	 * <i>"allusers.txt"</i>
	 * 
	 * @param allUser
	 *            todos os usuários encontrados no AD
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
	 * Caso a conexão com o AD não for bem sucedida adiciona a varival
	 * <code>allUser</code> os usuarios que estão no arquivo
	 * <i>"allusers.txt"</i> preenchido na ultima conexão com o AD
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
	 * @return jMenuItemExport
	 */
	public JMenuItem getJMenuItemExport() {
		return jMenuItemExport;
	}

	/**
	 * @return jMenuItemCommit
	 */
	public JMenuItem getJMenuItemCommit() {
		return jMenuItemCommit;
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
	 * Setar diretório selecionado
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
	 * Setar permissões selecionada
	 * 
	 * @param permissoesSelecionada
	 *            permissoes selecionada
	 */
	public void setPermissoesSelecionada(String permissoesSelecionada) {
		this.permissoesSelecionada = permissoesSelecionada;
	}

	/**
	 * @return usuarioSelecionado
	 */
	public String getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	/**
	 * 
	 * Setar usuário selecionado
	 * 
	 * @param usuarioSelecionado
	 *            usuário selecionado
	 */
	public void setUsuarioSelecionado(String usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	/**
	 * @return listarGrupos
	 */
	public List<String> getListarGrupos() {
		return listarGrupos;
	}

	/**
	 * @return listarDiretorios
	 */
	public List<String> getListarDiretorios() {
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
	 * Setar lista de usuários do Grupo
	 * 
	 * @param listaUsuariosGrupo
	 *            lista usuarios grupo
	 */
	public void setUsuariosDoGrupo(List<String> listaUsuariosGrupo) {
		this.listaUsuariosGrupo = listaUsuariosGrupo;
	}

	/**
	 * 
	 * Setar lista de permissão do Diretório
	 * 
	 * @param listaPermissaoDiretorio
	 *            lista permissão diretorio
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
	public void atulizaListaGrupos() {
		listarGrupos = getGerenciadorDeGrupos().listarGrupos();
	}

	/**
	 * Atualiza diretórios
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
	 * Atualiza lista de diretórios
	 */
	public void atualizaListaDiretorios() {
		listarDiretorios = getGerenciadorDePermissoes().listaDiretorios();
	}

	/**
	 * Atualiza usuários
	 */
	public void atualizaUsuarios() {
		((DefaultListModel<String>) listaUsuarios.getModel()).removeAllElements();
		for (String usuarios : listaUsuariosGrupo) {
			((DefaultListModel<String>) listaUsuarios.getModel()).addElement(usuarios);
		}
	}

	/**
	 * Atualiza permissões
	 */
	public void atualizaPermissoes() {
		((DefaultListModel<String>) listaPermissoes.getModel()).removeAllElements();
		for (String usuarios : listaPermissaoDiretorio) {
			((DefaultListModel<String>) listaPermissoes.getModel()).addElement(usuarios);
		}
	}

	/**
	 * Adiciona evento para quando fechar a janela apagar os arquivos de
	 * sincronização e saída
	 */
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
