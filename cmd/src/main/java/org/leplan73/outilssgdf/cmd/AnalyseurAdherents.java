package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseuradherents", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurAdherents extends CommonParamsG {

	@Option(names = "-batch", description = "Fichier de batch contenant les extractions à effectuer (Valeur par défaut: ${DEFAULT-VALUE})")
	private File batch = new File("conf/batch_adherents.txt");

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_adherents.xlsx");

	@Option(names = "-entree", required=true, description = "entree")
	private File entree;

	@Option(names = "-sortie", required=true, description = "sortie")
	private File sortie;
	
	@Option(names = "-age", description = "Gère l'âge des adhérents (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean age = false;

	@Override
	public void run(CommandLine commandLine) throws IOException, ExtractionException, JDOMException, InvalidFormatException
	{
		Instant now = Instant.now();
		
		Logging.logger_.info("Lancement");
	    
	    chargeParametres();
	    
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

			ExtraKey extra = new ExtraKey(pbatch.getProperty("fichier." + index, pbatch.getProperty("nom." + index, "")), pbatch.getProperty("nom." + index, ""),
					pbatch.getProperty("batchtype." + index, "tout_adherents"));
			File fichier = new File(dossierStructure, extra.fichier_+"."+generateur);
			
		    Logging.logger_.info("Chargement du fichier \""+fichier.getName()+"\"");
		    
		    if (extra.ifTout())
			{
				fichierAdherents = fichier;
			}
			else
				extraMap.put(extra, new ExtracteurExtraHtml(fichier,age));
			index++;
		}

		Logging.logger_.info("Chargement du fichier \""+fichierAdherents.getName()+"\"");
		ExtracteurIndividusHtml adherents = new ExtracteurIndividusHtml(fichierAdherents, extraMap,age, version_);
		adherents.calculGlobal();

		FileOutputStream outputStream = new FileOutputStream(sortie);

	    Logging.logger_.info("Generation du fichier \""+sortie.getName()+"\" à partir du modèle \""+modele.getName()+"\"");
		adherents.transforme(modele, outputStream);
		
		outputStream.flush();
		outputStream.close();
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
}
