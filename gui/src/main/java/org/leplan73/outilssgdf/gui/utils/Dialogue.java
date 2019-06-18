package org.leplan73.outilssgdf.gui.utils;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import org.slf4j.Logger;

public class Dialogue extends JDialog implements LoggedDialog {

	protected Logger logger_;
	protected JTextArea txtLog;
	
	public Dialogue()
	{
		setIconImage(Images.getIcon());
	}

	@Override
	public void addLog(String message) {
		String texte = txtLog.getText();
		if (texte.length() > 0)
			txtLog.append("\n");
		txtLog.append(message);
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}
}
