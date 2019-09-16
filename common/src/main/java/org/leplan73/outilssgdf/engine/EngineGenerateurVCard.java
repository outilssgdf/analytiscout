package org.leplan73.outilssgdf.engine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.formatage.VCardFormatteur;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.slf4j.Logger;

public class EngineGenerateurVCard extends EngineConnecte {

	public EngineGenerateurVCard(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(ExtractionAdherents app, String identifiant, String motdepasse, File categories, File sortie, int structure) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException
	{
		logger_.info("Extraction VCard (structure="+structure+")");
		String donnees = app.extract(structure, true, ExtractionIntranet.TYPE_INSCRIT, true, null, ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE, ExtractionIntranet.CATEGORIE_TOUT, ExtractionIntranet.DIPLOME_TOUT,ExtractionIntranet.QUALIFICATION_TOUT,ExtractionIntranet.FORMATION_TOUT, ExtractionIntranet.FORMAT_INDIVIDU,false);
		
		logger_.info("Conversion");
		ExtracteurIndividusHtml x = new ExtracteurIndividusHtml();
		x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true);
		
		// Génération de l'archive zip
		logger_.info("Génération de l'archive "+sortie.getName());
		PrintStream fout = new PrintStream(new BufferedOutputStream(new FileOutputStream(sortie)),false, Consts.ENCODING_UTF8);
		VCardFormatteur f = new VCardFormatteur();
		
		Properties modeled = new Properties();
		modeled.load(new BufferedInputStream(new FileInputStream(categories)));
	    f.genereEmail(x.getUnites(), x.getAdherents(), x.getColonnes(), modeled, fout);
	    fout.flush();
	    fout.close();
		return true;
	}

	public void go(String identifiant, String motdepasse, File categories, File sortie, int structure, int[] structures) throws EngineException
	{
		start();
		try
		{
			ExtractionAdherents app = new ExtractionAdherents();
			progress_.setProgress(30, "Connexion");
			login(app, identifiant, motdepasse);
			progress_.setProgress(40, "Génération des fichiers");
			
			gopriv(app, identifiant, motdepasse, categories, sortie, structure);
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
