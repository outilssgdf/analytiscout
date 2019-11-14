package org.leplan73.outilssgdf.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.leplan73.outilssgdf.gui.utils.VersionCheck;

public class OutilsSGDF extends JFrame {
	public OutilsSGDF() {
	}
	protected JPanel contentPane;
	
	public void go()
	{
		setVisible(true);
		afficheAlertes();
	}
	
	private boolean afficheAlerteIdentifiants()
	{
		String identifiant = Preferences.lit(Consts.INTRANET_IDENTIFIANT, "", true);
		String motdepasse = Preferences.lit(Consts.INTRANET_MOTDEPASSE, "", true);
		String structure = Preferences.lit(Consts.INTRANET_STRUCTURE, "", true);
		if (identifiant.isEmpty() || motdepasse.isEmpty() || structure.isEmpty())
		{
			JOptionPane.showMessageDialog(this,"Merci de rentrer identifiant, mot de passe\ndans la configuration (en bas à gauche)");
			return true;
		}
		return false;
	}
	
	private boolean afficheAlerteExportDonnees()
	{
		boolean ret = Preferences.litb(Consts.ALERTE_EXPORTDONNEES, false);
		if (!ret)
		{
			JOptionPane.showMessageDialog(this, 
					"Les informations contenues dans le fichier que vous allez extraire sont la propriété exclusive\n"+
					"des Scouts et Guides de France. Ce fichier ne saurait être vendu, prêté ou communiqué à quiconque,\n"+
					"personne ou organisme extérieur à l'Association pour quelque usage que ce soit sans l'autorisation\n"+
					"expresse du Conseil d'Administration. Toute utilisation abusive de ces informations entraînera\n"+
					"la mise en cause de votre propre responsabilité civile et pénale personnelle en cas de non respect\n"+
					"de cette règle de confidentialité.\n\n"+
					"En cliquant sur le bouton OK, je déclare accepter cette règle.");
			Preferences.sauveb(Consts.ALERTE_EXPORTDONNEES, true);
			return true;
		}
		return false;
	}
	
	protected void afficheAlertes()
	{
		afficheAlerteIdentifiants();
		afficheAlerteExportDonnees();
	}
	
	protected void updateVersion(JLabel lblVersionStatus)
	{
		Thread t = new Thread(new Runnable()
		{
			public void run() {
				VersionCheck.check(lblVersionStatus);
	            }
		});
		t.setDaemon(true);
		t.start();
	}

}
