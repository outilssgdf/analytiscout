package org.leplan73.outilssgdf.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.intranet.ExtractionRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActiviteHeure;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceUnite;
import org.slf4j.Logger;

public class EngineAnalyseurCECEnLigne extends EngineConnecte {

	public EngineAnalyseurCECEnLigne(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionRegistrePresence app, int structure, int annee, File fSortie, File fModele) throws ClientProtocolException, IOException, JDOMException
	{
		progress_.setProgress(20);
		
		String donneesAnnee = app.extract(structure, annee, 0 , true);
		String donneesAnneeP = app.extract(structure, annee-1, 0 , true);
		
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		logger_.info("Chargement des données de l'année " + annee + "\"");
		progress_.setProgress(30);
		int anneeDebut = ex.charge(new ByteArrayInputStream(donneesAnnee.getBytes()))+1;
		logger_.info("Chargement des données de l'année " + (annee-1) + "\"");
		ex.charge(new ByteArrayInputStream(donneesAnneeP.getBytes()));
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		});
		
		return true;
	}

	public void go(String identifiant, String motdepasse, File fSortie, File fModele, int annee, int[] structures) throws IOException, EngineException, JDOMException {
		Instant now = Instant.now();
		
		try
		{
			ExtractionRegistrePresence app = new ExtractionRegistrePresence();
			login(app, identifiant, motdepasse);
			progress_.setProgress(40);
			
			for (int istructure : structures)
			{
				boolean ret = gopriv(app, istructure, annee, fSortie, fModele);
				if (ret == false)
					break;
			}
			logout();
		} catch (IOException e) {
			throw e;
		} catch (EngineException e) {
			throw e;
		} catch (JDOMException e) {
			throw e;
		}
		progress_.setProgress(100);

		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		logger_.info("Terminé en " + d + " secondes");
	}
}
