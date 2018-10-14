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
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

public class ExtracteurBatch {

	public static void main(String[] args) {
		Logging.initLogger(ExtracteurBatch.class);
		
		Logging.logger_.info("Lancement");
				
		Options options = new Options();
		options.addOption( new Option( "debug", "debuggage" ));
		
		Option optionParams = new Option( "params", "fichier de parametres");
		optionParams.setArgs(1);
		optionParams.setRequired(true);
		optionParams.setArgName("params");
		options.addOption( optionParams );
		
		Option optionBatch = new Option( "batch", "batch");
		optionBatch.setArgs(1);
		optionBatch.setArgName("batch");
		optionBatch.setRequired(true);
		options.addOption( optionBatch );
		
		Option optionSortie = new Option( "sortie", "sortie");
		optionSortie.setArgs(1);
		optionSortie.setArgName("sortie");
		optionBatch.setRequired(true);
		options.addOption( optionSortie );
		
		Option optionStructure = new Option( "structure", "structure");
		optionStructure.setArgs(1);
		optionStructure.setArgName("structure");
		optionStructure.setRequired(true);
		options.addOption( optionStructure );
		
		CommandLine line = null;
		CommandLineParser parser = new DefaultParser();
	    try {
	        line = parser.parse( options, args );
	    }
	    catch( ParseException exp ) {
	    	Logging.logger_.error( exp.getMessage() );
	    	
	    	HelpFormatter formatter = new HelpFormatter();
	        formatter.printHelp(ExtracteurBatch.class.getName(), options);
	        return;
	    }
	    
	    if (line.hasOption("debug"))
	    {
	    	Logging.enableDebug();
	    }
	    
    	int structure = Integer.parseInt(line.getOptionValue(optionStructure.getArgName()));
	    
	    String params = line.getOptionValue("params");
	    String batch = line.getOptionValue(optionBatch.getArgName());

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
		    String sortie = line.getOptionValue("sortie");

		    Logging.logger_.info("Chargement du fichier de traitement");
			
			Properties pbatch = new Properties();
			pbatch.load(new FileInputStream(new File(batch)));

		    Logging.logger_.info("Connexion");
			ExtractionAdherents app = new ExtractionAdherents();
			app.init();
			app.login(identifiant,motdepasse);
			
			int index=1;
			for(;;)
			{
				// generateur.x
				// format.x
				// categorie.x
				// fonction.x
				// diplome.x
				// qualif.x
				// formation.x
				// nom.x
				// type.x
				String generateur = pbatch.getProperty("generateur."+index);
				if (generateur == null)
				{
					break;
				}
				int diplome = pbatch.getProperty("diplome."+index,"").isEmpty() ? ExtractionMain.DIPLOME_TOUT : Integer.parseInt(pbatch.getProperty("diplome."+index));
				int qualif = pbatch.getProperty("qualif."+index,"").isEmpty() ? ExtractionMain.QUALIFICATION_TOUT : Integer.parseInt(pbatch.getProperty("qualif."+index));
				int formation = pbatch.getProperty("formation."+index,"").isEmpty() ? ExtractionMain.FORMATION_TOUT : Integer.parseInt(pbatch.getProperty("formation."+index));
				int format = pbatch.getProperty("format."+index,"").isEmpty() ? ExtractionMain.FORMAT_INDIVIDU : Integer.parseInt(pbatch.getProperty("format."+index));
				int categorie = pbatch.getProperty("categorie."+index,"").isEmpty() ? ExtractionMain.CATEGORIE_TOUT : Integer.parseInt(pbatch.getProperty("categorie."+index));
				int type = pbatch.getProperty("type."+index,"").isEmpty() ? ExtractionMain.TYPE_TOUT : Integer.parseInt(pbatch.getProperty("type."+index));
				String nom = pbatch.getProperty("nom."+index,"");
				String fonction = pbatch.getProperty("fonction."+index);
				
				String nomFichier = sortie+"/"+nom+"_"+structure+"."+generateur;
				
				if (generateur.compareTo(ExtractionMain.GENERATEUR_XLS) == 0)
				{
					Logging.logger_.info("Extraction du fichier "+index);
					
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nomFichier), "UTF-8"));
					String donnees = app.extract(structure,type, fonction,categorie, diplome,qualif,formation,format, true);
					out.write(donnees);
					out.flush();
					out.close();
					Logging.logger_.info("Extraction du fichier "+index+" fait");
				}
				else
				if (generateur.compareTo(ExtractionMain.GENERATEUR_XML) == 0)
				{
					Logging.logger_.info("Extraction du fichier "+index);
					
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nomFichier), "UTF-8"));
					String donnees = app.extract(structure,type, fonction,categorie, diplome,qualif,formation,format, false);
					out.write(donnees);
					out.flush();
					out.close();
					Logging.logger_.info("Extraction du fichier "+index+" fait");
				}
				else
				if (generateur.compareTo(ExtractionMain.GENERATEUR_CSV) == 0)
				{
					Logging.logger_.info("Extraction du fichier "+index);
					
					final CSVPrinter out = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(new File(nomFichier), Charset.forName("UTF-8"));
					
					String donnees = app.extract(structure,type, null,categorie,diplome,qualif,formation,format, false);
					
					XPathFactory xpfac = XPathFactory.instance();
					SAXBuilder builder = new SAXBuilder();
			        org.jdom2.Document docx = builder.build(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))));
			        
			        // Scan des colonnes
			     	XPathExpression<?> xpac = xpfac.compile("tbody/tr[1]/td/text()");
			     	List<?> resultsc = xpac.evaluate(docx);
			     	int nbColumns = resultsc.size();	 
			     			 
			        XPathExpression<?> xpa = xpfac.compile("tbody/tr/td");
			        
			        List<?> results = xpa.evaluate(docx);
			        
			        int indexCsv = 0;
					Iterator<?> iter = results.iterator();
					while (iter.hasNext())
					{
						Object result = iter.next();
						Element resultElement = (Element) result;
						out.print(resultElement.getText());
						indexCsv++;
			        	if (indexCsv % nbColumns == 0)
			        	{
			        		out.println();
			        	}
					}
					out.flush();
					out.close();
					
					Logging.logger_.info("Extraction du fichier "+index+" fait");
				}
				index++;
			}
			app.close();
		} catch (IOException e) {
			Logging.logger_.error(e);
		} catch (JDOMException e) {
			Logging.logger_.error(e);
		}
	}
}
