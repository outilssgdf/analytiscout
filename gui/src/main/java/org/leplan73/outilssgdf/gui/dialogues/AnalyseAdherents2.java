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
import org.leplan73.outilssgdf.gui.analyseur.AnalyseurResponsables;
import org.leplan73.outilssgdf.gui.extracteur.ExtracteurBatchResponsables;
import org.leplan73.outilssgdf.gui.utils.ElementFactory;

public class AnalyseAdherents2 extends Template {

	/**
	 * Create the panel.
	 */
	public AnalyseAdherents2() {
		super();

					JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Analyse des maitrises et compas</b></html>");
					GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_title1.getLayout();
					gbl_panel_title1.rowWeights = new double[]{0.0};
					gbl_panel_title1.rowHeights = new int[]{0};
					gbl_panel_title1.columnWeights = new double[]{0.0, 0.0};
					gbl_panel_title1.columnWidths = new int[]{0, 0};
									
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
									gbl_panel_7.columnWidths = new int[]{232, 231, 0};
									gbl_panel_7.rowHeights = new int[]{0, 23, 0, 0};
									gbl_panel_7.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
									gbl_panel_7.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
									panel_7.setLayout(gbl_panel_7);
									
									JButton button_2 = new JButton("<html><p style=\"text-align:center;\">Analyser des données<br>déjà extraites</p>");
									button_2.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new AnalyseurResponsables().setVisible(true);
										}
									});
									GridBagConstraints gbc_button_2 = new GridBagConstraints();
									gbc_button_2.fill = GridBagConstraints.HORIZONTAL;
									gbc_button_2.insets = new Insets(0, 0, 5, 5);
									gbc_button_2.gridx = 0;
									gbc_button_2.gridy = 1;
									panel_7.add(button_2, gbc_button_2);
									
									JButton btnexporterDesDonnesen = new JButton("<html><p style=\"text-align:center;\">Exporter des données<br>en batch</p>");
									btnexporterDesDonnesen.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new ExtracteurBatchResponsables().setVisible(true);
										}
									});
									GridBagConstraints gbc_btnexporterDesDonnesen = new GridBagConstraints();
									gbc_btnexporterDesDonnesen.fill = GridBagConstraints.HORIZONTAL;
									gbc_btnexporterDesDonnesen.insets = new Insets(0, 0, 5, 0);
									gbc_btnexporterDesDonnesen.gridx = 1;
									gbc_btnexporterDesDonnesen.gridy = 1;
									panel_7.add(btnexporterDesDonnesen, gbc_btnexporterDesDonnesen);
									
									JPanel panel_title2 = ElementFactory.createActionTitle("<html><b>Analyse des compas</b></html>");
									GridBagLayout gbl_panel_title2 = (GridBagLayout) panel_title2.getLayout();
									gbl_panel_title2.rowWeights = new double[]{0.0};
									gbl_panel_title2.rowHeights = new int[]{0};
									gbl_panel_title2.columnWeights = new double[]{0.0, 0.0};
									gbl_panel_title2.columnWidths = new int[]{0, 0};
									
									JPanel panel_title3 = ElementFactory.createActionTitle("<html><b>Analyse des adhérents</b></html>");
									GridBagLayout gbl_panel_title3 = (GridBagLayout) panel_title3.getLayout();
									gbl_panel_title3.rowWeights = new double[]{0.0};
									gbl_panel_title3.rowHeights = new int[]{0};
									gbl_panel_title3.columnWeights = new double[]{0.0, 0.0};
									gbl_panel_title3.columnWidths = new int[]{0, 0};

	}

}
