package org.leplan73.analytiscout.cmd;

import java.io.File;
import java.io.IOException;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.cmd.utils.CmdLineException;
import org.leplan73.analytiscout.cmd.utils.CommonParamsG;
import org.leplan73.analytiscout.cmd.utils.CommonParamsIntranet;
import org.leplan73.analytiscout.cmd.utils.Logging;
import org.leplan73.analytiscout.engine.EngineException;
import org.leplan73.analytiscout.engine.EngineGenerateurVCard;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;
import org.leplan73.analytiscout.intranet.LoginEngineException;

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
		} catch (LoginEngineException e) {
			Logging.logError(e);
		}
	}
}
