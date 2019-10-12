package org.leplan73.outilssgdf.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.http.client.ClientProtocolException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.ParamSortie;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.alerte.Alertes;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.slf4j.Logger;

import com.jcabi.manifests.Manifests;

public class EngineAnalyseurEnLigne extends EngineConnecte {

	public EngineAnalyseurEnLigne(Progress progress, Logger logger) {
		super(progress, logger);
	}

	private boolean gopriv(ExtractionAdherents app, Properties pbatch, String identifiant, String motdepasse, InputStream modele, int structure, boolean age, String batch_type, ParamSortie sortie, boolean anonymiser, boolean garderFichiers) throws ExtractionException, TransformeurException, ClientProtocolException, IOException, JDOMException
	{
		Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
		
		String donneesAdherents=null;
		int index = 1;
		for (;;) {
			
			if (progress_.isCanceled()) {
				logger_.info("Action annulée");
				return false;
			}
			
			// generateur.x
			// format.x
			// categorie.x
			// specialite.x
			// fonction.x
			// diplome.x
			// qualif.x
			// formation.x
			// nom.x
			// type.x
			String generateur = pbatch.getProperty("generateur." + index);
			if (generateur == null) {
				break;
			}
			int diplome = pbatch.getProperty("diplome." + index, "").isEmpty()
					? ExtractionIntranet.DIPLOME_TOUT
					: Integer.parseInt(pbatch.getProperty("diplome." + index));
			int qualif = pbatch.getProperty("qualif." + index, "").isEmpty()
					? ExtractionIntranet.QUALIFICATION_TOUT
					: Integer.parseInt(pbatch.getProperty("qualif." + index));
			int formation = pbatch.getProperty("formation." + index, "").isEmpty()
					? ExtractionIntranet.FORMATION_TOUT
					: Integer.parseInt(pbatch.getProperty("formation." + index));
			int format = pbatch.getProperty("format." + index, "").isEmpty()
					? ExtractionIntranet.FORMAT_INDIVIDU
					: Integer.parseInt(pbatch.getProperty("format." + index));
			int categorie = pbatch.getProperty("categorie." + index, "").isEmpty()
					? ExtractionIntranet.CATEGORIE_TOUT
					: Integer.parseInt(pbatch.getProperty("categorie." + index));
			int type = pbatch.getProperty("type." + index, "").isEmpty() ? ExtractionIntranet.TYPE_TOUT
					: Integer.parseInt(pbatch.getProperty("type." + index));
			int specialite = pbatch.getProperty("specialite." + index, "").isEmpty()
					? ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE
					: Integer.parseInt(pbatch.getProperty("specialite." + index));
			boolean adherentsseuls = pbatch.getProperty("adherents." + index, "").isEmpty() ? false
					: Boolean.parseBoolean(pbatch.getProperty("adherents." + index));
			String nom = pbatch.getProperty("nom." + index, "");
			String fonction = pbatch.getProperty("fonction." + index);

			ExtraKey extra = new ExtraKey(pbatch.getProperty("fichier." + index, nom), nom, pbatch.getProperty("batchtype." + index, "tout_responsables"));
			
			logger_.info("Extraction de \""+nom+"\"");
			String donnees = app.extract(structure,true,type,adherentsseuls,fonction,specialite,categorie, diplome,qualif,formation,format, false);
			logger_.info("Extraction de \""+nom+"\" fait");
			
			if (garderFichiers)
			{
				FileOutputStream fos = new FileOutputStream(new File("log",batch_type+"_"+nom+"_"+structure+".xml"));
				fos.write(donnees.getBytes());
				fos.flush();
				fos.close();
			}
			
			if (extra.ifTout()) {
				donneesAdherents = donnees;
			} else {
				InputStream in = new ByteArrayInputStream(donnees.getBytes(Consts.ENCODING_UTF8));
				extraMap.put(extra, new ExtracteurExtraHtml(in, age));
			}
			index++;
		}
		progress_.setProgress(50);
		
		InputStream in = new ByteArrayInputStream(donneesAdherents.getBytes(Consts.ENCODING_UTF8));
		ExtracteurIndividusHtml adherents = new ExtracteurIndividusHtml(in, extraMap,age, anonymiser);
 
		AdherentFormes adherentsFormes = new AdherentFormes();
		adherentsFormes.charge(adherents,extraMap);
		progress_.setProgress(60);
		
		String version = "";
		try
		{
			version = Manifests.read("version");
		}
		catch(java.lang.IllegalArgumentException e) {
		}
		General general = new General(version);
		Global global = new Global(adherents.getGroupe(), adherents.getMarins());
		adherents.calculGlobal(global);
		progress_.setProgress(80);
		
		Alertes alertesJeunes = new Alertes();
		adherents.construitsAlertes(alertesJeunes, true, age);
		Alertes alertesResponsables = new Alertes();
		adherents.construitsAlertes(alertesResponsables, false, age);

		File fichier_sortie = sortie.construit(structure, ".xlsx");
		
	    if (sortie.getIsStream())
	    {
	    	logger_.info("Génération du stream de sortie à partir du modèle");
	    }
	    else
	    {
	    	logger_.info("Génération du fichier \""+fichier_sortie.getName()+"\" à partir du modèle");
	    }
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

	public void go(String identifiant, String motdepasse, InputStream batch, InputStream modele, int structure, boolean age, String batch_type, boolean recursif, ParamSortie sortie, boolean anonymiser, boolean garderFichiers) throws EngineException
	{
		start();
		try
		{
			logger_.info("Chargement du fichier de traitement");
			Properties pbatch = new Properties();
			pbatch.load(batch);
	
			ExtractionAdherents app = new ExtractionAdherents();
			login(app, identifiant, motdepasse);
			progress_.setProgress(40);
			
			logger_.info("Traitement de la structure "+structure);
			gopriv(app, pbatch, identifiant, motdepasse, modele, structure, age, batch_type, sortie, anonymiser, garderFichiers);
			logout();
		} catch (IOException | JDOMException | ExtractionException | TransformeurException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}

	public void go(String identifiant, String motdepasse, InputStream batch, InputStream modele, int[] structures, boolean age, String batch_type, boolean recursif, ParamSortie sortie, boolean anonymiser, boolean garderFichiers) throws EngineException
	{
		start();
		try
		{
			logger_.info("Chargement du fichier de traitement");
			Properties pbatch = new Properties();
			pbatch.load(batch);
	
			ExtractionAdherents app = new ExtractionAdherents();
			login(app, identifiant, motdepasse);
			progress_.setProgress(40);
			
			for (int istructure : structures)
			{
				logger_.info("Traitement de la structure "+istructure);
				boolean ret = gopriv(app, pbatch, identifiant, motdepasse, modele, istructure, age, batch_type, sortie, anonymiser, garderFichiers);
				if (ret == false)
					break;
			}
			logout();
		} catch (IOException | JDOMException | ExtractionException | TransformeurException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
