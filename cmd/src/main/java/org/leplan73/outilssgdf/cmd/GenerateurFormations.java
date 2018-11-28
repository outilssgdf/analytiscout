package org.leplan73.outilssgdf.cmd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.intranet.ExtractionFormations;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generateurformations", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class GenerateurFormations extends CommonParamsIntranet {

	@Option(names = "-batch", required=true, description = "batch")
	private String batch = "";
	
	@Option(names = "-sortie", required=true, description = "sortie")
	private String sortie = "";

	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		Instant now = Instant.now();
		checkParams();
		
		Logging.logger_.info("Lancement");
	    
	    chargeParametres();
	    
		try {
			charge();
			
			// Connexion
		    Logging.logger_.info("Connexion");
		    ExtractionFormations app = new ExtractionFormations();
		    login(app);

			// Extraction des données
			Logging.logger_.info("Extraction \"Responsables\" (structure="+structures[0]+")");
			String donnees = app.extract(structures[0], null, ExtractionMain.CATEGORIE_RESPONSABLE, ExtractionMain.FORMAT_INDIVIDU|ExtractionMain.FORMAT_PARENTS);
			app.close();
			
			// Conversion des données
			Logging.logger_.info("Conversion \"Responsables\"");
			ExtracteurHtml x = new ExtracteurHtml();
			x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true);
			
			// Extraction des données
			Logging.logger_.info("Extraction \"Compas\" (structure="+structures[0]+")");
			String donneesCompas = app.extract(structures[0], "140", ExtractionMain.CATEGORIE_RESPONSABLE, ExtractionMain.FORMAT_INDIVIDU|ExtractionMain.FORMAT_PARENTS);
			logout();
			
			// Conversion des données
			Logging.logger_.info("Conversion \"Compas\"");
			ExtracteurHtml y = new ExtracteurHtml();
			y.charge(new ByteArrayInputStream(donneesCompas.getBytes(Charset.forName("UTF-8"))),true);
			
			
		} catch (IOException|JDOMException|ExtractionException e) {
			Logging.logError(e);
		}
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
}
