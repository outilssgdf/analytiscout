package org.leplan73.outilssgdf.gui.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Preferences {
	
	static private File file_;
	static private Properties p_ = new Properties();
	
	static public void init()
	{
		file_ = new File(System.getProperty("user.home")+"/"+"outilssgdf.properties");
		try {
			file_.createNewFile();
			p_.load(new FileInputStream(file_));
		} catch (IOException e) {
		}
	}
	
	public static double lit(String nom, double defaut)
	{
		String v = p_.getProperty(nom, String.valueOf(defaut));
		return Double.valueOf(v);
	}
	
	static public void sauve(String nom, double d)
	{
		p_.setProperty(nom, String.valueOf(d));
		try {
			p_.store(new FileOutputStream(file_), "outilsgdf");
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
			p_.store(new FileOutputStream(file_), "outilsgdf");
		} catch (IOException | CryptoException e) {
		}
	}
}
