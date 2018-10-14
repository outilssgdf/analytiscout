package org.leplan73.outilssgdf.cmd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.intranet.ExtractionFormations;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

public class GenerateurFormations extends Logging {

	public static void main(String[] args) {
		Logging.initLogger(GenerateurFormations.class);
		Logging.logger_.info("Lancement");
				
		Options options = new Options();
		options.addOption( new Option( "debug", "debuggage" ));
		
		Option optionSortie = new Option( "sortie", "fichier de sortie");
		optionSortie.setArgs(1);
		optionSortie.setRequired(true);
		optionSortie.setArgName("file");
		options.addOption( optionSortie );
		
		Option optionParams = new Option( "params", "fichier de parametres");
		optionParams.setArgs(1);
		optionParams.setRequired(true);
		optionParams.setArgName("params");
		options.addOption( optionParams );
		
		Option optionStructure = new Option( "structure", "structure");
		optionStructure.setArgs(1);
		optionStructure.setArgName("structure");
		options.addOption( optionStructure );
		
		CommandLine line = null;
		CommandLineParser parser = new DefaultParser();
	    try {
	        line = parser.parse( options, args );
	    }
	    catch( ParseException exp ) {
	    	Logging.logger_.error( exp.getMessage() );
	    	
	    	HelpFormatter formatter = new HelpFormatter();
	        formatter.printHelp(ExtracteurFormations.class.getName(), options);
	        return;
	    }
	    
	    if (line.hasOption("debug"))
	    {
	    	Logging.enableDebug();
	    }
	    String sortie = line.getOptionValue("sortie");
	    String params = line.getOptionValue("params");
	    
	    int structure = ExtractionMain.STRUCTURE_TOUT;
	    if (line.hasOption(optionStructure.getArgName()))
	    {
	    	structure = Integer.valueOf(line.getOptionValue(optionStructure.getArgName()));
	    }

	    Logging.logger_.info("Chargement du fichier de propriétés");
	    
		Properties pfile = new Properties();
		try {
			pfile.load(new FileInputStream(new File(params)));
			
			String identifiant = pfile.getProperty("identifiant");
			String motdepasse = pfile.getProperty("motdepasse");
			if (identifiant == null)
			{
				Logging.logger_.error("Pas d'identifiant");
				return;
			}
			if (motdepasse == null)
			{
				Logging.logger_.error("Pas de mot de passe");
				return;
			}
			
			// Connexion
		    Logging.logger_.info("Connexion");
		    ExtractionFormations app = new ExtractionFormations();
			app.init();
			if (app.login(identifiant,motdepasse) == false)
			{
				Logging.logger_.error("erreur de connexion");
			}

			// Extraction des données
			Logging.logger_.info("Extraction \"Responsables\" (structure="+structure+")");
			String donnees = app.extract(structure, null, ExtractionMain.CATEGORIE_RESPONSABLE, ExtractionMain.FORMAT_TOUT);
			app.close();
			
			// Conversion des données
			Logging.logger_.info("Conversion \"Responsables\"");
			ExtracteurHtml x = new ExtracteurHtml();
			x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))));
			
			// Extraction des données
			Logging.logger_.info("Extraction \"Compas\" (structure="+structure+")");
			String donneesCompas = app.extract(structure, "140", ExtractionMain.CATEGORIE_RESPONSABLE, ExtractionMain.FORMAT_TOUT);
			app.close();
			
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
	}
}
