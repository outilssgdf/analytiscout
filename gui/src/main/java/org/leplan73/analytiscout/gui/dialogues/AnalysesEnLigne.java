package org.leplan73.analytiscout.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.leplan73.analytiscout.gui.Template;
import org.leplan73.analytiscout.gui.analyseurenligne.AnalyseurEnLigneJeunes;
import org.leplan73.analytiscout.gui.analyseurenligne.AnalyseurEnLigneMarins;
import org.leplan73.analytiscout.gui.analyseurenligne.AnalyseurEnLigneResponsables;
import org.leplan73.analytiscout.gui.utils.ElementFactory;

@SuppressWarnings("serial")
public class AnalysesEnLigne extends Template {

	/**
	 * Create the panel.
	 */
	public AnalysesEnLigne() {
		super();

		JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Analyse des maitrises et compas</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_title1.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{322, 0};
		gridBagLayout.rowHeights = new int[]{51, 64, 59, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_collection.setLayout(gridBagLayout);

		JPanel panel_title3 = ElementFactory.createActionTitle("<html><b>Analyse des jeunes</b></html>");
		GridBagLayout gbl_panel_title3 = (GridBagLayout) panel_title3.getLayout();
		gbl_panel_title3.rowWeights = new double[] { 0.0 };
		gbl_panel_title3.rowHeights = new int[] { 0 };
		gbl_panel_title3.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title3.columnWidths = new int[] { 0, 0 };

		JPanel panel_title9 = ElementFactory.createActionTitle("<html><b>Analyse des marins</b></html>");
		GridBagLayout gbl_panel_title9 = (GridBagLayout) panel_title9.getLayout();
		gbl_panel_title9.rowWeights = new double[] { 0.0 };
		gbl_panel_title9.rowHeights = new int[] { 0 };
		gbl_panel_title9.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title9.columnWidths = new int[] { 0, 0 };
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel_collection.add(panel_1, gbc_panel_1);
		panel_1.add(panel_title1, BorderLayout.NORTH);
		
		JPanel panel_11 = new JPanel();
		panel_1.add(panel_11, BorderLayout.CENTER);
		panel_11.setLayout(new BorderLayout(0, 0));
		
		JButton button_1 = new JButton(
				"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
		panel_11.add(button_1, BorderLayout.NORTH);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseurEnLigneResponsables().setVisible(true);
			}
		});

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new BorderLayout(0, 0));
		panel_3.setBorder(null);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.anchor = GridBagConstraints.NORTH;
		gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 1;
		panel_collection.add(panel_3, gbc_panel_3);
		panel_3.add(panel_title3, BorderLayout.NORTH);
				
		JPanel panel_31 = new JPanel();
		panel_3.add(panel_31, BorderLayout.CENTER);
		panel_31.setLayout(new BorderLayout(0, 0));
		
		JButton btnAnalyserDesDonnesAdherents = new JButton(
				"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
		btnAnalyserDesDonnesAdherents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseurEnLigneJeunes().setVisible(true);
			}
		});
		panel_31.add(btnAnalyserDesDonnesAdherents, BorderLayout.NORTH);
		
		JPanel panel_9 = new JPanel();
		panel_9.setLayout(new BorderLayout(0, 0));
		panel_9.setBorder(null);
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.anchor = GridBagConstraints.NORTH;
		gbc_panel_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_9.insets = new Insets(0, 0, 5, 0);
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 2;
		panel_collection.add(panel_9, gbc_panel_9);
		panel_9.add(panel_title9, BorderLayout.NORTH);
				
		JPanel panel_91 = new JPanel();
		panel_9.add(panel_91, BorderLayout.CENTER);
		panel_91.setLayout(new BorderLayout(0, 0));
		
		JButton btnAnalyserDesDonnesMarins = new JButton(
				"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
		btnAnalyserDesDonnesMarins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseurEnLigneMarins().setVisible(true);
			}
		});
		panel_91.add(btnAnalyserDesDonnesMarins, BorderLayout.NORTH);
	}
}
