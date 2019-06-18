package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.dialogues.AnalyseAdherents2;
import org.leplan73.outilssgdf.gui.dialogues.Camps;
import org.leplan73.outilssgdf.gui.dialogues.ExportAdherents;
import org.leplan73.outilssgdf.gui.dialogues.RegistrePresence2;
import org.leplan73.outilssgdf.gui.utils.Images;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.leplan73.outilssgdf.gui.utils.Version;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import com.jcabi.manifests.Manifests;

public class OutilsSGDFAdvanced extends JFrame {

	private JPanel contentPane;
	
	public void go()
	{
		setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public OutilsSGDFAdvanced() {
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
		setBounds((int) x, (int) y, 799, 430);
		
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		AnalyseAdherents2 a = new AnalyseAdherents2();
		tabbedPane.addTab("Formations, qualifications et diplômes",a);
		
		RegistrePresence2 c = new RegistrePresence2();
		tabbedPane.addTab("Registre de présence / CEC",c);
		
		Camps d = new Camps();
		tabbedPane.addTab("Camps",d);
		
		ExportAdherents b = new ExportAdherents();
		tabbedPane.addTab("Exporter",b);
	}

}
