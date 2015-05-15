package br.com.svn_acl.listener;

import java.util.List;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.gui.SvnAclGUI;

public class ListaGrupoListener implements ListSelectionListener {

	private SvnAclGUI svnAclGUI;

	public ListaGrupoListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<?> lista = (JList<?>) e.getSource();
		String grupoSelecionado = (String) lista.getSelectedValue();
		svnAclGUI.setGrupoSelecionado(grupoSelecionado);
		atualizaUsuarios(grupoSelecionado);
	}

	private void atualizaUsuarios(String grupoSelecionado) {
		List<String> listaUsuariosGrupo = svnAclGUI.getGerenciadorDeGrupos().listaUsuariosGrupo(grupoSelecionado);
		svnAclGUI.setUsuariosDoGrupo(listaUsuariosGrupo);
		svnAclGUI.atualizaUsuarios();
	}

}
