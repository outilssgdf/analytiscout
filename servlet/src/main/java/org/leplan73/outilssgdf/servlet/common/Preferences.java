package org.leplan73.outilssgdf.servlet.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.leplan73.outilssgdf.outils.CryptoException;
import org.leplan73.outilssgdf.outils.PasswdCrypt;

public class Preferences {
	
	public static final String MAIL_SERVEUR_HOST = "messagerie.serveur";
	public static final String MAIL_SERVEUR_PORT = "messagerie.port";
	public static final String MAIL_SERVEUR_ADRESSE = "messagerie.adresse";
	public static final String MAIL_SERVEUR_ADRESSE_DEST = "messagerie.adresse_dest";
	public static final String MAIL_SERVEUR_NOM = "messagerie.nom";
	public static final String MAIL_SERVEUR_UTILISATEUR = "messagerie.utilisateur";
	public static final String MAIL_SERVEUR_MOTDEPASSE = "messagerie.motdepasse";
	
	static private Properties p_ = new Properties();
	static public void init(File fichier)
	{
		try {
			p_.load(new FileInputStream(fichier));
		} catch (IOException e) {
		}
	}
	
	public static double litd(String nom, double defaut)
	{
		String v = p_.getProperty(nom, String.valueOf(defaut));
		return Double.valueOf(v);
	}
	
	public static int liti(String nom, int defaut)
	{
		String v = p_.getProperty(nom, String.valueOf(defaut));
		return Integer.valueOf(v);
	}
	
	public static boolean litb(String nom, boolean defaut)
	{
		String v = p_.getProperty(nom, String.valueOf(defaut));
		return Boolean.valueOf(v);
	}
	
	public static String lit(String nom, String defaut, boolean encrypte)
	{
		String v = p_.getProperty(nom, String.valueOf(defaut));
		if (encrypte)
		{
			try {
				v = PasswdCrypt.decrypt(v);
			} catch (CryptoException e) {
			}
		}
		return v;
	}
}
