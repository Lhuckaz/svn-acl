package br.com.svn_acl.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.svn_acl.controler.GerenciadorDeGrupos;

public class AlteraPermissoes extends JDialog implements ActionListener {

	public AlteraPermissoes(SvnAclGUI owner, GerenciadorDeGrupos gerenciadorDeGrupos) {
		super(owner.getFrame(), "Altera Permissoes", true);
		JPanel painelAlteraPermissoes = new JPanel(new FlowLayout());

		String[] permissoes = { "LEITURA", "LEITURA\\ESCRITA", "ESCRITA" };
		JComboBox<String> comboPermissoes = new JComboBox<>(permissoes);

		JButton botaoOk = new JButton("OK");
		botaoOk.addActionListener(this);

		painelAlteraPermissoes.add(new JLabel("Permiss�es"));
		painelAlteraPermissoes.add(comboPermissoes);
		painelAlteraPermissoes.add(botaoOk);
		getContentPane().add(painelAlteraPermissoes);
		pack();
		setLocationRelativeTo(owner.getFrame());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		setVisible(false);
	}

}