package org.leplan73.analytiscout.cmd;

import java.io.File;
import java.io.FileInputStream;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.Options;
import org.leplan73.analytiscout.ParamSortie;
import org.leplan73.analytiscout.cmd.utils.CmdLineException;
import org.leplan73.analytiscout.cmd.utils.CommonParamsG;
import org.leplan73.analytiscout.cmd.utils.CommonParamsIntranet;
import org.leplan73.analytiscout.cmd.utils.Logging;
import org.leplan73.analytiscout.engine.EngineAnalyseurEnLigne;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;
import org.leplan73.analytiscout.outils.ResetableFileInputStream;

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

	@Option(names = "-garder", description = "Garder les fichiers téléchargés (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
	private boolean garder = false;
	
	@Option(names = "-pargroupe", description = "Générer un fichier par groupe (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean pargroupe = false;
	
	@Option(names = "-ddcs", description = "Ajouter l'onglet d'aide à la déclaration trimestrielle de la DDCS (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean ddcs = false;
	
	@Option(names = "-preinscrits", description = "Inclure les adhérents préinscrits (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean preinscrits = false;
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		if (this.qualifications == true)
		{
			ExtractionIntranet.setQualifications(true);
		}
		Logging.logger_.info("Lancement");
		chargeParametres();
		
		Options options = new Options();
		if (preinscrits) options.add(Options.OPTION_PREINSCRITS);

		try {
			charge();
			check();
			CmdProgress progress = new CmdProgress();
			EngineAnalyseurEnLigne en = new EngineAnalyseurEnLigne(progress, Logging.logger_);
			ParamSortie psortie = new ParamSortie(sortie, pargroupe || structures.length > 1, Consts.NOM_FICHIER_ANALYSE_RESPONSABLES);
			en.go(identifiant,motdepasse, new ResetableFileInputStream(new FileInputStream(batch)), new ResetableFileInputStream(new FileInputStream(modele)), structures, age, "tout_responsables", recursif, psortie, anonymiser, garder, pargroupe, ddcs, options);
		} catch (Exception e) {
			Logging.logger_.error(Logging.dumpStack(null, e));
		}
	}
}
