package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.intranet.ExtractionRegistrePresence;
import org.slf4j.Logger;

public class EngineRegistreDePresence extends Engine {

	private ExtractionRegistrePresence connection_;
	
	public EngineRegistreDePresence(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private void login(ExtractionRegistrePresence connection, String identifiant, String motdepasse) throws ClientProtocolException, IOException, EngineException
	{
		connection_ = connection;
		logger_.info("Connexion");
		
		connection_.init();
		if (connection_.login(identifiant,motdepasse) == false)
		{
			throw new EngineException("erreur de connexion", true);
		}
	}
	
	private void logout() throws IOException
	{
		connection_.close();
	}
	
	private boolean gopriv(ExtractionRegistrePresence app, String identifiant, String motdepasse, File sortie, int structure, int annee) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException
	{
		logger_.info("Extraction registre de presence (annee="+annee+")");
		String donnees = app.extract(structure, annee, 0 , true);
		
		// Génération du fichier csv
		logger_.info("Génération du fichier "+sortie.getName());
		FileOutputStream os = new FileOutputStream(sortie);
		os.write(donnees.getBytes());
		os.flush();
		os.close();
		return true;
	}

	public void go(String identifiant, String motdepasse, File sortie, int structure, int annee) throws Exception
	{
		Instant now = Instant.now();
		try
		{
			ExtractionRegistrePresence app = new ExtractionRegistrePresence();
			login(app, identifiant, motdepasse);
			progress_.setProgress(40);
			
			gopriv(app, identifiant, motdepasse, sortie, structure, annee);
			progress_.setProgress(80);
			
			logout();
		} catch (IOException | JDOMException | InvalidFormatException | ExtractionException e) {
			throw e;
		}
		progress_.setProgress(100);

		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		logger_.info("Terminé en " + d + " secondes");
	}

}
