package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.leplan73.outilssgdf.gui.Template;
import org.leplan73.outilssgdf.gui.extracteur.Extracteur;
import org.leplan73.outilssgdf.gui.generateur.Generateur;
import org.leplan73.outilssgdf.gui.utils.ElementFactory;

public class ExportAdherents extends Template {

	/**
	 * Create the panel.
	 */
	public ExportAdherents() {
		super();

		JPanel panelExporter = new JPanel();
		panel_collection.add(panelExporter, BorderLayout.NORTH);

		JPanel panel_titleExporter = ElementFactory.createActionTitle("<html><b>Exporter</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_titleExporter.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };

		panelExporter.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		panelExporter.add(panel_1, BorderLayout.SOUTH);
		panelExporter.add(panel_titleExporter, BorderLayout.NORTH);

		JButton btnNewButton = new JButton("Exporter des données");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Extracteur().setVisible(true);
			}
		});
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(btnNewButton, BorderLayout.SOUTH);

		JPanel panelGenerer = new JPanel();
		panel_collection.add(panelGenerer, BorderLayout.CENTER);

		JPanel panel_titleGenerer = ElementFactory.createActionTitle("<html><b>Générateur</b></html>");
		GridBagLayout gbl_panel_title2 = (GridBagLayout) panel_titleGenerer.getLayout();
		gbl_panel_title2.rowWeights = new double[] { 0.0 };
		gbl_panel_title2.rowHeights = new int[] { 0 };
		gbl_panel_title2.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title2.columnWidths = new int[] { 0, 0 };

		panelGenerer.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panelGenerer.add(panel_3);
		panelGenerer.add(panel_titleGenerer, BorderLayout.NORTH);

		JButton btnNewButton_1 = new JButton("Générer une archive zip");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Generateur().setVisible(true);
			}
		});
		panel_3.setLayout(new BorderLayout(0, 0));
		panel_3.add(btnNewButton_1, BorderLayout.NORTH);

	}

}
