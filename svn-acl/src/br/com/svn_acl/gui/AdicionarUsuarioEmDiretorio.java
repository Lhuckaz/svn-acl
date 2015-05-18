package br.com.svn_acl.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.svn_acl.controler.GerenciadorDeGrupos;
import br.com.svn_acl.controler.GerenciadorDePermissoes;

public class AdicionarUsuarioEmDiretorio extends JDialog implements ActionListener {
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;

	public AdicionarUsuarioEmDiretorio(SvnAclGUI owner, GerenciadorDeGrupos gerenciadorDeGrupos) {
		super(owner.getFrame(), "Adicionar Usuario em Diretorio", true);
		JPanel painelAdiciona = new JPanel(new FlowLayout());
		
		JLabel usuario = new JLabel("Usuario");
		Vector<String> listaUsuarios = new Vector<String>(gerenciadorDeGrupos.listarUsuarios());
		JComboBox<String> comboUsuarios = new JComboBox<>(listaUsuarios);
		
		String[] permissoes = { "LEITURA", "LEITURA\\ESCRITA", "ESCRITA" };
		JComboBox<String> comboPermissoes = new JComboBox<>(permissoes);
		
		JButton botaoAdiciona = new JButton("Adicionar");
		botaoAdiciona.addActionListener(this);
		
		painelAdiciona.add(usuario);
		painelAdiciona.add(comboUsuarios);
		painelAdiciona.add(new JLabel("Permissões"));
		painelAdiciona.add(comboPermissoes);
		painelAdiciona.add(botaoAdiciona);
		getContentPane().add(painelAdiciona);
		pack();
		setLocationRelativeTo(owner.getFrame());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		setVisible(false);
	}

}
