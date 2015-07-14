package br.com.svn_acl.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import br.com.svn_acl.util.Util;

/**
 * 
 * Interface gráfica extends {@link JDialog} implements {@link ActionListener}
 * ouvinte do {@link JButton} "Alterar Permissões" no {@link JTabbedPane}
 * "Permissões" da interface principal {@link SvnAclGUI} para alterar as
 * permissões dos grupos e usuários
 * 
 * @author Lhuckaz
 *
 */
@SuppressWarnings("serial")
public class AlteraPermissoes extends JDialog implements ActionListener {

	private JComboBox<String> comboPermissoes;
	private String permissoes;
	private String diretorioSelecionado;
	private String grupoOuUser;
	private SvnAclGUI svnAclGUI;

	/**
	 * 
	 * Construtor da classe {@link AlteraPermissoes} monta a interface gráfica do
	 * {@link JDialog}
	 * 
	 * @param svnAclGUI
	 *            interface principal
	 * @param diretorioSelecionado
	 *            diretório escolhido
	 * @param grupoOuUser
	 *            grupo ao usuário para alterar a permissão
	 */
	public AlteraPermissoes(SvnAclGUI svnAclGUI, String diretorioSelecionado, String grupoOuUser) {
		super(svnAclGUI.getFrame(), "Altera Permissoes", true);
		this.svnAclGUI = svnAclGUI;
		this.diretorioSelecionado = diretorioSelecionado;
		this.grupoOuUser = grupoOuUser;
		JPanel painelAlteraPermissoes = new JPanel(new FlowLayout());

		String[] permissoes = { "LEITURA", "LEITURA/ESCRITA", "ESCRITA" };
		comboPermissoes = new JComboBox<>(permissoes);

		JButton botaoOk = new JButton("OK");
		botaoOk.addActionListener(this);

		painelAlteraPermissoes.add(new JLabel("Permissões"));
		painelAlteraPermissoes.add(comboPermissoes);
		painelAlteraPermissoes.add(botaoOk);
		getContentPane().add(painelAlteraPermissoes);
		pack();
		setLocationRelativeTo(svnAclGUI.getFrame());
		setModal(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		permissoes = (String) comboPermissoes.getSelectedItem();
		String permissao = Util.getPermissao(permissoes);
		svnAclGUI.getGerenciadorDePermissoes().alteraPermissoesDoGrupoDoDir(diretorioSelecionado, grupoOuUser, permissao);
		setVisible(false);
	}
}
