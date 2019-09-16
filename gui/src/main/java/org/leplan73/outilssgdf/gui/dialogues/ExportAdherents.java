package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.leplan73.outilssgdf.gui.Template;
import org.leplan73.outilssgdf.gui.extracteur.Extracteur;
import org.leplan73.outilssgdf.gui.generateur.Generateur;
import org.leplan73.outilssgdf.gui.generateur.GenerateurVCard;
import org.leplan73.outilssgdf.gui.utils.ElementFactory;

public class ExportAdherents extends Template {

	/**
	 * Create the panel.
	 */
	public ExportAdherents() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{312, 0};
		gridBagLayout.rowHeights = new int[]{37, 238, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_collection.setLayout(gridBagLayout);

		JPanel panel_titleExporter = ElementFactory.createActionTitle("<html><b>Exporter</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_titleExporter.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		
				JPanel panelExporter = new JPanel();
				GridBagConstraints gbc_panelExporter = new GridBagConstraints();
				gbc_panelExporter.anchor = GridBagConstraints.NORTH;
				gbc_panelExporter.fill = GridBagConstraints.HORIZONTAL;
				gbc_panelExporter.insets = new Insets(0, 0, 5, 0);
				gbc_panelExporter.gridx = 0;
				gbc_panelExporter.gridy = 0;
				panel_collection.add(panelExporter, gbc_panelExporter);
				
						panelExporter.setLayout(new BorderLayout(0, 0));
						
								JPanel panel_1 = new JPanel();
								panel_1.setBorder(null);
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

		JPanel panel_titleGenerer = ElementFactory.createActionTitle("<html><b>Générateur</b></html>");
		GridBagLayout gbl_panel_title2 = (GridBagLayout) panel_titleGenerer.getLayout();
		gbl_panel_title2.rowWeights = new double[] { 0.0 };
		gbl_panel_title2.rowHeights = new int[] { 0 };
		gbl_panel_title2.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title2.columnWidths = new int[] { 0, 0 };
		
				JPanel panelGenerer = new JPanel();
				GridBagConstraints gbc_panelGenerer = new GridBagConstraints();
				gbc_panelGenerer.anchor = GridBagConstraints.NORTH;
				gbc_panelGenerer.insets = new Insets(0, 0, 5, 0);
				gbc_panelGenerer.fill = GridBagConstraints.HORIZONTAL;
				gbc_panelGenerer.gridx = 0;
				gbc_panelGenerer.gridy = 1;
				panel_collection.add(panelGenerer, gbc_panelGenerer);
				
						panelGenerer.setLayout(new BorderLayout(0, 0));
						
								JPanel panel_3 = new JPanel();
								panelGenerer.add(panel_3);
								panelGenerer.add(panel_titleGenerer, BorderLayout.NORTH);
										GridBagLayout gbl_panel_3 = new GridBagLayout();
										gbl_panel_3.columnWidths = new int[]{147, 149, 0};
										gbl_panel_3.rowHeights = new int[]{23, 0};
										gbl_panel_3.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
										gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
										panel_3.setLayout(gbl_panel_3);
										
												JButton btnNewButton_1 = new JButton("Générer une archive zip");
												btnNewButton_1.addActionListener(new ActionListener() {
													public void actionPerformed(ActionEvent e) {
														new Generateur().setVisible(true);
													}
												});
												GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
												gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
												gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH;
												gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
												gbc_btnNewButton_1.gridx = 0;
												gbc_btnNewButton_1.gridy = 0;
												panel_3.add(btnNewButton_1, gbc_btnNewButton_1);
										
										JButton btnGnrerUneArchive = new JButton("Générer un fichier vcard");
										btnGnrerUneArchive.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												new GenerateurVCard().setVisible(true);
											}
										});
										GridBagConstraints gbc_btnGnrerUneArchive = new GridBagConstraints();
										gbc_btnGnrerUneArchive.fill = GridBagConstraints.HORIZONTAL;
										gbc_btnGnrerUneArchive.anchor = GridBagConstraints.NORTH;
										gbc_btnGnrerUneArchive.gridx = 1;
										gbc_btnGnrerUneArchive.gridy = 0;
										panel_3.add(btnGnrerUneArchive, gbc_btnGnrerUneArchive);
	}

}
