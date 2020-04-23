package org.leplan73.analytiscout.engine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.intranet.ExtractionCamps;
import org.leplan73.analytiscout.intranet.LoginEngineException;
import org.slf4j.Logger;

public class EngineExtracteurCamps extends EngineConnecte {

	public EngineExtracteurCamps(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionCamps app, String identifiant, String motdepasse, File sortie, int structure, Date debut, Date fin) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException
	{
		logger_.info("Extraction Camps (debut="+debut.toString()+","+"fin="+fin.toString()+")");
		String donnees = app.extract(structure, debut, fin, true);
		
		logger_.info("Sauvegarde");
		PrintStream fout = new PrintStream(new BufferedOutputStream(new FileOutputStream(sortie)),false, Consts.ENCODING_UTF8);
		fout.write(donnees.getBytes());
		fout.flush();
	    fout.close();
		return true;
	}

	public void go(String identifiant, String motdepasse, File sortie, int structure, Date debut, Date fin) throws EngineException, LoginEngineException
	{
		start();
		try
		{
			ExtractionCamps app = new ExtractionCamps();
			progress_.setProgress(30, "Connexion");
			login(app, identifiant, motdepasse);
			progress_.setProgress(40, "Génération des fichiers");
			
			gopriv(app, identifiant, motdepasse, sortie, structure, debut, fin);
			progress_.setProgress(80,"Déconnexion");
			
			logout();
		} catch (IOException | JDOMException | InvalidFormatException | ExtractionException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}

}
