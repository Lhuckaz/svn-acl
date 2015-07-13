package br.com.svn_acl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Util {

	public final static String arquivoProperties = "system.properties";
	public static Properties propertiesSystem = new Properties();
	public final static String FILE = "svn.acl";

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
			return FILE;
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
		return propertiesSystem.getProperty("number.port.ssh");
	}

	public static int getNumberPortDefault() {
		return 22;
	}

	public static String getHostName() {
		try {
			propertiesSystem.load(new FileInputStream(arquivoProperties));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("host.ssh");
	}

	public static String getUserNameSsh() {
		try {
			propertiesSystem.load(new FileInputStream(arquivoProperties));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("user.ssh");
	}

	public static String getUserNameSvn() {
		try {
			propertiesSystem.load(new FileInputStream(arquivoProperties));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("user.svn");
	}

	public static String getDirSsh() {
		try {
			propertiesSystem.load(new FileInputStream(arquivoProperties));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("dir.ssh");
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

	public static void setAtributosSsh(String host, String user, String dir, int porta) {
		try {
			FileInputStream fileInputStream = new FileInputStream(Util.arquivoProperties);
			propertiesSystem.load(new FileInputStream(Util.arquivoProperties));
			propertiesSystem.setProperty("host.ssh", host);
			propertiesSystem.setProperty("user.ssh", user);
			propertiesSystem.setProperty("dir.ssh", dir);
			propertiesSystem.setProperty("number.port", String.valueOf(porta));
			File file = new File(Util.getArquivoProperties());
			FileOutputStream fos = new FileOutputStream(file);
			Util.propertiesSystem.store(fos, "Alteracao de URL");
			fileInputStream.close();
			fos.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Verifique arquivo system.properties", "Erro",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void setUserSvn(String user) {
		try {
			FileInputStream fileInputStream = new FileInputStream(Util.arquivoProperties);
			propertiesSystem.load(new FileInputStream(Util.arquivoProperties));
			if (propertiesSystem.getProperty("user.svn") != user) {
				propertiesSystem.setProperty("user.svn", user);
				File file = new File(Util.getArquivoProperties());
				FileOutputStream fos = new FileOutputStream(file);
				Util.propertiesSystem.store(fos, "Alteracao de User SVN");
				fileInputStream.close();
				fos.close();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Verifique arquivo system.properties", "Erro",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
