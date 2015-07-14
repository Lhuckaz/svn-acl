package br.com.svn_acl.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe respons�vel por gerenciar as permiss�es
 * 
 * @author Lhuckaz
 *
 */
public class GerenciadorDePermissoes {
	public static final String LEITURA = "r";
	public static final String ESCRITA = "w";
	public static final String LEITURA_ESCRITA = "rw";

	private FileReader fileReader = null;
	private BufferedReader leitor = null;
	private FileWriter fileWriter = null;
	private File file;

	public GerenciadorDePermissoes(String path) {
		this.file = new File(path);
	}

	public GerenciadorDePermissoes(File file) {
		this.file = file;
	}

	/**
	 * 
	 * @return retorna lista de diretorios contido no arquivo
	 */
	public List<String> listaDiretorios() {
		List<String> diretorios = new ArrayList<>();
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.matches("^.*.+:.*") && line.startsWith("[")) {
					String diretorio = line.substring(1, line.length() - 1);
					diretorios.add(diretorio);
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
		return diretorios;
	}

	/**
	 * 
	 * Lista grupos de um determinado diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @return retorna a lista de grupos
	 */
	public List<String> listaGruposDeUmDiretorio(String diretorio) {
		List<String> grupos = new ArrayList<>(listaGruposEUserESuasPermissoesDeUmDiretorio(diretorio));
		List<String> gruposRetorno = new ArrayList<>();
		for (String grupo : grupos) {
			String[] split = grupo.trim().replaceAll(" ", "").split("=");
			if (split[0].startsWith("@")) {
				gruposRetorno.add(split[0]);
			}
		}
		return gruposRetorno;
	}

	/**
	 * 
	 * Lista usu�rios de um determinado diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @return retorna a lista de usu�rios
	 */
	public List<String> listaUsuariosDeUmDiretorio(String diretorio) {
		List<String> grupos = new ArrayList<>(listaGruposEUserESuasPermissoesDeUmDiretorio(diretorio));
		List<String> gruposRetorno = new ArrayList<>();
		for (String grupo : grupos) {
			String[] split = grupo.trim().replaceAll(" ", "").split("=");
			if (!split[0].startsWith("@")) {
				gruposRetorno.add(split[0]);
			}
		}
		return gruposRetorno;
	}

	/**
	 * 
	 * Lista grupos e usu�rios e euas permiss�es de um determinado diretorio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @return retorna a lista de grupos e usu�rios
	 */
	public List<String> listaGruposEUserESuasPermissoesDeUmDiretorio(String diretorio) {
		List<String> grupos = new ArrayList<>();
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
					while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
					// && !line.trim().equals("")
					) {
						if (!line.equals("")) {
							grupos.add(line);
						}
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
		return grupos;
	}

	/**
	 * 
	 * Altera permissoes do grupo do diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param grupo
	 *            nome do grupo
	 * @param permissao
	 *            permiss�o para que ir� ser colocada no grupo
	 * @return retorna <code>true</code> caso a alterac�o tenha sido realizada
	 */
	public boolean alteraPermissoesDoGrupoDoDir(String diretorio, String grupo, String permissao) {
		if (!verificaSeDiretorioExiste(diretorio)) {
			System.out.println("Diretorio \"" + diretorio + "\" nao existe");
			return false;
		} else if (!verificaSeGrupoOuUserExisteNoDiretorio(diretorio, grupo)) {
			System.out.println("Grupo \"" + grupo + "\" nao existe no diretorio \"" + diretorio + "\"");
			return false;
		} else {
			try {
				fileReader = new FileReader(file);
				leitor = new BufferedReader(fileReader);
				fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\n");
						while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
								&& !line.trim().equals("")) {
							if (line.contains(grupo + " ")) {
								String[] linha = line.trim().replace(" ", "").split("=");
								fileWriter.write(linha[0] + " = " + permissao + "\n");
							} else {
								fileWriter.write(line + "\n");
							}
						}
					}
					if (!(line == null))
						fileWriter.write(line + "\n");
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
		return true;
	}

	/**
	 * 
	 * Comando para auxiliar. Verifica se grupo ou usu�rio existe no diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param grupo
	 *            nome do grupo ou usu�rio
	 * @return retorna <code>true</code> caso o grupo ou usu�rio pertencer
	 */
	private boolean verificaSeGrupoOuUserExisteNoDiretorio(String diretorio, String grupo) {
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
					while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
					// && !line.trim().equals("")
					) {
						if (line.contains(grupo + " ")) {
							return true;
						}
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
		return false;
	}

