package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ParamEntree;
import org.leplan73.outilssgdf.ParamSortie;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.engine.EngineAnalyseur;
import org.leplan73.outilssgdf.engine.EngineException;
import org.leplan73.outilssgdf.outils.ResetableFileInputStream;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseur", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class Analyseur extends CommonParamsG {

	@Option(names = "-batch", description = "Fichier de batch contenant les extractions à effectuer (Valeur par défaut: ${DEFAULT-VALUE})")
	private File batch = new File("conf/batch_responsables.txt");

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_responsables.xlsx");

	@Option(names = "-entree", required=true, description = "Fichier d'entrée")
	private File entree;

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;
	
	@Option(names = "-age", description = "Gère l'âge des adhérents (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean age = false;
	
	@Option(names = "-pargroupe", description = "Générer un fichier par groupe (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean pargroupe = false;
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		try {
			check();
			CmdProgress progress = new CmdProgress();
			EngineAnalyseur en = new EngineAnalyseur(progress, Logging.logger_);
			
			ParamEntree pentree = new ParamEntree(entree, structures);
			ParamSortie psortie = new ParamSortie(sortie, pargroupe || structures.length > 1, Consts.NOM_FICHIER_ANALYSE_RESPONSABLES);
			en.go(pentree, new ResetableFileInputStream(new FileInputStream(batch)), new ResetableFileInputStream(new FileInputStream(modele)), structures, age, "tout_responsables" , psortie, anonymiser, pargroupe);
		} catch (EngineException | FileNotFoundException e) {
			Logging.logError(e);
		}
	}
}
