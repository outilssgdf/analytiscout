package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.dialogues.AnalyseJeunes;
import org.leplan73.outilssgdf.gui.dialogues.Camps;
import org.leplan73.outilssgdf.gui.dialogues.ExportAdherents;
import org.leplan73.outilssgdf.gui.dialogues.RegistrePresence;
import org.leplan73.outilssgdf.gui.utils.Images;
import org.leplan73.outilssgdf.gui.utils.JHyperlink;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.leplan73.outilssgdf.gui.utils.Version;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import com.jcabi.manifests.Manifests;

public class OutilsSGDFNormal extends OutilsSGDF {

	/**
	 * Create the frame.
	 */
	public OutilsSGDFNormal() {
		super();
		setTitle("Outils SGDF v0.0.0");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Images.getIcon());
		setFont(new Font("Dialog", Font.PLAIN, 12));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Preferences.sauved(Consts.FENETRE_PRINCIPALE_X, getLocation().getX());
				Preferences.sauved(Consts.FENETRE_PRINCIPALE_Y, getLocation().getY());
			}
		});
		try {
			Version v = Version.parse(Manifests.read("version"));
			setTitle("Outils SGDF v" + v.toString() + ExtractionIntranet.getIntranetServeur());
		} catch (java.lang.IllegalArgumentException e) {
			setTitle("Outils SGDF (dev)" + ExtractionIntranet.getIntranetServeur());
		}
		
		double x = Preferences.litd(Consts.FENETRE_PRINCIPALE_X, 100.0);
		double y = Preferences.litd(Consts.FENETRE_PRINCIPALE_Y, 100.0);
		setBounds(100, 100, 799, 460);
		setBounds((int) x, (int) y, 799, 428);
		
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		AnalyseJeunes a = new AnalyseJeunes();
		tabbedPane.addTab("Formations, qualifications et diplômes",a);
		
		RegistrePresence c = new RegistrePresence();
		tabbedPane.addTab("Registre de présence / CEC",c);
		
		Camps d = new Camps();
		tabbedPane.addTab("Camps",d);
		
		ExportAdherents b = new ExportAdherents();
		tabbedPane.addTab("Exporter",b);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 5, 0, 0));
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblVersionStatus = new JLabel("");
		panel.add(lblVersionStatus, BorderLayout.WEST);
		
		JHyperlink btnNewButton_2;
		try {
			
			JPanel panel_1 = new JPanel();
			panel.add(panel_1, BorderLayout.EAST);
			panel_1.setLayout(new BorderLayout(0, 0));
			btnNewButton_2 = new JHyperlink("Lien", "https://www.facebook.com/groups/outilssgdf");
			panel_1.add(btnNewButton_2, BorderLayout.CENTER);
			btnNewButton_2.setText("Consultez le groupe Facebook : outilssgdf");
			
			JLabel lblBesoinDaide = new JLabel("Besoin d'aide ?");
			panel_1.add(lblBesoinDaide, BorderLayout.WEST);
		} catch (URISyntaxException e) {
		}
		
		updateVersion(lblVersionStatus);
	}
}
