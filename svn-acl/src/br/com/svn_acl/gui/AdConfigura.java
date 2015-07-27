package br.com.svn_acl.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import br.com.svn_acl.ad.ActiveDirectory;
import br.com.svn_acl.util.Criptografa;
import br.com.svn_acl.util.SpringUtilities;
import br.com.svn_acl.util.Util;

/**
 * 
 * Interface gráfica extends {@link JDialog} para configurações do acesso ao
 * {@link ActiveDirectory}
 * 
 * @author Lhuckaz
 *
 */
@SuppressWarnings("serial")
public class AdConfigura extends JDialog {

	private SvnAclGUI svnAclGUI;
	JTextField dominio;
	JTextField user;
	JTextField password;
	private String dominioString = "";
	private String userString = "";

	/**
	 * 
	 * Construtor da classe {@link AdConfigura} monta a interface com
	 * {@link SpringLayout}
	 * 
	 * @param svnAclGUI
	 *            interface principal
	 */
	public AdConfigura(SvnAclGUI svnAclGUI) {
		super(svnAclGUI.getFrame(), "Configurações", true);
		this.svnAclGUI = svnAclGUI;
		Configura configura = new Configura(this);

		carregaProperties();

		JPanel principal = new JPanel();

		final String[] labels = { "Dominio: ", "Usuário: ", "Senha: " };
		int labelsLength = labels.length;

		JPanel p = new JPanel(new SpringLayout());

		JLabel l = new JLabel(labels[0], JLabel.TRAILING);
		p.add(l);
		dominio = new JTextField(50);
		dominio.setText(dominioString);
		dominio.addActionListener(configura);
		l.setLabelFor(dominio);
		p.add(dominio);

		JPanel users = new JPanel(new BorderLayout());
		JLabel l1 = new JLabel(labels[1], JLabel.TRAILING);
		p.add(l1);
		user = new JTextField(20);
		user.setText(userString);
		user.addActionListener(configura);
		l1.setLabelFor(user);
		users.add(user, BorderLayout.WEST);
		p.add(users);

		JPanel passwords = new JPanel(new BorderLayout());
		JLabel l2 = new JLabel(labels[2], JLabel.TRAILING);
		p.add(l2);
		password = new JPasswordField(20);
		password.setText(recuperaSenha());
		password.addActionListener(configura);
		l2.setLabelFor(password);
		passwords.add(password, BorderLayout.WEST);
		p.add(passwords);

		JPanel botoes = new JPanel(new BorderLayout());
		p.add(new JLabel());
		JButton button = new JButton("OK");
		button.addActionListener(configura);
		botoes.add(button, BorderLayout.EAST);
		p.add(botoes);

		// Coloca para fora do painel.
		SpringUtilities.makeCompactGrid(p, labelsLength + 1, 2, 7, 7, 7, 7);

		principal.add(p);
		getContentPane().add(principal);

		// Adiciona foco no host pois a porta ja tem valor padrao
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				dominio.requestFocus();
			}
		});

		pack();
		setLocationRelativeTo(svnAclGUI.getFrame());
		setModal(true);
		setVisible(true);
	}

	/**
	 * Carrega atributos da classes com conteúdo do properties
	 */
	private void carregaProperties() {
		Properties properties;
		try {
			properties = new Properties();
			FileInputStream fileInputStream;

			fileInputStream = new FileInputStream(new File(Util.ARQUIVO_PROPERTIES));

			properties.load(fileInputStream);
			dominioString = properties.getProperty("domain.ldap");
			userString = properties.getProperty("username.ldap");

			fileInputStream.close();
		} catch (IOException e) {
		}
		properties = null;
	}

	/**
	 * 
	 * Classe ouvinte do {@link JButton} "OK" da classe {@link AdConfigura} para
	 * conexão com AD
	 * 
	 * @author Lhuckaz
	 *
	 */
	public class Configura implements ActionListener {

		private AdConfigura adConfigura;

		public Configura(AdConfigura adConfigura) {
			this.adConfigura = adConfigura;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String dominio = adConfigura.dominio.getText();
			String user = adConfigura.user.getText();
			String password = adConfigura.password.getText();

			// Na tela de configurações de AD se caso não for preenchido nenhum
			// valor, a tela e fechada, se iver algum preenchido ele lança um
			// JOptionPane pedindo para preencher todos os campos, se estiver
			// completo ele tenta conectar no AD
			if (dominio.equals("") && user.equals("") && password.equals("")) {
				Util.gravaAtributosAd("", "", "");
				setVisible(false);
			} else if (dominio.equals("") || user.equals("") || password.equals("")) {
				JOptionPane.showMessageDialog(svnAclGUI.getFrame(), "Preencha todos os campos", "AD",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				Util.gravaAtributosAd(dominio, user, password);
				boolean verificaUsuariosAD = SvnAclGUI.verificaUsuariosAD();
				if (verificaUsuariosAD)
					setVisible(false);
			}
		}

	}

	/**
	 * 
	 * @return retorna a senha recuperada
	 */
	public static String recuperaSenha() {
		try {
			FileInputStream fileInputStream = new FileInputStream(Util.ARQUIVO_PROPERTIES);
			Util.propertiesSystem.load(fileInputStream);
			fileInputStream.close();
			String password = Util.propertiesSystem.getProperty("password.ldap");

			return Criptografa.decriptografa(password);
		} catch (Exception e) {
			return "";
		}
	}
}
