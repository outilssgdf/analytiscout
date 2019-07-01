package org.leplan73.outilssgdf.gui.utils;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;

public class Dialogue extends JDialog implements LoggedDialog {

	protected Logger logger_;
	protected JTextArea txtLog;
	
	public Dialogue()
	{
		setIconImage(Images.getIcon());
	}

	@Override
	public void initLog() {
		txtLog.setText("");
	}

	@Override
	public void addLog(String message) {
		String texte = txtLog.getText();
		if (texte.length() > 0)
			txtLog.append("\n");
		txtLog.append(message);
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}
	
	static protected int[] construitStructures(JTextField txfCodeStructure)
	{
		String stStructures[] = txfCodeStructure.getText().split(",");
		int structures[] = new int[stStructures.length];
		int index = 0;
		for (String stStructure : stStructures)
		{
			structures[index++] = Integer.parseInt(stStructure);
		}
		return structures;
	}
}
