package br.com.svn_acl.listener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.gui.SvnAclGUI;

/**
 * 
 * Classe ouvinte das mudan�as da {@link JList} para alterar as permiss�es
 * 
 * @author Lhuckaz
 *
 */
public class ListaPermissoesListener implements ListSelectionListener {

	private SvnAclGUI svnAclGUI;

	public ListaPermissoesListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		JList<?> lista = (JList<?>) e.getSource();
		String permissoesSelecionado = (String) lista.getSelectedValue();
		if (permissoesSelecionado != null) {
			svnAclGUI.setPermissoesSelecionada(permissoesSelecionado);
		} else {
			svnAclGUI.setPermissoesSelecionada("");

		}
	}

}
