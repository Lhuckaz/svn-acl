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
		if (e.getValueIsAdjusting())
			return;
		JList<?> lista = (JList<?>) e.getSource();
		String diretorioSelecionado = (String) lista.getSelectedValue();
		if (diretorioSelecionado != null) {
			svnAclGUI.setDiretorioSelecionado(diretorioSelecionado);
			atualizaPermissoes(diretorioSelecionado);
		}
	}

	public void atualizaPermissoes(String diretorioSelecionado) {
		List<String> listaPermissoesDiretorio = svnAclGUI.getGerenciadorDePermissoes()
				.listaGruposEUserESuasPermissoesDeUmDiretorio(diretorioSelecionado);
		svnAclGUI.setPermissoesDoDiretorio(listaPermissoesDiretorio);
		svnAclGUI.atualizaPermissoes();
		if (!svnAclGUI.getGerenciadorDePermissoes().listaDiretorios().equals(svnAclGUI.getListarDiretorios())) {
			svnAclGUI.atulizaListaDiretorios();
			svnAclGUI.atualizaDiretorios();
		}
	}

}
