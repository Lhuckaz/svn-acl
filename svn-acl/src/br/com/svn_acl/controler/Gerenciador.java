package br.com.svn_acl.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Gerenciador {

	private static final String ARQUIVO_SINCRONIZACAO = "~svn-sync.acl";
	private static final String ARQUIVO_SAIDA = "~svn-saida.acl";
	
	private File arquivo;
	private String caminhoArquivo;
	
	private File arquivoOculto;
	private String caminhoArquivoOculto;
	
	private String caminhoSaidaOculto;
	
	private GerenciadorDeGrupos gerenciadorDeGrupos;
	private GerenciadorDePermissoes gerenciadorDePermissoes;
	private FileReader fileReader;
	private FileWriter fileWriter;
	private BufferedReader leitor;

	public Gerenciador(String arquivo) {
		String adicionaArquivoParaSincronizar = adicionaArquivoParaSincronizar(arquivo);
		gerenciadorDeGrupos = new GerenciadorDeGrupos(adicionaArquivoParaSincronizar);
		gerenciadorDePermissoes = new GerenciadorDePermissoes(adicionaArquivoParaSincronizar);
	}

	public GerenciadorDeGrupos getGerenciadorDeGrupos() {
		return gerenciadorDeGrupos;
	}

	public GerenciadorDePermissoes getGerenciadorDePermissoes() {
		return gerenciadorDePermissoes;
	}

	private String adicionaArquivoParaSincronizar(String arquivo) {
		apagaArquivoSincronizacao(arquivo);
		try {
			fileReader = new FileReader(arquivo);
			fileWriter = new FileWriter(caminhoArquivoOculto);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				fileWriter.write(line + "\r\n");
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
		setHidden(caminhoArquivoOculto);
		return arquivoOculto.getAbsolutePath();
	}

	private void setHidden(String dir) {
		try {
			// Setar o arquivo como oculto
			Runtime.getRuntime().exec("attrib +H " + dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void apagaArquivoSincronizacao(String caminho) {
		arquivo = new File(caminho);
		caminhoArquivo = arquivo.getParent();
		caminhoArquivoOculto = caminhoArquivo + "\\" + ARQUIVO_SINCRONIZACAO;
		arquivoOculto = new File(caminhoArquivoOculto);
		if (arquivoOculto.exists()) {
			arquivoOculto.delete();
		}
	}

	private void apagaArquivoSaida(String caminho) {
		arquivo = new File(caminho);
		caminhoArquivo = arquivo.getParent();
		caminhoSaidaOculto = caminhoArquivo + "\\" + ARQUIVO_SAIDA;
		File paraApagar = new File(caminhoSaidaOculto);
		if (paraApagar.exists()) {
			paraApagar.delete();
		}
	}

	private void atualizaArquivo() {
		String caminho = arquivo.getAbsolutePath();
		apagaArquivoSaida(caminho);
		// Copia para saida
		try {
			fileReader = new FileReader(caminhoArquivoOculto);
			fileWriter = new FileWriter(caminhoSaidaOculto);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				fileWriter.write(line + "\r\n");
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
		setHidden(caminhoSaidaOculto);

		// Sincroniza com arquivo de sincronizacao
		apagaArquivoSincronizacao(caminho);
		try {
			fileReader = new FileReader(caminhoSaidaOculto);
			fileWriter = new FileWriter(caminhoArquivoOculto);
			leitor = new BufferedReader(fileReader);
			String line = "";
			while ((line = leitor.readLine()) != null) {
				fileWriter.write(line + "\r\n");
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

	public static void main(String[] args) {
		// TODO Teste para sincronizacao de arquivos
		final Gerenciador gerenciador = new Gerenciador("C:\\Users\\lucas.fernandes\\Documents\\svn.acl");
		gerenciador.atualizaArquivo();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gerenciador.atualizaArquivo();
			}
		}).run();
	}

}
