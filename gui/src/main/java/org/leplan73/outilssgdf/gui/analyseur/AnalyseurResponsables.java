package org.leplan73.outilssgdf.gui.analyseur;

import java.io.File;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurResponsables extends Analyseur {

	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurResponsables.class);
	
	public AnalyseurResponsables() {
		super("Analyseur (Responsables)",logger_,new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false),"analyse_responsables.xlsx"), new File("conf/batch_responsables.txt"),new File("conf/modele_responsables.xlsx"));
	}
}
