package org.leplan73.outilssgdf.cmd;

import java.io.File;

import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineException;
import org.leplan73.outilssgdf.engine.EngineExtractionRegistreDePresence;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "extracteurregistredepresence", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class ExtracteurRegistreDePresence extends CommonParamsIntranet {

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;
	
	@Option(names = "-annee", description = "Année", required = true)
	private int annee = 2019;

	@Option(names = "-recursif", description = "Extraction récursive (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean recursif = true;
	
	protected void check() throws EngineException
	{
		if (sortie.isDirectory() == true && structures.length == 1) {
			throw new EngineException("Le paramètre pour l'option -sortie doit être un fichier", false);
		}
		super.check();
	}
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		Logging.logger_.info("Lancement");
		chargeParametres();

		try {
			charge();
			check();
			CmdProgress progress = new CmdProgress();
			EngineExtractionRegistreDePresence en = new EngineExtractionRegistreDePresence(progress, Logging.logger_);
			en.go(identifiant,motdepasse, sortie, structures, recursif, annee);
		} catch (Exception e) {
			Logging.logger_.error(Logging.dumpStack(null, e));
		}
	}
}
