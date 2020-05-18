package org.leplan73.analytiscout.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.jdom2.JDOMException;
import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.ExtracteurExtraHtml;
import org.leplan73.analytiscout.ExtracteurIndividusHtml;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.Options;
import org.leplan73.analytiscout.ParamEntree;
import org.leplan73.analytiscout.ParamSortie;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.Transformeur;
import org.leplan73.analytiscout.TransformeurException;
import org.leplan73.analytiscout.alerte.Alertes;
import org.leplan73.analytiscout.calcul.General;
import org.leplan73.analytiscout.calcul.Global;
import org.leplan73.analytiscout.extraction.AdherentForme.ExtraKey;
import org.leplan73.analytiscout.extraction.AdherentsFormes;
import org.leplan73.analytiscout.outils.Structure;
import org.slf4j.Logger;

public class EngineAnalyseur extends Engine {

	public EngineAnalyseur(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(Properties pbatch, ParamEntree entree, InputStream batch, InputStream modele, int structure, boolean age, String batch_type, ParamSortie sortie, boolean anonymiser, boolean pargroupe, boolean generer_ddcs, Options options) throws TransformeurException, ExtractionException, IOException, JDOMException
	{
		logger_.info("Traitement de la structure "+Structure.formatStructure(structure));
		
		Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
		
		List<InputStream> lfis = new ArrayList<InputStream>();
		List<String> lfiles = new ArrayList<String>();
		Set<String> retirer_code = null;

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
			String optionsp = pbatch.getProperty("options." + index, null);
			File fichier_entree = new File(dossierStructure, extra.fichier_ + "." + generateur);

			logger_.info("Chargement du fichier \"" + fichier_entree.getName() + "\"");

			if (extra.ifTout()) {
				if (optionsp == null || (options != null && options.contains(optionsp))) {
					lfis.add(new FileInputStream(fichier_entree));
					lfiles.add(fichier_entree.getName());
					String retirer_code_p = pbatch.getProperty("retirer_code." + index);
					if (retirer_code_p != null)
					{
						retirer_code = new HashSet<>();
						for (String name : retirer_code_p.split(",")) {
							retirer_code.add(name);
						}
					}
				}
			} else
				extraMap.put(extra, new ExtracteurExtraHtml(fichier_entree, age));
			index++;
		}
		
		progress_.setProgress(50);
		lfiles.forEach(v -> logger_.info("Chargement du fichier \"" + v + "\""));
		
		ExtracteurIndividusHtml adherents = null;
		try {
			adherents = new ExtracteurIndividusHtml(lfis, extraMap, age, anonymiser, retirer_code);
		}
		catch(IOException e) {
			logger_.error("Erreur d'extraction : ",e);
		}
		finally {
			lfis.forEach(v -> {
				try {
					v.close();
				} catch (IOException e) {
				}
			});
		}
		
		General general = General.generer();
		
		if (pargroupe)
		{
			Map<String,ExtracteurIndividusHtml> groupes = adherents.genereGroupes();
			
			Set<String> codeGroupes = groupes.keySet();
			for (String codeGroupe : codeGroupes)
			{
				ExtracteurIndividusHtml groupe = groupes.get(codeGroupe);
				AdherentsFormes compas = new AdherentsFormes();
				compas.charge(groupe, extraMap);
				
				File fichier_sortie = sortie.construit(codeGroupe, groupe.getGroupe(), ".xlsx");
				
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
			File fichier_sortie = sortie.construit(structure, ".xlsx");
			
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
			
			if (!generer_ddcs)
			{
				Transformeur.retireOnglet(fichier_sortie, Consts.ONGLET_DDCS);
			}
		}
		
		return true;
	}

	public void go(ParamEntree entree, InputStream batch, InputStream modele, int[] structures, boolean age, String batch_type, ParamSortie sortie, boolean anonymiser, boolean pargroupe, boolean generer_ddcs, Options options) throws EngineException {
		start();
		
		chargeParametres();

		try {
			logger_.info("Chargement du fichier de traitement");
			Properties pbatch = new Properties();
			pbatch.load(batch);
			
			if (structures == null)
			{
				logger_.info("Traitement de la structure");
				gopriv(pbatch, entree, batch, modele, 0, age, batch_type, sortie, anonymiser, pargroupe, generer_ddcs, options);
			}
			else
				for (int structure : structures)
				{
					logger_.info("Traitement de la structure "+Structure.formatStructure(structure));
					boolean ret = gopriv(pbatch, entree, batch, modele, structure, age, batch_type, sortie, anonymiser, pargroupe, generer_ddcs, options);
					if (ret == false)
						break;
				}
		} catch (TransformeurException | IOException | JDOMException | ExtractionException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
			try {
				modele.close();
				batch.close();
			} catch (IOException e) {
			}
		}
	}

}
