package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.leplan73.outilssgdf.gui.Template;

public class Camps extends Template {

	/**
	 * Create the panel.
	 */
	public Camps(boolean anonymiser) {
		super(anonymiser);
		
		JLabel lblaArrive = new JLabel("ça arrive !!!");
		lblaArrive.setHorizontalAlignment(SwingConstants.CENTER);
		panel_collection.add(lblaArrive, BorderLayout.CENTER);
	}

}
