package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.FileInputStream;

import org.leplan73.outilssgdf.ParamSortie;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineAnalyseurEnLigne;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.leplan73.outilssgdf.outils.ResetableFileInputStream;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseurenligne", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurEnLigne extends CommonParamsIntranet {

	@Option(names = "-batch", description = "Fichier de batch contenant les extractions à effectuer (Valeur par défaut: ${DEFAULT-VALUE})")
	private File batch = new File("conf/batch_responsables.txt");

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_responsables.xlsx");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;
	
	@Option(names = "-age", description = "Gère l'âge des adhérents (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean age = false;

	@Option(names = "-recursif", description = "Extraction récursive (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean recursif = true;

	@Option(names = "-garder", description = "Garder fichiers téléchargés (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
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
			EngineAnalyseurEnLigne en = new EngineAnalyseurEnLigne(progress, Logging.logger_);
			ParamSortie psortie = new ParamSortie(sortie, structures, "responsables_");
			en.go(identifiant,motdepasse, new ResetableFileInputStream(new FileInputStream(batch)), new ResetableFileInputStream(new FileInputStream(modele)), structures, age, "tout_responsables", recursif, psortie, anonymiser, garder);
		} catch (Exception e) {
			Logging.logger_.error(Logging.dumpStack(null, e));
		}
	}
}
