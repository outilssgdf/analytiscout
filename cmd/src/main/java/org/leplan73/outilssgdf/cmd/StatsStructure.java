package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.FileInputStream;

import org.leplan73.outilssgdf.ParamSortie;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineStatsEnLigne;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.leplan73.outilssgdf.outils.ResetableFileInputStream;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "statsstructure", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class StatsStructure extends CommonParamsIntranet {

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_statsstructure.xlsx");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;

	@Option(names = "-garder", description = "Garder les fichiers téléchargés (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
	private boolean garder = false;
	
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
			EngineStatsEnLigne en = new EngineStatsEnLigne(progress, Logging.logger_);
			ParamSortie psortie = new ParamSortie(sortie);
			en.go(identifiant,motdepasse, new ResetableFileInputStream(new FileInputStream(modele)), structures[0], psortie, anonymiser, garder);
		} catch (Exception e) {
			Logging.logger_.error(Logging.dumpStack(null, e));
		}
	}
}
