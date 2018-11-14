package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.fusesource.jansi.AnsiConsole;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentFormes;

import net.sf.jett.transform.ExcelTransformer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

@Command(name = "Analyseur", mixinStandardHelpOptions = true, version = "1.0")
public class Analyseur extends CommonParamsG {

	@Option(names = "-batch", required=true, description = "batch")
	private File batch;

	@Option(names = "-modele", required=true, description = "modele")
	private File modele;

	@Option(names = "-entree", required=true, description = "entree")
	private File entree;

	@Option(names = "-sortie", required=true, description = "sortie")
	private File sortie;
	
	public void run() throws IOException, ExtractionException, JDOMException, InvalidFormatException
	{
		Instant now = Instant.now();
		Logging.initLogger(Analyseur.class);
		
		Logging.logger_.info("Lancement");
	    
	    if (debug)
	    {
	    	Logging.enableDebug();
	    }
	    
	    Logging.logger_.info("Chargement du fichier de traitement");
		
		Properties pbatch = new Properties();
		pbatch.load(new FileInputStream(batch));

		Map<String, ExtracteurExtraHtml> extraMap = new TreeMap<String, ExtracteurExtraHtml>();
		File fichierAdherents = null;

		File dossierStructure = new File(entree,""+structures[0]);
		dossierStructure.exists();
		
		int index=1;
		for(;;)
		{
			String generateur = pbatch.getProperty("generateur."+index);
			if (generateur == null)
			{
				break;
			}
			
			String nom = pbatch.getProperty("nom."+index,"");
			File fichier = new File(dossierStructure, nom+"."+generateur);
			
		    Logging.logger_.info("Chargement du fichier \""+fichier.getName()+"\"");
		    
			if (nom.compareTo("tout") == 0)
			{
				fichierAdherents = fichier;
			}
			else
				extraMap.put(nom, new ExtracteurExtraHtml(fichier.getAbsolutePath()));
			index++;
		}

		Logging.logger_.info("Chargement du fichier \""+fichierAdherents.getName()+"\"");
		ExtracteurHtml adherents = new ExtracteurHtml(fichierAdherents, extraMap);
		 
		AdherentFormes compas = new AdherentFormes();
		compas.charge(adherents,extraMap);
		
		General general = new General();
		Global global = new Global(adherents.getGroupe(), adherents.getMarins());
		adherents.calculGlobal(global);

		FileOutputStream outputStream = new FileOutputStream(sortie);

	    Logging.logger_.info("Generation du fichier \""+sortie.getName()+"\" à partir du modèle \""+modele.getName()+"\"");
		ExcelTransformer trans = new ExcelTransformer();
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("chefs", adherents.getChefsList());
		beans.put("compas", adherents.getCompasList());
		beans.put("unites", adherents.getUnitesList());
		beans.put("general", general);
		beans.put("global", global);
		Workbook workbook = trans.transform(new FileInputStream(modele), beans);
		workbook.write(outputStream);
		
		outputStream.flush();
		outputStream.close();
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
	
	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		Analyseur command = new Analyseur();
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
