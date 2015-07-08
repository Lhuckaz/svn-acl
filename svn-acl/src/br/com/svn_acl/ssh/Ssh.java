package br.com.svn_acl.ssh;

import java.io.File;
import java.io.FileInputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Ssh {
	
	public boolean transfere(String host, String user, String password, String dir) {
		return transfere(host, user, password, dir, 22);
	}

	public boolean transfere(String host, String user, String password, String dir, int porta) {
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, porta);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			channelSftp.cd(dir);
			File f = new File("~svn-saida.acl");
			channelSftp.put(new FileInputStream(f), "svn.acl");
		} catch (Exception e) {
			return false;
		}
		return true;
		
	}
	

}
