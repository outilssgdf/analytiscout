package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineExtracteurBatch;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

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
		Logging.logger_.info("Lancement");
		checkParams();
		try {
			check();
			charge();
			CmdProgress progress = new CmdProgress();
			EngineExtracteurBatch en = new EngineExtracteurBatch(connection_, progress, Logging.logger_);
			en.go(identifiant, motdepasse, batch, sortie, structure, structures, true);
		} catch (IOException|JDOMException e) {
			Logging.logError(e);
		} catch (Exception e) {
			Logging.logError(e);
		}
	}
}
