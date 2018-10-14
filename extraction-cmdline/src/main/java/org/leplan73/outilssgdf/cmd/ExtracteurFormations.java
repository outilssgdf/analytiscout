package org.leplan73.outilssgdf.cmd;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.intranet.ExtractionFormations;

public class ExtracteurFormations {

	public static void main(String[] args) {
		Logging.initLogger(ExtracteurFormations.class);
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
		
		Option optionGenerateur = new Option( "generateur", "generateur");
		optionGenerateur.setArgs(1);
		optionGenerateur.setArgName("generateur");
		options.addOption( optionGenerateur );
		
		Option optionFormat = new Option( "format", "format");
		optionFormat.setArgs(1);
		optionFormat.setArgName("format");
		options.addOption( optionFormat );
		
		Option optionCategorie = new Option( "categorie", "categorie");
		optionCategorie.setArgs(1);
		optionCategorie.setArgName("categorie");
		options.addOption( optionCategorie );
		
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
	    
	    String generateur = "cvs";
	    if (line.hasOption(optionGenerateur.getArgName()))
		{
	    	generateur = line.getOptionValue(optionGenerateur.getArgName());
		}
	    int format = -1;
	    if (line.hasOption(optionFormat.getArgName()))
		{
	    	format = Integer.parseInt(line.getOptionValue(optionFormat.getArgName()));
		}
	    int structure = 0;
	    if (line.hasOption(optionStructure.getArgName()))
		{
	    	structure = Integer.parseInt(line.getOptionValue(optionStructure.getArgName()));
		}
	    int categorie = -1;
	    if (line.hasOption(optionCategorie.getArgName()))
		{
	    	categorie = Integer.parseInt(line.getOptionValue(optionCategorie.getArgName()));
		}

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
			
			if (generateur.compareTo("xml") == 0)
			{
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sortie), "UTF-8"));
				
				ExtractionFormations app = new ExtractionFormations();
				app.init();

				if (app.login(identifiant,motdepasse) == false)
				{
					Logging.logger_.error("erreur de connexion");
				}
				String donnees = app.extract(structure, null, categorie,format);
				app.close();
				
				out.write(donnees);
				out.flush();
				out.close();
			}
			
			if (generateur.compareTo("csv") == 0)
			{
				final CSVPrinter out = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(new File(sortie), Charset.forName("UTF-8"));
				
				ExtractionFormations app = new ExtractionFormations();
				app.init();
				app.login(identifiant,motdepasse);
				String donnees = app.extract(structure,null, categorie,format);
				app.close();
				
				XPathFactory xpfac = XPathFactory.instance();
				SAXBuilder builder = new SAXBuilder();
		        org.jdom2.Document docx = builder.build(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))));
		        
		        // Scan des colonnes
		     	XPathExpression<?> xpac = xpfac.compile("tbody/tr[1]/td/text()");
		     	List<?> resultsc = xpac.evaluate(docx);
		     	int nbColumns = resultsc.size();	 
		     			 
		        XPathExpression<?> xpa = xpfac.compile("tbody/tr/td");
		        
		        List<?> results = xpa.evaluate(docx);
		        
		        int index = 0;
				Iterator<?> iter = results.iterator();
				while (iter.hasNext())
				{
					Object result = iter.next();
					Element resultElement = (Element) result;
					out.print(resultElement.getText());
	                index++;
		        	if (index % nbColumns == 0)
		        	{
		        		out.println();
		        	}
				}
				out.flush();
				out.close();
			}
			
		} catch (IOException e) {
			Logging.logger_.error(e);
		} catch (JDOMException e) {
			Logging.logger_.error(e);
		}
	}
}
