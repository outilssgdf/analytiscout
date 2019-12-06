package org.leplan73.outilssgdf.outils;

import java.io.File;

public class FichierSortie extends File {

	private static final long serialVersionUID = 1L;

	public FichierSortie(File parent, String child) {
		super(parent, protegeNomFichier(child));
	}
	
	public FichierSortie(String string, String child) {
		super(string, protegeNomFichier(child));
	}

	private static String protegeNomFichier(String nomFichier)
	{
		String nomFichierConverti = nomFichier.replaceAll("/", "_");
		nomFichierConverti = nomFichierConverti.replaceAll("\\\\", "_");
		nomFichierConverti = nomFichierConverti.replaceAll(":", "_");
		return nomFichierConverti;
	}

}
