package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.leplan73.outilssgdf.gui.Template;
import org.leplan73.outilssgdf.gui.analyseurenligne.AnalyseurEnLigneJeunes;
import org.leplan73.outilssgdf.gui.analyseurenligne.AnalyseurEnLigneResponsables;
import org.leplan73.outilssgdf.gui.utils.ElementFactory;

public class AnalyseJeunes extends Template {

	/**
	 * Create the panel.
	 */
	public AnalyseJeunes() {
		super();

		JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Analyse des maitrises et compas</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_title1.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };
		panel_collection.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_collection.add(panel_1, BorderLayout.NORTH);
		panel_1.add(panel_title1, BorderLayout.NORTH);

		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new BorderLayout(0, 0));

		JButton button_1 = new JButton(
				"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
		panel_6.add(button_1, BorderLayout.NORTH);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseurEnLigneResponsables().setVisible(true);
			}
		});

		JPanel panel_title3 = ElementFactory.createActionTitle("<html><b>Analyse des jeunes</b></html>");
		GridBagLayout gbl_panel_title3 = (GridBagLayout) panel_title3.getLayout();
		gbl_panel_title3.rowWeights = new double[] { 0.0 };
		gbl_panel_title3.rowHeights = new int[] { 0 };
		gbl_panel_title3.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title3.columnWidths = new int[] { 0, 0 };

		JPanel panel_9 = new JPanel();
		panel_9.setLayout(new BorderLayout(0, 0));
		panel_9.setBorder(null);
		panel_collection.add(panel_9, BorderLayout.CENTER);
		panel_9.add(panel_title3, BorderLayout.NORTH);

		JPanel panel_3 = new JPanel();
		panel_9.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));

		JButton btnAnalyserDesDonnesAdherents = new JButton(
				"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
		btnAnalyserDesDonnesAdherents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseurEnLigneJeunes().setVisible(true);
			}
		});
		panel_3.add(btnAnalyserDesDonnesAdherents, BorderLayout.NORTH);
	}
}
