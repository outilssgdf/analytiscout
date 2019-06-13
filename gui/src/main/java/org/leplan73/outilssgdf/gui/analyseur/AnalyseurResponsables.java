package org.leplan73.outilssgdf.gui.analyseur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurResponsables extends Analyseur {

	private static File fSortie = new File("./données/analyse.xlsx");
	private static File fBatch = new File("./conf/batch_responsables.txt");
	private static File fModele = new File("conf/modele_responsables.xlsx");
	private static Logger logger_ = LoggerFactory.getLogger(Analyseur.class);
	
	public AnalyseurResponsables() {
		super("Analyseur (Responsables)",logger_,fSortie, fBatch,fModele);
	}
}
