package org.leplan73.analytiscout.cmd;

import java.io.File;

import org.leplan73.analytiscout.cmd.utils.CmdLineException;
import org.leplan73.analytiscout.cmd.utils.CommonParamsG;
import org.leplan73.analytiscout.cmd.utils.CommonParamsIntranet;
import org.leplan73.analytiscout.cmd.utils.Logging;
import org.leplan73.analytiscout.engine.EngineAnalyseurCECEnLigne;
import org.leplan73.analytiscout.engine.EngineException;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseurcecenligne", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurCECEnLigne extends CommonParamsIntranet {

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_registrepresence.xlsx");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;
	
	@Option(names = "-annee", description = "Année", required = true)
	private int annee;
	
	protected void check() throws EngineException
	{
		if (sortie.isDirectory() == false) {
			throw new EngineException("Le paramètre pour l'option -sortie doit être un répertoire", false);
		}
		if (modele.isFile() == false) {
			throw new EngineException("Le paramètre pour l'option -modele doit être un fichier", false);
		}
		super.check();
	}
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		if (this.qualifications == true)
		{
			ExtractionIntranet.setQualifications(true);
		}
		Logging.logger_.info("Lancement");
		chargeParametres();

		try {
			charge();
			check();
			CmdProgress progress = new CmdProgress();
			EngineAnalyseurCECEnLigne en = new EngineAnalyseurCECEnLigne(progress, Logging.logger_);
			en.go(identifiant,motdepasse, sortie, modele, annee, structures, anonymiser);
		} catch (Exception e) {
			Logging.logger_.error(Logging.dumpStack(null, e));
		}
	}
}
