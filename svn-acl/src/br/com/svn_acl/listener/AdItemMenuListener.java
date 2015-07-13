package br.com.svn_acl.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.com.svn_acl.gui.AdConfigura;
import br.com.svn_acl.gui.SvnAclGUI;

public class AdItemMenuListener implements ActionListener {

	private SvnAclGUI svnAclGUI;

	public AdItemMenuListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new AdConfigura(svnAclGUI);
		
	}

}
