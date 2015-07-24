/*
 * 
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import br.com.svn_acl.controler.Gerenciador;
import br.com.svn_acl.gui.SvnAclGUI;
import br.com.svn_acl.util.Diretorios;

/**
 * 
 * Classe ouvinte dos {@link javax.swing.JMenuItem JMenuItem} "Abrir" e "Salvar"
 * da classe {@link SvnAclGUI}
 * 
 * @author Lhuckaz
 *

public class ArquivoItemMenuListener implements ActionListener {

	/**
	 * Valor que confirma o resultado do {@link JOptionPane} para salvar o
	 * arquivo

	private static final int SALVAR = 0;
	private SvnAclGUI svnAclGUI;

	public ArquivoItemMenuListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object open = e.getSource();

		// MenuItem novo
		if (open == svnAclGUI.getJMenuItemNovo()) {
			carregarArquivoNovo();
		}

		// MenuItem abrir
		if (open == svnAclGUI.getJMenuItemAbrir()) {
			abrirArquivoChooser();
		}

		// MenuItem salvar
		if (open == svnAclGUI.getJMenuItemSalvar()) {
			abrirSalvarChooser();
		}
	}

	/**
	 * Carrega um arquivo novo

	private void carregarArquivoNovo() {
		File file = new File("svn.acl");
		if (file.exists()) {
			file.delete();
		}

		try {
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("[groups]\n");
			fileWriter.close();
			svnAclGUI.carregaArquivo(file.getAbsolutePath());
			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Abre o JFileChooser para abrir um arquivo

	private void abrirArquivoChooser() {
		Display display = null;
		Shell shell = null;
		try {
			display = new Display();
			shell = new Shell(display);
			FileDialog dialog = new FileDialog(shell);
			String[] filterNames = new String[] { "ACL Files (*.acl)", "All Files" };
			dialog.setFilterNames(filterNames);
			String[] filterExtensions = new String[] { "*.acl", "*" };
			dialog.setFilterExtensions(filterExtensions);
			String selected = dialog.open();

			File selectedFile = new File(selected);
			svnAclGUI.carregaArquivo(selectedFile.getAbsolutePath());
			atualizaListas();

			shell.close();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();

		} catch (NullPointerException e) {
			// FileDialog cancelado
		} catch (SWTException e) {
			// FileDialog cancelado
		} finally {
			try {
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.close();
				}
				display.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Abre o JFileChooser para salvar um arquivo

	private void abrirSalvarChooser() {
		Display display = null;
		Shell shell = null;
		try {
			display = new Display();
			shell = new Shell(display);
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFileName(Diretorios.retornaArquivoParaSalvar());

			String[] filterNames = new String[] { "ACL Files (*.acl)", "All Files" };
			dialog.setFilterNames(filterNames);
			String[] filterExtensions = new String[] { "*.acl", "*" };
			dialog.setFilterExtensions(filterExtensions);
			String selected = dialog.open();

			File selectedFile = new File(selected);
			verificaSeExisteESalva(selectedFile);

			shell.close();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();

			// Salvar, Commit e Transferir
			SvnAclGUI.arquivoSalvo = true;
		} catch (NullPointerException e) {
			// FileDialog cancelado
		} catch (SWTException e) {
			// FileDialog cancelado
		} finally {
			try {
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.close();
				}
				display.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Verifica se existe e se deseja sobrescrever
	 * 
	 * @param selectedFile
	 *            arquivo para salvar

	private void verificaSeExisteESalva(File selectedFile) {
		if (selectedFile.exists()) {
			int confirmar = JOptionPane.showConfirmDialog(svnAclGUI.getFrame(),
					"Arquivo já existe\nDeseja sobrescrever ?", "Salvar", JOptionPane.YES_NO_OPTION);
			desejaSobrescrever(confirmar, selectedFile);

		} else {
			salvarArquivo(selectedFile);
		}
	}

	/**
	 * 
	 * Se deseja sobrescrever salva se não abre o {@link JFileChooser} para
	 * salvar novamente
	 * 
	 * @param confirmar
	 *            resultado do {@link JOptionPane}
	 * @param selectedFile
	 *            arquivo para salvar

	private void desejaSobrescrever(int confirmar, File selectedFile) {
		if (confirmar == SALVAR) {
			salvarArquivo(selectedFile);
		} else {
			abrirSalvarChooser();
		}
	}

	/**
	 * 
	 * Salvar o arquivo
	 * 
	 * @param selectedFile
	 *            arquivo para ser salvo

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

	private void atualizaListas() {
		svnAclGUI.atulizaListaGrupos();
		svnAclGUI.atualizaGrupos();
		svnAclGUI.atualizaListaDiretorios();
		svnAclGUI.atualizaDiretorios();
	}

}
 * 
 * */

