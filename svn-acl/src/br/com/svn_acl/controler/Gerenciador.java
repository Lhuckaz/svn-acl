package br.com.svn_acl.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.com.svn_acl.gui.SvnAclGUI;

/**
 * Classe respons�vel por gerenciar os arquivos de sincroniza��o e sa�da.
 * 
 * @author Lhuckaz
 *
 */
public class Gerenciador {

	private static final String ARQUIVO_SINCRONIZACAO = "~svn-sync.acl";
	private static final String ARQUIVO_SAIDA = "~svn-saida.acl";
	private static final String ARQUIVO_RETORNAR = "~svn-retorno.acl";

	private static File arquivo;
	private static String caminhoSaidaOculto;

	private File arquivoOculto;
	private String caminhoArquivoOculto;

	private File arquivoRetorno;
	private String caminhoArquivoRetorno;

	private GerenciadorDeGrupos gerenciadorDeGrupos;
	private GerenciadorDePermissoes gerenciadorDePermissoes;
	private FileReader fileReader;
	private FileWriter fileWriter;
	private BufferedReader leitor;

	/**
	 * Construtor da classe {@link Gerenciador}
	 * 
	 * @param arquivo
	 *            recebe o arquivo que ir� ser gerenciado pelo programa
	 */
	public Gerenciador(String arquivo) {
		// Apagar arquivos caso existam ao iniciar o programa
		apagaArquivosDeGerenciamento();
		String adicionaArquivoParaSincronizar = adicionaArquivoParaSincronizar(arquivo);
		gerenciadorDeGrupos = new GerenciadorDeGrupos(adicionaArquivoParaSincronizar);
		gerenciadorDePermissoes = new GerenciadorDePermissoes(adicionaArquivoParaSincronizar);
		atualizaArquivo();
	}

	/**
	 * 
	 * @return gerenciadorDeGrupos
	 */
	public GerenciadorDeGrupos getGerenciadorDeGrupos() {
		return gerenciadorDeGrupos;
	}

	/**
	 * 
	 * @return gerenciadorDePermissoes
	 */
	public GerenciadorDePermissoes getGerenciadorDePermissoes() {
		return gerenciadorDePermissoes;
	}

	/**
	 * 
	 * Ao pegar o caminho do arquivo de saida o metodo o apaga para as classes
	 * {@link GerenciadorDeGrupos} e {@link GerenciadorDePermissoes} criar um
	 * outro arquivo com as modifica��es
	 * 
	 * @param saida
	 *            <code>true</code> se n�o for apagar o arquivo
	 * @return retorna o caminho do arquivo de sa�da
	 */
	public static String getCaminhoSaidaOculto(boolean saida) {
		if (saida == false)
			apagaArquivoSaida(arquivo.getAbsolutePath());
		return caminhoSaidaOculto;
	}

	/**
	 * 
	 * Recebe o arquivo que ir� ser lido para os arquivos de gerenciamento de
	 * sincroniza��o e sa�da
	 * 
	 * @param arquivo
	 *            arquivo que ir� ser lido
	 * @return retorno caminho de arquivo de sincroniza��o
	 */
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

	/**
	 * Oculta os arquivos com o comando do Windows <strong>attrib +H +S
	 * "&lt;dir&gt;"</strong>
	 * 
	 * @param dir
	 *            o caminho do arquivo para ser ocultado
	 */
	private void setHidden(String dir) {
		try {
			// Setar o arquivo como oculto
			Runtime.getRuntime().exec("attrib +H +S " + "\"" + dir + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Apaga arquivo de sincroniza��o
	 * 
	 * @param caminho
	 *            caminho do arquivo
	 */
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

	/**
	 * Apaga o arquivo de sa�da
	 * 
	 * @param caminho
	 *            caminho do arquivo
	 */
	private static void apagaArquivoSaida(String caminho) {
		// Arquivo criado no diretorio em que esta o arquivo ao qual foi aberto,
		// nao e mais usado ..\svn.acl
		arquivo = new File(caminho);

		// caminhoParenteArquivo = arquivo.getParent();
		// caminhoSaidaOculto = caminhoParenteArquivo + "\\" + ARQUIVO_SAIDA;
		caminhoSaidaOculto = ARQUIVO_SAIDA;
		File paraApagar = new File(caminhoSaidaOculto);
		if (paraApagar.exists()) {
			paraApagar.delete();
		}
	}

	/**
	 * Apaga arquivo de retorno
	 * 
	 * @param caminho
	 *            caminho do arquivo
	 */
	private void apagaArquivoRetornar(String caminho) {

		arquivo = new File(caminho);

		caminhoArquivoRetorno = ARQUIVO_RETORNAR;
		arquivoRetorno = new File(caminhoArquivoRetorno);
		if (arquivoRetorno.exists()) {
			arquivoRetorno.delete();
		}
	}

	/**
	 * Copia o conte�do do arquivo de sincroniza��o para o de sa�da e deixa um
	 * backup da altera��o anterior no arquivo de retorno
	 */
	public void atualizaArquivo() {
		String caminho = arquivo.getAbsolutePath();

		apagaArquivoRetornar(caminho);
		try {
			fileReader = new FileReader(caminhoArquivoOculto);
			fileWriter = new FileWriter(caminhoArquivoRetorno);
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
		setHidden(caminhoArquivoRetorno);
		setHidden(caminhoArquivoOculto);
		// Esconde o arquivo de saida apos alteracao das classes de
		// Gerenciamento
		setHidden(caminhoSaidaOculto);

		SvnAclGUI.habilitaRetorno();
		// Abrir, Checkout, Importar e altera��es
		SvnAclGUI.arquivoSalvo = false;
	}

	/**
	 * Desfaz ou refaz a altera��o do usu�rio
	 */
	public void alterarArquivo() {
		String caminho = arquivo.getAbsolutePath();

		apagaArquivoSaida(caminho);
		try {
			fileReader = new FileReader(caminhoArquivoRetorno);
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

		// Esconde o arquivo de saida apos alteracao das classes de
		// Gerenciamento
		setHidden(caminhoSaidaOculto);

		// Abrir, Checkout, Importar e altera��es
		SvnAclGUI.arquivoSalvo = false;
	}

	/**
	 * Apaga arquivo de sincroniza��o e de sa�da
	 */
	public void apagaArquivosDeGerenciamento() {
		if (arquivo != null) {
			String caminhoArquivo = arquivo.getAbsolutePath();
			apagaArquivoSincronizacao(caminhoArquivo);
			apagaArquivoSaida(caminhoArquivo);
			apagaArquivoRetornar(caminhoArquivo);
		}
	}

}
