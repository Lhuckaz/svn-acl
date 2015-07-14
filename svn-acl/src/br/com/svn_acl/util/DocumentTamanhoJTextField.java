package br.com.svn_acl.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 
 * Define o tamanho máximo de um {@link javax.swing.JTextField JTextField}
 * 
 * @author Lhuckaz
 *
 */
@SuppressWarnings("serial")
public class DocumentTamanhoJTextField extends PlainDocument {
	private int tamMax;

	public DocumentTamanhoJTextField(int tamMax) {
		super();
		this.tamMax = tamMax;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;
		// Define a condição para aceitar qualquer número de caracteres
		if (tamMax <= 0) {
			super.insertString(offset, str, attr);
			return;
		}
		int tam = (getLength() + str.length());
		// Se o tamanho final for menor, chama insertString() aceitando a String
		if (tam <= tamMax) {
			super.insertString(offset, str, attr);
		} else {
			// Caso contrário, limita a string e envia para insertString() que
			// aceita a string
			if (getLength() == tamMax)
				return;
			String novaStr = str.substring(0, (tamMax - getLength()));
			super.insertString(offset, novaStr, attr);
		}
	}

}
