package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.intranet.ExtractionRegistrePresence;
import org.slf4j.Logger;

public class EngineRegistreDePresence extends EngineConnecte {

	public EngineRegistreDePresence(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionRegistrePresence app, String identifiant, String motdepasse, File sortie, int structure, int annee, boolean sous_dossier) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException
	{
		logger_.info("Extraction registre de presence (annee="+annee+")");
		String donnees = app.extract(structure, annee, 0 , true);
		
		File fichier_sortie = sous_dossier ? new File(sortie, "registredepresence_"+structure+".csv") : sortie;
		
		// Génération du fichier csv
		logger_.info("Génération du fichier "+fichier_sortie.getName());
		FileOutputStream os = new FileOutputStream(fichier_sortie);
		os.write(donnees.getBytes());
		os.flush();
		os.close();
		return true;
	}

	public void go(String identifiant, String motdepasse, File sortie, int[] structures, int annee) throws EngineException
	{
		start();
		try
		{
			ExtractionRegistrePresence app = new ExtractionRegistrePresence();
			progress_.setProgress(30, "Connexion");
			login(app, identifiant, motdepasse);
			progress_.setProgress(40, "Extraction");
			
			for (int istructure : structures)
			{
				logger_.info("Traitement de la structure "+istructure);
				boolean ret = gopriv(app, identifiant, motdepasse, sortie, istructure, annee, (structures.length > 1));
				if (ret == false)
					break;
			}
			progress_.setProgress(80,"Déconnexion");
			
			logout();
		} catch (IOException | JDOMException | InvalidFormatException | ExtractionException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		stop();
	}

}
