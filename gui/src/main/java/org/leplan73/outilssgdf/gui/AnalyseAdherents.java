package org.leplan73.outilssgdf.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.leplan73.outilssgdf.gui.analyseur.AnalyseurCompas;
import org.leplan73.outilssgdf.gui.analyseur.AnalyseurResponsables;
import org.leplan73.outilssgdf.gui.analyseurenligne.AnalyseurEnLigneAdherents;
import org.leplan73.outilssgdf.gui.analyseurenligne.AnalyseurEnLigneCompas;
import org.leplan73.outilssgdf.gui.analyseurenligne.AnalyseurEnLigneResponsables;
import org.leplan73.outilssgdf.gui.extracteur.Extracteur;
import org.leplan73.outilssgdf.gui.extracteur.ExtracteurBatchCompas;
import org.leplan73.outilssgdf.gui.extracteur.ExtracteurBatchResponsables;
import org.leplan73.outilssgdf.gui.utils.Images;

import java.awt.BorderLayout;
import javax.swing.JLabel;

public class AnalyseAdherents extends JPanel {

	/**
	 * Create the panel.
	 */
	public AnalyseAdherents() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.WEST);
		
		JLabel label = new JLabel("");
		panel_1.add(label);
		label.setIcon(Images.getImage());
					
					JPanel panel_5 = new JPanel();
					add(panel_5, BorderLayout.CENTER);
					
					
					
					JPanel panel = new JPanel();
					panel_5.add(panel, BorderLayout.SOUTH);
					
					GridBagLayout gbl_panel = new GridBagLayout();
					gbl_panel.columnWidths = new int[] { 20, 0, 215, 226, 20, 0 };
					gbl_panel.rowHeights = new int[] { 30, 48, 37, 30, 0 };
					gbl_panel.columnWeights = new double[] { 1.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
					gbl_panel.rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
					panel.setLayout(gbl_panel);
					
					JPanel panel_2 = new JPanel();
					panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Analyse des ma\u00EEtrises", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
					GridBagConstraints gbc_panel_2 = new GridBagConstraints();
					gbc_panel_2.insets = new Insets(0, 0, 5, 5);
					gbc_panel_2.fill = GridBagConstraints.VERTICAL;
					gbc_panel_2.gridx = 2;
					gbc_panel_2.gridy = 1;
					panel.add(panel_2, gbc_panel_2);
					GridBagLayout gbl_panel_2 = new GridBagLayout();
					gbl_panel_2.columnWidths = new int[]{231, 0};
					gbl_panel_2.rowHeights = new int[]{23, 23, 0, 0, 0};
					gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
					gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					panel_2.setLayout(gbl_panel_2);
					
					JButton btnAnalyserDesDonnes = new JButton("<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
					GridBagConstraints gbc_btnAnalyserDesDonnes = new GridBagConstraints();
					gbc_btnAnalyserDesDonnes.fill = GridBagConstraints.HORIZONTAL;
					gbc_btnAnalyserDesDonnes.anchor = GridBagConstraints.NORTH;
					gbc_btnAnalyserDesDonnes.insets = new Insets(0, 0, 5, 0);
					gbc_btnAnalyserDesDonnes.gridx = 0;
					gbc_btnAnalyserDesDonnes.gridy = 0;
					panel_2.add(btnAnalyserDesDonnes, gbc_btnAnalyserDesDonnes);
					btnAnalyserDesDonnes.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new AnalyseurEnLigneResponsables().setVisible(true);
						}
					});
					
					JButton btnNewButton_1 = new JButton("<html><p style=\"text-align:center;\">Analyser des données<br>déjà extraites</p>");
					GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
					gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
					gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH;
					gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
					gbc_btnNewButton_1.gridx = 0;
					gbc_btnNewButton_1.gridy = 1;
					panel_2.add(btnNewButton_1, gbc_btnNewButton_1);
					
							JButton btnNewButton = new JButton("Exporter des données");
							GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
							gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
							gbc_btnNewButton.gridx = 0;
							gbc_btnNewButton.gridy = 2;
							panel_2.add(btnNewButton, gbc_btnNewButton);
							
									JButton button = new JButton("Exporter des données en batch");
									GridBagConstraints gbc_button = new GridBagConstraints();
									gbc_button.gridx = 0;
									gbc_button.gridy = 3;
									panel_2.add(button, gbc_button);
									button.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new ExtracteurBatchResponsables().setVisible(true);
										}
									});
									btnNewButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new Extracteur().setVisible(true);
										}
									});
									btnNewButton_1.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new AnalyseurResponsables().setVisible(true);
										}
									});
									
									JPanel panel_4 = new JPanel();
									panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Analyse des compas", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
									GridBagConstraints gbc_panel_4 = new GridBagConstraints();
									gbc_panel_4.insets = new Insets(0, 0, 5, 5);
									gbc_panel_4.fill = GridBagConstraints.BOTH;
									gbc_panel_4.gridx = 3;
									gbc_panel_4.gridy = 1;
									panel.add(panel_4, gbc_panel_4);
									GridBagLayout gbl_panel_4 = new GridBagLayout();
									gbl_panel_4.columnWidths = new int[]{231, 0};
									gbl_panel_4.rowHeights = new int[]{23, 0, 0, 0};
									gbl_panel_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
									gbl_panel_4.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
									panel_4.setLayout(gbl_panel_4);
									
									JButton btnextraireEtAnalyserdes = new JButton("<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
									btnextraireEtAnalyserdes.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new AnalyseurEnLigneCompas().setVisible(true);
										}
									});
									GridBagConstraints gbc_btnextraireEtAnalyserdes = new GridBagConstraints();
									gbc_btnextraireEtAnalyserdes.fill = GridBagConstraints.HORIZONTAL;
									gbc_btnextraireEtAnalyserdes.insets = new Insets(0, 0, 5, 0);
									gbc_btnextraireEtAnalyserdes.anchor = GridBagConstraints.NORTH;
									gbc_btnextraireEtAnalyserdes.gridx = 0;
									gbc_btnextraireEtAnalyserdes.gridy = 0;
									panel_4.add(btnextraireEtAnalyserdes, gbc_btnextraireEtAnalyserdes);
									
									JButton btnexporterDesDonnesen = new JButton("<html><p style=\"text-align:center;\">Exporter des données<br>en batch</p>");
									btnexporterDesDonnesen.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new ExtracteurBatchCompas().setVisible(true);
										}
									});
									
									JButton btnanalyserDesDonnesdj = new JButton("<html><p style=\"text-align:center;\">Analyser des données<br>déjà extraites</p>");
									btnanalyserDesDonnesdj.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new AnalyseurCompas().setVisible(true);
										}
									});
									GridBagConstraints gbc_btnanalyserDesDonnesdj = new GridBagConstraints();
									gbc_btnanalyserDesDonnesdj.fill = GridBagConstraints.HORIZONTAL;
									gbc_btnanalyserDesDonnesdj.anchor = GridBagConstraints.NORTH;
									gbc_btnanalyserDesDonnesdj.insets = new Insets(0, 0, 5, 0);
									gbc_btnanalyserDesDonnesdj.gridx = 0;
									gbc_btnanalyserDesDonnesdj.gridy = 1;
									panel_4.add(btnanalyserDesDonnesdj, gbc_btnanalyserDesDonnesdj);
									GridBagConstraints gbc_btnexporterDesDonnesen = new GridBagConstraints();
									gbc_btnexporterDesDonnesen.anchor = GridBagConstraints.NORTH;
									gbc_btnexporterDesDonnesen.fill = GridBagConstraints.HORIZONTAL;
									gbc_btnexporterDesDonnesen.gridx = 0;
									gbc_btnexporterDesDonnesen.gridy = 2;
									panel_4.add(btnexporterDesDonnesen, gbc_btnexporterDesDonnesen);
									
									JPanel panel_3 = new JPanel();
									panel_3.setBorder(new TitledBorder(null, "Analyse des adh\u00E9rents", TitledBorder.LEADING, TitledBorder.TOP, null, null));
									GridBagConstraints gbc_panel_3 = new GridBagConstraints();
									gbc_panel_3.insets = new Insets(0, 0, 5, 5);
									gbc_panel_3.fill = GridBagConstraints.BOTH;
									gbc_panel_3.gridx = 3;
									gbc_panel_3.gridy = 2;
									panel.add(panel_3, gbc_panel_3);
									GridBagLayout gbl_panel_3 = new GridBagLayout();
									gbl_panel_3.columnWidths = new int[]{231, 0};
									gbl_panel_3.rowHeights = new int[]{23, 0};
									gbl_panel_3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
									gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
									panel_3.setLayout(gbl_panel_3);
									
									JButton btnAnalyserDesDonnesAdherents = new JButton("<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
									btnAnalyserDesDonnesAdherents.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new AnalyseurEnLigneAdherents().setVisible(true);
										}
									});
									GridBagConstraints gbc_btnAnalyserDesDonnesAdherents = new GridBagConstraints();
									gbc_btnAnalyserDesDonnesAdherents.fill = GridBagConstraints.HORIZONTAL;
									gbc_btnAnalyserDesDonnesAdherents.anchor = GridBagConstraints.NORTH;
									gbc_btnAnalyserDesDonnesAdherents.gridx = 0;
									gbc_btnAnalyserDesDonnesAdherents.gridy = 0;
									panel_3.add(btnAnalyserDesDonnesAdherents, gbc_btnAnalyserDesDonnesAdherents);

	}

}
