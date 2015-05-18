package br.com.svn_acl.listener;

import java.util.List;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.gui.SvnAclGUI;

public class ListaDiretoriosListener implements ListSelectionListener {

	private SvnAclGUI svnAclGUI;

	public ListaDiretoriosListener(SvnAclGUI svnAclGUI) {
		this.svnAclGUI = svnAclGUI;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<?> lista = (JList<?>) e.getSource();
		String diretorioSelecionado = (String) lista.getSelectedValue();
		svnAclGUI.setDiretorioSelecionado(diretorioSelecionado);
		atualizaPermissoes(diretorioSelecionado);
	}

	private void atualizaPermissoes(String diretorioSelecionado) {
		List<String> listaPermissoesDiretorio = svnAclGUI.getGerenciadorDePermissoes().listaGruposEUserESuasPermissoesDeUmDiretorio(diretorioSelecionado);
		svnAclGUI.setPermissoesDoDiretorio(listaPermissoesDiretorio);
		svnAclGUI.atualizaPermissoes();
	}

}
