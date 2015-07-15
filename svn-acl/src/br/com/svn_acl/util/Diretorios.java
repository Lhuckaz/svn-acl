package br.com.svn_acl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;

/**
 * 
 * Classe responsável por gerenciar os diretórios
 * 
 * @author Lhuckaz
 *
 */
public class Diretorios {
	
	private static String diretorioCorrente = retornaUserDocuments();
	private static String fileExportName;
	private static String endereco;

	/**
	 * 
	 * @return retorna o caminho para User Home
	 */
	public static String retornaUserHome() {
		return System.getProperty("user.home");
	}

	/**
	 * 
	 * @return retorna uma string para abrir o JChooser em Documentos
	 */
	public static String retornaUserDocuments() {
		return "\\Documents\\";
	}
	
	/**
	 * 
	 * @return retorna caminho do diretorio atual
	 */
	public static String retornaDiretorioCorrente() {
		return diretorioCorrente;
	}
	
	/**
	 * 
	 * @param diretorio setar o valor do diretorio
	 */
	public static void setDiretorioCorrente(String diretorio) {
		diretorioCorrente = diretorio;
	}
	
	/**
	 * 
	 * @return retorna nome do arquivo para salvar
	 */
	public static String retornaArquivoParaSalvar() {
		return "svn";
	}
	
	/**
	 * 
	 * @param user usuário
	 * @param nome do arquivo exportado
	 */
	public static void setFileExportNameAndUser(String nome, String user) {
		fileExportName = nome;
		setURLNoProperties();
		Util.setUserSvn(user);
	}
	
	/**
	 * Salva o url do svn no properties para tornar mais dinâmico
	 */
	private static void setURLNoProperties() {
		try {
			FileInputStream fileInputStream = new FileInputStream(Util.ARQUIVO_PROPERTIES);
			Util.propertiesSystem.load(new FileInputStream(Util.ARQUIVO_PROPERTIES));
			Util.propertiesSystem.setProperty("url.svn.file", endereco + "/" + fileExportName);
			File file = new File(Util.ARQUIVO_PROPERTIES);
			FileOutputStream fos = new FileOutputStream(file);
			Util.propertiesSystem.store(fos, "Alteracao de URL");
			fileInputStream.close();
			fos.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Verifique arquivo system.properties", "Erro",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * 
	 * @return retorna nome do arquivo exportado
	 */
	public static String retornaFileExportName() {
		return fileExportName;
	}

	/**
	 * 
	 * @param url setar a url
	 */
	public static void setUrl(String url) {
		endereco = url;
	}

	/**
	 * 
	 * @return retorna a url
	 */
	public static String retornaUrl() {
		return endereco;
	}

}
