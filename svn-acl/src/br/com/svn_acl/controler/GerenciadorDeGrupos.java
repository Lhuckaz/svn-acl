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

	public boolean adicionaUsuarioNoGrupo(String grupo, String usuario) {
		if (!grupoExiste(grupo)) {
			return false;
		}
		if (!procuraSeUsuarioPartDoGrupo(grupo, usuario)) {
			try {
				fileReader = new FileReader(file);
				fileWriter = new FileWriter(new File("svn2.acl"));
				leitor = new BufferedReader(fileReader);
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.startsWith(grupo + " ")) {
						fileWriter.write(line + " " + usuario + "," + "\r\n");
					} else {
						fileWriter.write(line + "\r\n");
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fileWriter.close();
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

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
				fileWriter = new FileWriter(new File("svn2.acl"));
				leitor = new BufferedReader(fileReader);
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.startsWith(grupo + " ")) {
						StringBuffer string = new StringBuffer();
						string.append(grupo + " =");
						for (String usuariox : usuarios) {
							string.append(" " + usuariox + ",");
						}
						fileWriter.write(string + "\r\n");
					} else {
						fileWriter.write(line + "\r\n");
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fileWriter.close();
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return true;
	}

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

	public void removeUsuarioDeTodosOsGrupos(String usuario) {
		List<String> listaGruposDoUsuario = listaGruposDoUsuario(usuario);
		try {
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(new File("svn2.acl"));
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
					fileWriter.write(string + "\r\n");
					System.out.println("Removido do Grupo: " + grupo);
				} else if (line.matches(usuario + "\\s{0,}=\\s{0,}.{0,}")) {
					// nao faz nada, para remover caso o usuario tenha um
					// permissao associada a ele proprio
				} else {
					fileWriter.write(line + "\r\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

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

	private String retornaGrupoDaLinha(String line) {
		if (line.startsWith("@")) {
			return "";
		}
		String[] split;
		// TODO \w{1,}\s{0,}=\s{0,}[[r]|[rw]]
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

	public void adicionaUsuarioNosGrupos(List<String> gruposAdd, String usuario) {
		// verificar se grupo existe
		ArrayList<String> grupos = new ArrayList<>(removeGruposInexistentes(gruposAdd));
		// remove grupos que usuario ja participa
		for (String grupo : listaGruposDoUsuario(usuario)) {
			grupos.remove(grupo);
		}
		try {
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(new File("svn2.acl"));
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (grupos.contains(retornaGrupoDaLinha(line))) {
					fileWriter.write(line + " " + usuario + "," + "\r\n");
				} else {
					fileWriter.write(line + "\r\n");
				}
			}
			System.out.println("Adicionado " + usuario + " ao grupos " + grupos);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void removeGrupoEPermissoes(String grupo) {
		try {
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(new File("svn2.acl"));
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (retornaGrupoDaLinha(line).equals(grupo) || line.startsWith("@" + grupo)) {
					// nao escreve
				} else {
					fileWriter.write(line + "\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
