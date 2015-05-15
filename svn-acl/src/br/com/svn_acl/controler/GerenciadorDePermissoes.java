package br.com.svn_acl.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	private List<String> listaDiretorios() {
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

	private List<String> listaGruposEUserESuasPermissoesDeUmDiretorio(String diretorio) {
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

	private boolean alteraPermissoesDoGrupoDoDir(String diretorio, String grupo, String permissao) {
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
				fileWriter = new FileWriter(new File("svn2.acl"));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\r\n");
						while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
								&& !line.trim().equals("")) {
							if (line.contains(grupo + " ")) {
								String[] linha = line.trim().replace(" ", "").split("=");
								fileWriter.write(linha[0] + " = " + permissao + "\r\n");
							}
						}
					}
					fileWriter.write(line + "\r\n");
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

	private boolean adicionaGrupoEPermissoesNoDiretorio(String diretorio, String grupo, String permissao) {
		GerenciadorDeGrupos gerenciadorDeGrupos = new GerenciadorDeGrupos(file);
		if (!gerenciadorDeGrupos.grupoExiste(grupo)) {
			gerenciadorDeGrupos = null;
			return false;
		} else if (!verificaSeDiretorioExiste(diretorio)) {
			System.out.println("Diretorio \"" + diretorio + "\" nao existe");
			return false;
		} else if (verificaSeGrupoOuUserExisteNoDiretorio(diretorio, grupo)) {
			System.out.println("Grupo \"" + grupo + "\" ja existe no diretorio \"" + diretorio + "\"");
			return false;
		} else {
			try {
				fileReader = new FileReader(file);
				leitor = new BufferedReader(fileReader);
				fileWriter = new FileWriter(new File("svn2.acl"));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\r\n");
						while ((line = leitor.readLine()) != null && !line.trim().equals("")) {
							if (line.startsWith("@")) {
								fileWriter.write(line + "\r\n");
							}
						}
						fileWriter.write("@" + grupo + " = " + permissao + "\r\n\r\n");
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
					leitor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	private boolean adicionaUserEPermissoesNoDiretorio(String diretorio, String usuario, String permissao) {
		GerenciadorDeGrupos gerenciadorDeGrupos = new GerenciadorDeGrupos(file);
		if (!gerenciadorDeGrupos.usuarioExiste(usuario)) {
			gerenciadorDeGrupos = null;
			return false;
		} else if (!verificaSeDiretorioExiste(diretorio)) {
			System.out.println("Diretorio \"" + diretorio + "\" nao existe");
			return false;
		} else if (verificaSeGrupoOuUserExisteNoDiretorio(diretorio, usuario)) {
			System.out.println("Usuario \"" + usuario + "\" ja existe no diretorio \"" + diretorio + "\"");
			return false;
		} else {
			try {
				fileReader = new FileReader(file);
				leitor = new BufferedReader(fileReader);
				fileWriter = new FileWriter(new File("svn2.acl"));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\r\n");
						while ((line = leitor.readLine()) != null && !line.trim().equals("")) {
							if (line.startsWith("@")) {
								fileWriter.write(line + "\r\n");
							}
						}
						fileWriter.write(usuario + " = " + permissao + "\r\n\r\n");
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
					leitor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	private boolean removeGrupoOuUserDoDir(String diretorio, String grupo) {
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
				fileWriter = new FileWriter(new File("svn2.acl"));
				String line = "";
				while ((line = leitor.readLine()) != null) {
					if (line.matches("^.*.+:.*") && line.startsWith("[") && line.equals("[" + diretorio + "]")) {
						fileWriter.write(line + "\r\n");
						while ((line = leitor.readLine()) != null && !line.startsWith("[") && !line.startsWith("#")
								&& !line.trim().equals("")) {
							if (line.contains(grupo + " ")) {
								// nao escreve o grupo
							} else {
								fileWriter.write(line + "\r\n");
							}

						}
					}
					fileWriter.write(line + "\r\n");
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

	private HashMap<String, String> listaQuaisDiretoriosUmGrupoTemAcessoEQuaisPermissoes(String grupo) {
		HashMap<String, String> diretoriosEPermissoes = new HashMap<>();
		for (String diretorio : listaDiretorios()) {
			if (verificaSeGrupoOuUserExisteNoDiretorio(diretorio, grupo)) {
				diretoriosEPermissoes.put(diretorio, qualPermissaoGrupoOuUserTemNoDir(diretorio, grupo));
			}
		}
		return diretoriosEPermissoes;
	}
}
