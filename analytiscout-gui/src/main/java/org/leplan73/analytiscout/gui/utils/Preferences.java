package org.leplan73.analytiscout.gui.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.leplan73.analytiscout.outils.CryptoException;
import org.leplan73.analytiscout.outils.PasswdCrypt;

public class Preferences {
	
	private static final String ANALYTISCOUT = "analytiscout";
	private static final String ANALYTISCOUT_PROPERTIES = "analytiscout.properties";
	
	static private File file_;
	static private Properties p_ = new Properties();
	
	static private void migration()
	{
		File fichierOutilSgdf = new File(System.getProperty("user.home")+"/"+"outilssgdf.properties");
		if (fichierOutilSgdf.exists())
		{
			File fichierAnalytiScout = new File(System.getProperty("user.home")+"/"+ANALYTISCOUT_PROPERTIES);
			fichierOutilSgdf.renameTo(fichierAnalytiScout);
		}
	}
	
	static public void init()
	{
		migration();
		file_ = new File(System.getProperty("user.home")+"/"+ANALYTISCOUT_PROPERTIES);
		try {
			file_.createNewFile();
			p_.load(new FileInputStream(file_));
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
	
	static public void sauveb(String nom, boolean d)
	{
		p_.setProperty(nom, String.valueOf(d));
		try {
			p_.store(new FileOutputStream(file_), ANALYTISCOUT);
		} catch (IOException e) {
		}
	}
	
	static public void sauvei(String nom, int d)
	{
		p_.setProperty(nom, String.valueOf(d));
		try {
			p_.store(new FileOutputStream(file_), ANALYTISCOUT);
		} catch (IOException e) {
		}
	}
	
	static public void sauved(String nom, double d)
	{
		p_.setProperty(nom, String.valueOf(d));
		try {
			p_.store(new FileOutputStream(file_), ANALYTISCOUT);
		} catch (IOException e) {
		}
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
	
	static public void sauve(String nom, String d, boolean encrypte)
	{
		try {
			if (encrypte)
			{
				d = PasswdCrypt.encrypt(d);
			}
			p_.setProperty(nom, d);
			p_.store(new FileOutputStream(file_), ANALYTISCOUT);
		} catch (IOException | CryptoException e) {
		}
	}
}
