package org.leplan73.outilssgdf.cmd;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

@Command(name = "Extracteur", mixinStandardHelpOptions = true, version = "1.0")
public class Extracteur extends CommonParamsIntranet {

	@Option(names = "-sortie", required=true, description = "sortie")
	private File sortie;

	@Option(names = "-diplome", description = "diplome")
	private Integer diplome = ExtractionMain.DIPLOME_TOUT;

	@Option(names = "-qualif", description = "qualif")
	private Integer qualif = ExtractionMain.QUALIFICATION_TOUT;

	@Option(names = "-formation", description = "formation")
	private Integer formation = ExtractionMain.FORMATION_TOUT;

	@Option(names = "-format", description = "format")
	private Integer format = ExtractionMain.FORMAT_INDIVIDU;

	@Option(names = "-categorie", description = "categorie")
	private Integer categorie = ExtractionMain.CATEGORIE_TOUT;

	@Option(names = "-fonction", description = "fonction")
	private String fonction = "";

	@Option(names = "-type", description = "type")
	private Integer type = ExtractionMain.TYPE_TOUT;

	@Option(names = "-adherents", description = "adherents")
	private boolean adherents = false;

	@Option(names = "-generateur", description = "generateur")
	private String generateur = "xls";
	
	public void run()
	{
		checkParams();
		
		Logging.initLogger(Extracteur.class);
			
		Logging.logger_.info("Lancement");
	    
	    if (debug)
	    {
	    	Logging.enableDebug();
	    }
	    
		try {
			charge();
			
			if (generateur.compareTo(ExtractionMain.GENERATEUR_XLS) == 0)
			{
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sortie), "UTF-8"));

				ExtractionAdherents app = new ExtractionAdherents();
				login(app);
			    Logging.logger_.info("Extraction");
				String donnees = app.extract(structure,type, adherents, fonction,categorie,diplome,qualif,formation,format, true);
				logout();
				
				out.write(donnees);
				out.flush();
				out.close();
			}
			else
			if (generateur.compareTo(ExtractionMain.GENERATEUR_XML) == 0)
			{
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sortie), "UTF-8"));

				ExtractionAdherents app = new ExtractionAdherents();
				login(app);
			    Logging.logger_.info("Extraction");
				String donnees = app.extract(structure,type, adherents, fonction,categorie,diplome,qualif,formation,format, false);
				logout();
				
				out.write(donnees);
				out.flush();
				out.close();
			}
			else
			if (generateur.compareTo(ExtractionMain.GENERATEUR_CSV) == 0)
			{
				final CSVPrinter out = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(sortie, Charset.forName("UTF-8"));
				
				ExtractionAdherents app = new ExtractionAdherents();
				login(app);
			    Logging.logger_.info("Extraction");
				String donnees = app.extract(structure,type, adherents, fonction,categorie,diplome,qualif,formation,format, false);
				logout();
				
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
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		
		Logging.logger_.info("Terminé");
	}
	
	public static void main(String[] args) {
		Extracteur command = new Extracteur();
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
