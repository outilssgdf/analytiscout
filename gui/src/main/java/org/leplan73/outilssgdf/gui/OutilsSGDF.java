package org.leplan73.outilssgdf.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.utils.JHyperlink;
import org.leplan73.outilssgdf.gui.utils.Logging;
import org.leplan73.outilssgdf.gui.utils.Preferences;

import com.jcabi.manifests.Manifests;

public class OutilsSGDF extends JFrame {

	private JFrame frmAaa;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Logging.initLogger(OutilsSGDF.class, true);
		try {
			Preferences.init();
//			UIManager.setLookAndFeel(useopenjdk ? "javax.swing.plaf.windows.WindowsLookAndFeel" : "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
		} catch (InstantiationException e1) {
		} catch (IllegalAccessException e1) {
		} catch (UnsupportedLookAndFeelException e1) {
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OutilsSGDF window = new OutilsSGDF();
					window.frmAaa.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OutilsSGDF() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAaa = new JFrame();
		frmAaa.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Preferences.sauved(Consts.FENETRE_PRINCIPALE_X, frmAaa.getLocation().getX());
				Preferences.sauved(Consts.FENETRE_PRINCIPALE_Y, frmAaa.getLocation().getY());
			}
		});
		frmAaa.setResizable(false);
		try {
			frmAaa.setTitle("Outils SGDF v" + Manifests.read("version"));
		} catch (java.lang.IllegalArgumentException e) {
			frmAaa.setTitle("Outils SGDF (dev)");
		}

		double x = Preferences.litd(Consts.FENETRE_PRINCIPALE_X, 100.0);
		double y = Preferences.litd(Consts.FENETRE_PRINCIPALE_Y, 100.0);
		frmAaa.setBounds((int) x, (int) y, 683, 398);
		frmAaa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAaa.getContentPane().setLayout(new BoxLayout(frmAaa.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		frmAaa.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 40, 169, 0, 28, 0 };
		gbl_panel.rowHeights = new int[] { 74, 48, 51, 57, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblNewLabel = new JLabel("Je souhaite...");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		try {
					
					JButton btnAnalyserDesDonnes = new JButton("Extraire et analyser des données en ligne");
					btnAnalyserDesDonnes.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new AnalyseurEnLigne().setVisible(true);
						}
					});
					btnAnalyserDesDonnes.setFont(new Font("Tahoma", Font.PLAIN, 14));
					GridBagConstraints gbc_btnAnalyserDesDonnes = new GridBagConstraints();
					gbc_btnAnalyserDesDonnes.insets = new Insets(0, 0, 5, 5);
					gbc_btnAnalyserDesDonnes.gridx = 2;
					gbc_btnAnalyserDesDonnes.gridy = 1;
					panel.add(btnAnalyserDesDonnes, gbc_btnAnalyserDesDonnes);
			
					JButton button = new JButton("Exporter des données en batch");
					button.setFont(new Font("Tahoma", Font.PLAIN, 14));
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new ExtracteurBatch().setVisible(true);
						}
					});
							
									JButton btnNewButton = new JButton("Exporter des données");
									btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
									btnNewButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new Extracteur().setVisible(true);
										}
									});
									
											JButton btnNewButton_1 = new JButton("Analyser des données déjà extraites");
											btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
											btnNewButton_1.addActionListener(new ActionListener() {
												public void actionPerformed(ActionEvent e) {
													new Analyseur().setVisible(true);
												}
											});
											GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
											gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
											gbc_btnNewButton_1.gridx = 2;
											gbc_btnNewButton_1.gridy = 2;
											panel.add(btnNewButton_1, gbc_btnNewButton_1);
									GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
									gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
									gbc_btnNewButton.gridx = 2;
									gbc_btnNewButton.gridy = 3;
									panel.add(btnNewButton, gbc_btnNewButton);
					
							GridBagConstraints gbc_button = new GridBagConstraints();
							gbc_button.insets = new Insets(0, 0, 5, 5);
							gbc_button.gridx = 2;
							gbc_button.gridy = 4;
							panel.add(button, gbc_button);
			JHyperlink btnNewButton_2 = new JHyperlink("New button", "https://www.facebook.com/groups/outilssgdf");
			btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnNewButton_2.setText("Besoin d'aide ?");
			GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
			gbc_btnNewButton_2.insets = new Insets(0, 0, 0, 5);
			gbc_btnNewButton_2.gridx = 1;
			gbc_btnNewButton_2.gridy = 6;
			panel.add(btnNewButton_2, gbc_btnNewButton_2);
		} catch (URISyntaxException e1) {
		}
	}

	@Override
	public void dispose() {
		Preferences.sauved("x", this.getLocation().getX());
		Preferences.sauved("y", this.getLocation().getY());
		super.dispose();
	}

}
