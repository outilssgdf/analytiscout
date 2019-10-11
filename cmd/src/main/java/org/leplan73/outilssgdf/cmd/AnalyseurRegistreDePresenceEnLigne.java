package org.leplan73.outilssgdf.cmd;

import java.io.File;

import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineAnalyseurRegistreDePresenceEnLigne;
import org.leplan73.outilssgdf.engine.EngineException;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseurregistredepresenceenligne", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurRegistreDePresenceEnLigne extends CommonParamsIntranet {

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_registrepresence.xlsx");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;
	
	@Option(names = "-annee", description = "Année", required = true)
	private int annee;

	@Option(names = "-recursif", description = "Extraction récursive (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean recursif = true;

	@Option(names = "-garder", description = "Garder fichiers téléchargés (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
	private boolean garder = false;
	
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
			EngineAnalyseurRegistreDePresenceEnLigne en = new EngineAnalyseurRegistreDePresenceEnLigne(progress, Logging.logger_);
			en.go(identifiant,motdepasse, sortie, modele, annee, structures, recursif, true, garder);
		} catch (Exception e) {
			Logging.logger_.error(Logging.dumpStack(null, e));
		}
	}
}
