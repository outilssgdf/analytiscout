package org.leplan73.analytiscout.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.Transformeur;
import org.leplan73.analytiscout.TransformeurException;
import org.leplan73.analytiscout.outils.FichierSortie;
import org.leplan73.analytiscout.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.analytiscout.registrepresence.RegistrePresenceActiviteHeure;
import org.leplan73.analytiscout.registrepresence.RegistrePresenceUnite;
import org.slf4j.Logger;

public class EngineAnalyseurCEC extends Engine {

	public EngineAnalyseurCEC(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	public void go(File fEntreeAnneeP, File fEntreeAnnee, File fSortie, File fModele, boolean anonymiser) throws EngineException {
		start();
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		progress_.setProgress(30);
		try {
			logger_.info("Chargement du fichier \"" + fEntreeAnneeP.getName() + "\"");
			int anneeDebut = ex.charge(new FileInputStream(fEntreeAnneeP))+1;
			logger_.info("Chargement du fichier \"" + fEntreeAnnee.getName() + "\"");
			ex.charge(new FileInputStream(fEntreeAnnee));
			if (anonymiser)
			{
				ex.anonymiser();
			}
			progress_.setProgress(40);
			
			Collection<RegistrePresenceUnite> unites = ex.getUnites();
			for (RegistrePresenceUnite unite : unites)
			{
				Set<String> chefs = unite.getChefs();
				for (String chef : chefs)
				{
					List<RegistrePresenceActiviteHeure> activites_cec_chef = new ArrayList<RegistrePresenceActiviteHeure>();
					ex.getActivitesCecChef(chef, unite, anneeDebut, activites_cec_chef);
					
					Map<String, Object> beans = new HashMap<String, Object>();
					beans.put("annee", anneeDebut);
					beans.put("activites_cec", activites_cec_chef);
					FileInputStream fismodele = new FileInputStream(fModele);
					FileOutputStream fosSortie = new FileOutputStream(new FichierSortie(fSortie, "CEC-"+anneeDebut+"-"+chef+".xlsx"));
					Transformeur.go(fismodele, beans, fosSortie);
					fismodele.close();
					fosSortie.close();
				};
			};
		} catch (IOException | TransformeurException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
