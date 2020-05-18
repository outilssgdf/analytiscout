package org.leplan73.analytiscout.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.Options;
import org.leplan73.analytiscout.ParamEntree;
import org.leplan73.analytiscout.ParamSortie;
import org.leplan73.analytiscout.cmd.utils.CommonParamsG;
import org.leplan73.analytiscout.cmd.utils.Logging;
import org.leplan73.analytiscout.engine.EngineAnalyseur;
import org.leplan73.analytiscout.engine.EngineException;
import org.leplan73.analytiscout.outils.ResetableFileInputStream;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseurjeunes", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurJeunes extends CommonParamsG {

	@Option(names = "-batch", description = "Fichier de batch contenant les extractions à effectuer (Valeur par défaut: ${DEFAULT-VALUE})")
	private File batch = new File("conf/batch_jeunes.txt");

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_jeunes.xlsx");

	@Option(names = "-entree", required=true, description = "entree")
	private File entree;

	@Option(names = "-sortie", required=true, description = "sortie")
	private File sortie;
	
	@Option(names = "-age", description = "Gère l'âge des adhérents (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean age = false;
	
	@Option(names = "-pargroupe", description = "Générer un fichier par groupe (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean pargroupe = false;
	
	@Option(names = "-ddcs", description = "Ajouter l'onglet d'aide à la déclaration trimestrielle de la DDCS (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean ddcs = false;
	
	@Option(names = "-preinscrits", description = "Inclure les adhérents préinscrits (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean preinscrits = false;

	@Override
	public void run(CommandLine commandLine) throws IOException, ExtractionException, JDOMException, InvalidFormatException
	{
		try {
			check();
			CmdProgress progress = new CmdProgress();
			EngineAnalyseur en = new EngineAnalyseur(progress, Logging.logger_);
			
			Options options = new Options();
			if (preinscrits) options.add(Options.OPTION_PREINSCRITS);
			
			ParamEntree pentree = new ParamEntree(entree, structures);
			ParamSortie psortie = new ParamSortie(sortie, pargroupe || structures.length > 1, Consts.NOM_FICHIER_ANALYSE_JEUNES);
			en.go(pentree, new ResetableFileInputStream(new FileInputStream(batch)), new ResetableFileInputStream(new FileInputStream(modele)), structures, age, "tout_jeunes" , psortie, anonymiser, pargroupe, ddcs, options);
		} catch (EngineException e) {
			Logging.logError(e);
		}
	}
}
