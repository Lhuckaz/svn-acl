package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.com.svn_acl.gui.SshTransfere;
import br.com.svn_acl.gui.SvnAclGUI;

public class SshItemMenuListener implements ActionListener {

	private SvnAclGUI svnAclGUI;

	public SshItemMenuListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new SshTransfere(svnAclGUI);
	}

}
