package br.com.svn_acl.listener;

import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.gui.SvnAclGUI;

/**
 * 
 * Classe ouvinte das mudanças da {@link JList} para alterar os usuários
 * 
 * @author Lhuckaz
 *
 */
public class ListaUsuariosListener implements ListSelectionListener {

	private SvnAclGUI svnAclGUI;

	public ListaUsuariosListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		JList<?> lista = (JList<?>) e.getSource();
		ArrayList<String> usuariosSelecionados = null;
		try {
			usuariosSelecionados = (ArrayList<String>) lista.getSelectedValuesList();
		} catch (ClassCastException ex) {
			// Setar usuariosSelecionados como Array vazia
		}
		if (usuariosSelecionados != null) {
			svnAclGUI.setUsuariosSelecionados(usuariosSelecionados);
		} else {
			svnAclGUI.setUsuariosSelecionados(new ArrayList<String>());

		}
	}
}
