package br.com.svn_acl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * 
 * Classe útil
 * 
 * @author Lhuckaz
 *
 */
public class Util {

	/**
	 * Nome do arquivo properties
	 */
	public final static String ARQUIVO_PROPERTIES = "system.properties";
	public static Properties propertiesSystem = new Properties();
	/**
	 * Nome do arquivo svn-acl
	 */
	public final static String FILE = "svn.acl";

	/**
	 * Se conter @ no começo e grupo se não usuário
	 * 
	 * @param permissoesSelecionada
	 *            permissões selecionada
	 * @return Retorna grupo ao usuario selecionado em permissões
	 */
	public static String getGrupoOuUser(String permissoesSelecionada) {
		if (permissoesSelecionada.startsWith("@"))
			return permissoesSelecionada.substring(1, permissoesSelecionada.indexOf(" "));
		else
			return permissoesSelecionada.substring(0, permissoesSelecionada.indexOf(" "));
	}

	/**
	 * Retorna permissões selecionada
	 * 
	 * @param permissoesSelecionada
	 *            permissões selecionada
	 * @return retorna o valor requerido pelo SVN "r", "rw" ou "w"
	 */
	public static String getPermissao(String permissoesSelecionada) {
		if (permissoesSelecionada.equals("LEITURA"))
			return "r";
		else if (permissoesSelecionada.equals("LEITURA/ESCRITA"))
			return "rw";
		else
			return "w";
	}

	/**
	 * 
	 * Retorna apenas o nome do arquivo no final da url
	 * 
	 * @param url
	 *            url completa
	 * @return retorna o nome do arquivo
	 */
	public static String getNomeArquivoURL(String url) {
		try {
			String[] split = url.split("/");
			String string = split[split.length - 1];
			return string;
		} catch (Exception e) {
			return FILE;
		}
	}

	/**
	 * 
	 * Metodo para vaildar string
	 * 
	 * @param string
	 *            conteúdo
	 * @return retorna <code>true</code> se a String for válida
	 */
	public static boolean validaString(String string) {
		try {
			string.equals("null");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Endereço da url salva no arquivo properties
	 * 
	 * @return retorna a url
	 */
	public static String enderecoPadraoComArquivo() {
		try {
			propertiesSystem.load(new FileInputStream(ARQUIVO_PROPERTIES));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Verifique arquivo system.properties", "Erro",
					JOptionPane.INFORMATION_MESSAGE);
			return "";
		}
		return propertiesSystem.getProperty("url.svn.file");
	}

	/**
	 * 
	 * Endereço padrão sem o arquivo
	 * 
	 * @return retorna a url sem o arquivo
	 */
	public static String enderecoPadrao() {
		return validaURL(enderecoPadraoComArquivo());
	}

	/**
	 * 
	 * Retira o arquivo no final da url
	 * 
	 * @param url
	 * @return retorna a url sem o arquivo
	 */
	public static String validaURL(String url) {
		String ret;
		try {
			ret = url.substring(0, url.lastIndexOf("/"));
			return ret;
		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
	}

	/**
	 * 
	 * Retorna o número da porta do SSH salvo no properties caso não tenha
	 * retorna o padrão
	 * 
	 * @return numero da porta
	 */
	public static String getNumberPort() {
		try {
			propertiesSystem.load(new FileInputStream(ARQUIVO_PROPERTIES));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("number.port.ssh");
	}

	/**
	 * 
	 * @return retorna número de porta padrão do SSH
	 */
	public static int getNumberPortDefault() {
		return 22;
	}

	/**
	 * 
	 * @return retorn Retona nome do host ou IP salvo no properties do SSH
	 */
	public static String getHostName() {
		try {
			propertiesSystem.load(new FileInputStream(ARQUIVO_PROPERTIES));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("host.ssh");
	}

	/**
	 * 
	 * @return retorn Retona nome do usuário salvo no properties do SSH
	 */
	public static String getUserNameSsh() {
		try {
			propertiesSystem.load(new FileInputStream(ARQUIVO_PROPERTIES));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("user.ssh");
	}

	/**
	 * 
	 * @return retorn Retona nome do usuário salvo no properties do SVN
	 */
	public static String getUserNameSvn() {
		try {
			propertiesSystem.load(new FileInputStream(ARQUIVO_PROPERTIES));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("user.svn");
	}

	/**
	 * 
	 * @return retorn Retona diretorio salvo no properties do SSH
	 */
	public static String getDirSsh() {
		try {
			propertiesSystem.load(new FileInputStream(ARQUIVO_PROPERTIES));
		} catch (Exception e) {
			return "";
		}
		return propertiesSystem.getProperty("dir.ssh");
	}

	/**
	 * 
	 * Transforma <code>byte[]</code> em <code>String</code>
	 * 
	 * @param value
	 *            valor do <code>byte[]</code>
	 * @return Retorna a String
	 */
	public static String byteToString(byte[] value) {
		return stringArrayToString(Arrays.toString(value));

	}

	/**
	 * 
	 * Transforma <code>Arrays.toString(byte[])</code> em <code>String</code>
	 * 
	 * @param array
	 *            uma String no formato de Array
	 * @return Retorna string
	 */
	public static String stringArrayToString(String array) {
		return new String(stringArrayToByte(array));
	}

	/**
	 * 
	 * Transforma <code>Arrays.toString(byte[])</code> em <code>byte[]</code>
	 * 
	 * @param array
	 *            uma String no formato de Array
	 * @return Retorna byte[]
	 */
	public static byte[] stringArrayToByte(String array) {
		String[] byteValues = array.substring(1, array.length() - 1).split(",");

		byte[] bytes = new byte[byteValues.length];
		for (int i = 0, len = bytes.length; i < len; i++) {
			bytes[i] = Byte.parseByte(byteValues[i].trim());
		}
		return bytes;
	}

	/**
	 * Salva atributos do SSH no properties
	 * 
	 * @param host
	 *            nome do host ou IP
	 * @param user
	 *            nome do usuário
	 * @param dir
	 *            diretório
	 * @param porta
	 *            porta
	 */
	public static void setAtributosSsh(String host, String user, String dir, int porta) {
		try {
			FileInputStream fileInputStream = new FileInputStream(Util.ARQUIVO_PROPERTIES);
			propertiesSystem.load(new FileInputStream(Util.ARQUIVO_PROPERTIES));
			propertiesSystem.setProperty("host.ssh", host);
			propertiesSystem.setProperty("user.ssh", user);
			propertiesSystem.setProperty("dir.ssh", dir);
			propertiesSystem.setProperty("number.port", String.valueOf(porta));
			File file = new File(ARQUIVO_PROPERTIES);
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
	 * Salva usuário no properties
	 * 
	 * @param user
	 *            nome do usuário
	 */
	public static void setUserSvn(String user) {
		try {
			FileInputStream fileInputStream = new FileInputStream(Util.ARQUIVO_PROPERTIES);
			propertiesSystem.load(new FileInputStream(Util.ARQUIVO_PROPERTIES));
			if (propertiesSystem.getProperty("user.svn") != user) {
				propertiesSystem.setProperty("user.svn", user);
				File file = new File(ARQUIVO_PROPERTIES);
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
