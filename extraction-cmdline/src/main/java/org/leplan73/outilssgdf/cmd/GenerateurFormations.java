package org.leplan73.outilssgdf.cmd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.intranet.ExtractionFormations;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

@Command(name = "GenerateurFormations", mixinStandardHelpOptions = true, version = "1.0")
public class GenerateurFormations extends CommonParamsIntranet {

	@Option(names = "-batch", required=true, description = "batch")
	private String batch = "";
	
	@Option(names = "-sortie", required=true, description = "sortie")
	private String sortie = "";
	
	public void run()
	{
		checkParams();
		
		Logging.initLogger(GenerateurFormations.class);
		
		Logging.logger_.info("Lancement");
	    
	    if (debug)
	    {
	    	Logging.enableDebug();
	    }
	    
	    Logging.logger_.info("Chargement du fichier de propriétés");
	    
		try {
			charge();
			
			// Connexion
		    Logging.logger_.info("Connexion");
		    ExtractionFormations app = new ExtractionFormations();
		    login(app);

			// Extraction des données
			Logging.logger_.info("Extraction \"Responsables\" (structure="+structure+")");
			String donnees = app.extract(structure, null, ExtractionMain.CATEGORIE_RESPONSABLE, ExtractionMain.FORMAT_INDIVIDU|ExtractionMain.FORMAT_PARENTS);
			app.close();
			
			// Conversion des données
			Logging.logger_.info("Conversion \"Responsables\"");
			ExtracteurHtml x = new ExtracteurHtml();
			x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))));
			
			// Extraction des données
			Logging.logger_.info("Extraction \"Compas\" (structure="+structure+")");
			String donneesCompas = app.extract(structure, "140", ExtractionMain.CATEGORIE_RESPONSABLE, ExtractionMain.FORMAT_INDIVIDU|ExtractionMain.FORMAT_PARENTS);
			logout();
			
			// Conversion des données
			Logging.logger_.info("Conversion \"Compas\"");
			ExtracteurHtml y = new ExtracteurHtml();
			y.charge(new ByteArrayInputStream(donneesCompas.getBytes(Charset.forName("UTF-8"))));
			
			
		} catch (IOException e) {
			Logging.logger_.error(e);
		} catch (JDOMException e) {
			Logging.logger_.error(e);
		} catch (ExtractionException e) {
			Logging.logger_.error(e);
		}
		
		Logging.logger_.info("Terminé");
	}
	
	public static void main(String[] args) {
		GenerateurFormations command = new GenerateurFormations();
		try
		{
			new CommandLine(command).parse(args);
	        command.run();
		}
		catch(PicocliException e)
		{
			System.out.print("Erreur : " + e.getMessage());
			CommandLine.usage(command, System.out);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
}
