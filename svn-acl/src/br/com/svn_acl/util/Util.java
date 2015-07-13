package br.com.svn_acl.util;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Util {

	public static String arquivoProperties = "system.properties";
	public static Properties propertiesSystem = new Properties();

	public static String getArquivoProperties() {
		return arquivoProperties;
	}

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
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String enderecoPadraoComArquivo() {
		try {
			propertiesSystem.load(new FileInputStream(arquivoProperties));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Verifique arquivo system.properties", "Erro",
					JOptionPane.INFORMATION_MESSAGE);
			return "";
		}
		return propertiesSystem.getProperty("url.svn.file");
	}

	public static String enderecoPadrao() {
		return validaURL(enderecoPadraoComArquivo());
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

	public static String getNumberPort() {
		try {
			propertiesSystem.load(new FileInputStream(arquivoProperties));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("number.port");
	}

	public static int getNumberPortDefault() {
		return 22;
	}

	public static String byteToString(byte[] value) {
		return stringArrayToString(Arrays.toString(value));

	}
	
	public static byte[] stringArrayToByte(String array) {
		String[] byteValues = array.substring(1, array.length() - 1).split(",");

		byte[] bytes = new byte[byteValues.length];
		for (int i = 0, len = bytes.length; i < len; i++) {
			bytes[i] = Byte.parseByte(byteValues[i].trim());
		}
		return bytes;
	}

	public static String stringArrayToString(String array) {
		return new String(stringArrayToByte(array));
	}
}
