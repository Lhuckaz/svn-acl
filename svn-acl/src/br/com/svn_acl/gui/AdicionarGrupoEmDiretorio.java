package br.com.svn_acl.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.acl.Owner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.svn_acl.controler.GerenciadorDeGrupos;

public class AdicionarGrupoEmDiretorio extends JDialog implements ActionListener {
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;
	private SvnAclGUI owner;

	public AdicionarGrupoEmDiretorio(SvnAclGUI owner, GerenciadorDeGrupos gerenciadorDeGrupos) {
		super(owner.getFrame(), "Adicionar Grupo em Diretorio", true);
		this.owner = owner;
		JPanel painelAdiciona = new JPanel(new FlowLayout());
		
		JLabel grupo = new JLabel("Grupo");
		Vector<String> listarGrupos = new Vector<String>(gerenciadorDeGrupos.listarGrupos());
		JComboBox<String> comboGrupos = new JComboBox<>(listarGrupos);
		
		String[] permissoes = { "LEITURA", "LEITURA\\ESCRITA", "ESCRITA" };
		JComboBox<String> comboPermissoes = new JComboBox<>(permissoes);
		
		
		JButton botaoAdiciona = new JButton("Adicionar");
		botaoAdiciona.addActionListener(this);
		
		painelAdiciona.add(grupo);
		painelAdiciona.add(comboGrupos);
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
