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
 * Classe ouvinte das mudan�as da {@link JList} para alterar os diret�rios
 * 
 * @author Lhuckaz
 *
 */
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

	/**
	 * 
	 * Atualiza as permiss�es quando a diret�rio � alterado
	 * 
	 * @param diretorioSelecionado
	 *            diret�rio selecionado
	 */
	public void atualizaPermissoes(String diretorioSelecionado) {
		List<String> listaPermissoesDiretorio = svnAclGUI.getGerenciadorDePermissoes()
				.listaGruposEUserESuasPermissoesDeUmDiretorio(diretorioSelecionado);
		svnAclGUI.setPermissoesDoDiretorio(listaPermissoesDiretorio);
		svnAclGUI.atualizaPermissoes();
		List<String> listaDiretorios = svnAclGUI.getGerenciadorDePermissoes().listaDiretorios();
		// Organiza em ordem alfabetica para compara��o
		Collections.sort(listaDiretorios, new NaturalOrderComparatorStringInsensitive());
		if (!listaDiretorios.equals(svnAclGUI.getListarDiretorios())) {
			svnAclGUI.atualizaListaDiretorios();
			svnAclGUI.atualizaDiretorios();
		}
	}

}
