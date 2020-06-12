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
import org.leplan73.analytiscout.gui.formations.ExtracteurFormations;
import org.leplan73.analytiscout.gui.utils.ElementFactory;

@SuppressWarnings("serial")
public class Formations extends Template {

	/**
	 * Create the panel.
	 */
	public Formations() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{322, 0};
		gridBagLayout.rowHeights = new int[]{46, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_collection.setLayout(gridBagLayout);

		JPanel panel_titleExporter = ElementFactory.createActionTitle("<html><b>Exporter</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_titleExporter.getLayout();
		gbl_panel_title1.rowWeights = new double[] { 0.0 };
		gbl_panel_title1.rowHeights = new int[] { 0 };
		gbl_panel_title1.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panel_title1.columnWidths = new int[] { 0, 0 };
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		
		JPanel panelExporter = new JPanel();
		GridBagConstraints gbc_panelExporter = new GridBagConstraints();
		gbc_panelExporter.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelExporter.anchor = GridBagConstraints.NORTH;
		gbc_panelExporter.insets = new Insets(0, 0, 5, 0);
		gbc_panelExporter.gridx = 0;
		gbc_panelExporter.gridy = 0;
		panel_collection.add(panelExporter, gbc_panelExporter);
		
				panelExporter.setLayout(new BorderLayout(0, 0));
				
						JPanel panel_1 = new JPanel();
						panel_1.setBorder(null);
						panelExporter.add(panel_1, BorderLayout.SOUTH);
						panelExporter.add(panel_titleExporter, BorderLayout.NORTH);
						
								JButton btnNewButton = new JButton("Exporter des données");
								btnNewButton.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										new ExtracteurFormations().setVisible(true);
									}
								});
								panel_1.setLayout(new BorderLayout(0, 0));
								panel_1.add(btnNewButton, BorderLayout.SOUTH);
	}

}
