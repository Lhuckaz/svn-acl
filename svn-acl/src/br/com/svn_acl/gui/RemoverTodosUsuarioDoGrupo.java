package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.svn_acl.util.NaturalOrderComparatorStringInsensitive;

/**
 * 
 * Interface gráfica extends {@link JDialog} implements
 * {@link ListSelectionListener} ouvinte do {@link JButton}
 * "Remover de todos os grupos" no {@link JTabbedPane} "Grupos" da interface
 * principal {@link SvnAclGUI} para remove usuarios de todos os grupos
 * 
 * @author Lhuckaz
 *
 */
@SuppressWarnings("serial")
public class RemoverTodosUsuarioDoGrupo extends JDialog implements ListSelectionListener {

	private ArrayList<String> usuarios;
	protected boolean remove;
	private SvnAclGUI svnAclGUI;

	public RemoverTodosUsuarioDoGrupo(SvnAclGUI svnAclGUI) {
		super(svnAclGUI.getFrame(), "Remover de todos os grupos", true);
		this.svnAclGUI = svnAclGUI;
		this.setModal(true);
		Dimension dimensao = new Dimension(300, 300);

		JPanel jPanelPrincipalListGrupos = new JPanel(new BorderLayout());
		JPanel jPanelDeUsuarios = new JPanel();
		DefaultListModel<String> modeloUsuariosLotes = new DefaultListModel<>();
		JList<String> listaDeUsuarios = new JList<>(modeloUsuariosLotes);
		listaDeUsuarios.addListSelectionListener(this);
		JScrollPane jScrollDeUsuarios = new JScrollPane(listaDeUsuarios);
		// Definir tamanho to JScrollPane
		jScrollDeUsuarios.setPreferredSize(dimensao);
		jPanelDeUsuarios.add(jScrollDeUsuarios);
		jPanelPrincipalListGrupos.add(jPanelDeUsuarios);

		JPanel jPanelDeBotoes = new JPanel();
		JButton button = new JButton("Remover");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				remove = true;
				setVisible(false);
			}
		});
		jPanelDeBotoes.add(button);
		jPanelPrincipalListGrupos.add(jPanelDeBotoes, BorderLayout.SOUTH);

		carregaListaDeUsuarios(modeloUsuariosLotes);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().add(jPanelPrincipalListGrupos);
		pack();
		setLocationRelativeTo(svnAclGUI.getFrame());
		setModal(true);
		setVisible(true);
	}

	/**
	 * 
	 * Adiciona os usuários na {@link JList} para seleção de remover os
	 * usuários de todos os grupos
	 * 
	 * @param modeloUsuariosLotes
	 *            modelo da {@link JList}
	 */
	private void carregaListaDeUsuarios(DefaultListModel<String> modeloUsuariosLotes) {
		ArrayList<String> todosUsuarios = (ArrayList<String>) svnAclGUI.getGerenciadorDeGrupos().listarUsuarios();
		Collections.sort(todosUsuarios, new NaturalOrderComparatorStringInsensitive());
		for (String usuarios : todosUsuarios) {
			modeloUsuariosLotes.addElement(usuarios);

		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		JList<?> lista = (JList<?>) e.getSource();
		if ((String) lista.getSelectedValue() != null) {
			@SuppressWarnings("unchecked")
			ArrayList<String> usuariosSelecionados = (ArrayList<String>) lista.getSelectedValuesList();
			usuarios = new ArrayList<>();
			usuarios.addAll(usuariosSelecionados);
		}
	}

	/**
	 * 
	 * @return retorna os usuarios que foram selecionados
	 */
	public ArrayList<String> getUsuariosSelecionados() {
		return usuarios;

	}
}