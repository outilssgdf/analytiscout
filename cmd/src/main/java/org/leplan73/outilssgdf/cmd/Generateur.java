package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineGenerateur;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generateur", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class Generateur extends CommonParamsIntranet {

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;

	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		Instant now = Instant.now();
		
		checkParams();
		
		Logging.logger_.info("Lancement");
	    
	    chargeParametres();
	    
		try {
			CmdProgress progress = new CmdProgress();
			EngineGenerateur en = new EngineGenerateur(progress, Logging.logger_);
			en.go(identifiant,motdepasse, sortie, structures[0], structures);
			
		} catch (IOException|JDOMException|ExtractionException e) {
			Logging.logError(e);
		} catch (Exception e) {
			Logging.logError(e);
		}
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
}
