package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.border.TitledBorder;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.utils.JHyperlink;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.leplan73.outilssgdf.gui.utils.Version;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import com.jcabi.manifests.Manifests;
import java.awt.Toolkit;

public class OutilsSGDF extends JFrame {

	private JFrame frmOutils;
	
	public void go()
	{
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
		frmOutils.setIconImage(Toolkit.getDefaultToolkit().getImage(OutilsSGDF.class.getResource("/org/leplan73/outilssgdf/gui/icone.png")));
		frmOutils.setFont(new Font("Dialog", Font.PLAIN, 12));
		frmOutils.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Preferences.sauved(Consts.FENETRE_PRINCIPALE_X, frmOutils.getLocation().getX());
				Preferences.sauved(Consts.FENETRE_PRINCIPALE_Y, frmOutils.getLocation().getY());
			}
		});
		frmOutils.setResizable(false);
		try {
			Version v = Version.parse(Manifests.read("version"));
			frmOutils.setTitle("Outils SGDF v" + v.toString() + ExtractionIntranet.getIntranetServeur());
		} catch (java.lang.IllegalArgumentException e) {
			frmOutils.setTitle("Outils SGDF (dev)" + ExtractionIntranet.getIntranetServeur());
		}

		double x = Preferences.litd(Consts.FENETRE_PRINCIPALE_X, 100.0);
		double y = Preferences.litd(Consts.FENETRE_PRINCIPALE_Y, 100.0);
		frmOutils.setBounds((int) x, (int) y, 580, 331);
		frmOutils.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOutils.getContentPane().setLayout(new BoxLayout(frmOutils.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		frmOutils.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 35, 209, 135, 47, 0 };
		gbl_panel.rowHeights = new int[] { 63, 48, 37, 46, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);
		try {
											
											JPanel panel_2 = new JPanel();
											panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Analyse des ma\u00EEtrises", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
											GridBagConstraints gbc_panel_2 = new GridBagConstraints();
											gbc_panel_2.insets = new Insets(0, 0, 5, 5);
											gbc_panel_2.fill = GridBagConstraints.BOTH;
											gbc_panel_2.gridx = 1;
											gbc_panel_2.gridy = 1;
											panel.add(panel_2, gbc_panel_2);
											GridBagLayout gbl_panel_2 = new GridBagLayout();
											gbl_panel_2.columnWidths = new int[]{231, 0};
											gbl_panel_2.rowHeights = new int[]{23, 0};
											gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
											gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
											panel_2.setLayout(gbl_panel_2);
													
													JButton btnAnalyserDesDonnes = new JButton("Extraire et analyser des données en ligne");
													GridBagConstraints gbc_btnAnalyserDesDonnes = new GridBagConstraints();
													gbc_btnAnalyserDesDonnes.anchor = GridBagConstraints.NORTHWEST;
													gbc_btnAnalyserDesDonnes.gridx = 0;
													gbc_btnAnalyserDesDonnes.gridy = 0;
													panel_2.add(btnAnalyserDesDonnes, gbc_btnAnalyserDesDonnes);
													btnAnalyserDesDonnes.addActionListener(new ActionListener() {
														public void actionPerformed(ActionEvent e) {
															new AnalyseurEnLigneResponsables().setVisible(true);
														}
													});
			
			JPanel panel_4 = new JPanel();
			panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Analyse des compas", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel_4 = new GridBagConstraints();
			gbc_panel_4.insets = new Insets(0, 0, 5, 5);
			gbc_panel_4.fill = GridBagConstraints.BOTH;
			gbc_panel_4.gridx = 2;
			gbc_panel_4.gridy = 1;
			panel.add(panel_4, gbc_panel_4);
			GridBagLayout gbl_panel_4 = new GridBagLayout();
			gbl_panel_4.columnWidths = new int[]{231, 0};
			gbl_panel_4.rowHeights = new int[]{23, 0};
			gbl_panel_4.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_panel_4.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel_4.setLayout(gbl_panel_4);
			
			JButton button_1 = new JButton("Extraire et analyser des données en ligne");
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new AnalyseurEnLigneCompas().setVisible(true);
				}
			});
			GridBagConstraints gbc_button_1 = new GridBagConstraints();
			gbc_button_1.anchor = GridBagConstraints.NORTHWEST;
			gbc_button_1.gridx = 0;
			gbc_button_1.gridy = 0;
			panel_4.add(button_1, gbc_button_1);
			
			JPanel panel_5 = new JPanel();
			panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Extraction", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel_5 = new GridBagConstraints();
			gbc_panel_5.insets = new Insets(0, 0, 5, 5);
			gbc_panel_5.fill = GridBagConstraints.BOTH;
			gbc_panel_5.gridx = 1;
			gbc_panel_5.gridy = 2;
			panel.add(panel_5, gbc_panel_5);
			GridBagLayout gbl_panel_5 = new GridBagLayout();
			gbl_panel_5.columnWidths = new int[]{231, 0};
			gbl_panel_5.rowHeights = new int[]{23, 0};
			gbl_panel_5.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_panel_5.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel_5.setLayout(gbl_panel_5);
			
					JButton btnNewButton = new JButton("Exporter des données");
					GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
					gbc_btnNewButton.gridx = 0;
					gbc_btnNewButton.gridy = 0;
					panel_5.add(btnNewButton, gbc_btnNewButton);
					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new Extracteur().setVisible(true);
						}
					});
			
			JPanel panel_3 = new JPanel();
			panel_3.setBorder(new TitledBorder(null, "Analyse des adh\u00E9rents", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel_3 = new GridBagConstraints();
			gbc_panel_3.insets = new Insets(0, 0, 5, 5);
			gbc_panel_3.fill = GridBagConstraints.BOTH;
			gbc_panel_3.gridx = 2;
			gbc_panel_3.gridy = 2;
			panel.add(panel_3, gbc_panel_3);
			GridBagLayout gbl_panel_3 = new GridBagLayout();
			gbl_panel_3.columnWidths = new int[]{231, 0};
			gbl_panel_3.rowHeights = new int[]{23, 0};
			gbl_panel_3.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel_3.setLayout(gbl_panel_3);
			
			JButton btnAnalyserDesDonnesAdherents = new JButton("Extraire et analyser des données en ligne");
			btnAnalyserDesDonnesAdherents.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new AnalyseurEnLigneAdherents().setVisible(true);
				}
			});
			GridBagConstraints gbc_btnAnalyserDesDonnesAdherents = new GridBagConstraints();
			gbc_btnAnalyserDesDonnesAdherents.anchor = GridBagConstraints.NORTHWEST;
			gbc_btnAnalyserDesDonnesAdherents.gridx = 0;
			gbc_btnAnalyserDesDonnesAdherents.gridy = 0;
			panel_3.add(btnAnalyserDesDonnesAdherents, gbc_btnAnalyserDesDonnesAdherents);
			
			JPanel panel_1 = new JPanel();
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.insets = new Insets(0, 0, 5, 5);
			gbc_panel_1.fill = GridBagConstraints.VERTICAL;
			gbc_panel_1.gridx = 1;
			gbc_panel_1.gridy = 4;
			panel.add(panel_1, gbc_panel_1);
			panel_1.setLayout(new BorderLayout(0, 0));
			JHyperlink btnNewButton_2 = new JHyperlink("New button", "https://www.facebook.com/groups/outilssgdf");
			panel_1.add(btnNewButton_2, BorderLayout.NORTH);
			btnNewButton_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btnNewButton_2.setText("Besoin d'aide ?\r");
			
			JLabel lblNewLabel_1 = new JLabel("Consultez le groupe Facebook : outilssgdf");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			panel_1.add(lblNewLabel_1);
			
			JButton btnQuitter = new JButton("Quitter");
			GridBagConstraints gbc_btnQuitter = new GridBagConstraints();
			gbc_btnQuitter.anchor = GridBagConstraints.EAST;
			gbc_btnQuitter.insets = new Insets(0, 0, 5, 5);
			gbc_btnQuitter.gridx = 2;
			gbc_btnQuitter.gridy = 4;
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
