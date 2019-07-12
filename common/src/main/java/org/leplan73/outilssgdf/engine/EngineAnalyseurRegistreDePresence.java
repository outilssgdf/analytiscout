package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActivite;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActiviteHeure;
import org.slf4j.Logger;

public class EngineAnalyseurRegistreDePresence extends Engine {

	public EngineAnalyseurRegistreDePresence(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(File entree, File modele, File sortie, int structure, boolean sous_dossier) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException, TransformeurException
	{
		progress_.setProgress(20,"Chargement des fichiers");
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		logger_.info("Chargement du fichier \"" + entree.getName() + "\"");
		int anneeDebut = ex.charge(new FileInputStream(entree))+1;
		progress_.setProgress(40,"Calculs");
		logger_.info("Calculs");
		
		List<RegistrePresenceActiviteHeure> activites_personnes = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activites_personnes);
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_personnes", activites_personnes);
		beans.put("activites_cec", activites_cec);
		
		File fichier_sortie = sous_dossier ? new File(sortie, "registredepresence_"+structure+".xlsx") : sortie;

		logger_.info("Génération du fichier");
		Transformeur.go(modele, beans, fichier_sortie);
		
		return true;
	}

	public void go(File entree, File modele, File sortie, int[] structures, boolean sous_dossier) throws Exception
	{
		start();
		try
		{
			if (structures == null)
			{
				logger_.info("Traitement de la structure");
				gopriv(entree, modele, sortie, 0, sous_dossier);
			}
			else
				for (int istructure : structures)
				{
					logger_.info("Traitement de la structure "+istructure);
					gopriv(entree, modele, sortie, istructure, sous_dossier);
				}
		} catch (IOException | JDOMException | ExtractionException | TransformeurException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		stop();
	}
}
