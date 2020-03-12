package org.leplan73.analytiscout.gui.dialogues;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.leplan73.analytiscout.gui.Template;

@SuppressWarnings("serial")
public class Camps extends Template {

	/**
	 * Create the panel.
	 */
	public Camps() {
		super();
		
		JLabel lblaArrive = new JLabel("ça arrive !!!");
		lblaArrive.setHorizontalAlignment(SwingConstants.CENTER);
		panel_collection.add(lblaArrive, BorderLayout.CENTER);
	}

}
