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
 * Interface gr�fica extends {@link JDialog} implements {@link ActionListener}
 * ouvinte do {@link JButton} "Alterar Permiss�es" no {@link JTabbedPane}
 * "Permiss�es" da interface principal {@link SvnAclGUI} para alterar as
 * permiss�es dos grupos e usu�rios
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
	private SvnAclGUI owner;

	/**
	 * 
	 * Construtor da classe {@link AlteraPermissoes} monta a interface gr�fica do
	 * {@link JDialog}
	 * 
	 * @param owner
	 *            interface principal
	 * @param diretorioSelecionado
	 *            diret�rio escolhido
	 * @param grupoOuUser
	 *            grupo ao usu�rio para alterar a permiss�o
	 */
	public AlteraPermissoes(SvnAclGUI owner, String diretorioSelecionado, String grupoOuUser) {
		super(owner.getFrame(), "Altera Permissoes", true);
		this.owner = owner;
		this.diretorioSelecionado = diretorioSelecionado;
		this.grupoOuUser = grupoOuUser;
		JPanel painelAlteraPermissoes = new JPanel(new FlowLayout());

		String[] permissoes = { "LEITURA", "LEITURA/ESCRITA", "ESCRITA" };
		comboPermissoes = new JComboBox<>(permissoes);

		JButton botaoOk = new JButton("OK");
		botaoOk.addActionListener(this);

		painelAlteraPermissoes.add(new JLabel("Permiss�es"));
		painelAlteraPermissoes.add(comboPermissoes);
		painelAlteraPermissoes.add(botaoOk);
		getContentPane().add(painelAlteraPermissoes);
		pack();
		setLocationRelativeTo(owner.getFrame());
		setModal(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		permissoes = (String) comboPermissoes.getSelectedItem();
		String permissao = Util.getPermissao(permissoes);
		owner.getGerenciadorDePermissoes().alteraPermissoesDoGrupoDoDir(diretorioSelecionado, grupoOuUser, permissao);
		setVisible(false);
	}
}
