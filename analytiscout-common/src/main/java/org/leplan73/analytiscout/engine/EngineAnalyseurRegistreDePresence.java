package org.leplan73.analytiscout.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.Transformeur;
import org.leplan73.analytiscout.TransformeurException;
import org.leplan73.analytiscout.calcul.General;
import org.leplan73.analytiscout.calcul.Global;
import org.leplan73.analytiscout.outils.FichierSortie;
import org.leplan73.analytiscout.outils.Structure;
import org.leplan73.analytiscout.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.analytiscout.registrepresence.RegistrePresenceActivite;
import org.leplan73.analytiscout.registrepresence.RegistrePresenceActiviteHeure;
import org.slf4j.Logger;

public class EngineAnalyseurRegistreDePresence extends Engine {

	public EngineAnalyseurRegistreDePresence(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(File fEntreeAnneeP, File fEntreeAnnee, File modele, File sortie, int structure, boolean sous_dossier, boolean anonymiser) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException, TransformeurException
	{
		progress_.setProgress(20,"Chargement des fichiers");
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		int anneeDebut = 0;
		int anneeFin = 0;
		if (fEntreeAnneeP != null)
		{
			logger_.info("Chargement du fichier \"" + fEntreeAnneeP.getName() + "\"");
			anneeDebut = ex.charge(new FileInputStream(fEntreeAnneeP))+1;
			anneeFin = anneeDebut;
		}
		if (fEntreeAnnee != null)
		{
			logger_.info("Chargement du fichier \"" + fEntreeAnnee.getName() + "\"");
			int ann = ex.charge(new FileInputStream(fEntreeAnnee))+1;
			if (anneeDebut != 0) anneeDebut = ann;
			anneeFin = ann;
		}
		if (anonymiser)
		{
			ex.anonymiser();
		}
		
		if (fEntreeAnneeP != null && fEntreeAnnee != null)
		{
			ex.purger(anneeFin);
		}
		
		progress_.setProgress(40,"Calculs");
		logger_.info("Calculs");

		General general = General.generer();
		
		List<RegistrePresenceActiviteHeure> activites_personnes = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activites_personnes);
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Global global = new Global(ex.getGroupe(), false);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_personnes", activites_personnes);
		beans.put("activites_cec", activites_cec);
		beans.put("general", general);
		beans.put("global", global);
		
		File fichier_sortie = sous_dossier ? new FichierSortie(sortie, "registredepresence_"+structure+".xlsx") : sortie;

		logger_.info("Génération du fichier");
		
		FileInputStream fismodele = new FileInputStream(modele);
		FileOutputStream fosSortie = new FileOutputStream(fichier_sortie);
		Transformeur.go(fismodele, beans, fosSortie);
		fismodele.close();
		fosSortie.close();
		
		return true;
	}

	public void go(File fEntreeAnneeP, File fEntreeAnnee, File modele, File sortie, int[] structures, boolean sous_dossier, boolean anonymiser) throws Exception
	{
		start();
		try
		{
			if (structures == null)
			{
				logger_.info("Traitement de la structure");
				gopriv(fEntreeAnneeP, fEntreeAnnee, modele, sortie, 0, sous_dossier, anonymiser);
			}
			else
				for (int structure : structures)
				{
					logger_.info("Traitement de la structure "+Structure.formatStructure(structure));
					gopriv(fEntreeAnneeP, fEntreeAnnee, modele, sortie, structure, sous_dossier, anonymiser);
				}
		} catch (IOException | JDOMException | ExtractionException | TransformeurException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
