package org.leplan73.analytiscout.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.ClientProtocolException;
import org.jdom2.JDOMException;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.ParamSortie;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.Transformeur;
import org.leplan73.analytiscout.TransformeurException;
import org.leplan73.analytiscout.calcul.General;
import org.leplan73.analytiscout.calcul.Groupe;
import org.leplan73.analytiscout.intranet.ExtractionStats;
import org.leplan73.analytiscout.intranet.LoginEngineException;
import org.leplan73.analytiscout.outils.Structure;
import org.leplan73.analytiscout.stats.EffectifG;
import org.leplan73.analytiscout.stats.Effectifs;
import org.slf4j.Logger;

public class EngineStatsEnLigne extends EngineConnecte {

	public EngineStatsEnLigne(Progress progress, Logger logger) {
		super(progress, logger);
	}

	private boolean gopriv(ExtractionStats app, String identifiant, String motdepasse, InputStream modele, int structure, ParamSortie sortie, boolean anonymiser, boolean garderFichiers) throws ExtractionException, TransformeurException, ClientProtocolException, IOException, JDOMException
	{
		progress_.setProgress(60);
		logger_.info("Extraction des stats de la structure "+Structure.formatStructure(structure));
		
		Map<Groupe,Map<Integer, Effectifs>> effectifs_findannee = app.extract(structure, true);
		Map<Groupe,Map<Integer, Effectifs>> effectifs = app.extract(structure, false);
		
		List<EffectifG> effg_findannee = new ArrayList<EffectifG>();
		effectifs_findannee.forEach((k,v) -> 
		{
			EffectifG e = new EffectifG();
			e.groupe = k;
			e.eff = v;
			effg_findannee.add(e);
		});
		
		List<EffectifG> effg = new ArrayList<EffectifG>();
		effectifs.forEach((k,v) -> 
		{
			EffectifG e = new EffectifG();
			e.groupe = k;
			e.eff = v;
			effg.add(e);
		});

		if (anonymiser)
		{
			AtomicInteger ai = new AtomicInteger(100000000);
			AtomicInteger groupeId = new AtomicInteger();
			
			for (int i=0;i<effg_findannee.size();i++)
			{
				int n = ai.incrementAndGet();
				int id = groupeId.incrementAndGet();
				effg_findannee.get(i).groupe.setNom(n + " - STRUCTURE A"+ id);
				effg.get(i).groupe.setNom(n + " - STRUCTURE A"+ id);
			}
		}
		
		General general = General.generer();
		
		File fichier_sortie = sortie.construit(structure, ".xlsx");

		logger_.info("Génération du fichier \"" + fichier_sortie.getName() + "\" à partir du modèle");
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("effectifs", effg);
		beans.put("effectifs_findannee", effg_findannee);
		beans.put("general", general);
		progress_.setProgress(80);
		
		modele.reset();
		
		if (sortie.getIsStream())
		{
			Transformeur.go(modele, beans, sortie.getStream());
			sortie.close();
		}
		else
		{
			FileOutputStream fosSortie = new FileOutputStream(fichier_sortie);
			Transformeur.go(modele, beans, fosSortie);
			fosSortie.close();
		}
		return true;
	}

	public void go(String identifiant, String motdepasse, InputStream modele, int[] structures, ParamSortie sortie, boolean anonymiser, boolean garderFichiers) throws EngineException, LoginEngineException
	{
		start();
		try
		{
			logger_.info("Chargement du fichier de traitement");
			ExtractionStats app = new ExtractionStats();
			login(app, identifiant, motdepasse, true);
			progress_.setProgress(40);
			
			for (int structure : structures)
			{
				logger_.info("Traitement de la structure "+Structure.formatStructure(structure));
				gopriv(app, identifiant, motdepasse, modele, structure, sortie, anonymiser, garderFichiers);
			}
			logout();
		} catch (IOException | JDOMException | ExtractionException | TransformeurException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
			try {
				modele.close();
			} catch (IOException e) {
			}
		}
	}
}
