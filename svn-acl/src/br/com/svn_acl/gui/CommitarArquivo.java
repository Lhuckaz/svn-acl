package br.com.svn_acl.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JDialog;

public class CommitarArquivo extends JDialog implements ActionListener {

	public CommitarArquivo(SvnAclGUI owner) {
		super(owner.getFrame(), "Commitar Arquivo", true);
		byte[] bytes = getBytes(new File("svn.acl"));
		pack();
		setLocationRelativeTo(owner.getFrame());
		setModal(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

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
	
	public static void main(String[] args) {
		new CommitarArquivo(new SvnAclGUI());
	}

}
