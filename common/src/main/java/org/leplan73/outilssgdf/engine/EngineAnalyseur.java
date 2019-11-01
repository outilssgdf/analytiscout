package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.ParamEntree;
import org.leplan73.outilssgdf.ParamSortie;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.alerte.Alertes;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentsFormes;
import org.slf4j.Logger;

import com.jcabi.manifests.Manifests;

public class EngineAnalyseur extends Engine {

	public EngineAnalyseur(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(Properties pbatch, ParamEntree entree, InputStream batch, InputStream modele, int structure, boolean age, String batch_type, ParamSortie sortie, boolean anonymiser, boolean pargroupe) throws TransformeurException, ExtractionException, IOException, JDOMException
	{
		logger_.info("Traitement de la structure "+structure);
		
		Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
		File fichierAdherents = null;

		File dossierStructure = entree.construit(structure);
		dossierStructure.mkdirs();
		progress_.setProgress(40);

		int index = 1;
		for (;;) {
			String generateur = pbatch.getProperty("generateur." + index);
			if (generateur == null) {
				break;
			}

			ExtraKey extra = new ExtraKey(
					pbatch.getProperty("fichier." + index, pbatch.getProperty("nom." + index, "")),
					pbatch.getProperty("nom." + index, ""),
					pbatch.getProperty("batchtype." + index, batch_type));
			File fichier_entree = new File(dossierStructure, extra.fichier_ + "." + generateur);

			logger_.info("Chargement du fichier \"" + fichier_entree.getName() + "\"");

			if (extra.ifTout()) {
				fichierAdherents = fichier_entree;
			} else
				extraMap.put(extra, new ExtracteurExtraHtml(fichier_entree, age));
			index++;
		}
		
		File fichier_sortie = sortie.construit(structure, ".xlsx");

		progress_.setProgress(50);
		logger_.info("Chargement du fichier \"" + fichierAdherents.getName() + "\"");
		ExtracteurIndividusHtml adherents = new ExtracteurIndividusHtml(fichierAdherents, extraMap, age, anonymiser);
		
		String version = "";
		try {
			version = Manifests.read("version");
		} catch (java.lang.IllegalArgumentException e) {
		}
		General general = new General(version);
		
		if (pargroupe)
		{
			Map<String,ExtracteurIndividusHtml> groupes = adherents.genereGroupes();
			
			Set<String> codeGroupes = groupes.keySet();
			for (String codeGroupe : codeGroupes)
			{
				ExtracteurIndividusHtml groupe = groupes.get(codeGroupe);
				AdherentsFormes compas = new AdherentsFormes();
				compas.charge(groupe, extraMap);
				
				Global global = new Global(groupe.getGroupe(), groupe.getMarins());
				groupe.calculGlobal(global);
				progress_.setProgress(60);
				
				Alertes alertesJeunes = new Alertes();
				groupe.construitsAlertes(alertesJeunes, true, age);
				Alertes alertesResponsables = new Alertes();
				groupe.construitsAlertes(alertesResponsables, false, age);
				progress_.setProgress(70);
				
				logger_.info("Génération du fichier \"" + fichier_sortie.getName() + "\" à partir du modèle");
				Map<String, Object> beans = new HashMap<String, Object>();
				beans.put("adherents", groupe.getAdherentsList());
				beans.put("chefs", groupe.getChefsList());
				beans.put("compas", groupe.getCompasList());
				beans.put("unites", groupe.getUnitesList());
				beans.put("alertes_jeunes", alertesJeunes);
				beans.put("alertes_responsables", alertesResponsables);
				beans.put("general", general);
				beans.put("global", global);
		
				modele.reset();
				FileOutputStream fosSortie = new FileOutputStream(fichier_sortie);
				Transformeur.go(modele, beans, fosSortie);
				fosSortie.close();
			};
			progress_.setProgress(80);
		}
		else
		{
			AdherentsFormes compas = new AdherentsFormes();
			compas.charge(adherents, extraMap);
			progress_.setProgress(60);
			
			Global global = new Global(adherents.getGroupe(), adherents.getMarins());
			adherents.calculGlobal(global);
			progress_.setProgress(80);
			
			Alertes alertesJeunes = new Alertes();
			adherents.construitsAlertes(alertesJeunes, true, age);
			Alertes alertesResponsables = new Alertes();
			adherents.construitsAlertes(alertesResponsables, false, age);
	
			logger_.info("Génération du fichier \"" + fichier_sortie.getName() + "\" à partir du modèle");
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("adherents", adherents.getAdherentsList());
			beans.put("chefs", adherents.getChefsList());
			beans.put("compas", adherents.getCompasList());
			beans.put("unites", adherents.getUnitesList());
			beans.put("alertes_jeunes", alertesJeunes);
			beans.put("alertes_responsables", alertesResponsables);
			beans.put("general", general);
			beans.put("global", global);
	
			modele.reset();
			FileOutputStream fosSortie = new FileOutputStream(fichier_sortie);
			Transformeur.go(modele, beans, fosSortie);
			fosSortie.close();
		}
		
		return true;
	}

	public void go(ParamEntree entree, InputStream batch, InputStream modele, int[] structures, boolean age, String batch_type, ParamSortie sortie, boolean anonymiser, boolean pargroupe) throws EngineException {
		start();
		
		chargeParametres();

		try {
			logger_.info("Chargement du fichier de traitement");
			Properties pbatch = new Properties();
			pbatch.load(batch);
			
			if (structures == null)
			{
				logger_.info("Traitement de la structure");
				gopriv(pbatch, entree, batch, modele, 0, age, batch_type, sortie, anonymiser, pargroupe);
			}
			else
				for (int istructure : structures)
				{
					logger_.info("Traitement de la structure "+istructure);
					boolean ret = gopriv(pbatch, entree, batch, modele, istructure, age, batch_type, sortie, anonymiser, pargroupe);
					if (ret == false)
						break;
				}
		} catch (TransformeurException | IOException | JDOMException | ExtractionException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}

}
