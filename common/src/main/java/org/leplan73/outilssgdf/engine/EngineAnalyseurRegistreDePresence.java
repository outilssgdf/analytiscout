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
import org.leplan73.outilssgdf.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActivite;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActiviteHeure;
import org.slf4j.Logger;

public class EngineAnalyseurRegistreDePresence extends Engine {

	public EngineAnalyseurRegistreDePresence(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(File entree, File modele, File sortie) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException
	{
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		int anneeDebut = ex.charge(new FileInputStream(entree))+1;
		
		List<RegistrePresenceActiviteHeure> activites = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activitesForfaitaire = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_jeunes = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_chefs = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_heures_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activites, activitesForfaitaire, activites_jeunes, activites_chefs);
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_heures", activites);
		beans.put("activites_forfaitaires", activitesForfaitaire);
		beans.put("activites_heures_cec", activites_heures_cec);
		beans.put("activites_cec", activites_cec);
		beans.put("activites_heures_jeunes", activites_jeunes);
		beans.put("activites_heures_chefs", activites_chefs);

		Transformeur.go(modele, beans, sortie);
		
		return true;
	}

	public void go(File entree, File modele, File sortie) throws Exception
	{
		Instant now = Instant.now();
		try
		{
			gopriv(entree, modele, sortie);
			progress_.setProgress(80);
		} catch (IOException | JDOMException | InvalidFormatException | ExtractionException e) {
			throw e;
		}
		progress_.setProgress(100);

		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		logger_.info("Terminé en " + d + " secondes");
	}

}
