package br.com.svn_acl.util;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * 
 * Classe que liimita o {@link javax.swing.JTextField JTextField} para receber
 * apenas n�meros
 * 
 * @author Lhuckaz
 *
 */
public class DocumentFilterOnlyNumbers extends DocumentFilter {
	@Override
	public void insertString(DocumentFilter.FilterBypass fp, int offset, String string, AttributeSet aset)
			throws BadLocationException {
		int len = string.length();
		boolean isValidInteger = true;

		for (int i = 0; i < len; i++) {
			if (!Character.isDigit(string.charAt(i))) {
				isValidInteger = false;
				break;
			}
		}
		if (isValidInteger)
			super.insertString(fp, offset, string, aset);
		else
			Toolkit.getDefaultToolkit().beep();
	}

	@Override
	public void replace(DocumentFilter.FilterBypass fp, int offset, int length, String string, AttributeSet aset)
			throws BadLocationException {
		int len = string.length();
		boolean isValidInteger = true;

		for (int i = 0; i < len; i++) {
			if (!Character.isDigit(string.charAt(i))) {
				isValidInteger = false;
				break;
			}
		}
		if (isValidInteger)
			super.replace(fp, offset, length, string, aset);
		else
			Toolkit.getDefaultToolkit().beep();
	}
}
