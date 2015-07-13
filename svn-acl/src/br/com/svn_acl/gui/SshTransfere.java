package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import br.com.svn_acl.ssh.Ssh;
import br.com.svn_acl.util.SpringUtilities;
import br.com.svn_acl.util.Util;

@SuppressWarnings("serial")
public class SshTransfere extends JDialog {

	private SvnAclGUI owner;
	JTextField host;
	JTextField port;
	JTextField user;
	JTextField password;
	JTextField dir;

	public SshTransfere(SvnAclGUI owner) {
		super(owner.getFrame(), "Transferência", true);
		this.owner = owner;
		JPanel principal = new JPanel();

		final String[] labels = { "Porta: ", "Host: ", "Usuário: ", "Senha: ", "Diretorio: " };
		int labelsLength = labels.length;

		JPanel p = new JPanel(new SpringLayout());

		JPanel ports = new JPanel(new BorderLayout());
		JLabel lPort = new JLabel(labels[0], JLabel.TRAILING);
		p.add(lPort);
		port = new JTextField(3);
		port.setText(Util.getNumberPort());
		lPort.setLabelFor(port);
		ports.add(port, BorderLayout.WEST);
		p.add(ports);

		JLabel l = new JLabel(labels[1], JLabel.TRAILING);
		p.add(l);
		host = new JTextField(50);
		l.setLabelFor(host);
		p.add(host);

		JPanel users = new JPanel(new BorderLayout());
		JLabel l1 = new JLabel(labels[2], JLabel.TRAILING);
		p.add(l1);
		user = new JTextField(20);
		l1.setLabelFor(user);
		users.add(user, BorderLayout.WEST);
		p.add(users);

		JPanel passwords = new JPanel(new BorderLayout());
		JLabel l2 = new JLabel(labels[3], JLabel.TRAILING);
		p.add(l2);
		password = new JPasswordField(20);
		l2.setLabelFor(password);
		passwords.add(password, BorderLayout.WEST);
		p.add(passwords);

		JLabel l3 = new JLabel(labels[4], JLabel.TRAILING);
		p.add(l3);
		dir = new JTextField(10);
		l3.setLabelFor(dir);
		p.add(dir);

		JPanel botoes = new JPanel(new BorderLayout());
		p.add(new JLabel());
		JButton button = new JButton("OK");
		button.addActionListener(new Transfere(this));
		botoes.add(button, BorderLayout.EAST);
		p.add(botoes);

		// Coloca para fora do painel.
		SpringUtilities.makeCompactGrid(p, labelsLength + 1, 2, 7, 7, 7, 7);

		principal.add(p);
		getContentPane().add(principal);

		// Adiciona foco no host pois a porta ja tem valor padrao
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				host.requestFocus();
			}
		});

		pack();
		setLocationRelativeTo(owner.getFrame());
		setModal(true);
		setVisible(true);
	}

	public class Transfere implements ActionListener {

		private SshTransfere sshTransfere;

		public Transfere(SshTransfere sshTransfere) {
			this.sshTransfere = sshTransfere;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String port = sshTransfere.port.getText();
			int porta = pegaNumberPorta(port);

			String host = sshTransfere.host.getText();
			String user = sshTransfere.user.getText();
			String password = sshTransfere.password.getText();
			String dir = sshTransfere.dir.getText();

			Ssh ssh = new Ssh();
			boolean transferindo = false;

			transferindo = ssh.transfere(host, user, password, dir, porta);

			String message;
			int option;
			if (transferindo) {
				message = "Sucesso";
				option = JOptionPane.INFORMATION_MESSAGE;
				setVisible(false);
			} else {
				message = "Falha, tente novamente mais tarde";
				option = JOptionPane.ERROR_MESSAGE;
				setVisible(false);
			}
			JOptionPane.showMessageDialog(owner.getFrame(), message, "Transferir", option);
		}

		private int pegaNumberPorta(String port) {
			try {
				return Integer.parseInt(port);
			} catch (NumberFormatException ex) {
				try {
					return Integer.parseInt(Util.getNumberPort());
				} catch (NumberFormatException exnfe) {
					return Util.getNumberPortDefault();
				}
			}
		}

	}

}
