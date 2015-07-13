package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.activity.InvalidActivityException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import br.com.svn_acl.ssh.Ssh;
import br.com.svn_acl.util.SpringUtilities;
import br.com.svn_acl.util.Util;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

@SuppressWarnings("serial")
public class SshGui extends JDialog {

	private SvnAclGUI owner;
	JTextField host;
	JTextField port;
	JTextField user;
	JTextField password;
	JTextField dir;
	JTextField file;

	public SshGui(SvnAclGUI owner, String titulo) {
		super(owner.getFrame());
		this.owner = owner;
		this.setTitle(titulo);
		this.setModal(true);
		JPanel principal = new JPanel();

		final String[] labels = { "Porta: ", "Host: ", "Usuário: ", "Senha: ", "Diretorio: ", "Arquivo: " };
		int labelsLength = labels.length;

		JPanel p = new JPanel(new SpringLayout());

		JPanel ports = new JPanel(new BorderLayout());
		JLabel lPort = new JLabel(labels[0], JLabel.TRAILING);
		p.add(lPort);
		port = new JTextField(3);
		((AbstractDocument) port.getDocument()).setDocumentFilter(new DocumentFilterOnlyNumbers());
		port.setText(Util.getNumberPort());
		lPort.setLabelFor(port);
		ports.add(port, BorderLayout.WEST);
		p.add(ports);

		JLabel l = new JLabel(labels[1], JLabel.TRAILING);
		p.add(l);
		host = new JTextField(50);
		host.setText(Util.getHostName());
		l.setLabelFor(host);
		p.add(host);

		JPanel users = new JPanel(new BorderLayout());
		JLabel l1 = new JLabel(labels[2], JLabel.TRAILING);
		p.add(l1);
		user = new JTextField(20);
		user.setText(Util.getUserNameSsh());
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
		if (!titulo.equals("Importar"))
			dir.setText(Util.validaURL(Util.getDirSsh()));
		else
			dir.setText(Util.getDirSsh());
		l3.setLabelFor(dir);
		p.add(dir);

		// Retira um campo para a tela "Importar" para nao adicionar o
		// Arquivo
		labelsLength--;
		if (!titulo.equals("Importar")) {
			JPanel files = new JPanel(new BorderLayout());
			JLabel l4 = new JLabel(labels[5], JLabel.TRAILING);
			p.add(l4);
			file = new JTextField(20);
			file.setText(Util.FILE);
			l4.setLabelFor(file);
			files.add(file, BorderLayout.WEST);
			p.add(files);
			labelsLength++;
		}

		JPanel botoes = new JPanel(new BorderLayout());
		p.add(new JLabel());
		JButton button = new JButton("OK");
		button.addActionListener(new AcaoSsh(this, titulo));
		botoes.add(button, BorderLayout.EAST);
		p.add(botoes);

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

	public class AcaoSsh implements ActionListener {

		private SshGui sshTransfere;
		private String titulo;

		public AcaoSsh(SshGui sshGui, String titulo) {
			this.sshTransfere = sshGui;
			this.titulo = titulo;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (titulo.equals("Transferir")) {
				String port = sshTransfere.port.getText();
				int porta = pegaNumberPorta(port);

				String host = sshTransfere.host.getText();
				String user = sshTransfere.user.getText();
				String password = sshTransfere.password.getText();
				String dir = sshTransfere.dir.getText();
				String file = sshTransfere.file.getText();

				Ssh ssh = new Ssh();
				boolean transferindo = false;
				String message = "";

				try {
					transferindo = ssh.transfere(host, user, password, dir, porta, file);
					Util.setAtributosSsh(host, user, dir, porta);
				} catch (JSchException ex) {
					String messageEx = ex.getMessage();
					if (messageEx.equals("Auth fail"))
						message = "Usuario ou senha inválidos";
				} catch (InvalidActivityException ex) {
					message = "Hostname ou porta inválidos";
				} catch (SftpException ex) {
					message = "Diretório inválido";
				} catch (Exception ex) {
					message = "Erro Fatal";
				}

				int option;
				if (transferindo) {
					message = "Sucesso";
					option = JOptionPane.INFORMATION_MESSAGE;
					setVisible(false);
				} else {
					option = JOptionPane.ERROR_MESSAGE;
				}
				JOptionPane.showMessageDialog(owner.getFrame(), message, "Transferir", option);
			} else {
				String port = sshTransfere.port.getText();
				int porta = pegaNumberPorta(port);

				String host = sshTransfere.host.getText();
				String user = sshTransfere.user.getText();
				String password = sshTransfere.password.getText();
				String dir = sshTransfere.dir.getText();

				Ssh ssh = new Ssh();
				boolean importando = false;
				String message = "";

				try {
					importando = ssh.importar(host, user, password, dir, porta);
					File fileExport = new File(Util.getNomeArquivoURL(dir));
					owner.carregaArquivo(Util.FILE);
					fileExport.delete();
					Util.setAtributosSsh(host, user, dir, porta);
				} catch (JSchException ex) {
					String messageEx = ex.getMessage();
					if (messageEx.equals("Auth fail"))
						message = "Usuario ou senha inválidos";
				} catch (InvalidActivityException ex) {
					message = "Hostname ou porta inválidos";
				} catch (SftpException ex) {
					message = "Diretório inválido";
				} catch (IOException ex) {
					message = "Diretorio inválido. passe o caminho de um arquivo";
				} catch (Exception ex) {
					message = "Erro Fatal";
				}

				int option;
				if (importando) {
					message = "Sucesso";
					option = JOptionPane.INFORMATION_MESSAGE;
					setVisible(false);
				} else {
					option = JOptionPane.ERROR_MESSAGE;
				}
				JOptionPane.showMessageDialog(owner.getFrame(), message, "Importar", option);
			}
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

	private class DocumentFilterOnlyNumbers extends DocumentFilter {
		@Override
		public void insertString(DocumentFilter.FilterBypass fp, int offset, String string, AttributeSet aset)
				throws BadLocationException {
			int len = string.length();
			boolean isValidInteger = true;

			for (int i = 0; i < len; i++) {
				if (!Character.isDigit(string.charAt(i))) {
					isValidInteger = false;
					break;
				}
			}
			if (isValidInteger)
				super.insertString(fp, offset, string, aset);
			else
				Toolkit.getDefaultToolkit().beep();
		}

		@Override
		public void replace(DocumentFilter.FilterBypass fp, int offset, int length, String string, AttributeSet aset)
				throws BadLocationException {
			int len = string.length();
			boolean isValidInteger = true;

			for (int i = 0; i < len; i++) {
				if (!Character.isDigit(string.charAt(i))) {
					isValidInteger = false;
					break;
				}
			}
			if (isValidInteger)
				super.replace(fp, offset, length, string, aset);
			else
				Toolkit.getDefaultToolkit().beep();
		}
	}

}
