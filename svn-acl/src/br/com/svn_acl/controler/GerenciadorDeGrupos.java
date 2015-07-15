package br.com.svn_acl.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Classe responsável por gerenciar os grupos
 * 
 * @author Lhuckaz
 *
 */
public class GerenciadorDeGrupos {

	private FileReader fileReader = null;
	private BufferedReader leitor = null;
	private FileWriter fileWriter = null;
	private File file;

	public GerenciadorDeGrupos(String path) {
		this.file = new File(path);
	}

	public GerenciadorDeGrupos(File file) {
		this.file = file;
	}

	/**
	 * Acessa o arquivo e verifica se o grupo existe
	 * 
	 * @param grupo
	 *            nome do grupo
	 * @return returna <code>true<code> se o grupo existe
	 */
	public boolean grupoExiste(String grupo) {
		boolean contem = false;
		if (grupo.equals("")) {
			System.out.println("Grupo \"\" nao foi encontrado");
			return false;
		}
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.startsWith("@")) {
					break;
				}
				// if criado antes para verificar se o grupo existe
				if (line.startsWith(grupo + " ")) {
					return true;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!contem) {
			System.out.println("Grupo \"" + grupo + "\" nao foi encontrado");
		}
		return false;
	}

	/**
	 * 
	 * Acessa o arquivo e verifica se o usuario existe
	 * 
	 * @param usuario
	 *            nome do usuário
	 * @return retorna <code>true<code> se o grupo existe
	 */
	public boolean usuarioExiste(String usuario) {
		boolean contem = false;
		Collection<String> todosOsUsuarios = new HashSet<>();
		if (usuario.equals("")) {
			System.out.println("Usuario \"\" nao foi encontrado");
			return false;
		}
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.startsWith("@")) {
					break;
				}
				if (line.matches("^\\s$")) {
					line = "";
				}
				List<String> listaUsuariosDaLinha = listaUsuariosDaLinha(line);
				if (!(listaUsuariosDaLinha.size() == 0)) {
					if (!(listaUsuariosDaLinha.size() == 1 && listaUsuariosDaLinha.get(0).equals(""))) {
						todosOsUsuarios.addAll(listaUsuariosDaLinha);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (todosOsUsuarios.contains(usuario)) {
			return true;
		}
		if (!contem) {
			System.out.println("Usuario \"" + usuario + "\" nao foi encontrado");
		}
		return false;
	}

	/**
	 * 
	 * @param grupo
	 *            nome do grupo
	 * @param usuario
	 *            nome do usuario
	 * @return retorna se <code>true<code> se o usuario existe
	 */
	public boolean procuraSeUsuarioPartDoGrupo(String grupo, String usuario) {
		if (!grupoExiste(grupo)) {
			return false;
		}
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				// if criado antes para verificar se o grupo existe, verifica se
				// contem o usuario
				if (line.startsWith(grupo + " ") && line.contains(" " + usuario + ",")) {
					return true;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 
	 * Acessa o arquivo e retorna e lista de usuario de um grupo
	 * 
	 * @param grupo
	 *            nome do grupo
	 * @return retorna a lista de usuarios do grupo
	 */
	public List<String> listaUsuariosGrupo(String grupo) {
		String[] split = {};
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				// linha contem o nome do grupo, linha nao pode comecao com @,
				// nem ter o nome diferente " ", nao pode ser comentario
				if (line.startsWith("@")) {
					break;
				}
				if (line.startsWith(grupo + " ")) {
					// algumas linhas nao possuem espacos apos o sinal =
					line = line.equals(grupo + " =") ? line + " " : line;
					split = line.split("=");
					split = split[1].trim().replaceAll(" ", "").split(",");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (split.length == 0) {
			System.out.println("Grupo nao foi encontrado");
		}
		return Arrays.asList(split);
	}

	/**
	 * 
	 * Acessa o arquivo e adiciona um usuario no grupo
	 * 
	 * @param grupo
	 *            nome do grupo
	 * @param usuario
	 *            nome do usuario
	 * @return returna <code>true</code> se o usuário foi adicionado
	 */
	public boolean adicionaUsuarioNoGrupo(String grupo, String usuario) {
		if (!grupoExiste(grupo)) {
			return false;
		}
		if (!procuraSeUsuarioPartDoGrupo(grupo, usuario)) {
			try {
				fileReader = new FileReader(file);
				fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
				leitor = new BufferedReader(fileReader);
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.startsWith(grupo + " ")) {
						fileWriter.write(line + " " + usuario + "," + "\n");
					} else {
						fileWriter.write(line + "\n");
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fileWriter.close();
					fileReader.close();
					leitor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Acessa o arquivo e remove um usuario do grupo
	 * 
	 * @param grupo
	 *            nome do grupo
	 * @param usuario
	 *            nome do usuario
	 * @return returna <code>true</code> se o usuário foi removido
	 */
	public boolean removeUsuarioDoGrupo(String grupo, String usuario) {
		if (!grupoExiste(grupo)) {
			return false;
		}
		List<String> usuarios = new ArrayList<>(listaUsuariosGrupo(grupo));

		// verifica se usuario exista no grupo, pois quando grupo nao tem nenhum
		// usuario retorna um usuario ""(vazio)
		if (usuarios.size() == 0) {
			System.out.println("Usuario nao participa do grupo: " + grupo);
			return false;
		}
		if (usuarios.size() == 1) {
			if (usuarios.get(0).equals(" ") || usuarios.get(0).equals("")) {
				System.out.println("Usuario nao participa do grupo: " + grupo);
				return false;
			}
		}

		usuarios.remove(usuario);
		if (procuraSeUsuarioPartDoGrupo(grupo, usuario)) {
			try {
				fileReader = new FileReader(file);
				fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
				leitor = new BufferedReader(fileReader);
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.startsWith(grupo + " ")) {
						StringBuffer string = new StringBuffer();
						string.append(grupo + " =");
						for (String usuariox : usuarios) {
							string.append(" " + usuariox + ",");
						}
						fileWriter.write(string + "\n");
					} else {
						fileWriter.write(line + "\n");
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fileWriter.close();
					fileReader.close();
					leitor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return true;
	}

	/**
	 * 
	 * @return retorna a lista de grupos
	 */
	public List<String> listarGrupos() {
		List<String> grupos = new ArrayList<>();
		String[] split = {};
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				// Se chegar nas permissoes programa para de ler para nao
				// confundir usuarios com grupos
				if (line.startsWith("@")) {
					break;
				}
				if (line.matches("\\w{1,}\\s{0,}=\\s{0,}.{0,}")) {
					split = line.split("=");
					grupos.add(split[0].replaceAll(" ", ""));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (grupos.size() == 0) {
			System.out.println("Grupo nao foi encontrado");
		}
		return grupos;
	}

	/**
	 * 
	 * @return retorna a lista de usuários
	 */
	public List<String> listarUsuarios() {
		Collection<String> todosOsUsuarios = new HashSet<>();
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.startsWith("@")) {
					break;
				}
				if (line.matches("^\\s$")) {
					line = "";
				}
				List<String> listaUsuariosDaLinha = listaUsuariosDaLinha(line);
				if (!(listaUsuariosDaLinha.size() == 0)) {
					if (!(listaUsuariosDaLinha.size() == 1 && listaUsuariosDaLinha.get(0).equals(""))) {
						todosOsUsuarios.addAll(listaUsuariosDaLinha);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ArrayList<String> todos = new ArrayList<>(todosOsUsuarios);
		Collections.sort(todos);
		return todos;
	}

	/**
	 * 
	 * @param usuario
	 *            nome do usuario
	 * @return retorna lista de grupos que contém o usuario
	 */
	public List<String> listaGruposDoUsuario(String usuario) {
		List<String> grupos = new ArrayList<>();
		boolean existe = false;
		for (String grupo : listarGrupos()) {
			if (procuraSeUsuarioPartDoGrupo(grupo, usuario)) {
				existe = true;
				grupos.add(grupo);
			}
		}
		if (!existe) {
			System.out.println("Usuario \"" + usuario + "\" ainda nao existe");
		}
		return grupos;
	}

	/**
	 * Remove o usuario de todos os grupos e suas permissões
	 * 
	 * @param usuario
	 */
	public void removeUsuarioDeTodosOsGrupos(String usuario) {
		List<String> listaGruposDoUsuario = listaGruposDoUsuario(usuario);
		try {
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				String grupo = retornaGrupoDaLinha(line);
				if (listaGruposDoUsuario.contains(grupo)) {
					List<String> listaUsuarioGrupo = new ArrayList<>(listaUsuariosDaLinha(line));
					listaUsuarioGrupo.remove(usuario);
					StringBuffer string = new StringBuffer();
					string.append(grupo + " =");
					for (String usuariox : listaUsuarioGrupo) {
						string.append(" " + usuariox + ",");
					}
					fileWriter.write(string + "\n");
					System.out.println("Removido do Grupo: " + grupo);
				} else if (line.matches(usuario + "\\s{0,}=\\s{0,}.{0,}")) {
					// nao faz nada, para remover caso o usuario tenha um
					// permissao associada a ele proprio
				} else {
					fileWriter.write(line + "\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Comando para auxiliar. Lista usuários de uma linha do arquivo
	 * 
	 * @param line
	 *            linha do arquivo
	 * @return retorna os usuários
	 */
	private List<String> listaUsuariosDaLinha(String line) {
		String[] split = {};
		if (line.startsWith("@")) {
			// nao faz nada pois nao e grupo
		}
		if (line.startsWith(retornaGrupoDaLinha(line) + " ")) {
			// algumas linhas nao possuem espacos apos o sinal =
			line = line.equals(retornaGrupoDaLinha(line) + " =") ? line + " " : line;
			split = line.split("=");
			split = split[1].trim().replaceAll(" ", "").split(",");
		}
		return Arrays.asList(split);
	}

	/**
	 * 
	 * Comando para auxiliar. Retorna grupo de uma linha do arquivo
	 * 
	 * @param line
	 *            linha do arquivo
	 * @return retorna o grupo
	 */
	private String retornaGrupoDaLinha(String line) {
		if (line.startsWith("@")) {
			return "";
		}
		String[] split;
		// Regex identicar se linha e de permissoes
		if (line.matches("\\w{1,}\\s{0,}=\\s{0,}w$") || line.matches("\\w{1,}\\s{0,}=\\s{0,}rw$")
				|| line.matches("\\w{1,}\\s{0,}=\\s{0,}w$")) {
			return "";
		}
		if (line.matches("\\w{1,}\\s{0,}=\\s{0,}.{0,}")) {
			split = line.split("=");
			return split[0].trim();
		}
		return "";
	}

	/**
	 * Comando para auxiliar o metodo
	 * {@link #adicionaUsuarioNosGrupos(List, String) adicionaUsuarioNosGrupos}.
	 * Remove grupos inexistente
	 * 
	 * @param grupos
	 *            lista de grupos
	 * @return retorna uma nova lista sem os grupo que nao existem
	 */
	private List<String> removeGruposInexistentes(List<String> grupos) {
		ArrayList<String> deletarGrupoInexistentes = new ArrayList<>();
		for (String grupo : grupos) {
			if (!grupoExiste(grupo)) {
				deletarGrupoInexistentes.add(grupo);
			}
		}
		for (String grupo : deletarGrupoInexistentes) {
			grupos.remove(grupo);
		}
		return grupos;

	}

	/**
	 * 
	 * Adiciona o usuário em uma lista de grupos
	 * 
	 * @param gruposAdd
	 *            lista de grupo
	 * @param usuario
	 *            nome do usuário
	 */
	public void adicionaUsuarioNosGrupos(List<String> gruposAdd, String usuario) {
		// verificar se grupo existe
		ArrayList<String> grupos = new ArrayList<>(removeGruposInexistentes(gruposAdd));
		// remove grupos que usuario ja participa
		for (String grupo : listaGruposDoUsuario(usuario)) {
			grupos.remove(grupo);
		}
		try {
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (grupos.contains(retornaGrupoDaLinha(line))) {
					fileWriter.write(line + " " + usuario + "," + "\n");
				} else {
					fileWriter.write(line + "\n");
				}
			}
			System.out.println("Adicionado " + usuario + " ao grupos " + grupos);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Remove o grupo e suas permissões
	 * 
	 * @param grupo
	 *            nome do grupo
	 */
	public void removeGrupoEPermissoes(String grupo) {
		try {
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				// nao escreve a linha que contêm o grupo
				if (retornaGrupoDaLinha(line).equals(grupo) || line.startsWith("@" + grupo)) {
					// nao escreve a linha que está vazia se estiver após do
					// grupo
					if ((line = leitor.readLine()).equals("")) {
					} else {
						fileWriter.write(line + "\n");
					}
				} else {
					fileWriter.write(line + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adiciona o grupo
	 * 
	 * @param grupo
	 *            nome do grupo
	 * @return retorna <code>true</code> se grupo foi adicionado
	 */
	public boolean adicionaGrupo(String grupo) {
		if (grupoExiste(grupo)) {
			System.out.println("Grupo já existe");
			return false;
		}
		List<String> listarGrupos = listarGrupos();
		String ultimoGrupo = listarGrupos.get(listarGrupos.size() - 1);
		try {
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				fileWriter.write(line + "\n");
				if (line.startsWith(ultimoGrupo)) {
					fileWriter.write("\n" + grupo + " =\n");
				}
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
				leitor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
