package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
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
	
	private boolean gopriv(File entree, File modele, File sortie) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException, TransformeurException
	{
		progress_.setProgress(20);
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		logger_.info("Chargement du fichier \"" + entree.getName() + "\"");
		int anneeDebut = ex.charge(new FileInputStream(entree))+1;
		progress_.setProgress(40);
		
		List<RegistrePresenceActiviteHeure> activitesReel = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activitesForfaitaire = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activitesReel, activitesForfaitaire);
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_forfaitaires", activitesForfaitaire);
		beans.put("activites_reel", activitesReel);
		beans.put("activites_cec", activites_cec);

		Transformeur.go(modele, beans, sortie);
		
		return true;
	}

	public void go(File entree, File modele, File sortie) throws Exception
	{
		Instant now = Instant.now();
		gopriv(entree, modele, sortie);
		progress_.setProgress(100);

		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		logger_.info("Terminé en " + d + " secondes");
	}
}
