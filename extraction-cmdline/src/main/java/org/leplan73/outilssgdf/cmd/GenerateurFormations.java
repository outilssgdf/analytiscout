package org.leplan73.outilssgdf.cmd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;

import org.fusesource.jansi.AnsiConsole;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.intranet.ExtractionFormations;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;
import picocli.CommandLine.Help.Ansi;

@Command(name = "GenerateurFormations", mixinStandardHelpOptions = true, version = "1.0")
public class GenerateurFormations extends CommonParamsIntranet {

	@Option(names = "-batch", required=true, description = "batch")
	private String batch = "";
	
	@Option(names = "-sortie", required=true, description = "sortie")
	private String sortie = "";
	
	public void run()
	{
		Instant now = Instant.now();
		checkParams();
		
		Logging.initLogger(GenerateurFormations.class);
		
		Logging.logger_.info("Lancement");
	    
	    if (debug)
	    {
	    	Logging.enableDebug();
	    }
	    
		try {
			charge();
			
			// Connexion
		    Logging.logger_.info("Connexion");
		    ExtractionFormations app = new ExtractionFormations();
		    login(app);

			// Extraction des données
			Logging.logger_.info("Extraction \"Responsables\" (structure="+structures+")");
			String donnees = app.extract(structures[0], null, ExtractionMain.CATEGORIE_RESPONSABLE, ExtractionMain.FORMAT_INDIVIDU|ExtractionMain.FORMAT_PARENTS);
			app.close();
			
			// Conversion des données
			Logging.logger_.info("Conversion \"Responsables\"");
			ExtracteurHtml x = new ExtracteurHtml();
			x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))));
			
			// Extraction des données
			Logging.logger_.info("Extraction \"Compas\" (structure="+structures+")");
			String donneesCompas = app.extract(structures[0], "140", ExtractionMain.CATEGORIE_RESPONSABLE, ExtractionMain.FORMAT_INDIVIDU|ExtractionMain.FORMAT_PARENTS);
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
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
	
	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		GenerateurFormations command = new GenerateurFormations();
		try
		{
			new CommandLine(command).parse(args);
	        command.run();
		}
		catch(PicocliException e)
		{
			System.out.println("Erreur : " + e.getMessage());
			CommandLine.usage(command, System.out, Ansi.ON);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		AnsiConsole.systemUninstall();
    }
}
