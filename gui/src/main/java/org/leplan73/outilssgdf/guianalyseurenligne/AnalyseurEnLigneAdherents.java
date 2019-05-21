package org.leplan73.outilssgdf.guianalyseurenligne;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurEnLigneAdherents extends AnalyseurEnLigne {

	private static File fSortie = new File("./données/analyse_adherents.xlsx");
	private static File fBatch = new File("./conf/batch_adherents.txt");
	private static File fModele = new File("conf/modele_adherents.xlsx");
	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurEnLigneCompas.class);
	
	public AnalyseurEnLigneAdherents() {
		super("Analyseur en ligne (Adhérents)", logger_, fSortie, fBatch, fModele);
	}
}
