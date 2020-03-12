package org.leplan73.analytiscout.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.jdom2.JDOMException;
import org.leplan73.analytiscout.ExtracteurIndividusHtml;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.extraction.Adherent;
import org.leplan73.analytiscout.extraction.Adherents;
import org.leplan73.analytiscout.intranet.ExtractionAdherents;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;
import org.leplan73.analytiscout.intranet.LoginEngineException;
import org.slf4j.Logger;

public class EngineDetection extends EngineConnecte {

	public EngineDetection(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	public EngineConnexion go(String identifiant, String motdepasse) throws EngineException, LoginEngineException
	{
		EngineConnexion utilisateur = new EngineConnexion();
		
		start();
		try
		{
			ExtractionAdherents app = new ExtractionAdherents();
			app.init(false);
			
			logger_.info("Connexion");
			progress_.setProgress(20);
			boolean ret = app.login(identifiant, motdepasse);
			if (!ret)
			{
				throw new LoginEngineException("Identifiant ou mot de passe incorrect");
			}
			
			logger_.info("Extraction");
			progress_.setProgress(40);
			String donnees = app.extract(ExtractionIntranet.STRUCTURE_TOUT, false, ExtractionIntranet.TYPE_INSCRIT, true, null, ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE, ExtractionIntranet.CATEGORIE_RESPONSABLE, ExtractionIntranet.DIPLOME_TOUT,ExtractionIntranet.QUALIFICATION_TOUT,ExtractionIntranet.FORMATION_TOUT, ExtractionIntranet.FORMAT_INDIVIDU,false);
			ExtracteurIndividusHtml x = new ExtracteurIndividusHtml();
			x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true,false);
			
			logger_.info("Recherche adhérent");
			progress_.setProgress(60);
			Adherents adherents = x.getAdherents();
			Adherent adherent = adherents.get(Integer.parseInt(identifiant));
			utilisateur.structure = adherent.getCodestructure();
			utilisateur.email = adherent.getEmailPersonnel();
			
			app.close();
			progress_.stop();
		} catch (IOException | JDOMException | ExtractionException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
		
		return utilisateur;
	}
}
