package org.leplan73.outilssgdf.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.leplan73.outilssgdf.Consts;
import org.slf4j.Logger;

public class Dialogue extends JDialog implements LoggedDialog {

	protected Logger logger_;
	protected JTextArea txtLog;
	
	public Dialogue()
	{
		setIconImage(Images.getIcon());
		addEscapeListener(this);
	}

	@Override
	public void initLog() {
		txtLog.setText("");
	}
	
	private static void addEscapeListener(final JDialog dialog) {
	    ActionListener escListener = new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            dialog.setVisible(false);
	        }
	    };

	    dialog.getRootPane().registerKeyboardAction(escListener,
	            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	            JComponent.WHEN_IN_FOCUSED_WINDOW);

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
	
	protected boolean checkStructures(String structures)
	{
		if (structures.isEmpty()) {
			logger_.error("Le code de structure est vide");
			return false;
		}
		try
		{
			String stStructures[] = structures.split(",");
			for (String stStructure : stStructures)
			{
				Integer.parseInt(stStructure);
			}
		}
		catch(NumberFormatException e)
		{
			logger_.error("Code de structure invalide");
			return false;
		}
		if (structures.compareTo(Consts.STRUCTURE_NATIONAL) == 0)
		{
			logger_.error("Code de structure interdit");
			return false;
		}
		return true;
	}
}
