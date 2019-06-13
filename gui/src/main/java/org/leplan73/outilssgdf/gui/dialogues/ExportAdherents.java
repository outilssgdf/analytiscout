package org.leplan73.outilssgdf.gui.dialogues;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.leplan73.outilssgdf.gui.ElementFactory;
import org.leplan73.outilssgdf.gui.Template;
import org.leplan73.outilssgdf.gui.extracteur.Extracteur;

public class ExportAdherents extends Template {

	/**
	 * Create the panel.
	 */
	public ExportAdherents() {
		super();

					JPanel panel_title1 = ElementFactory.createActionTitle("<html><b>Exporter</b></html>");
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
									panel_1.add(panel_6, BorderLayout.SOUTH);
									panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.X_AXIS));
									
									JPanel panel_7 = new JPanel();
									panel_7.setBorder(null);
									panel_6.add(panel_7);
									GridBagLayout gbl_panel_7 = new GridBagLayout();
									gbl_panel_7.columnWidths = new int[]{292, 0};
									gbl_panel_7.rowHeights = new int[]{0, 0};
									gbl_panel_7.columnWeights = new double[]{1.0, Double.MIN_VALUE};
									gbl_panel_7.rowWeights = new double[]{0.0, Double.MIN_VALUE};
									panel_7.setLayout(gbl_panel_7);
									
									JButton button_3 = new JButton("Exporter des données");
									button_3.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new Extracteur().setVisible(true);
										}
									});
									GridBagConstraints gbc_button_3 = new GridBagConstraints();
									gbc_button_3.anchor = GridBagConstraints.NORTH;
									gbc_button_3.fill = GridBagConstraints.HORIZONTAL;
									gbc_button_3.gridx = 0;
									gbc_button_3.gridy = 0;
									panel_7.add(button_3, gbc_button_3);
									
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
