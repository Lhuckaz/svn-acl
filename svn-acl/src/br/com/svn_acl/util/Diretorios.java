package br.com.svn_acl.util;

public class Diretorios {
	
	private static String diretorioCorrente = retornaUserDocuments();

	/**
	 * 
	 * @return Retorna o caminho para User Home
	 */
	public static String retornaUserHome() {
		return System.getProperty("user.home");
	}

	/**
	 * 
	 * @return Retorna uma string para abrir o JChooser em Documentos
	 */
	public static String retornaUserDocuments() {
		return "\\Documents\\";
	}
	
	/**
	 * 
	 * @return Retorna caminho do diretorio atual
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
	 * @return Retorna nome do arquivo para salvar
	 */
	public static String retornaArquivoParaSalvar() {
		return "svn";
	}

}
