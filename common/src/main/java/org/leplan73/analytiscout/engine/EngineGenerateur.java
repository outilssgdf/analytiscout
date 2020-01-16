package org.leplan73.analytiscout.engine;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipOutputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.analytiscout.ExtracteurIndividusHtml;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.formatage.ArchiveFormateur;
import org.leplan73.analytiscout.intranet.ExtractionAdherents;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;
import org.leplan73.analytiscout.intranet.LoginEngineException;
import org.slf4j.Logger;

public class EngineGenerateur extends EngineConnecte {

	public EngineGenerateur(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionAdherents app, String identifiant, String motdepasse, File sortie, int structure) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException
	{
		logger_.info("Extraction (structure="+structure+")");
		String donnees = app.extract(structure, true, ExtractionIntranet.TYPE_INSCRIT, true, null, ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE, ExtractionIntranet.CATEGORIE_TOUT, ExtractionIntranet.DIPLOME_TOUT,ExtractionIntranet.QUALIFICATION_TOUT,ExtractionIntranet.FORMATION_TOUT, ExtractionIntranet.FORMAT_INDIVIDU|ExtractionIntranet.FORMAT_PARENTS,false);
		
		logger_.info("Conversion");
		ExtracteurIndividusHtml x = new ExtracteurIndividusHtml();
		x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true, false);
		
		// Génération de l'archive zip
		logger_.info("Génération de l'archive "+sortie.getName());
		ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(sortie)));
		ArchiveFormateur f = new ArchiveFormateur();
	    f.genereEmail(x.getUnites(), x.getParents(), x.getAdherents(), x.getColonnes(), null, zipOut);
	    zipOut.flush();
	    zipOut.close();
		return true;
	}

	public void go(String identifiant, String motdepasse, File sortie, int structure, int[] structures) throws EngineException, LoginEngineException
	{
		start();
		try
		{
			ExtractionAdherents app = new ExtractionAdherents();
			progress_.setProgress(30, "Connexion");
			login(app, identifiant, motdepasse);
			progress_.setProgress(40, "Génération des fichiers");
			
			gopriv(app, identifiant, motdepasse, sortie, structure);
			progress_.setProgress(80,"Déconnexion");
			
			logout();
		} catch (IOException | JDOMException | InvalidFormatException | ExtractionException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
