package br.com.svn_acl.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import br.com.svn_acl.gui.SvnAclGUI;

@SuppressWarnings("serial")
public class UndoAction extends AbstractAction {

	private SvnAclGUI svnAclGUI;

	public UndoAction(SvnAclGUI svnAclGUI, String text, String desc) {
		super(text);
		this.svnAclGUI = svnAclGUI;
		putValue(SHORT_DESCRIPTION, desc);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("control Z");
		svnAclGUI.retornaArquivo();
		SvnAclGUI.habilitaAvanco();
		setEnabled(false);
	}
}
