package org.leplan73.outilssgdf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Params extends Properties {
	
	private static final long serialVersionUID = 1L;
	
	static private Params this_;
	static private Logger logger_;
    
	static {
		this_ = new Params();
		logger_ = LogManager.getLogger(Params.class);
	}
	
	static public void init(String path)
	{
		try {
			this_.load(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			logger_.warn("Erreur charge fichier de paramètres : ", e);
		} catch (IOException e) {
			logger_.warn("Erreur charge fichier de paramètres : ", e);
		}
	}
	
	static public String get(String nom)
	{
		return this_.getProperty(nom);
	}
	
	static public String get(String nom, String defaut)
	{
		return this_.getProperty(nom, defaut);
	}
}
