package br.com.svn_acl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class Criptografa {
	public static final String ALGORITHM = "RSA";
	/** * Local da chave privada no sistema de arquivos. */
	public static final String PATH_CHAVE_PRIVADA = "private.key";
	/** * Local da chave pública no sistema de arquivos. */
	public static final String PATH_CHAVE_PUBLICA = "public.key";

	/**
	 * * Gera a chave que contém um par de chave Privada e Pública usando 1025
	 * bytes. * Armazena o conjunto de chaves nos arquivos private.key e
	 * public.key
	 */
	public static void geraChave() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			keyGen.initialize(1024);
			final KeyPair key = keyGen.generateKeyPair();
			File chavePrivadaFile = new File(PATH_CHAVE_PRIVADA);
			File chavePublicaFile = new File(PATH_CHAVE_PUBLICA);

			if (chavePrivadaFile.getParentFile() != null) {
				chavePrivadaFile.getParentFile().mkdirs();
			}
			chavePrivadaFile.createNewFile();
			if (chavePublicaFile.getParentFile() != null) {
				chavePublicaFile.getParentFile().mkdirs();
			}
			chavePublicaFile.createNewFile();

			OutputStream chavePublicaOS = new ObjectOutputStream(new FileOutputStream(chavePublicaFile));
			((ObjectOutputStream) chavePublicaOS).writeObject(key.getPublic());
			chavePublicaOS.close();
			// Salva a Chave Privada no arquivo Object
			OutputStream chavePrivadaOS = new ObjectOutputStream(new FileOutputStream(chavePrivadaFile));
			((ObjectOutputStream) chavePrivadaOS).writeObject(key.getPrivate());
			chavePrivadaOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** * Verifica se o par de chaves Pública e Privada já foram geradas. */
	public static boolean verificaSeExisteChavesNoSO() {
		File chavePrivada = new File(PATH_CHAVE_PRIVADA);
		File chavePublica = new File(PATH_CHAVE_PUBLICA);
		if (chavePrivada.exists() && chavePublica.exists()) {
			return true;
		}
		return false;
	}

	/** * Criptografa o texto puro usando chave pública. */
	public static byte[] criptografa(String texto, PublicKey chave) {
		byte[] cipherText = null;
		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, chave);
			cipherText = cipher.doFinal(texto.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	/** * Decriptografa o texto puro usando chave privada. */
	public static String decriptografa(byte[] texto, PrivateKey chave) {
		byte[] dectyptedText = null;
		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, chave);
			dectyptedText = cipher.doFinal(texto);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new String(dectyptedText);
	}

}
