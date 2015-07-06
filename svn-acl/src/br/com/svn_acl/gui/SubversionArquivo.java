package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import br.com.svn_acl.util.Diretorios;

public class SubversionArquivo extends JDialog implements ActionListener {

	public SubversionArquivo(SvnAclGUI owner, String titulo) {
		super(owner.getFrame(), titulo, true);
		
		JPanel principal = new JPanel(new BorderLayout());

		final String[] labels = { "URL: ", "Usuário: ", "Senha: ", "Comentario: " };
		int labelsLength = labels.length;
		

		JPanel p = new JPanel(new SpringLayout());
		
		// for (int i = 0; i < labelsLength; i++) {
		// JLabel l = new JLabel(labels[i], JLabel.TRAILING);
		// p.add(l);
		// JTextField textField = new JTextField(10);
		// l.setLabelFor(textField);
		// p.add(textField);
		// }

		JLabel l = new JLabel(labels[0], JLabel.TRAILING);
		p.add(l);
		JTextField url = new JTextField(10);
		l.setLabelFor(url);
		p.add(url);

		JLabel l1 = new JLabel(labels[1], JLabel.TRAILING);
		p.add(l1);
		JTextField user = new JTextField(10);
		l.setLabelFor(user);
		p.add(user);

		JLabel l2 = new JLabel(labels[2], JLabel.TRAILING);
		p.add(l2);
		JTextField password = new JPasswordField(10);
		l.setLabelFor(password);
		p.add(password);

		JLabel l3 = new JLabel(labels[3], JLabel.TRAILING);
		p.add(l3);
		JTextArea comentario = new JTextArea(5, 20);
		// Quebra de linha automatica
		comentario.setFont(new Font("Verdana", Font.PLAIN, 11));
		comentario.setLineWrap(true);
		// Nao quebra linha no meio da palvra
		comentario.setWrapStyleWord(true);
		l.setLabelFor(comentario);
		p.add(new JScrollPane(comentario));

		p.add(new JLabel());
		p.add(new JLabel());

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(p, labelsLength + 1, 2, // rows, cols
				7, 7, // initX, initY
				7, 7); // xPad, yPad
		
		JPanel botoes = new JPanel();
		botoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton button = new JButton("OK");
		botoes.setBorder(new EmptyBorder(0, 0, 0, 0));
		botoes.add(button);
		
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// Execute when button is pressed
				System.out.println("Test");
			}
		});

		principal.add(p);
		principal.add(botoes, BorderLayout.SOUTH);
		getContentPane().add(principal);
		pack();
		setLocationRelativeTo(owner.getFrame());
		setModal(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// byte[] bytes = getBytes(new File("~svn-saida.acl"));

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

}
