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
import org.leplan73.analytiscout.gui.cec.AnalyseCEC;
import org.leplan73.analytiscout.gui.cec.AnalyseCECEnligne;
import org.leplan73.analytiscout.gui.registredepresence.AnalyseRegistreDePresence;
import org.leplan73.analytiscout.gui.registredepresence.AnalyseRegistreDePresenceCivile;
import org.leplan73.analytiscout.gui.registredepresence.AnalyseRegistreDePresenceEnLigne;
import org.leplan73.analytiscout.gui.utils.ElementFactory;

public class RegistrePresence extends Template {

	/**
	 * Create the panel.
	 */
	public RegistrePresence() {
		super();

		JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Registre de présence</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_title1.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };
		panel_collection.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setBorder(null);
		panel_collection.add(panel_1, BorderLayout.NORTH);
		panel_1.add(panel_title1, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.SOUTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{135, 139, 0, 0};
		gbl_panel.rowHeights = new int[]{37, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
				JButton btnextraireEtAnalyserdes = new JButton(
						"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
				btnextraireEtAnalyserdes.setEnabled(false);
				GridBagConstraints gbc_btnextraireEtAnalyserdes = new GridBagConstraints();
				gbc_btnextraireEtAnalyserdes.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnextraireEtAnalyserdes.anchor = GridBagConstraints.NORTH;
				gbc_btnextraireEtAnalyserdes.insets = new Insets(0, 0, 0, 5);
				gbc_btnextraireEtAnalyserdes.gridx = 0;
				gbc_btnextraireEtAnalyserdes.gridy = 0;
				panel.add(btnextraireEtAnalyserdes, gbc_btnextraireEtAnalyserdes);
				btnextraireEtAnalyserdes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new AnalyseRegistreDePresenceEnLigne().setVisible(true);
					}
				});
		
		JButton btnextraireEtAnalyserdes_1 = new JButton("Analyser des données");
		btnextraireEtAnalyserdes_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseRegistreDePresence().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnextraireEtAnalyserdes_1 = new GridBagConstraints();
		gbc_btnextraireEtAnalyserdes_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnextraireEtAnalyserdes_1.fill = GridBagConstraints.BOTH;
		gbc_btnextraireEtAnalyserdes_1.gridx = 1;
		gbc_btnextraireEtAnalyserdes_1.gridy = 0;
		panel.add(btnextraireEtAnalyserdes_1, gbc_btnextraireEtAnalyserdes_1);
		
		JButton btnanneCalendaire = new JButton("<html><p style=\"text-align:center;\">Analyser des données<br>(Année civile)</p>");
		btnanneCalendaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseRegistreDePresenceCivile().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnanneCalendaire = new GridBagConstraints();
		gbc_btnanneCalendaire.fill = GridBagConstraints.BOTH;
		gbc_btnanneCalendaire.gridx = 2;
		gbc_btnanneCalendaire.gridy = 0;
		panel.add(btnanneCalendaire, gbc_btnanneCalendaire);

		JPanel panel_title2 = ElementFactory.createActionTitle("<html><b>CEC</b></html>");
		GridBagLayout gbl_panel_title2 = (GridBagLayout) panel_title2.getLayout();
		gbl_panel_title2.rowWeights = new double[] { 0.0 };
		gbl_panel_title2.rowHeights = new int[] { 0 };
		gbl_panel_title2.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title2.columnWidths = new int[] { 0, 0 };

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout(0, 0));
		panel_2.setBorder(null);
		panel_collection.add(panel_2, BorderLayout.CENTER);
		panel_2.add(panel_title2, BorderLayout.NORTH);

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.CENTER);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{135, 139, 0};
		gbl_panel_3.rowHeights = new int[]{220, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
				JButton button = new JButton(
						"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
				button.setEnabled(false);
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new AnalyseCECEnligne().setVisible(true);
					}
				});
				GridBagConstraints gbc_button = new GridBagConstraints();
				gbc_button.fill = GridBagConstraints.HORIZONTAL;
				gbc_button.anchor = GridBagConstraints.NORTH;
				gbc_button.insets = new Insets(0, 0, 0, 5);
				gbc_button.gridx = 0;
				gbc_button.gridy = 0;
				panel_3.add(button, gbc_button);
		
		JButton btnGnrationDesFiches = new JButton("Génération des fiches");
		btnGnrationDesFiches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseCEC().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnGnrationDesFiches = new GridBagConstraints();
		gbc_btnGnrationDesFiches.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGnrationDesFiches.anchor = GridBagConstraints.NORTH;
		gbc_btnGnrationDesFiches.gridx = 1;
		gbc_btnGnrationDesFiches.gridy = 0;
		panel_3.add(btnGnrationDesFiches, gbc_btnGnrationDesFiches);
	}
}
