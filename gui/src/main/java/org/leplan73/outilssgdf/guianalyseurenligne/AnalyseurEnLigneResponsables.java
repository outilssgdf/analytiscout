package org.leplan73.outilssgdf.guianalyseurenligne;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyseurEnLigneResponsables extends AnalyseurEnLigne {

	private static File fSortie = new File("./données/analyse.xlsx");
	private static File fBatch = new File("./conf/batch_responsables.txt");
	private static File fModele = new File("conf/modele_responsables.xlsx");
	private static Logger logger_ = LoggerFactory.getLogger(AnalyseurEnLigneCompas.class);
	
	public AnalyseurEnLigneResponsables() {
		super("Analyseur en ligne (Responsables)", logger_, fSortie, fBatch, fModele);
	}
}
