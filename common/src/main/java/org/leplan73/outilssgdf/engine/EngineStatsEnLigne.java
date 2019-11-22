package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.ParamSortie;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Groupe;
import org.leplan73.outilssgdf.intranet.ExtractionStats;
import org.leplan73.outilssgdf.stats.EffectifG;
import org.leplan73.outilssgdf.stats.Effectifs;
import org.slf4j.Logger;

import com.jcabi.manifests.Manifests;

public class EngineStatsEnLigne extends EngineConnecte {

	public EngineStatsEnLigne(Progress progress, Logger logger) {
		super(progress, logger);
	}

	private boolean gopriv(ExtractionStats app, String identifiant, String motdepasse, InputStream modele, String structure, ParamSortie sortie, boolean garderFichiers) throws ExtractionException, TransformeurException, ClientProtocolException, IOException, JDOMException
	{
		progress_.setProgress(60);
		logger_.info("Extraction des stats de la structure "+structure);
		
		Map<Groupe,Map<Integer, Effectifs>> effectifs = app.extract(structure);
		
//		effectifs.forEach((k,v) ->
//		{
//			System.out.print(k.toString() + " --> ");
//			v.forEach((k2,v2) ->
//			{
//				System.out.print(k2+"="+v2.toString()+",");
//			});
//			System.out.println();
//		});
		
		List<EffectifG> effg = new ArrayList<EffectifG>();
		effectifs.forEach((k,v) -> 
		{
			EffectifG e = new EffectifG();
			e.groupe = k;
			e.eff = v;
			effg.add(e);
		});
		
		String version = "";
		try
		{
			version = Manifests.read("version");
		}
		catch(java.lang.IllegalArgumentException e) {
		}
		General general = new General(version);
		File fichier_sortie = sortie.construit(structure, ".xlsx");

		logger_.info("Génération du fichier \"" + fichier_sortie.getName() + "\" à partir du modèle");
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("effectifs", effg);
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

	public void go(String identifiant, String motdepasse, InputStream modele, String structure, ParamSortie sortie, boolean garderFichiers) throws EngineException, LoginEngineException
	{
		start();
		try
		{
			logger_.info("Chargement du fichier de traitement");
			ExtractionStats app = new ExtractionStats();
			login(app, identifiant, motdepasse, true);
			progress_.setProgress(40);
			
			logger_.info("Traitement de la structure "+structure);
			gopriv(app, identifiant, motdepasse, modele, structure, sortie, garderFichiers);
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
