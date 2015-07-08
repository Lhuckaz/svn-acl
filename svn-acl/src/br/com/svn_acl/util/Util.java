package br.com.svn_acl.util;

public class Util {

	public static String getGrupoOuUser(String permissoesSelecionada) {
		if (permissoesSelecionada.startsWith("@"))
			return permissoesSelecionada.substring(1, permissoesSelecionada.indexOf(" "));
		else
			return permissoesSelecionada.substring(0, permissoesSelecionada.indexOf(" "));
	}

	public static String getPermissao(String permissoesSelecionada) {
		if (permissoesSelecionada.equals("LEITURA"))
			return "r";
		else if (permissoesSelecionada.equals("LEITURA/ESCRITA"))
			return "rw";
		else
			return "w";
	}

	public static String getNomeArquivoURL(String url) {
		try {
			String[] split = url.split("/");
			String string = split[split.length - 1];
			return string;
		} catch (Exception e) {
			return "svn.acl";
		}
	}

	public static boolean validaString(String string) {
		try {
			string.equals("null");
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	

	public static String enderecoPadraoComArquivo() {
		return "http://mizar/svn/brad2011/dn1/svn_acl/svn.acl";
	}
	
	public static String enderecoPadrao() {
		return "http://mizar/svn/brad2011/dn1/svn_acl/";
	}

	public static String validaURL(String url) {
		String ret;
		try {
			ret = url.substring(0, url.lastIndexOf("/"));
			return ret;
		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
	}

}
