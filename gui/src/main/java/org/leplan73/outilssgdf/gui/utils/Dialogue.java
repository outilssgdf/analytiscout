package org.leplan73.outilssgdf.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.leplan73.outilssgdf.Consts;
import org.slf4j.Logger;

public class Dialogue extends JDialog implements LoggedDialog {

	protected String identifiant_;
	protected String motdepasse_;
	protected String structure_;
	
	protected Logger logger_;
	protected JTextArea txtLog;
	
	public Dialogue()
	{
		setIconImage(Images.getIcon());
		addEscapeListener(this);
		
		identifiant_ = Preferences.lit(Consts.INTRANET_IDENTIFIANT, "", true);
		motdepasse_ = Preferences.lit(Consts.INTRANET_MOTDEPASSE, "", true);
		structure_ = Preferences.lit(Consts.INTRANET_STRUCTURE, "", true);
	}

	@Override
	public void initLog() {
		if (txtLog != null) txtLog.setText("");
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
	
	protected boolean checkIntranet()
	{
		if (identifiant_.isEmpty()) {
			logger_.error("L'identifiant est vide");
			return false;
		}
		if (motdepasse_.isEmpty()) {
			logger_.error("Le mode de passe est vide");
			return false;
		}
		if (checkStructures() == false) {
			return false;
		}
		return true;
	}
	
	protected void ajouteExtensionFichier(JFileChooser fc, JLabel label, File f, String extension)
	{
		f = fc.getSelectedFile();
		String path = f.getPath();
		if (path.endsWith("."+extension) == false) {
			path = path + "."+extension;
			f = new File(path);
		}
		label.setText(path);
	}

	@Override
	public void addLog(String message) {
		String texte = txtLog.getText();
		if (texte.length() > 0)
			txtLog.append("\n");
		if (txtLog != null) txtLog.append(message);
		if (txtLog != null) txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}
	
	protected int[] construitStructures()
	{
		String stStructures[] = structure_.split(",");
		int structures[] = new int[stStructures.length];
		int index = 0;
		for (String stStructure : stStructures)
		{
			structures[index++] = Integer.parseInt(stStructure);
		}
		return structures;
	}
	
	private boolean checkStructures()
	{
		if (structure_.isEmpty()) {
			logger_.error("Le code de structure est vide");
			return false;
		}
		try
		{
			String stStructures[] = structure_.split(",");
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
		if (structure_.compareTo(Consts.STRUCTURE_NATIONAL) == 0)
		{
			logger_.error("Code de structure interdit");
			return false;
		}
		return true;
	}
}