	/**
	 * 
	 * Comando para auxiliar. Verifica se grupo existe no diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param grupo
	 *            nome do grupo
	 * @return retorna <code>true</code> caso o grupo pertencer
	 */
	private boolean verificaSeGrupoExisteNoDiretorio(String diretorio, String grupo) {
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
					while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
					// && !line.trim().equals("")
					) {
						if (line.contains("@" + grupo + " ")) {
							return true;
						}
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
		return false;
	}

	/**
	 * 
	 * Comando para auxiliar. Verifica se usu�rio existe no diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param user
	 *            nome do usu�rio
	 * @return retorna <code>true</code> caso o usu�rio pertencer
	 */
	private boolean verificaSeUserExisteNoDiretorio(String diretorio, String user) {
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
					while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
					// && !line.trim().equals("")
					) {
						if (line.startsWith(user + " ")) {
							return true;
						}
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
		return false;
	}

	/**
	 * 
	 * Verifica se diret�rio existe
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @return retorna <code>true</code> caso o diret�rio existir
	 */
	private boolean verificaSeDiretorioExiste(String diretorio) {
		try {
			fileReader = new FileReader(file);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
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
	 * Adiciona grupo e permiss�o em um determinado diretorio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param grupo
	 *            nome do grupo
	 * @param permissao
	 *            permiss�o para que ir� ser colocada no grupo
	 * @return retorna <code>true</code> caso o grupo foi adicionado
	 */
	public boolean adicionaGrupoEPermissoesNoDiretorio(String diretorio, String grupo, String permissao) {
		GerenciadorDeGrupos gerenciadorDeGrupos = new GerenciadorDeGrupos(file);
		if (!gerenciadorDeGrupos.grupoExiste(grupo)) {
			gerenciadorDeGrupos = null;
			return false;
		} else if (!verificaSeDiretorioExiste(diretorio)) {
			System.out.println("Diretorio \"" + diretorio + "\" nao existe");
			return false;
		} else if (verificaSeGrupoExisteNoDiretorio(diretorio, grupo)) {
			System.out.println("Grupo \"" + grupo + "\" ja existe no diretorio \"" + diretorio + "\"");
			return false;
		} else {
			try {
				fileReader = new FileReader(file);
				leitor = new BufferedReader(fileReader);
				fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\n");
						while ((line = leitor.readLine()) != null && !line.trim().equals("")) {
							if (line.startsWith("@") || line.matches("^\\w.+=.*")) {
								fileWriter.write(line + "\n");
							}
						}
						// Nao faz a validacao pois se for a ultima linha ele
						// devera escrever
						// if (!(line == null))
						fileWriter.write("@" + grupo + " = " + permissao + "\n\n");
					} else {
						if (!(line == null))
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
	}

	/**
	 * Adiciona usu�rio e permiss�o em um determinado diretorio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param usuario
	 *            nome do usu�rio
	 * @param permissao
	 *            permiss�o para que ir� ser colocada no usu�rio
	 * @return retorna <code>true</code> caso o usu�rio foi adicionado
	 */
	public boolean adicionaUserEPermissoesNoDiretorio(String diretorio, String usuario, String permissao) {
		GerenciadorDeGrupos gerenciadorDeGrupos = new GerenciadorDeGrupos(file);
		if (!gerenciadorDeGrupos.usuarioExiste(usuario)) {
			gerenciadorDeGrupos = null;
			return false;
		} else if (!verificaSeDiretorioExiste(diretorio)) {
			System.out.println("Diretorio \"" + diretorio + "\" nao existe");
			return false;
		} else if (verificaSeUserExisteNoDiretorio(diretorio, usuario)) {
			System.out.println("Usuario \"" + usuario + "\" ja existe no diretorio \"" + diretorio + "\"");
			return false;
		} else {
			try {
				fileReader = new FileReader(file);
				leitor = new BufferedReader(fileReader);
				fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\n");
						while ((line = leitor.readLine()) != null && !line.trim().equals("")) {
							if (line.startsWith("@") || line.matches("^\\w.+=.*")) {
								fileWriter.write(line + "\n");
							}
						}
						// Nao faz a validacao pois se for a ultima linha ele
						// devera escrever
						// if (!(line == null))
						fileWriter.write(usuario + " = " + permissao + "\n\n");
					} else {
						if (!(line == null))
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
	}

	/**
	 * 
	 * Remove usu�rio do diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param usuario
	 *            nome do usu�rio
	 * @return retorna <code>true</code> caso o usu�rio foi removido
	 */
	public boolean removeUserDoDir(String diretorio, String usuario) {
		if (!verificaSeDiretorioExiste(diretorio)) {
			System.out.println("Diretorio \"" + diretorio + "\" nao existe");
			return false;
		} else if (!verificaSeUserExisteNoDiretorio(diretorio, usuario)) {
			System.out.println("Usuario \"" + usuario + "\" nao existe no diretorio \"" + diretorio + "\"");
			return false;
		} else {
			try {
				fileReader = new FileReader(file);
				leitor = new BufferedReader(fileReader);
				fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\n");
						while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
								&& !line.trim().equals("")) {
							if (line.startsWith(usuario + " ")) {
								// nao escreve o user
							} else {
								fileWriter.write(line + "\n");
							}

						}
					}
					if (!(line == null))
						fileWriter.write(line + "\n");
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
		return true;
	}

	/**
	 * 
	 * Remove grupo do diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param grupo
	 *            nome do grupo
	 * @return retorna <code>true</code> caso o grupo foi removido
	 */
	public boolean removeGrupoDoDir(String diretorio, String grupo) {
		if (!verificaSeDiretorioExiste(diretorio)) {
			System.out.println("Diretorio \"" + diretorio + "\" nao existe");
			return false;
		} else if (!verificaSeGrupoExisteNoDiretorio(diretorio, grupo)) {
			System.out.println("Grupo \"" + grupo + "\" nao existe no diretorio \"" + diretorio + "\"");
			return false;
		} else {
			try {
				fileReader = new FileReader(file);
				leitor = new BufferedReader(fileReader);
				fileWriter = new FileWriter(new File(Gerenciador.getCaminhoSaidaOculto(false)));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\n");
						while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
								&& !line.trim().equals("")) {
							if (line.startsWith("@" + grupo + " ")) {
								// nao escreve o grupo
							} else {
								fileWriter.write(line + "\n");
							}

						}
					}
					if (!(line == null))
						fileWriter.write(line + "\n");
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
		return true;
	}

	/**
	 * 
	 * Procura qual permissao grupo ou usu�rio tem no diret�rio
	 * 
	 * @param diretorio
	 *            caminho do diret�rio
	 * @param grupo
	 *            nome do grupo ou usu�rio
	 * @return retorna a permis�o do grupo ou usu�rio
	 */
	private String qualPermissaoGrupoOuUserTemNoDir(String diretorio, String grupo) {
		if (!verificaSeDiretorioExiste(diretorio)) {
			System.out.println("Diretorio \"" + diretorio + "\" nao existe");
			return null;
		} else if (!verificaSeGrupoOuUserExisteNoDiretorio(diretorio, grupo)) {
			System.out.println("Grupo \"" + grupo + "\" nao existe no diretorio \"" + diretorio + "\"");
			return null;
		} else {
			String permissao = null;
			try {
				fileReader = new FileReader(file);
				leitor = new BufferedReader(fileReader);
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
						while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
						// && !line.trim().equals("")
						) {
							if (line.matches("[@].+=$")) {
								return "";
							} else if (line.contains(grupo + " ")) {
								String[] linha = line.trim().replace(" ", "").split("=");
								permissao = linha[1];
							}
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
			return permissao;
		}
	}

	/**
	 * 
	 * Lista quais diret�rios um grupo ou usu�rio tem acesso e quais as suas
	 * permiss�es
	 * 
	 * @param grupo
	 *            nome do grupo ou usu�rio
	 * @return Retorna o HashMap
	 */
	public Map<String, String> listaQuaisDiretoriosUmGrupoOuUserTemAcessoEQuaisPermissoes(String grupo) {
		HashMap<String, String> diretoriosEPermissoes = new HashMap<>();
		for (String diretorio : listaDiretorios()) {
			if (verificaSeGrupoOuUserExisteNoDiretorio(diretorio, grupo)) {
				diretoriosEPermissoes.put(diretorio, qualPermissaoGrupoOuUserTemNoDir(diretorio, grupo));
			}
		}
		return diretoriosEPermissoes;
	}
}
