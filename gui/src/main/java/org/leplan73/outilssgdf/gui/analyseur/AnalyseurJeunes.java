package org.leplan73.outilssgdf.gui.analyseur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurJeunes extends Analyseur {

	private static File fSortie = new File("./données/analyse_jeunes.xlsx");
	private static File fBatch = new File("./conf/batch_jeunes.txt");
	private static File fModele = new File("conf/modele_jeunes.xlsx");
	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurJeunes.class);
	
	public AnalyseurJeunes() {
		super("Analyseur (Jeunes)",logger_,fSortie, fBatch,fModele);
	}
}
