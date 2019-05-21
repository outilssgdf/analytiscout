package org.leplan73.outilssgdf.gui.analyseurenligne;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurEnLigneCompas extends AnalyseurEnLigne {

	private static File fSortie = new File("./données/analyse_compas.xlsx");
	private static File fBatch = new File("./conf/batch_compas.txt");
	private static File fModele = new File("conf/modele_compas.xlsx");
	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurEnLigneCompas.class);
	
	public AnalyseurEnLigneCompas() {
		super("Analyseur en ligne (Compas)", logger_, fSortie, fBatch, fModele);
	}
}
