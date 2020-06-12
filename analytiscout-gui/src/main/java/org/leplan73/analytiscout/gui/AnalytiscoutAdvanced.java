package org.leplan73.analytiscout.gui;

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

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.gui.dialogues.Analyses;
import org.leplan73.analytiscout.gui.dialogues.Camps;
import org.leplan73.analytiscout.gui.dialogues.ExportAdherents;
import org.leplan73.analytiscout.gui.dialogues.Formations;
import org.leplan73.analytiscout.gui.dialogues.RegistrePresence2;
import org.leplan73.analytiscout.gui.utils.Images;
import org.leplan73.analytiscout.gui.utils.JHyperlink;
import org.leplan73.analytiscout.gui.utils.Preferences;

public class AnalytiscoutAdvanced extends Analytiscout {

	/**
	 * Create the frame.
	 */
	public AnalytiscoutAdvanced() {
		super();
		setTitle("AnalytiScout v0.0.0");
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
		majTitre();
		
		double x = Preferences.litd(Consts.FENETRE_PRINCIPALE_X, 100.0);
		double y = Preferences.litd(Consts.FENETRE_PRINCIPALE_Y, 100.0);
		setBounds((int) x, (int) y, 799, 516);
		
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		Analyses a = new Analyses();
		tabbedPane.addTab("Formations, qualifications et diplômes",a);
		
		RegistrePresence2 c = new RegistrePresence2();
		tabbedPane.addTab("Registre de présence / CEC",c);
		
		Camps d = new Camps();
		tabbedPane.addTab("Camps", d);
		
		ExportAdherents b = new ExportAdherents();
		tabbedPane.addTab("Exporter",b);
		
		Formations f = new Formations();
		tabbedPane.addTab("Formations", f);
		
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
			
			JLabel lblNewLabel = new JLabel("Besoin d'aide ?");
			panel_1.add(lblNewLabel, BorderLayout.WEST);
			btnNewButton_2 = new JHyperlink("Lien", "https://www.facebook.com/groups/analytiscout");
			panel_1.add(btnNewButton_2);
			btnNewButton_2.setText("Consultez le groupe Facebook : analytiscout");
		} catch (URISyntaxException e) {
		}
		
		updateVersion(lblVersionStatus);

	}

}
