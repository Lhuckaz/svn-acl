package br.com.svn_acl.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import br.com.svn_acl.gui.SvnAclGUI;

@SuppressWarnings("serial")
public class RedoAction extends AbstractAction {

	private SvnAclGUI svnAclGUI;

	public RedoAction(SvnAclGUI svnAclGUI, String text, String desc) {
		super(text);
		this.svnAclGUI = svnAclGUI;
		putValue(SHORT_DESCRIPTION, desc);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("control Y");
		svnAclGUI.avancaArquivo();
		SvnAclGUI.habilitaRetorno();
		setEnabled(false);
	}
}
