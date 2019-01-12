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
import java.awt.BorderLayout;

public class OutilsSGDF extends JFrame {

	private JFrame frmOutils;

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
					window.frmOutils.setVisible(true);
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
		frmOutils = new JFrame();
		frmOutils.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Preferences.sauved(Consts.FENETRE_PRINCIPALE_X, frmOutils.getLocation().getX());
				Preferences.sauved(Consts.FENETRE_PRINCIPALE_Y, frmOutils.getLocation().getY());
			}
		});
		frmOutils.setResizable(false);
		try {
			frmOutils.setTitle("Outils SGDF v" + Manifests.read("version"));
		} catch (java.lang.IllegalArgumentException e) {
			frmOutils.setTitle("Outils SGDF (dev)");
		}

		double x = Preferences.litd(Consts.FENETRE_PRINCIPALE_X, 100.0);
		double y = Preferences.litd(Consts.FENETRE_PRINCIPALE_Y, 100.0);
		frmOutils.setBounds((int) x, (int) y, 748, 353);
		frmOutils.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOutils.getContentPane().setLayout(new BoxLayout(frmOutils.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		frmOutils.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 40, 169, 0, 28, 0, 0 };
		gbl_panel.rowHeights = new int[] { 37, 48, 51, 57, 46, 37, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);
		try {
					
					JButton btnAnalyserDesDonnes = new JButton("Extraire et analyser des données en ligne");
					btnAnalyserDesDonnes.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new AnalyseurEnLigne().setVisible(true);
						}
					});
					btnAnalyserDesDonnes.setFont(new Font("Tahoma", Font.PLAIN, 11));
					GridBagConstraints gbc_btnAnalyserDesDonnes = new GridBagConstraints();
					gbc_btnAnalyserDesDonnes.insets = new Insets(0, 0, 5, 5);
					gbc_btnAnalyserDesDonnes.gridx = 2;
					gbc_btnAnalyserDesDonnes.gridy = 1;
					panel.add(btnAnalyserDesDonnes, gbc_btnAnalyserDesDonnes);
			
					JButton button = new JButton("Exporter des données en batch");
					button.setFont(new Font("Tahoma", Font.PLAIN, 11));
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new ExtracteurBatch().setVisible(true);
						}
					});
							
									JButton btnNewButton = new JButton("Exporter des données");
									btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
									btnNewButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											new Extracteur().setVisible(true);
										}
									});
									
											JButton btnNewButton_1 = new JButton("Analyser des données déjà extraites");
											btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
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
									
											JLabel lblNewLabel = new JLabel("Je souhaite...");
											lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
											lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
											GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
											gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
											gbc_lblNewLabel.gridx = 1;
											gbc_lblNewLabel.gridy = 3;
											panel.add(lblNewLabel, gbc_lblNewLabel);
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
			
			JPanel panel_1 = new JPanel();
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.insets = new Insets(0, 0, 5, 5);
			gbc_panel_1.fill = GridBagConstraints.BOTH;
			gbc_panel_1.gridx = 1;
			gbc_panel_1.gridy = 6;
			panel.add(panel_1, gbc_panel_1);
			panel_1.setLayout(new BorderLayout(0, 0));
			JHyperlink btnNewButton_2 = new JHyperlink("New button", "https://www.facebook.com/groups/outilssgdf");
			panel_1.add(btnNewButton_2, BorderLayout.NORTH);
			btnNewButton_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnNewButton_2.setText("Besoin d'aide ?\r");
			
			JLabel lblNewLabel_1 = new JLabel("Pour le moment, consultez le site Facebook : outilssgdf");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			panel_1.add(lblNewLabel_1);
			
			JButton btnQuitter = new JButton("Quitter");
			GridBagConstraints gbc_btnQuitter = new GridBagConstraints();
			gbc_btnQuitter.insets = new Insets(0, 0, 5, 5);
			gbc_btnQuitter.gridx = 3;
			gbc_btnQuitter.gridy = 6;
			panel.add(btnQuitter, gbc_btnQuitter);
			btnQuitter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmOutils.dispose();
				}
			});
		} catch (URISyntaxException e1) {
		}
	}

}
