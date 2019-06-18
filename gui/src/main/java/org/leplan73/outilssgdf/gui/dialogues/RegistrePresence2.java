package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.leplan73.outilssgdf.gui.ElementFactory;
import org.leplan73.outilssgdf.gui.Template;
import org.leplan73.outilssgdf.gui.registredepresence.ExtractionRegistreDePresence;

public class RegistrePresence2 extends Template {

	/**
	 * Create the panel.
	 */
	public RegistrePresence2() {
		super();

		JPanel panel_title1 = ElementFactory
				.createActionTitle("<html><b>Extraction du registre de présence</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_title1.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setBorder(null);
		panel_collection.add(panel_1, BorderLayout.NORTH);
		panel_1.add(panel_title1, BorderLayout.NORTH);
		
		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6, BorderLayout.SOUTH);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(null);
		panel_6.add(panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[]{232, 0};
		gbl_panel_7.rowHeights = new int[]{0, 0};
		gbl_panel_7.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_7.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_7.setLayout(gbl_panel_7);
		
		JButton btnNewButton = new JButton("Extraction");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExtractionRegistreDePresence().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		panel_7.add(btnNewButton, gbc_btnNewButton);
	}

}
