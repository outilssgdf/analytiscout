package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActiviteHeure;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceUnite;
import org.slf4j.Logger;

public class EngineAnalyseurCEC extends Engine {

	public EngineAnalyseurCEC(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	public void go(File fEntreeAnnee, File fEntreeAnneeP, File fSortie, File fModele) throws FileNotFoundException {
		progress_.setProgress(20);
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		logger_.info("Chargement du fichier \"" + fEntreeAnnee.getName() + "\"");
		progress_.setProgress(30);
		int anneeDebut = ex.charge(new FileInputStream(fEntreeAnnee))+1;
		logger_.info("Chargement du fichier \"" + fEntreeAnneeP.getName() + "\"");
		ex.charge(new FileInputStream(fEntreeAnneeP));
		progress_.setProgress(40);
		
		Collection<RegistrePresenceUnite> unites = ex.getUnites();
		unites.forEach(unite ->
		{
			Set<String> chefs = unite.getChefs();
			chefs.forEach(chef ->
			{
				List<RegistrePresenceActiviteHeure> activites_cec_chef = new ArrayList<RegistrePresenceActiviteHeure>();
				ex.getActivitesCecChef(chef, unite, anneeDebut, activites_cec_chef);
				
				Map<String, Object> beans = new HashMap<String, Object>();
				beans.put("annee", anneeDebut);
				beans.put("activites_cec", activites_cec_chef);
				try {
					Transformeur.go(fModele, beans, new File(fSortie, "CEC-"+anneeDebut+"-"+chef+".xlsx"));
				} catch (TransformeurException e) {
					e.printStackTrace();
				}
			});
		});
		progress_.setProgress(90);
	}
}
