package org.leplan73.outilssgdf.cmd;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.fusesource.jansi.AnsiConsole;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

@Command(name = "Extracteur", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class Extracteur extends CommonParamsIntranet {

	private static final String ENCODING_WINDOWS = "Windows-1252";
	private static final String ENCODING_UTF8 = "UTF-8";

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;

	@Option(names = "-qualif", description = "Filtrage sur la qualification (Valeur par défaut: ${DEFAULT-VALUE}) (Valeurs possibles : -1=Sans importance, 1=Directeur SF (CAFDSF), 2=Animateur SF (CAFASF), 3=Responsable Unité SF (CAFRUSF)")
	private int qualif = ExtractionMain.QUALIFICATION_TOUT;

	@Option(names = "-diplome", description = "Filtrage sur le diplôme à extraire  (Valeur par défaut: ${DEFAULT-VALUE}) (Valeurs possibles : ...)")
	private int diplome = ExtractionMain.DIPLOME_TOUT;

	@Option(names = "-formation", description = "Filtrage sur la formation à extraire  (Valeur par défaut: ${DEFAULT-VALUE}) (Valeurs possibles : ...)")
	private int formation = ExtractionMain.FORMATION_TOUT;

	@Option(names = "-format", description = "Données à extraire (Valeur par défaut: ${DEFAULT-VALUE}) (Valeurs possibles (à additionner pour extraire plusieurs champs) : individu=1, parents=2, inscription=4, adhesion=8, JS=16, sans QF=32)")
	private int format = ExtractionMain.FORMAT_INDIVIDU;

	@Option(names = "-categorie", description = "Catégorie à extraire (Valeur par défaut: ${DEFAULT-VALUE}) (Valeurs possibles : -1=tout, 0=jeune, 1=responsable)")
	private int categorie = ExtractionMain.CATEGORIE_TOUT;

	@Option(names = "-specialite", description = "Filtrage sur le spécialité  (Valeur par défaut: ${DEFAULT-VALUE}) (Valeurs possibles : -1=Sans importance, 622=Marine, 624=sans spécialité, 623=Vent du Large")
	private int specialite = ExtractionMain.SPECIALITE_SANS_IMPORTANCE;

	@Option(names = "-fonction", description = "Code fonction (voir doc en stock), peut contenir des * ou plusieurs séparés par des virgules")
	private String fonction = "";

	@Option(names = "-type", description = "Filtrage sur le type (Valeur par défaut: ${DEFAULT-VALUE}) (Valeurs possibles : -1=tout, 0=inscrit, 1=invite, 2=pré-inscrit)")
	private int type = ExtractionMain.TYPE_TOUT;

	@Option(names = "-adherents", description = "Adhérents uniquement (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean adherents = false;

	@Option(names = "-generateur", description = "Format de génération des données (Valeur par défaut: ${DEFAULT-VALUE}) (Valeurs possibles : cvs, xls ou xml)")
	private String generateur = "xls";

	@Option(names = "-recursif", description = "Extraction récursive (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean recursif = true;
	
	public void run()
	{
		Instant now = Instant.now();
		checkParams();
		
		Logging.initLogger(Extracteur.class);
			
		Logging.logger_.info("Lancement");
	    
	    if (debug)
	    {
	    	Logging.enableDebug();
	    }
	    
	    chargeParametres();
	    
		try {
			charge();
			
			if (generateur.compareTo(ExtractionMain.GENERATEUR_XLS) == 0)
			{
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sortie), ENCODING_WINDOWS));

				ExtractionAdherents app = new ExtractionAdherents();
				login(app);
			    Logging.logger_.info("Extraction");
				String donnees = app.extract(structures[0],recursif,type, adherents, fonction,specialite,categorie,diplome,qualif,formation,format, true);
				logout();
				
				Logging.logger_.info("Ecriture du fichier \""+sortie.getAbsolutePath()+"\"");
				out.write(donnees);
				out.flush();
				out.close();
			}
			else
			if (generateur.compareTo(ExtractionMain.GENERATEUR_XML) == 0)
			{
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sortie), ENCODING_UTF8));

				ExtractionAdherents app = new ExtractionAdherents();
				login(app);
			    Logging.logger_.info("Extraction");
				String donnees = app.extract(structures[0],recursif,type, adherents, fonction,specialite,categorie,diplome,qualif,formation,format, false);
				logout();
				
				out.write(donnees);
				out.flush();
				out.close();
			}
			else
			if (generateur.compareTo(ExtractionMain.GENERATEUR_CSV) == 0)
			{
				final CSVPrinter out = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(sortie, Charset.forName(ENCODING_WINDOWS));
				
				ExtractionAdherents app = new ExtractionAdherents();
				login(app);
			    Logging.logger_.info("Extraction");
				String donnees = app.extract(structures[0],recursif,type, adherents, fonction,specialite,categorie,diplome,qualif,formation,format, false);
				logout();
				
				XPathFactory xpfac = XPathFactory.instance();
				SAXBuilder builder = new SAXBuilder();
		        org.jdom2.Document docx = builder.build(new ByteArrayInputStream(donnees.getBytes(Charset.forName(ENCODING_UTF8))));
		        
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
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
	
	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		Extracteur command = new Extracteur();
		try
		{
			CommandLine commandLine = new CommandLine(command);
			commandLine.parse(args);
			if (commandLine.isVersionHelpRequested())
			{
			    commandLine.printVersionHelp(System.out, Ansi.ON);
			    return;
			}
			if (commandLine.isUsageHelpRequested())
			{
			    commandLine.usage(System.out, Ansi.ON);
			    return;
			}
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
