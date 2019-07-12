package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.leplan73.outilssgdf.gui.Template;
import org.leplan73.outilssgdf.gui.cec.AnalyseCEC;
import org.leplan73.outilssgdf.gui.registredepresence.AnalyseRegistreDePresence;
import org.leplan73.outilssgdf.gui.registredepresence.ExportRegistreDePresence;
import org.leplan73.outilssgdf.gui.registredepresence.ExtractionRegistreDePresence;
import org.leplan73.outilssgdf.gui.utils.ElementFactory;

public class RegistrePresence2 extends Template {

	/**
	 * Create the panel.
	 */
	public RegistrePresence2() {
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

		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new BorderLayout(0, 0));

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(null);
		panel_6.add(panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[] { 115, 0, 0, 0 };
		gbl_panel_7.rowHeights = new int[] { 0, 0 };
		gbl_panel_7.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_7.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_7.setLayout(gbl_panel_7);

		JButton btnNewButton = new JButton("Extraction");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExtractionRegistreDePresence().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		panel_7.add(btnNewButton, gbc_btnNewButton);

		JButton btnAnalyse = new JButton("Analyse");
		btnAnalyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseRegistreDePresence().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnAnalyse = new GridBagConstraints();
		gbc_btnAnalyse.insets = new Insets(0, 0, 0, 5);
		gbc_btnAnalyse.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAnalyse.gridx = 1;
		gbc_btnAnalyse.gridy = 0;
		panel_7.add(btnAnalyse, gbc_btnAnalyse);
		
		JButton btnExport = new JButton("Export");
		btnExport.setEnabled(false);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExportRegistreDePresence().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExport.gridx = 2;
		gbc_btnExport.gridy = 0;
		panel_7.add(btnExport, gbc_btnExport);

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

		JPanel panel = new JPanel();
		panel_2.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_1 = new JButton("Génération");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AnalyseCEC().setVisible(true);
			}
		});
		panel.add(btnNewButton_1, BorderLayout.NORTH);
	}
		else
		{
			JLabel lblaArrive = new JLabel("ça arrive !!!");
			lblaArrive.setHorizontalAlignment(SwingConstants.CENTER);
			panel_collection.add(lblaArrive, BorderLayout.CENTER);
		}
	}

}
