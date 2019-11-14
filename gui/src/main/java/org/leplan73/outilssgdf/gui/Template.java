package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.leplan73.outilssgdf.gui.dialogues.Configuration;
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
		setMinimumSize(new Dimension(10, 300));
		setLayout(new BorderLayout(0, 0));
			
		panel_collection = new JPanel();
		add(panel_collection, BorderLayout.CENTER);
		panel_collection.setBorder(new EmptyBorder(15, 5, 0, 5));
		panel_collection.setLayout(new BorderLayout(0, 10));

		JPanel panel_illustration = new JPanel();
		add(panel_illustration, BorderLayout.WEST);
																									
		panel_illustration.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel_illustration.setLayout(new BorderLayout(0, 0));
																									
		lblBanner = new JLabel("");
		lblBanner.setVerticalAlignment(SwingConstants.TOP);
		lblBanner.setIcon(Images.getImage());
			
		panel_illustration.add( lblBanner, BorderLayout.NORTH);
		
		JButton btnParametres = new JButton("Paramètres");
		btnParametres.setFont(btnParametres.getFont().deriveFont(btnParametres.getFont().getSize() - 1f));
		btnParametres.setToolTipText("Définir l'identification utilisée et autres paramètres");
		btnParametres.setIcon(Images.getIconCog());
		btnParametres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Configuration().setVisible(true);
			}
		});
		panel_illustration.add(btnParametres, BorderLayout.CENTER);
	}

}
