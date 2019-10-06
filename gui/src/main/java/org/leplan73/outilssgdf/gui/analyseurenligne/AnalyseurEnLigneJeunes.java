package org.leplan73.outilssgdf.gui.analyseurenligne;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurEnLigneJeunes extends AnalyseurEnLigne {

	private static File fSortie = new File("données/analyse_jeunes.xlsx");
	private static File fBatch = new File("conf/batch_jeunes.txt");
	private static File fModele = new File("conf/modele_jeunes.xlsx");
	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurEnLigneJeunes.class);
	
	public AnalyseurEnLigneJeunes() {
		super("Analyseur en ligne (Jeunes)", logger_, fSortie, fBatch, fModele);
	}
}
