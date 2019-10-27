package org.leplan73.outilssgdf.engine;

import java.io.ByteArrayInputStream;
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

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.intranet.ExtractionRegistrePresence2;
import org.leplan73.outilssgdf.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActiviteHeure;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceUnite;
import org.slf4j.Logger;

public class EngineAnalyseurCECEnLigne extends EngineConnecte {

	public EngineAnalyseurCECEnLigne(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionRegistrePresence2 app, int structure, int annee, File fSortie, File fModele) throws IOException, JDOMException, TransformeurException, InterruptedException
	{
		progress_.setProgress(20);
		
		String donneesAnnee = null;
		String donneesAnneeP = null;
		try {
			donneesAnnee = app.extract(structure, true, annee, 0 , true);
			donneesAnneeP = app.extract(structure, true, annee-1, 0 , true);
		} catch (IOException | JDOMException e) {
			throw e;
		}
		
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		logger_.info("Chargement des données de l'année " + annee + "\"");
		progress_.setProgress(30);
		int anneeDebut = ex.charge(new ByteArrayInputStream(donneesAnnee.getBytes()))+1;
		logger_.info("Chargement des données de l'année " + (annee-1) + "\"");
		ex.charge(new ByteArrayInputStream(donneesAnneeP.getBytes()));
		progress_.setProgress(40);

		Collection<RegistrePresenceUnite> unites = ex.getUnites();
		for (RegistrePresenceUnite unite : unites)
		{
			Set<String> chefs = unite.getChefs();
			for (String chef : chefs)
			{
				logger_.info("Génération du fichier pour "+chef+" de l'unité\""+unite.getNom()+"\"");
				List<RegistrePresenceActiviteHeure> activites_cec_chef = new ArrayList<RegistrePresenceActiviteHeure>();
				ex.getActivitesCecChef(chef, unite, anneeDebut, activites_cec_chef);
				
				Map<String, Object> beans = new HashMap<String, Object>();
				beans.put("annee", anneeDebut);
				beans.put("activites_cec", activites_cec_chef);
				try {
					FileInputStream fismodele = new FileInputStream(fModele);
					FileOutputStream fosSortie = new FileOutputStream(new File(fSortie, "CEC-"+anneeDebut+"-"+chef+".xlsx"));
					Transformeur.go(fismodele, beans, fosSortie);
					fismodele.close();
					fosSortie.close();
				} catch (TransformeurException e) {
					throw e;
				}
			}
		}
		return true;
	}

	public void go(String identifiant, String motdepasse, File fSortie, File fModele, int annee, int[] structures) throws IOException, EngineException, JDOMException, LoginEngineException {
		start();
		try
		{
			ExtractionRegistrePresence2 app = new ExtractionRegistrePresence2();
			login(app, identifiant, motdepasse);
			progress_.setProgress(40);
			
			for (int istructure : structures)
			{
				logger_.info("Traitement de la structure "+istructure);
				boolean ret = gopriv(app, istructure, annee, fSortie, fModele);
				if (ret == false)
					break;
			}
			logout();
		} catch (IOException | TransformeurException | InterruptedException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
