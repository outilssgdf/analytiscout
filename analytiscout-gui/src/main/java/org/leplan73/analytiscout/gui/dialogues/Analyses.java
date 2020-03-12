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
import org.leplan73.analytiscout.gui.analyseur.AnalyseurJeunes;
import org.leplan73.analytiscout.gui.analyseur.AnalyseurMarins;
import org.leplan73.analytiscout.gui.extracteur.ExtracteurBatchJeunes;
import org.leplan73.analytiscout.gui.extracteur.ExtracteurBatchMarins;
import org.leplan73.analytiscout.gui.utils.ElementFactory;

@SuppressWarnings("serial")
public class Analyses extends Template {

	/**
	 * Create the panel.
	 */
	public Analyses() {
		super();

		JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Analyse des maitrises et compas</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_title1.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 322, 0 };
		gridBagLayout.rowHeights = new int[] { 51, 68, 59, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel_collection.setLayout(gridBagLayout);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setBorder(null);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel_collection.add(panel_1, gbc_panel_1);
		panel_1.add(panel_title1, BorderLayout.NORTH);

		JPanel panel_11 = new JPanel();
		panel_1.add(panel_11, BorderLayout.SOUTH);
		GridBagLayout gbl_panel_11 = new GridBagLayout();
		gbl_panel_11.columnWidths = new int[] { 139, 137, 0 };
		gbl_panel_11.rowHeights = new int[] { 37, 0 };
		gbl_panel_11.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_11.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_11.setLayout(gbl_panel_11);

		JButton button_3 = new JButton(
				"<html><p style=\"text-align:center;\">Analyser des données<br>déjà extraites</p>");
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_3.anchor = GridBagConstraints.NORTH;
		gbc_button_3.insets = new Insets(0, 0, 0, 5);
		gbc_button_3.gridx = 0;
		gbc_button_3.gridy = 0;
		panel_11.add(button_3, gbc_button_3);

		JButton button_4 = new JButton("<html><p style=\"text-align:center;\">Exporter des données<br>en batch</p>");
		GridBagConstraints gbc_button_4 = new GridBagConstraints();
		gbc_button_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_4.anchor = GridBagConstraints.NORTH;
		gbc_button_4.gridx = 1;
		gbc_button_4.gridy = 0;
		panel_11.add(button_4, gbc_button_4);

		JPanel panel_title3 = ElementFactory.createActionTitle("<html><b>Analyse des jeunes</b></html>");
		GridBagLayout gbl_panel_title3 = (GridBagLayout) panel_title3.getLayout();
		gbl_panel_title3.rowWeights = new double[] { 0.0 };
		gbl_panel_title3.rowHeights = new int[] { 0 };
		gbl_panel_title3.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title3.columnWidths = new int[] { 0, 0 };

		JPanel panel_title9 = ElementFactory.createActionTitle("<html><b>Analyse des marins</b></html>");
		GridBagLayout gbl_panel_title9 = (GridBagLayout) panel_title3.getLayout();
		gbl_panel_title9.rowWeights = new double[] { 0.0 };
		gbl_panel_title9.rowHeights = new int[] { 0 };
		gbl_panel_title9.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title9.columnWidths = new int[] { 0, 0 };

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
		GridBagLayout gbl_panel_31 = new GridBagLayout();
		gbl_panel_31.columnWidths = new int[] { 89, 183, 0 };
		gbl_panel_31.rowHeights = new int[] { 47, 0 };
		gbl_panel_31.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_31.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_31.setLayout(gbl_panel_31);

		JButton btnNewButton_1 = new JButton(
				"<html><p style=\"text-align:center;\">Analyser des données<br>déjà extraites</p>");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseurJeunes().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		panel_31.add(btnNewButton_1, gbc_btnNewButton_1);

		JButton btnNewButton = new JButton("Exporter des données en batch");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExtracteurBatchJeunes().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		panel_31.add(btnNewButton, gbc_btnNewButton);
		
		{


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
			GridBagLayout gbl_panel_91 = new GridBagLayout();
			gbl_panel_91.columnWidths = new int[] { 89, 183, 0 };
			gbl_panel_91.rowHeights = new int[] { 72, 0 };
			gbl_panel_91.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
			gbl_panel_91.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			panel_91.setLayout(gbl_panel_91);

			JButton btnNewButton_91 = new JButton(
					"<html><p style=\"text-align:center;\">Analyser des données<br>déjà extraites</p>");
			btnNewButton_91.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new AnalyseurMarins().setVisible(true);
				}
			});
			GridBagConstraints gbc_btnNewButton_91 = new GridBagConstraints();
			gbc_btnNewButton_91.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnNewButton_91.anchor = GridBagConstraints.NORTH;
			gbc_btnNewButton_91.insets = new Insets(0, 0, 0, 5);
			gbc_btnNewButton_91.gridx = 0;
			gbc_btnNewButton_91.gridy = 0;
			panel_91.add(btnNewButton_91, gbc_btnNewButton_91);

			JButton btnNewButton9 = new JButton("Exporter des données en batch");
			btnNewButton9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ExtracteurBatchMarins().setVisible(true);
				}
			});
			GridBagConstraints gbc_btnNewButton92 = new GridBagConstraints();
			gbc_btnNewButton92.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnNewButton92.anchor = GridBagConstraints.NORTH;
			gbc_btnNewButton92.gridx = 1;
			gbc_btnNewButton92.gridy = 0;
			panel_91.add(btnNewButton9, gbc_btnNewButton92);
		}

	}

}
