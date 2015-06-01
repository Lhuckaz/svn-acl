package br.com.svn_acl.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Gerenciador {

	private static final String ARQUIVO_SINCRONIZACAO = "~svn-sync.acl";
	private static final String ARQUIVO_SAIDA = "~svn-saida.acl";

	private static File arquivo;
	// private static String caminhoParenteArquivo;

	private File arquivoOculto;
	private String caminhoArquivoOculto;

	private static String caminhoSaidaOculto;

	private GerenciadorDeGrupos gerenciadorDeGrupos;
	private GerenciadorDePermissoes gerenciadorDePermissoes;
	private FileReader fileReader;
	private FileWriter fileWriter;
	private BufferedReader leitor;

	public Gerenciador(String arquivo) {
		apagaArquivosDeGerenciamento();
		String adicionaArquivoParaSincronizar = adicionaArquivoParaSincronizar(arquivo);
		gerenciadorDeGrupos = new GerenciadorDeGrupos(adicionaArquivoParaSincronizar);
		gerenciadorDePermissoes = new GerenciadorDePermissoes(adicionaArquivoParaSincronizar);
		atualizaArquivo();
	}

	public GerenciadorDeGrupos getGerenciadorDeGrupos() {
		return gerenciadorDeGrupos;
	}

	public GerenciadorDePermissoes getGerenciadorDePermissoes() {
		return gerenciadorDePermissoes;
	}

	public static String getCaminhoSaidaOculto(boolean saida) {
		if (saida == false)
			apagaArquivoSaida(arquivo.getAbsolutePath());
		return caminhoSaidaOculto;
	}

	private String adicionaArquivoParaSincronizar(String arquivo) {
		apagaArquivoSincronizacao(arquivo);
		try {
			fileReader = new FileReader(arquivo);
			fileWriter = new FileWriter(caminhoArquivoOculto);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
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
		setHidden(caminhoArquivoOculto);

		apagaArquivoSaida(arquivo);
		try {
			fileReader = new FileReader(caminhoArquivoOculto);
			fileWriter = new FileWriter(caminhoSaidaOculto);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
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
		setHidden(caminhoSaidaOculto);

		return arquivoOculto.getAbsolutePath();
	}

	private void setHidden(String dir) {
		try {
			// Setar o arquivo como oculto
			Runtime.getRuntime().exec("attrib +H +S " + "\"" + dir + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void apagaArquivoSincronizacao(String caminho) {
		// Arquivo criado no diretorio em que esta o arquivo ao qual foi aberto,
		// nao e mais usado
		arquivo = new File(caminho);
		// caminhoParenteArquivo = arquivo.getParent();
		// caminhoArquivoOculto = caminhoParenteArquivo + "\\" +
		// ARQUIVO_SINCRONIZACAO;
		caminhoArquivoOculto = ARQUIVO_SINCRONIZACAO;
		arquivoOculto = new File(caminhoArquivoOculto);
		if (arquivoOculto.exists()) {
			arquivoOculto.delete();
		}
	}

	private static void apagaArquivoSaida(String caminho) {
		// Arquivo criado no diretorio em que esta o arquivo ao qual foi aberto,
		// nao e mais usado
		arquivo = new File(caminho);
		// caminhoParenteArquivo = arquivo.getParent();
		// caminhoSaidaOculto = caminhoParenteArquivo + "\\" + ARQUIVO_SAIDA;
		caminhoSaidaOculto = ARQUIVO_SAIDA;
		File paraApagar = new File(caminhoSaidaOculto);
		if (paraApagar.exists()) {
			paraApagar.delete();
		}
	}

	public void atualizaArquivo() {
		String caminho = arquivo.getAbsolutePath();

		// Apaga arquivo de sincronizacao e escrever o conteudo do arquivo de
		// saida nele
		apagaArquivoSincronizacao(caminho);
		try {
			fileReader = new FileReader(caminhoSaidaOculto);
			fileWriter = new FileWriter(caminhoArquivoOculto);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
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
		setHidden(caminhoArquivoOculto);
		// Esconde o arquivo de saida apos alteracao das classes de
		// Gerenciamento
		setHidden(caminhoSaidaOculto);
	}

	public void apagaArquivosDeGerenciamento() {
		if (arquivo != null) {
			String caminhoArquivo = arquivo.getAbsolutePath();
			apagaArquivoSincronizacao(caminhoArquivo);
			apagaArquivoSaida(caminhoArquivo);
		}
	}

}
