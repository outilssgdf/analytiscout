package org.leplan73.outilssgdf.gui.analyseur;

import java.io.File;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurJeunes extends Analyseur {

	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurJeunes.class);
	
	public AnalyseurJeunes() {
		super("Analyseur (Jeunes)",logger_,new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false),"analyse_jeunes.xlsx"), new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false)), Consts.NOM_FICHIER_ANALYSE_JEUNES, new File("conf/batch_jeunes.txt"),new File("conf/modele_jeunes.xlsx"));
	}
}
