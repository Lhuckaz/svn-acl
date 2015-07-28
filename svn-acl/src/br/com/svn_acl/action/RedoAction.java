package br.com.svn_acl.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import br.com.svn_acl.gui.SvnAclGUI;

/**
 * 
 * Classe responsável pela ação da tecla Ctrl + Y para avançar uma ação
 * realizada pelo usuário
 * 
 * @author Lhuckaz
 *
 */
@SuppressWarnings("serial")
public class RedoAction extends AbstractAction {

	private SvnAclGUI svnAclGUI;

	/**
	 * 
	 * Construtor da classe {@link RedoAction}
	 * 
	 * @param svnAclGUI
	 *            interface principal
	 * @param text
	 *            texto
	 * @param desc
	 *            descrição
	 */
	public RedoAction(SvnAclGUI svnAclGUI, String text, String desc) {
		super(text);
		this.svnAclGUI = svnAclGUI;
		putValue(SHORT_DESCRIPTION, desc);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		svnAclGUI.avancaArquivo();
		SvnAclGUI.habilitaRetorno();
		setEnabled(false);
	}
}
