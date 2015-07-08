package br.com.svn_acl.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.svn_acl.util.Util;

@SuppressWarnings("serial")
public class AlteraPermissoes extends JDialog implements ActionListener {

	private JComboBox<String> comboPermissoes;
	private String permissoes;
	private String diretorioSelecionado;
	private String grupoOuUser;
	private SvnAclGUI owner;

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

		painelAlteraPermissoes.add(new JLabel("Permissões"));
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
