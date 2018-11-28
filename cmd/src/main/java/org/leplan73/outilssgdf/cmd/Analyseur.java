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
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;

import com.jcabi.manifests.Manifests;

import net.sf.jett.transform.ExcelTransformer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseur", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class Analyseur extends CommonParamsG {

	@Option(names = "-batch", required=true, description = "batch")
	private File batch;

	@Option(names = "-modele", required=true, description = "Fichier de modèle")
	private File modele;

	@Option(names = "-entree", required=true, description = "Fichier d'entrée")
	private File entree;

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;
	
	@Option(names = "-age", description = "Gère l'âge des adhérents (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean age = false;
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		Instant now = Instant.now();
		
		Logging.logger_.info("Lancement");
	    
	    chargeParametres();

		try {
		    Logging.logger_.info("Chargement du fichier de traitement");
			
			Properties pbatch = new Properties();
			pbatch.load(new FileInputStream(batch));
	
			Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
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
				
				ExtraKey extra = new ExtraKey(pbatch.getProperty("nom."+index,""), pbatch.getProperty("batchtype."+index,"tout"));
				File fichier = new File(dossierStructure, extra.nom_+"."+generateur);
				
			    Logging.logger_.info("Chargement du fichier \""+fichier.getName()+"\"");
			    
				if (extra.ifTout())
				{
					fichierAdherents = fichier;
				}
				else
					extraMap.put(extra, new ExtracteurExtraHtml(fichier.getAbsolutePath(),age));
				index++;
			}
	
			Logging.logger_.info("Chargement du fichier \""+fichierAdherents.getName()+"\"");
			ExtracteurHtml adherents = new ExtracteurHtml(fichierAdherents, extraMap,age);
			 
			AdherentFormes compas = new AdherentFormes();
			compas.charge(adherents,extraMap);
			
			String version = "";
			try
			{
				version = Manifests.read("version");
			}
			catch(java.lang.IllegalArgumentException e) {
			}
			General general = new General(version);
			Global global = new Global(adherents.getGroupe(), adherents.getMarins());
			adherents.calculGlobal(global);
	
			FileOutputStream outputStream = new FileOutputStream(sortie);
	
		    Logging.logger_.info("Génération du fichier \""+sortie.getName()+"\" à partir du modèle \""+modele.getName()+"\"");
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
			
		} catch (IOException|JDOMException | InvalidFormatException | ExtractionException e) {
			Logging.logError(e);
		}
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
}
