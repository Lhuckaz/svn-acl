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
 * Interface gráfica extends {@link JDialog} implements {@link ActionListener}
 * ouvinte do {@link JButton} "Adicionar" no {@link JTabbedPane} "Permissões" da
 * interface principal {@link SvnAclGUI} para adicionar grupo e usuários e as
 * permissões dos mesmos
 * 
 * @author Lhuckaz
 *
 */
@SuppressWarnings("serial")
public class AdicionarEmDiretorio extends JDialog implements ActionListener {

	private SvnAclGUI owner;
	private JCheckBox jCheckBoxUsuario;
	private JComboBox<String> comboGrupos;
	private JComboBox<String> comboUsuarios;
	private JComboBox<String> comboPermissoes;
	private String permissoes;
	private String diretorio;

	/**
	 * 
	 * Construtor da classe {@link AdicionarEmDiretorio} monta a interface gráfica do
	 * {@link JDialog}
	 * 
	 * @param owner
	 *            interface principal
	 * @param diretorio
	 */
	public AdicionarEmDiretorio(SvnAclGUI owner, String diretorio) {
		super(owner.getFrame(), "Adicionar em Diretorio", true);
		this.owner = owner;
		this.diretorio = diretorio;
		JPanel painelAdiciona = new JPanel(new GridLayout(2, 1));
		JPanel painelCheckUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JPanel painelOpcoes = new JPanel(new FlowLayout());

		final JLabel grupo = new JLabel("Grupo");
		Vector<String> listarGrupos = new Vector<String>(owner.getGerenciadorDeGrupos().listarGrupos());
		comboGrupos = new JComboBox<>(listarGrupos);

		final JLabel usuario = new JLabel("Usuario");
		Vector<String> listaUsuarios = new Vector<String>(owner.getGerenciadorDeGrupos().listarUsuarios());
		comboUsuarios = new JComboBox<>(listaUsuarios);

		String[] permissoes = { "LEITURA", "LEITURA/ESCRITA", "ESCRITA" };
		comboPermissoes = new JComboBox<>(permissoes);

		final JButton botaoAdiciona = new JButton("Adicionar");
		botaoAdiciona.addActionListener(this);

		final JLabel jLabelPermissoes = new JLabel("Permissões");

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
			 * @return retorna algum valor int para o método
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
		setLocationRelativeTo(owner.getFrame());
		setModal(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		permissoes = (String) comboPermissoes.getSelectedItem();
		String permissao = Util.getPermissao(permissoes);
		boolean adicionou = true;
		if (!jCheckBoxUsuario.isSelected()) {
			String grupoSelecionado = (String) comboGrupos.getSelectedItem();
			adicionou = owner.getGerenciadorDePermissoes().adicionaGrupoEPermissoesNoDiretorio(diretorio,
					grupoSelecionado, permissao);
		} else {
			String usuarioSelecionado = (String) comboUsuarios.getSelectedItem();
			adicionou = owner.getGerenciadorDePermissoes().adicionaUserEPermissoesNoDiretorio(diretorio,
					usuarioSelecionado, permissao);
		}
		if (!adicionou)
			JOptionPane.showMessageDialog(owner.getFrame(),
					"Não foi possivel adicionar\nVerifique se o grupo/usuário já tem permissões no diretório",
					"Adicionar", JOptionPane.ERROR_MESSAGE);
		setVisible(false);
	}

}
