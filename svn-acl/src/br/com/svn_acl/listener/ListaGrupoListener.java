package br.com.svn_acl.listener;

import java.util.Collections;
import java.util.List;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.gui.SvnAclGUI;
import br.com.svn_acl.util.NaturalOrderComparatorStringInsensitive;

/**
 * 
 * Classe ouvinte das mudanças da {@link JList} para alterar os grupos
 * 
 * @author Lhuckaz
 *
 */
public class ListaGrupoListener implements ListSelectionListener {

	private SvnAclGUI svnAclGUI;

	public ListaGrupoListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		JList<?> lista = (JList<?>) e.getSource();
		if ((String) lista.getSelectedValue() != null) {
			String grupoSelecionado = (String) lista.getSelectedValue();
			svnAclGUI.setGrupoSelecionado(grupoSelecionado);
			atualizaUsuarios(grupoSelecionado);
		}
	}

	/**
	 * 
	 * Atualiza os usuários quando o grupo é alterado
	 * 
	 * @param grupoSelecionado
	 *            grupo selecionado
	 */
	public void atualizaUsuarios(String grupoSelecionado) {
		List<String> listaUsuariosGrupo = svnAclGUI.getGerenciadorDeGrupos().listaUsuariosGrupo(grupoSelecionado);
		svnAclGUI.setUsuariosDoGrupo(listaUsuariosGrupo);
		svnAclGUI.atualizaUsuarios();
		List<String> listarGrupos = svnAclGUI.getGerenciadorDeGrupos().listarGrupos();
		// Organiza em ordem alfabetica para comparação
		Collections.sort(listarGrupos, new NaturalOrderComparatorStringInsensitive());
		if (!listarGrupos.equals(svnAclGUI.getListarGrupos())) {
			svnAclGUI.atualizaListaGrupos();
			svnAclGUI.atualizaGrupos();
		}
	}

}
