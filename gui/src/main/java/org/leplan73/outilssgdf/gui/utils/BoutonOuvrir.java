package org.leplan73.outilssgdf.gui.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;

public class BoutonOuvrir extends JButton {

	private JLabel chemin_;
	
	public BoutonOuvrir(String string, JLabel lblSortie) {
		super(string);
		chemin_ = lblSortie;
		maj();
	}
	
	public void ouvrir() throws SecurityException, IOException
	{
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(new File(chemin_.getText()).toURI());
	}
	
	public void maj()
	{
		File f = new File(chemin_.getText());
		this.setEnabled(f.exists());
	}
}
