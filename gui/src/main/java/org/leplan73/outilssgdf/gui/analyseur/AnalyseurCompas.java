package org.leplan73.outilssgdf.gui.analyseur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurCompas extends Analyseur {

	private static File fSortie = new File("./données/analyse_compas.xlsx");
	private static File fBatch = new File("./conf/batch_compas.txt");
	private static File fModele = new File("conf/modele_compas.xlsx");
	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurCompas.class);
	
	public AnalyseurCompas() {
		super("Analyseur (Compas)",logger_,fSortie, fBatch,fModele);
	}
}
