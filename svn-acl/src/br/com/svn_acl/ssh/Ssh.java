package br.com.svn_acl.ssh;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.activity.InvalidActivityException;

import br.com.svn_acl.util.Util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 
 * Classe responsável por transferir e importar arquivos através de conexão SSH
 * 
 * @author Lhuckaz
 *
 */
public class Ssh {

	/**
	 * 
	 * Transfere o arquivo através de conexão SSH
	 * 
	 * @param host
	 *            nome de host ou IP
	 * @param user
	 *            nome de usuário
	 * @param password
	 *            senha
	 * @param dir
	 *            diretório
	 * @return retorna <code>true</code> se conexão foi bem sucedida
	 * @throws Exception
	 *             lança caso ocorra algum erro de conexão
	 */
	public boolean transfere(String host, String user, String password, String dir) throws Exception {
		return transfere(host, user, password, dir, Util.getNumberPortDefault(), Util.FILE);
	}

	/**
	 * 
	 * Transfere o arquivo através de conexão SSH
	 * 
	 * @param host
	 *            nome de host ou IP
	 * @param user
	 *            nome de usuário
	 * @param password
	 *            senha
	 * @param dir
	 *            diretório
	 * @param porta
	 *            número da porta
	 * @param file
	 *            nome do arquivo para ser salvo
	 * @return retorna <code>true</code> se conexão foi bem sucedida
	 * @throws Exception
	 *             lança caso ocorra algum erro de conexão
	 */
	public boolean transfere(String host, String user, String password, String dir, int porta, String file)
			throws Exception {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		FileInputStream fileInputStream = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, porta);
			session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(dir);
			File f = new File("~svn-saida.acl");
			fileInputStream = new FileInputStream(f);
			channelSftp.put(fileInputStream, file);
			fileInputStream.close();
			channelSftp.disconnect();
			channel.disconnect();
			session.disconnect();
		} catch (JSchException e) {
			String message = e.getMessage();
			if (message.equals("Auth fail")) {
				System.out.println("Usuario ou senha inválidos");
				throw new JSchException("Auth fail");
			}
			System.out.println("Hostname ou porta inválidos");
			// Hostname invalidos
			throw new InvalidActivityException();
		} catch (SftpException e) {
			System.out.println("Diretório inválido");
			throw new SftpException(22, "Diretório inválido");
		} catch (Exception e) {
			throw new Exception();
		} finally {
			try {
				fileInputStream.close();
				channelSftp.disconnect();
				channel.disconnect();
				session.disconnect();
				session = null;
				channel = null;
				channelSftp = null;
			} catch (Exception e) {
			}
		}
		return true;

	}

	/**
	 * 
	 * Importa um arquivo através de conexão SSH
	 * 
	 * @param host
	 *            nome de host ou IP
	 * @param user
	 *            nome de usuário
	 * @param password
	 *            senha
	 * @param dir
	 *            diretório
	 * @param porta
	 *            número da porta
	 * @return retorna <code>true</code> se conexão foi bem sucedida
	 * @throws Exception lança caso ocorra algum erro de conexão
	 */
	public boolean importar(String host, String user, String password, String dir, int porta) throws Exception {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, porta);
			session.setPassword(password);
			Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			byte[] buffer = new byte[1024];
			InputStream inputStream = channelSftp.get(dir);
			bis = new BufferedInputStream(inputStream);
			OutputStream os = new FileOutputStream(Util.FILE);
			bos = new BufferedOutputStream(os);
			int readCount;
			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			channel.disconnect();
			bis.close();
			bos.close();
			session.disconnect();
		} catch (JSchException e) {
			String message = e.getMessage();
			if (message.equals("Auth fail")) {
				System.out.println("Usuario ou senha inválidos");
				throw new JSchException("Auth fail");
			}
			System.out.println("Hostname ou porta inválidos");
			// Hostname invalidos
			throw new InvalidActivityException();
		} catch (SftpException e) {
			System.out.println("Diretório inválido");
			throw new SftpException(22, "Diretório inválido");
		} catch (IOException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new Exception();
		} finally {
			try {
				channel.disconnect();
				bis.close();
				bos.close();
				session.disconnect();
				session = null;
				channel = null;
				channelSftp = null;
			} catch (Exception e) {
			}
		}
		return true;

	}
}
