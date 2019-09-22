package org.leplan73.outilssgdf.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.intranet.ExtractionRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActivite;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActiviteHeure;
import org.slf4j.Logger;

public class EngineAnalyseurRegistreDePresenceEnLigne extends EngineConnecte {

	public EngineAnalyseurRegistreDePresenceEnLigne(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionRegistrePresence app, int structure, boolean recursif, int annee, File sortie, File modele, boolean sous_dossier) throws ClientProtocolException, IOException, JDOMException, TransformeurException
	{
		progress_.setProgress(20, "Extraction");
		logger_.info("Extraction");
		String donnees = app.extract(structure, recursif, annee, 0, false);
		InputStream in = new ByteArrayInputStream(donnees.getBytes(Consts.ENCODING_UTF8));
		
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		int anneeDebut = ex.charge(in)+1;
		progress_.setProgress(40, "Chargement des fichiers");
		logger_.info("Chargement des fichiers");
		
		List<RegistrePresenceActiviteHeure> activites_personnes = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activites_personnes);

		progress_.setProgress(60, "Calculs");
		logger_.info("Calculs");
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_personnes", activites_personnes);
		beans.put("activites_reel", activites_personnes);
		beans.put("activites_cec", activites_cec);
		
		File fichier_sortie = sous_dossier ? new File(sortie, "registredepresence_"+structure+".xlsx") : sortie;

		logger_.info("Génération du fichier");
		Transformeur.go(modele, beans, fichier_sortie);
		
		return true;
	}

	public void go(String identifiant, String motdepasse, File fSortie, File fModele, int annee, int[] structures, boolean recursif, boolean sous_dossier) throws EngineException
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
				boolean ret = gopriv(app, istructure, recursif, annee, fSortie, fModele, sous_dossier);
				if (ret == false)
					break;
			}
			logout();
		} catch (IOException | JDOMException | TransformeurException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
	