package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNException;

import br.com.svn_acl.svn.Commit;
import br.com.svn_acl.svn.Export;
import br.com.svn_acl.util.Diretorios;
import br.com.svn_acl.util.SpringUtilities;
import br.com.svn_acl.util.Util;

@SuppressWarnings("serial")
public class SubversionArquivo extends JDialog {

	private SvnAclGUI owner;
	JTextField url;
	JTextField user;
	JTextField password;
	JTextArea comentario;

	public SubversionArquivo(SvnAclGUI owner, String titulo) {
		super(owner.getFrame(), titulo, true);
		this.owner = owner;

		JPanel principal = new JPanel(new BorderLayout());

		final String[] labels = { "URL: ", "Usuário: ", "Senha: ", "Comentario: " };
		int labelsLength = labels.length;

		JPanel p = new JPanel(new SpringLayout());

		JLabel l = new JLabel(labels[0], JLabel.TRAILING);
		p.add(l);
		url = new JTextField(50);

		if(titulo.equals("Exportar")) {
			if (Diretorios.retornaFileExportName() == null) {
				url.setText(Util.enderecoPadraoComArquivo());
			} else {
				url.setText(Diretorios.retornaUrl() + "/" + Diretorios.retornaFileExportName());
			}
		} else {
			if (Diretorios.retornaUrl() == null || !Util.validaString(Diretorios.retornaUrl())) {
				url.setText(Util.enderecoPadraoComArquivo());
			} else {
				url.setText(Diretorios.retornaUrl() + "/" + Diretorios.retornaFileExportName());
			}	
		}
		l.setLabelFor(url);
		p.add(url);

		JPanel users = new JPanel(new BorderLayout());
		JLabel l1 = new JLabel(labels[1], JLabel.TRAILING);
		p.add(l1);
		user = new JTextField(20);
		l1.setLabelFor(user);
		users.add(user, BorderLayout.WEST);
		p.add(users);

		JPanel passwords = new JPanel(new BorderLayout());
		JLabel l2 = new JLabel(labels[2], JLabel.TRAILING);
		p.add(l2);
		password = new JPasswordField(20);
		l2.setLabelFor(password);
		passwords.add(password, BorderLayout.WEST);
		p.add(passwords);

		// Retira um campo para a tela "Exportar" para nao adicionar o
		// Comentario
		labelsLength--;

		// Caso seja a tela Exportar nao adiciona o campo Comentario
		if (!titulo.equals("Exportar")) {
			JLabel l3 = new JLabel(labels[3], JLabel.TRAILING);
			p.add(l3);
			comentario = new JTextArea(5, 20);
			// Quebra de linha automatica
			comentario.setFont(new Font("Verdana", Font.PLAIN, 11));
			comentario.setLineWrap(true);
			// Nao quebra linha no meio da palvra
			comentario.setWrapStyleWord(true);
			l3.setLabelFor(comentario);
			p.add(new JScrollPane(comentario));
			// Coloca o campo novamente caso seja a tela "Commit"
			labelsLength++;
		}

		// Adicionando o botao em um JPanel
		JPanel botoes = new JPanel(new BorderLayout());
		JButton button = new JButton("OK");
		p.add(new JLabel());
		botoes.add(button, BorderLayout.EAST);
		button.addActionListener(new Acao(this, titulo));
		p.add(botoes);

		// Coloca para fora do painel.
		SpringUtilities.makeCompactGrid(p, labelsLength + 1, 2, // rows, cols
				7, 7, // initX, initY
				7, 7); // xPad, yPad

		principal.add(p);
		getContentPane().add(principal);
		pack();
		setLocationRelativeTo(owner.getFrame());
		setModal(true);
	}

	private byte[] getBytes(File file) {
		int len = (int) file.length();
		byte[] sendBuf = new byte[len];
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(file);
			inFile.read(sendBuf, 0, len);
		} catch (FileNotFoundException fnfex) {
			fnfex.printStackTrace();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} finally {
			try {
				inFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sendBuf;
	}

	public class Acao implements ActionListener {

		private SubversionArquivo subversionArquivo;
		private String titulo;

		public Acao(SubversionArquivo classe, String titulo) {
			this.subversionArquivo = classe;
			this.titulo = titulo;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			File file = null;
			if (titulo.equals("Exportar")) {
				String url = subversionArquivo.url.getText();
				String user = subversionArquivo.user.getText();
				String password = subversionArquivo.password.getText();

				Export export = new Export();
				boolean exportando = false;
				String message = "";
				
				try {
					exportando = export.exportando(url, user, password);
					// Retira o nome do arquivo da url
					Diretorios.setUrl(Util.validaURL(url));
				} catch (SVNAuthenticationException ex) {
					message = "Usuario ou senha invalidos";
				} catch (SVNException ex) {
					message = "URL Invalida, passe o caminho de um arquivo";
				} catch (Exception ex) {
					message = "Erro Fatal";
				}
				
				if (exportando) {
					String arquivo = Util.getNomeArquivoURL(url);
					file = new File(arquivo);
					Diretorios.setFileExportName(arquivo);
					owner.carregaArquivo(arquivo);
					file.delete();
					subversionArquivo.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(owner.getFrame(), message, "Export", JOptionPane.ERROR_MESSAGE);
				}
	
			} else {
				String url = subversionArquivo.url.getText();
				String user = subversionArquivo.user.getText();
				String password = subversionArquivo.password.getText();
				String comentario = subversionArquivo.comentario.getText();
				
				Commit commit = new Commit();
				boolean commitando = false;
				String message = "";
				try {
					File fileSaida = new File("~svn-saida.acl");
					byte[] bytes = getBytes(fileSaida);
					
					String arquivo = Diretorios.retornaFileExportName();
					
					commitando = commit.commitando(url, user, password, arquivo, bytes, comentario, false);
					// Retira o nome do arquivo da url
					url = Util.validaURL(url);
					Diretorios.setUrl(url);
				} catch (SVNAuthenticationException ex) {
					message = "Usuário ou senha inválidos";
				} catch (SVNCancelException ex) {
					message = "Arquivo não existe";
				} catch (SVNException ex) {
					message = "URL Invalida";
				} catch (Exception ex) {
					message = "Erro Fatal";
				}

				if (commitando) {
					subversionArquivo.setVisible(false);
					JOptionPane.showMessageDialog(owner.getFrame(), "Commitado", "Commit", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(owner.getFrame(), message, "Commit", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

	}
}
