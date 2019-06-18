package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.leplan73.outilssgdf.gui.Template;
import org.leplan73.outilssgdf.gui.analyseurenligne.AnalyseurEnLigneAdherents;
import org.leplan73.outilssgdf.gui.analyseurenligne.AnalyseurEnLigneResponsables;
import org.leplan73.outilssgdf.gui.utils.ElementFactory;

public class AnalyseAdherents extends Template {

	/**
	 * Create the panel.
	 */
	public AnalyseAdherents() {
		super();

					JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Analyse des maitrises et compas</b></html>");
					GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_title1.getLayout();
					gbl_panel_title1.rowWeights = new double[]{0.0};
					gbl_panel_title1.rowHeights = new int[]{0};
					gbl_panel_title1.columnWeights = new double[]{0.0, 0.0};
					gbl_panel_title1.columnWidths = new int[]{0, 0};
									panel_collection.setLayout(new BorderLayout(0, 0));
									
									JPanel panel_1 = new JPanel();
									panel_1.setLayout(new BorderLayout(0, 0));
									panel_1.setBorder(null);
									panel_collection.add(panel_1, BorderLayout.NORTH);
									panel_1.add(panel_title1, BorderLayout.NORTH);
									
									JPanel panel_6 = new JPanel();
									panel_1.add(panel_6, BorderLayout.CENTER);
									panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.X_AXIS));
									
									JPanel panel_7 = new JPanel();
									panel_7.setBorder(null);
									panel_6.add(panel_7);
									GridBagLayout gbl_panel_7 = new GridBagLayout();
									gbl_panel_7.columnWidths = new int[]{292, 0};
									gbl_panel_7.rowHeights = new int[]{23, 0};
									gbl_panel_7.columnWeights = new double[]{1.0, Double.MIN_VALUE};
									gbl_panel_7.rowWeights = new double[]{0.0, Double.MIN_VALUE};
									panel_7.setLayout(gbl_panel_7);
									
									JButton button_1 = new JButton("<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
									button_1.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new AnalyseurEnLigneResponsables().setVisible(true);
										}
									});
									GridBagConstraints gbc_button_1 = new GridBagConstraints();
									gbc_button_1.anchor = GridBagConstraints.NORTH;
									gbc_button_1.fill = GridBagConstraints.HORIZONTAL;
									gbc_button_1.gridx = 0;
									gbc_button_1.gridy = 0;
									panel_7.add(button_1, gbc_button_1);
									
									JPanel panel_title2 = ElementFactory.createActionTitle("<html><b>Analyse des compas</b></html>");
									GridBagLayout gbl_panel_title2 = (GridBagLayout) panel_title2.getLayout();
									gbl_panel_title2.rowWeights = new double[]{0.0};
									gbl_panel_title2.rowHeights = new int[]{0};
									gbl_panel_title2.columnWeights = new double[]{0.0, 0.0};
									gbl_panel_title2.columnWidths = new int[]{0, 0};
									
									JPanel panel_9 = new JPanel();
									
									JPanel panel_title3 = ElementFactory.createActionTitle("<html><b>Analyse des adhérents</b></html>");
									GridBagLayout gbl_panel_title3 = (GridBagLayout) panel_title3.getLayout();
									gbl_panel_title3.rowWeights = new double[]{0.0};
									gbl_panel_title3.rowHeights = new int[]{0};
									gbl_panel_title3.columnWeights = new double[]{0.0, 0.0};
									gbl_panel_title3.columnWidths = new int[]{0, 0};

									panel_collection.add(panel_9, BorderLayout.SOUTH);
									panel_9.setLayout(new BorderLayout(0, 0));
									panel_9.setBorder(null);
									panel_9.add(panel_title3, BorderLayout.NORTH);
									
									JPanel panel_3 = new JPanel();
									panel_9.add(panel_3);
									panel_3.setBorder(null);
									GridBagLayout gbl_panel_3 = new GridBagLayout();
									gbl_panel_3.columnWidths = new int[]{231, 0};
									gbl_panel_3.rowHeights = new int[]{23, 160, 0};
									gbl_panel_3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
									gbl_panel_3.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
									panel_3.setLayout(gbl_panel_3);
									
									JButton btnAnalyserDesDonnesAdherents = new JButton("<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
									btnAnalyserDesDonnesAdherents.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new AnalyseurEnLigneAdherents().setVisible(true);
										}
									});
									GridBagConstraints gbc_btnAnalyserDesDonnesAdherents = new GridBagConstraints();
									gbc_btnAnalyserDesDonnesAdherents.insets = new Insets(0, 0, 5, 0);
									gbc_btnAnalyserDesDonnesAdherents.fill = GridBagConstraints.BOTH;
									gbc_btnAnalyserDesDonnesAdherents.gridx = 0;
									gbc_btnAnalyserDesDonnesAdherents.gridy = 0;
									panel_3.add(btnAnalyserDesDonnesAdherents, gbc_btnAnalyserDesDonnesAdherents);

	}

}