package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import br.com.svn_acl.controler.Gerenciador;
import br.com.svn_acl.gui.SvnAclGUI;
import br.com.svn_acl.util.Diretorios;



/**
 * 
 * Classe ouvinte dos {@link javax.swing.JMenuItem JMenuItem} "Abrir" e "Salvar"
 * da classe {@link SvnAclGUI}
 * 
 * @author Lhuckaz
 *
 */
public class ArquivoItemMenuListener implements ActionListener {

	/**
	 * Valor que confirma o resultado do {@link JOptionPane} para salvar o
	 * arquivo
	 */
	private static final int SALVAR = 0;
	private SvnAclGUI svnAclGUI;

	public ArquivoItemMenuListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object open = e.getSource();

		// MenuItem novo
		if (open == svnAclGUI.getJMenuItemNovo()) {
			carregarArquivoNovo();
		}

		// MenuItem abrir
		if (open == svnAclGUI.getJMenuItemAbrir()) {
			abrirArquivoChooser();
		}

		// MenuItem salvar
		if (open == svnAclGUI.getJMenuItemSalvar()) {
			abrirSalvarChooser();
		}
	}

	/**
	 * Carrega um arquivo novo
	 */
	private void carregarArquivoNovo() {
		File file = new File("svn.acl");
		if (file.exists()) {
			file.delete();
		}

		try {
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("[groups]\n");
			fileWriter.close();
			svnAclGUI.carregaArquivo(file.getAbsolutePath());
			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Abre o {@link FileDialog} para abrir um arquivo
	 */
	private void abrirArquivoChooser() {
		Display display = null;
		Shell shell = null;
		try {
			display = new Display();
			shell = new Shell(display);
			FileDialog dialog = new FileDialog(shell);
			String[] filterNames = new String[] { "ACL Files (*.acl)", "All Files" };
			dialog.setFilterNames(filterNames);
			String[] filterExtensions = new String[] { "*.acl", "*" };
			dialog.setFilterExtensions(filterExtensions);
			String selected = dialog.open();

			File selectedFile = new File(selected);
			svnAclGUI.carregaArquivo(selectedFile.getAbsolutePath());
			atualizaListas();

			shell.close();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();

		} catch (NullPointerException e) {
			// FileDialog cancelado
		} catch (SWTException e) {
			// FileDialog cancelado
		} finally {
			try {
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.close();
				}
				display.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Abre o {@link FileDialog} para salvar um arquivo
	 */
	private void abrirSalvarChooser() {
		Display display = null;
		Shell shell = null;
		try {
			display = new Display();
			shell = new Shell(display);
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFileName(Diretorios.retornaArquivoParaSalvar());

			String[] filterNames = new String[] { "ACL Files (*.acl)", "All Files" };
			dialog.setFilterNames(filterNames);
			String[] filterExtensions = new String[] { "*.acl", "*" };
			dialog.setFilterExtensions(filterExtensions);
			String selected = dialog.open();

			File selectedFile = new File(selected);
			// FIXME Abrir o FileDialog novamente se nao deseja sobreescrever
			verificaSeExisteESalva(selectedFile);

			shell.close();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();

			// Salvar, Commit e Transferir
			SvnAclGUI.arquivoSalvo = true;
		} catch (NullPointerException e) {
			// FileDialog cancelado
		} catch (SWTException e) {
			// FileDialog cancelado
		} finally {
			try {
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.close();
				}
				display.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Verifica se existe e se deseja sobrescrever
	 * 
	 * @param selectedFile
	 *            arquivo para salvar
	 */
	private void verificaSeExisteESalva(File selectedFile) {
		if (selectedFile.exists()) {
			int confirmar = JOptionPane.showConfirmDialog(svnAclGUI.getFrame(),
					"Arquivo já existe\nDeseja sobrescrever ?", "Salvar", JOptionPane.YES_NO_OPTION);
			desejaSobrescrever(confirmar, selectedFile);

		} else {
			salvarArquivo(selectedFile);
		}
	}

	/**
	 * 
	 * Se deseja sobrescrever salva se não abre o {@link FileDialog} para
	 * salvar novamente
	 * 
	 * @param confirmar
	 *            resultado do {@link JOptionPane}
	 * @param selectedFile
	 *            arquivo para salvar
	 */
	private void desejaSobrescrever(int confirmar, File selectedFile) {
		if (confirmar == SALVAR) {
			salvarArquivo(selectedFile);
		} else {
			abrirSalvarChooser();
		}
	}

	/**
	 * 
	 * Salvar o arquivo
	 * 
	 * @param selectedFile
	 *            arquivo para ser salvo
	 */
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
		svnAclGUI.atualizaListaDiretorios();
		svnAclGUI.atualizaDiretorios();
	}

}
