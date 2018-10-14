package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.extraction.Chefs;

import net.sf.jett.transform.ExcelTransformer;

public class Analyseur {

	public static void main(String[] args) throws ExtractionException, IOException, JDOMException, InvalidFormatException
	{
		Logging.initLogger(Analyseur.class);
		
		Logging.logger_.info("Lancement");
		
		Options options = new Options();
		options.addOption( new Option( "debug", "debuggage" ));
		
		Option optionBatch = new Option( "batch", "batch");
		optionBatch.setArgs(1);
		optionBatch.setArgName("batch");
		optionBatch.setRequired(true);
		options.addOption( optionBatch );
		
		Option optionModele = new Option( "modele", "modele");
		optionModele.setArgs(1);
		optionModele.setArgName("modele");
		optionModele.setRequired(true);
		options.addOption( optionModele );
		
		Option optionStructure = new Option( "structure", "structure");
		optionStructure.setArgs(1);
		optionStructure.setArgName("structure");
		optionStructure.setRequired(true);
		options.addOption( optionStructure );
		
		Option optionSortie = new Option( "sortie", "sortie");
		optionSortie.setArgs(1);
		optionSortie.setArgName("sortie");
		optionBatch.setRequired(true);
		options.addOption( optionSortie );
		
		Option optionEntree = new Option( "entree", "entree");
		optionEntree.setArgs(1);
		optionEntree.setArgName("entree");
		optionEntree.setRequired(true);
		options.addOption( optionEntree );
		
		CommandLine line = null;
		CommandLineParser parser = new DefaultParser();
	    try {
	        line = parser.parse( options, args );
	    }
	    catch( ParseException exp ) {
	    	Logging.logger_.error( exp.getMessage() );
	    	
	    	HelpFormatter formatter = new HelpFormatter();
	        formatter.printHelp(Analyseur.class.getName(), options);
	        return;
	    }
	    
	    if (line.hasOption("debug"))
	    {
	    	Logging.enableDebug();
	    }
	    
    	int structure = Integer.parseInt(line.getOptionValue(optionStructure.getArgName()));
	    
	    String batch = line.getOptionValue(optionBatch.getArgName());
	    String modele = line.getOptionValue(optionModele.getArgName());
	    String entree = line.getOptionValue(optionEntree.getArgName());
	    String sortie = line.getOptionValue(optionSortie.getArgName());
	    
	    Logging.logger_.info("Chargement du fichier de traitement");
		
		Properties pbatch = new Properties();
		pbatch.load(new FileInputStream(new File(batch)));

		Map<String, ExtracteurHtml> map = new TreeMap<String, ExtracteurHtml>();
		String fichierAdherents = null;
		
		int index=1;
		for(;;)
		{
			String generateur = pbatch.getProperty("generateur."+index);
			if (generateur == null)
			{
				break;
			}
			String nom = pbatch.getProperty("nom."+index,"");
			String nomFichier = entree+"/"+nom+"_"+structure+"."+generateur;

		    Logging.logger_.info("Chargement du fichier \""+nomFichier+"\"");
		    
			if (nom.compareTo("tout") == 0)
			{
				fichierAdherents = nomFichier;
			}
			else
				map.put(nom, new ExtracteurHtml(nomFichier));
			index++;
		}

	    Logging.logger_.info("Chargement du fichier \""+fichierAdherents+"\"");
		 ExtracteurHtml adherents = new ExtracteurHtml(fichierAdherents, map);
		 
		 Chefs chefs = new Chefs();
		 chefs.charge(adherents,map);

		FileOutputStream outputStream = new FileOutputStream(sortie);

	    Logging.logger_.info("Generation du fichier \""+sortie+"\" à partir du modèle \""+modele+"\"");		
		ExcelTransformer trans = new ExcelTransformer();
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("chefs", adherents.getChefsList());
		beans.put("unites", adherents.getUnitesList());
		Workbook workbook = trans.transform(new FileInputStream(modele), beans);
		workbook.write(outputStream);
		
		outputStream.flush();
		outputStream.close();
	}
}
