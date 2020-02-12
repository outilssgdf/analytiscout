package org.leplan73.analytiscout.gui.analyseur;

import java.io.File;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.gui.utils.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class AnalyseurMarins extends Analyseur {

	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurMarins.class);
	
	public AnalyseurMarins() {
		super("Analyseur (Marins)",logger_,new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false),"analyse_marins.xlsx"), new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false)), Consts.NOM_FICHIER_ANALYSE_MARINS, new File("conf/batch_marins.txt"),new File("conf/modele_marins.xlsx"));
	}
}
