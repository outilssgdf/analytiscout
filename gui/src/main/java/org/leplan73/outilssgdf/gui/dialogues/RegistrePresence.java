package org.leplan73.outilssgdf.gui.dialogues;

import org.leplan73.outilssgdf.gui.Template;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;

public class RegistrePresence extends Template {

	/**
	 * Create the panel.
	 */
	public RegistrePresence() {
		super();
		
		JLabel lblaArrive = new JLabel("ça arrive !!!");
		lblaArrive.setHorizontalAlignment(SwingConstants.CENTER);
		panel_collection.add(lblaArrive, BorderLayout.CENTER);
	}

}
