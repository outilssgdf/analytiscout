package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.intranet.ExtractionRegistrePresence2;
import org.slf4j.Logger;

public class EngineExtractionRegistreDePresence extends EngineConnecte {

	public EngineExtractionRegistreDePresence(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionRegistrePresence2 app, String identifiant, String motdepasse, File sortie, int structure, boolean recursif, int annee, boolean sous_dossier) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException, InterruptedException
	{
		logger_.info("Extraction registre de presence (annee="+annee+")");
		String donnees = app.extract(structure, recursif, annee, 0 , false);
		
		File fichier_sortie = sous_dossier ? new File(sortie, "registredepresence_"+structure+".csv") : sortie;
		
		// Génération du fichier csv
		logger_.info("Génération du fichier "+fichier_sortie.getName());
		OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(fichier_sortie),Consts.ENCODING_WINDOWS);
		os.write(donnees);
		os.flush();
		os.close();
		return true;
	}

	public void go(String identifiant, String motdepasse, File sortie, int[] structures, boolean recursif, int annee) throws EngineException
	{
		start();
		try
		{
			ExtractionRegistrePresence2 app = new ExtractionRegistrePresence2();
			progress_.setProgress(30, "Connexion");
			login(app, identifiant, motdepasse, true);
			progress_.setProgress(40, "Extraction");
			
			for (int istructure : structures)
			{
				logger_.info("Traitement de la structure "+istructure);
				boolean ret = gopriv(app, identifiant, motdepasse, sortie, istructure, recursif, annee, (structures.length > 1));
				if (ret == false)
					break;
			}
			progress_.setProgress(80,"Déconnexion");
			
			logout();
		} catch (IOException | JDOMException | InvalidFormatException | ExtractionException | InterruptedException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}

}
