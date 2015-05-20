package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import br.com.svn_acl.gui.SvnAclGUI;
import br.com.svn_acl.util.Diretorios;

public class MenuItemMenuListener implements ActionListener {

	private static final int SALVAR = 0;
	private JFileChooser chooser;
	private SvnAclGUI svnAclGUI;

	public MenuItemMenuListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(Diretorios.retornaDiretorioCorrente()));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// Para selecionar apenas um diretorio ou arquivo
		chooser.setMultiSelectionEnabled(false);

		Object open = e.getSource();
		// MenuItem abrir
		if (open == svnAclGUI.getJMenuItemAbrir()) {
			abrirArquivoChooser();
		}

		// MenuItem salvar
		if (open == svnAclGUI.getJMenuItemSalvar()) {
			abrirSalvarChooser();
		}
	}

	private void abrirArquivoChooser() {
		try {
			int code = chooser.showOpenDialog(svnAclGUI.getFrame());
			if (code == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				svnAclGUI.carregaArquivo(selectedFile.getAbsolutePath());
				Diretorios.setDiretorioCorrente(chooser.getSelectedFile().getParent());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void abrirSalvarChooser() {
		chooser.setCurrentDirectory(new File(Diretorios.retornaDiretorioCorrente()));
		chooser.setSelectedFile(new File(Diretorios.retornaArquivoParaSalvar()));

		int code = chooser.showSaveDialog(svnAclGUI.getFrame());
		if (code == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			verficaSeExisteESalva(selectedFile);
		}
	}

	private void verficaSeExisteESalva(File selectedFile) {
		if (selectedFile.exists()) {
			int confirmar = JOptionPane.showConfirmDialog(null, "Arquivo já existe\nDeseja sobrescrever ?", "Salvar",
					JOptionPane.YES_NO_OPTION);
			desejaSobrescrever(confirmar, selectedFile);

		} else {
			salvarArquivo(selectedFile);
		}
	}

	private void desejaSobrescrever(int confirmar, File selectedFile) {
		if (confirmar == SALVAR) {
			salvarArquivo(selectedFile);
		} else {
			abrirSalvarChooser();
		}
	}

	private void salvarArquivo(File selectedFile) {
		try (FileOutputStream fos = new FileOutputStream(selectedFile);
				OutputStreamWriter out = new OutputStreamWriter(fos, Charset.forName("UTF-8"))) {
			out.write(/* TODO arquivo de saisa */"");
			Diretorios.setDiretorioCorrente(selectedFile.getAbsolutePath());
		} catch (Exception e) {
		}
	}

}
