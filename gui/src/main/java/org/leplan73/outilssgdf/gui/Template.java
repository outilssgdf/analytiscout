package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.leplan73.outilssgdf.gui.utils.Images;

public class Template extends JPanel {

	public JPanel panel_collection= null;
	protected JLabel lblBanner;
	
	/**
	 * Create the panel.
	 */
	public Template() {
		super();
		setBorder(new EmptyBorder(0, 0, 0, 5));
		setMinimumSize(new Dimension(10, 200));
		setLayout(new BorderLayout(0, 0));
			
		panel_collection = new JPanel();
		add(panel_collection, BorderLayout.CENTER);
		panel_collection.setBorder(new EmptyBorder(15, 5, 0, 5));
		panel_collection.setLayout(new BorderLayout(0, 10));
																				

		JPanel panel_illustration = new JPanel();
		add(panel_illustration, BorderLayout.WEST);
																									
		panel_illustration.setBorder(new EmptyBorder(10, 5, 10, 5));
		panel_illustration.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
																									
		lblBanner = new JLabel("");
		lblBanner.setIcon(Images.getImage());
			
		panel_illustration.add( lblBanner);
	}

}
