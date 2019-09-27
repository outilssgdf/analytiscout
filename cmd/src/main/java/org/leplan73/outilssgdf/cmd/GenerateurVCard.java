package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineException;
import org.leplan73.outilssgdf.engine.EngineGenerateurVCard;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generateurvcard", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class GenerateurVCard extends CommonParamsIntranet {

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;
	
	@Option(names = "-configuration", required=true, description = "Fichier de configuration")
	private File configuration = new File("./données/conf/"+Consts.FICHIER_CONF_EXPORT);

	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		if (this.qualifications == true)
		{
			ExtractionIntranet.setQualifications(true);
		}
		Instant now = Instant.now();
		
		checkParams();
		
		Logging.logger_.info("Lancement");
	    
	    chargeParametres();
	    
		try {
			charge();
			
			CmdProgress progress = new CmdProgress();
			EngineGenerateurVCard en = new EngineGenerateurVCard(progress, Logging.logger_);
			en.go(identifiant,motdepasse, configuration, sortie, structures[0], structures);
			
		} catch (IOException|EngineException e) {
			Logging.logError(e);
		}
	}
}
