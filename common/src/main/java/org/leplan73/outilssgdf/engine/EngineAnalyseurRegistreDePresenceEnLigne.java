package org.leplan73.outilssgdf.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
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
	
	private boolean gopriv(ExtractionRegistrePresence app, int structure, int annee, File sortie, File modele) throws ClientProtocolException, IOException, JDOMException, TransformeurException
	{
		progress_.setProgress(20);
		
		String donnees = app.extract(structure, annee, 0, false);
		InputStream in = new ByteArrayInputStream(donnees.getBytes(Consts.ENCODING_UTF8));
		
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		int anneeDebut = ex.charge(in)+1;
		progress_.setProgress(40);
		
		List<RegistrePresenceActiviteHeure> activitesReel = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activitesForfaitaire = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activitesReel, activitesForfaitaire);

		progress_.setProgress(60);
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_forfaitaires", activitesForfaitaire);
		beans.put("activites_reel", activitesReel);
		beans.put("activites_cec", activites_cec);

		Transformeur.go(modele, beans, sortie);

		progress_.setProgress(80);
		
		return true;
	}

	public void go(String identifiant, String motdepasse, File fSortie, File fModele, int annee, int structure, int[] structures) throws IOException, EngineException, JDOMException, TransformeurException
	{
		Instant now = Instant.now();
		
		try
		{
			ExtractionRegistrePresence app = new ExtractionRegistrePresence();
			login(app, identifiant, motdepasse);
			progress_.setProgress(40);
			
			if (structures == null)
			{
				gopriv(app, structure, annee, fSortie, fModele);
			}
			else
			{
				for (int istructure : structures)
				{
					boolean ret = gopriv(app, istructure, annee, fSortie, fModele);
					if (ret == false)
						break;
				}
			}
			
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
		} catch (TransformeurException e) {
			throw e;
		}
		progress_.setProgress(100);

		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		logger_.info("Terminé en " + d + " secondes");
	}
}
	