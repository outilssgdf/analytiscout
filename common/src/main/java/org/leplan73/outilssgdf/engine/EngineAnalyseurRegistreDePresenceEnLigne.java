package org.leplan73.outilssgdf.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.leplan73.outilssgdf.intranet.ExtractionRegistrePresence2;
import org.leplan73.outilssgdf.intranet.LoginEngineException;
import org.leplan73.outilssgdf.outils.FichierSortie;
import org.leplan73.outilssgdf.outils.Structure;
import org.leplan73.outilssgdf.registrepresence.ExtracteurRegistrePresence;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActivite;
import org.leplan73.outilssgdf.registrepresence.RegistrePresenceActiviteHeure;
import org.slf4j.Logger;

public class EngineAnalyseurRegistreDePresenceEnLigne extends EngineConnecte {

	public EngineAnalyseurRegistreDePresenceEnLigne(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionRegistrePresence2 app, int structure, boolean recursif, int annee, File sortie, File modele, boolean sous_dossier, boolean anonymiser, boolean garderFichiers) throws ClientProtocolException, IOException, JDOMException, TransformeurException, InterruptedException
	{
		progress_.setProgress(20, "Extraction");
		logger_.info("Extraction");
		String donnees = app.extract(structure, recursif, annee, 0, false);
		InputStream in = new ByteArrayInputStream(donnees.getBytes(Consts.ENCODING_WINDOWS));
		
		if (garderFichiers)
		{
			FileOutputStream fos = new FileOutputStream(new FichierSortie("log","registredepresence_"+structure+".csv"));
			fos.write(donnees.getBytes());
			fos.flush();
			fos.close();
		}
		
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		int anneeDebut = ex.charge(in, anonymiser)+1;
		progress_.setProgress(40, "Chargement des fichiers");
		logger_.info("Chargement des fichiers");
		
		List<RegistrePresenceActiviteHeure> activites_personnes = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activites_personnes);

		progress_.setProgress(60, "Calculs");
		logger_.info("Calculs");
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_personnes", activites_personnes);
		beans.put("activites_reel", activites_personnes);
		beans.put("activites_cec", activites_cec);
		
		File fichier_sortie = sous_dossier ? new FichierSortie(sortie, "registredepresence_"+structure+".xlsx") : sortie;

		logger_.info("Génération du fichier");
		FileInputStream fismodele = new FileInputStream(modele);
		FileOutputStream fosSortie = new FileOutputStream(fichier_sortie);
		Transformeur.go(fismodele, beans, fosSortie);
		fismodele.close();
		fosSortie.close();
		
		return true;
	}

	public void go(String identifiant, String motdepasse, File fSortie, File fModele, int annee, int[] structures, boolean recursif, boolean sous_dossier, boolean anonymiser, boolean garderFichiers) throws EngineException, LoginEngineException
	{
		start();
		try
		{
			ExtractionRegistrePresence2 app = new ExtractionRegistrePresence2();
			progress_.setProgress(30, "Connexion");
			login(app, identifiant, motdepasse, true);
			progress_.setProgress(40, "Extraction");
			
			for (int structure : structures)
			{
				logger_.info("Traitement de la structure "+Structure.formatStructure(structure));
				boolean ret = gopriv(app, structure, recursif, annee, fSortie, fModele, sous_dossier, anonymiser, garderFichiers);
				if (ret == false)
					break;
			}
			logout();
		} catch (IOException | JDOMException | TransformeurException | InterruptedException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
	