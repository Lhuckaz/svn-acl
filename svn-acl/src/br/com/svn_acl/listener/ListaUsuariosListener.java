package br.com.svn_acl.listener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.gui.SvnAclGUI;

public class ListaUsuariosListener implements ListSelectionListener  {
	
	private SvnAclGUI svnAclGUI;

	public ListaUsuariosListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<?> lista = (JList<?>) e.getSource();
		String usuarioSelecionado = (String) lista.getSelectedValue();
		if (usuarioSelecionado != null) {
			svnAclGUI.setUsuarioSelecionado(usuarioSelecionado);
		} else {
			svnAclGUI.setUsuarioSelecionado("");

		}
	}

}
