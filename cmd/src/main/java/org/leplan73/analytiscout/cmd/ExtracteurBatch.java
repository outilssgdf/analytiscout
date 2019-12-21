package org.leplan73.analytiscout.cmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.leplan73.analytiscout.cmd.utils.CmdLineException;
import org.leplan73.analytiscout.cmd.utils.CommonParamsG;
import org.leplan73.analytiscout.cmd.utils.CommonParamsIntranet;
import org.leplan73.analytiscout.cmd.utils.Logging;
import org.leplan73.analytiscout.engine.EngineException;
import org.leplan73.analytiscout.engine.EngineExtracteurBatch;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;
import org.leplan73.analytiscout.intranet.LoginEngineException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

@Command(name = "extracteurbatch", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class ExtracteurBatch extends CommonParamsIntranet {

	@Option(names = "-batch", description = "Fichier de batch contenant les extractions à effectuer (Valeur par défaut: ${DEFAULT-VALUE})")
	private File batch = new File("conf/batch_responsables.txt");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;

	@Option(names = "-recursif", description = "Extraction récursive (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean recursif = true;

	@Override
	public void run(CommandLine commandLine) throws CmdLineException {
		if (this.qualifications == true)
		{
			ExtractionIntranet.setQualifications(true);
		}
		Logging.logger_.info("Lancement");
		checkParams();
		try {
			check();
			charge();
			CmdProgress progress = new CmdProgress();
			EngineExtracteurBatch en = new EngineExtracteurBatch(progress, Logging.logger_);
			en.go(identifiant, motdepasse, batch, sortie, structures, true);
		} catch (EngineException e) {
			Logging.logError(e);
		} catch (PicocliException e) {
			Logging.logError(e);
		} catch (FileNotFoundException e) {
			Logging.logError(e);
		} catch (IOException e) {
			Logging.logError(e);
		} catch (LoginEngineException e) {
			Logging.logError(e);
		}
	}
}