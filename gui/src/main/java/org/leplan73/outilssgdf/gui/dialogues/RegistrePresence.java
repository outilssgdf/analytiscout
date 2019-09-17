package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.leplan73.outilssgdf.gui.Template;
import org.leplan73.outilssgdf.gui.cec.AnalyseCECEnligne;
import org.leplan73.outilssgdf.gui.registredepresence.AnalyseRegistreDePresenceEnLigne;
import org.leplan73.outilssgdf.gui.utils.ElementFactory;

public class RegistrePresence extends Template {

	/**
	 * Create the panel.
	 */
	public RegistrePresence() {
		super();

		if (true)
		{
		JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Registre de présence</b></html>");
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

		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JButton btnextraireEtAnalyserdes = new JButton(
				"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
		btnextraireEtAnalyserdes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseRegistreDePresenceEnLigne().setVisible(true);
			}
		});
		panel.add(btnextraireEtAnalyserdes, BorderLayout.NORTH);

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
		panel_3.setLayout(new BorderLayout(0, 0));

		JButton button = new JButton(
				"<html><p style=\"text-align:center;\">Extraire et analyser<br>des données en ligne</p>");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseCECEnligne().setVisible(true);
			}
		});
		panel_3.add(button, BorderLayout.NORTH);
		}
		else
		{
			JLabel lblaArrive = new JLabel("ça arrive !!!");
			lblaArrive.setHorizontalAlignment(SwingConstants.CENTER);
			panel_collection.add(lblaArrive, BorderLayout.CENTER);
		}
	}

}