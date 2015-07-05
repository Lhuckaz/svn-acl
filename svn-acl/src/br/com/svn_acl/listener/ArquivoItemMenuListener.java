package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.svn_acl.controler.Gerenciador;
import br.com.svn_acl.gui.SvnAclGUI;
import br.com.svn_acl.util.Diretorios;

public class ArquivoItemMenuListener implements ActionListener {

	private static final int SALVAR = 0;
	private JFileChooser chooser;
	private SvnAclGUI svnAclGUI;
	private FileNameExtensionFilter filter;

	public ArquivoItemMenuListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(Diretorios.retornaDiretorioCorrente()));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// Para selecionar apenas um diretorio ou arquivo
		chooser.setMultiSelectionEnabled(false);

		// Filtro de extensoes para selecionar somente arquivos acl
		filter = new FileNameExtensionFilter("ACL Files (*.acl)", "acl");
		chooser.setFileFilter(filter);

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
				atualizaListas();
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
			if (chooser.getFileFilter().equals(filter)) {
				if (!verificaSeContemExtensao(selectedFile.getName())) {
					verificaSeExisteESalva(new File(selectedFile + ".acl"));
				} else {
					verificaSeExisteESalva(selectedFile.getAbsoluteFile());
				}
			} else {
				verificaSeExisteESalva(selectedFile.getAbsoluteFile());
			}
		}
	}

	private boolean verificaSeContemExtensao(String selectedFile) {
		boolean retorno = false;
		try {
			retorno = selectedFile.substring(selectedFile.lastIndexOf("."), selectedFile.length()).equals(".acl");
		} catch (Exception e) {
			System.out.println("Nao especificado extensão");
		}
		return retorno;
	}

	private void verificaSeExisteESalva(File selectedFile) {
		if (selectedFile.exists()) {
			int confirmar = JOptionPane.showConfirmDialog(svnAclGUI.getFrame(),
					"Arquivo já existe\nDeseja sobrescrever ?", "Salvar", JOptionPane.YES_NO_OPTION);
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

		FileReader fileReader = null;
		FileWriter fileWriter = null;
		BufferedReader leitor = null;
		if (Gerenciador.getCaminhoSaidaOculto(true) == null) {
			JOptionPane.showMessageDialog(svnAclGUI.getFrame(), "Não foi possível salvar!",
					"Nenhum arquivo selecionado", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			fileReader = new FileReader(Gerenciador.getCaminhoSaidaOculto(true));
			fileWriter = new FileWriter(selectedFile);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				fileWriter.write(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
				leitor.close();
				fileWriter = null;
				fileReader = null;
				leitor = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Para atualizar lista depois de abrir um arquivo, verificar ordem que e
	 * chamado os métodos pois influenciam
	 */
	private void atualizaListas() {
		svnAclGUI.atulizaListaGrupos();
		svnAclGUI.atualizaGrupos();
		svnAclGUI.atulizaListaDiretorios();
		svnAclGUI.atualizaDiretorios();
	}

}
