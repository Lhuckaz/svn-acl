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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import br.com.svn_acl.util.Util;

/**
 * 
 * Interface gr�fica extends {@link JDialog} implements {@link ActionListener}
 * ouvinte do {@link JButton} "Adicionar" no {@link JTabbedPane} "Permiss�es" da
 * interface principal {@link SvnAclGUI} para adicionar grupo e usu�rios e as
 * permiss�es dos mesmos
 * 
 * @author Lhuckaz
 *
 */
@SuppressWarnings("serial")
public class AdicionarEmDiretorio extends JDialog implements ActionListener {

	private SvnAclGUI svnAclGUI;
	private JCheckBox jCheckBoxUsuario;
	private JComboBox<String> comboGrupos;
	private JComboBox<String> comboUsuarios;
	private JComboBox<String> comboPermissoes;
	private String permissoes;
	private String diretorio;

	/**
	 * 
	 * Construtor da classe {@link AdicionarEmDiretorio} monta a interface
	 * gr�fica do {@link JDialog}
	 * 
	 * @param svnAclGUI
	 *            interface principal
	 * @param diretorio
	 *            diret�rio
	 */
	public AdicionarEmDiretorio(SvnAclGUI svnAclGUI, String diretorio) {
		super(svnAclGUI.getFrame(), "Adicionar em Diretorio", true);
		this.svnAclGUI = svnAclGUI;
		this.diretorio = diretorio;
		JPanel painelAdiciona = new JPanel(new GridLayout(2, 1));
		JPanel painelCheckUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel painelOpcoes = new JPanel(new FlowLayout());

		final JLabel grupo = new JLabel("Grupo");
		Vector<String> listarGrupos = new Vector<String>(svnAclGUI.getGerenciadorDeGrupos().listarGrupos());
		comboGrupos = new JComboBox<>(listarGrupos);

		final JLabel usuario = new JLabel("Usuario");
		Vector<String> listaUsuarios = new Vector<String>(svnAclGUI.getGerenciadorDeGrupos().listarUsuarios());
		comboUsuarios = new JComboBox<>(listaUsuarios);

		String[] permissoes = { "LEITURA", "LEITURA/ESCRITA", "ESCRITA" };
		comboPermissoes = new JComboBox<>(permissoes);

		final JButton botaoAdiciona = new JButton("Adicionar");
		botaoAdiciona.addActionListener(this);

		final JLabel jLabelPermissoes = new JLabel("Permiss�es");

		jCheckBoxUsuario = new JCheckBox("Adicionar Usuario");
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
			 * Adiciona usu�rio no {@link javax.swing.JList JList} da interface
			 * principal {@link br.com.svn_acl.gui.SvnAclGUI SvnAclGUI}
			 * 
			 * @return retorna algum valor int para o m�todo
			 *         {@link ActionListener#itemStateChanged itemStateChanged}
			 */
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

			/**
			 * 
			 * Adiciona grupo no {@link javax.swing.JList JList} da interface
			 * principal {@link br.com.svn_acl.gui.SvnAclGUI SvnAclGUI}
			 * 
			 * @return retorna algum valor int para o m�todo
			 *         {@link ActionListener#itemStateChanged itemStateChanged}
			 */
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
		setLocationRelativeTo(svnAclGUI.getFrame());
		setModal(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		permissoes = (String) comboPermissoes.getSelectedItem();
		String permissao = Util.getPermissao(permissoes);
		boolean adicionou = true;
		if (!jCheckBoxUsuario.isSelected()) {
			String grupoSelecionado = (String) comboGrupos.getSelectedItem();
			adicionou = svnAclGUI.getGerenciadorDePermissoes().adicionaGrupoEPermissoesNoDiretorio(diretorio,
					grupoSelecionado, permissao);
		} else {
			String usuarioSelecionado = (String) comboUsuarios.getSelectedItem();
			adicionou = svnAclGUI.getGerenciadorDePermissoes().adicionaUserEPermissoesNoDiretorio(diretorio,
					usuarioSelecionado, permissao);
		}
		if (!adicionou)
			JOptionPane.showMessageDialog(svnAclGUI.getFrame(),
					"N�o foi possivel adicionar\nVerifique se o grupo/usu�rio j� tem permiss�es no diret�rio",
					"Adicionar", JOptionPane.ERROR_MESSAGE);
		setVisible(false);
	}

}
