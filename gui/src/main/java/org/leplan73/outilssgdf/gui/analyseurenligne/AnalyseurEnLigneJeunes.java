package org.leplan73.outilssgdf.gui.analyseurenligne;

import java.io.File;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurEnLigneJeunes extends AnalyseurEnLigne {

	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurEnLigneJeunes.class);

	public AnalyseurEnLigneJeunes() {
		super("Analyseur en ligne (Jeunes)", logger_, new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false),"analyse_jeunes.xlsx"), new File("conf/batch_jeunes.txt"), new File("conf/modele_jeunes.xlsx"));
	}
}
