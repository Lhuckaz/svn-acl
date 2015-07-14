package br.com.svn_acl.listener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.gui.SvnAclGUI;

/**
 * 
 * Classe ouvinte das mudan�as da {@link JList} para alterar os usu�rios
 * 
 * @author Lhuckaz
 *
 */
public class ListaUsuariosListener implements ListSelectionListener {

	private SvnAclGUI svnAclGUI;

	public ListaUsuariosListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		JList<?> lista = (JList<?>) e.getSource();
		String usuarioSelecionado = (String) lista.getSelectedValue();
		if (usuarioSelecionado != null) {
			svnAclGUI.setUsuarioSelecionado(usuarioSelecionado);
		} else {
			svnAclGUI.setUsuarioSelecionado("");

		}
	}

}
