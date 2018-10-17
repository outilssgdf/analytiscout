package org.leplan73.outilssgdf.cmd;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipOutputStream;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.formatage.GmailCsvFormatteur;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

@Command(name = "Generateur", mixinStandardHelpOptions = true, version = "1.0")
public class Generateur extends CommonParamsIntranet {

	@Option(names = "-sortie", required=true, description = "sortie")
	private File sortie;
	
	public void run()
	{
		checkParams();
		
		Logging.initLogger(Generateur.class);
		
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
			ExtractionAdherents app = new ExtractionAdherents();
			login(app);

			// Extraction des données
			Logging.logger_.info("Extraction (structure="+structure+")");
			String donnees = app.extract(structure, ExtractionMain.TYPE_TOUT, false, null, ExtractionMain.CATEGORIE_TOUT, ExtractionMain.DIPLOME_TOUT,ExtractionMain.QUALIFICATION_TOUT,ExtractionMain.FORMATION_TOUT, ExtractionMain.FORMAT_INDIVIDU|ExtractionMain.FORMAT_PARENTS,false);
			logout();
			
			// Conversion des données
			Logging.logger_.info("Conversion");
			ExtracteurHtml x = new ExtracteurHtml();
			x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))));
			GmailCsvFormatteur f = new GmailCsvFormatteur();

			// Génération de l'archive zip
			Logging.logger_.info("Génération de l'archive "+sortie.getName());
			ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(sortie)));
		    f.genereEmail(x.getUnites(), x.getParents(), x.getAdherents(), x.getColonnes(), null, zipOut);
		    zipOut.flush();
		    zipOut.close();
			
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
		Generateur command = new Generateur();
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
