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
import org.leplan73.outilssgdf.gui.extracteur.Extracteur;
import org.leplan73.outilssgdf.gui.generateur.Generateur;
import org.leplan73.outilssgdf.gui.utils.ElementFactory;

public class ExportAdherents extends Template {

	/**
	 * Create the panel.
	 */
	public ExportAdherents() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{15, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_collection.setLayout(gridBagLayout);
		
		JPanel panelExporter = new JPanel();
		panelExporter.setBorder(null);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		panel_collection.add(panelExporter, gbc_panel);
		
		JPanel panel_titleExporter = ElementFactory.createActionTitle("<html><b>Exporter</b></html>");
		GridBagLayout gbl_panel_title1 = (GridBagLayout) panel_titleExporter.getLayout();
		gbl_panel_title1.rowWeights = new double[]{0.0};
		gbl_panel_title1.rowHeights = new int[]{0};
		gbl_panel_title1.columnWeights = new double[]{0.0, 0.0};
		gbl_panel_title1.columnWidths = new int[]{0, 0};

		panelExporter.add(panel_titleExporter, BorderLayout.NORTH);
		panelExporter.setLayout(new BoxLayout(panelExporter, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		panel_collection.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{89, 0};
		gbl_panel_1.rowHeights = new int[]{23, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton btnNewButton = new JButton("Exporter des données");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Extracteur().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		panel_1.add(btnNewButton, gbc_btnNewButton);
		
		JPanel panelGenerer = new JPanel();
		panelGenerer.setBorder(null);
		GridBagConstraints gbc_panel2 = new GridBagConstraints();
		gbc_panel2.anchor = GridBagConstraints.NORTH;
		gbc_panel2.insets = new Insets(0, 0, 5, 0);
		gbc_panel2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel2.gridx = 0;
		gbc_panel2.gridy = 2;
		panel_collection.add(panelGenerer, gbc_panel2);
		
		JPanel panel_titleGenerer = ElementFactory.createActionTitle("<html><b>Générateur</b></html>");
		GridBagLayout gbl_panel_title2 = (GridBagLayout) panel_titleGenerer.getLayout();
		gbl_panel_title2.rowWeights = new double[]{0.0};
		gbl_panel_title2.rowHeights = new int[]{0};
		gbl_panel_title2.columnWeights = new double[]{0.0, 0.0};
		gbl_panel_title2.columnWidths = new int[]{0, 0};

		panelGenerer.add(panel_titleGenerer, BorderLayout.NORTH);
		panelGenerer.setLayout(new BoxLayout(panelGenerer, BoxLayout.X_AXIS));
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 3;
		panel_collection.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{209, 0};
		gbl_panel_3.rowHeights = new int[]{23, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JButton btnNewButton_1 = new JButton("Générer une archive zip");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Generateur().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		panel_3.add(btnNewButton_1, gbc_btnNewButton_1);

					

	}

}
