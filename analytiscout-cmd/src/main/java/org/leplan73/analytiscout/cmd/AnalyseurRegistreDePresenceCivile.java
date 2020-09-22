package org.leplan73.analytiscout.cmd;

import java.io.File;

import org.leplan73.analytiscout.cmd.utils.CmdLineException;
import org.leplan73.analytiscout.cmd.utils.CommonParamsG;
import org.leplan73.analytiscout.cmd.utils.Logging;
import org.leplan73.analytiscout.engine.EngineAnalyseurRegistreDePresence;
import org.leplan73.analytiscout.engine.EngineException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseurregistredepresencecivile", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurRegistreDePresenceCivile extends CommonParamsG {

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_registrepresence_civile.xlsx");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;

	@Option(names = "-annee", description = "Fichier Année \"N\"", required = true)
	private File annee;
	
	@Option(names = "-anneep", description = "Fichier Année \"N-1\"", required = true)
	private File anneep;
	
	protected void check() throws EngineException
	{
		if (sortie.isDirectory() == true && structures.length == 1) {
			throw new EngineException("Le paramètre pour l'option -sortie doit être un fichier", false);
		}
		if (modele.isFile() == false) {
			throw new EngineException("Le paramètre pour l'option -modele doit être un fichier", false);
		}
		super.check();
	}
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		Logging.logger_.info("Lancement");
		chargeParametres();

		try {
			check();
			CmdProgress progress = new CmdProgress();
			EngineAnalyseurRegistreDePresence en = new EngineAnalyseurRegistreDePresence(progress, Logging.logger_);
			en.go(anneep, annee, modele, sortie, structures, true, anonymiser);
		} catch (Exception e) {
			Logging.logger_.error(Logging.dumpStack(null, e));
		}
	}
}
