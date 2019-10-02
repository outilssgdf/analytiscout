package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.alerte.Alertes;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;
import org.slf4j.Logger;

import com.jcabi.manifests.Manifests;

public class EngineAnalyseur extends Engine {

	public EngineAnalyseur(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(Properties pbatch, File entree, File batch, File sortie, File modele, int structure, boolean age, String batch_type, boolean sous_dossier, String nom_fichier_sortie) throws TransformeurException, ExtractionException, IOException, JDOMException
	{
		logger_.info("Traitement de la structure "+structure);
		
		Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
		File fichierAdherents = null;

		File dossierStructure = sous_dossier ? new File(entree,""+structure) : entree;
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
		
		File fichier_sortie = sous_dossier ? new File(sortie, nom_fichier_sortie+structure+".xlsx") : sortie;

		progress_.setProgress(50);
		logger_.info("Chargement du fichier \"" + fichierAdherents.getName() + "\"");
		ExtracteurIndividusHtml adherents = new ExtracteurIndividusHtml(fichierAdherents, extraMap, age);

		AdherentFormes compas = new AdherentFormes();
		compas.charge(adherents, extraMap);
		progress_.setProgress(60);

		String version = "";
		try {
			version = Manifests.read("version");
		} catch (java.lang.IllegalArgumentException e) {
		}
		General general = new General(version);
		Global global = new Global(adherents.getGroupe(), adherents.getMarins());
		adherents.calculGlobal(global);
		progress_.setProgress(80);
		
		Alertes alertes = new Alertes();
		adherents.construitsAlertes(alertes);

		logger_.info("Génération du fichier \"" + fichier_sortie.getName() + "\" à partir du modèle \"" + modele.getName() + "\"");
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("adherents", adherents.getAdherentsList());
		beans.put("chefs", adherents.getChefsList());
		beans.put("compas", adherents.getCompasList());
		beans.put("unites", adherents.getUnitesList());
		beans.put("alertes", alertes);
		beans.put("general", general);
		beans.put("global", global);

		Transformeur.go(modele, beans, fichier_sortie);
		
		return true;
	}

	public void go(File entree, File batch, File sortie, File modele, int[] structures, boolean age, String batch_type, String fichier_sortie) throws EngineException {
		start();
		
		chargeParametres();

		try {
			logger_.info("Chargement du fichier de traitement");
			Properties pbatch = new Properties();
			pbatch.load(new FileInputStream(batch));
			
			if (structures == null)
			{
				logger_.info("Traitement de la structure");
				gopriv(pbatch, entree, batch, sortie, modele, 0, age, batch_type, false, fichier_sortie);
			}
			else
				for (int istructure : structures)
				{
					logger_.info("Traitement de la structure "+istructure);
					boolean ret = gopriv(pbatch, entree, batch, sortie, modele, istructure, age, batch_type, (structures.length > 1), fichier_sortie);
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
