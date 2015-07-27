package br.com.svn_acl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * Classe responsável pela criptografia
 * 
 * @author Lhuckaz
 *
 */
public class Criptografa {

	public static final String AES = "AES";
	public static final String KEY_FILE = "private.key";

	public static String criptografa(String texto) {
		try {
			return encrypt(texto);
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String decriptografa(String texto) {
		try {
			return decrypt(texto);
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private static String decrypt(String texto) throws IOException, GeneralSecurityException {
		File keyFile = new File(KEY_FILE);
		SecretKeySpec sks = getSecretKeySpec(keyFile);
		Cipher cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.DECRYPT_MODE, sks);
		byte[] decrypted = cipher.doFinal(hexStringToByteArray(texto));
		return new String(decrypted);
	}

	private static String encrypt(String texto) throws GeneralSecurityException, IOException {
		File keyFile = new File(KEY_FILE);
		if (!keyFile.exists()) {
			KeyGenerator keyGen = KeyGenerator.getInstance(AES);
			keyGen.init(128);
			SecretKey sk = keyGen.generateKey();
			FileWriter fw = new FileWriter(keyFile);
			fw.write(byteArrayToHexString(sk.getEncoded()));
			fw.flush();
			fw.close();
		}

		SecretKeySpec sks = getSecretKeySpec(keyFile);
		Cipher cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
		byte[] encrypted = cipher.doFinal(texto.getBytes());
		return byteArrayToHexString(encrypted);
	}

	private static SecretKeySpec getSecretKeySpec(File keyFile) throws IOException {
		byte[] key = readKeyFile(keyFile);
		SecretKeySpec sks = new SecretKeySpec(key, AES);
		return sks;
	}

	private static byte[] readKeyFile(File keyFile) throws IOException {
		FileReader fr = new FileReader(keyFile);
		BufferedReader leitor = new BufferedReader(fr);
		String keyValue = leitor.readLine();
		fr.close();
		leitor.close();
		return hexStringToByteArray(keyValue);
	}

	private static byte[] hexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

}
