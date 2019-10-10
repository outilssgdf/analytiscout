package org.leplan73.outilssgdf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class ParamSortie {

	public File sortie_;
	public boolean sous_dossier_;
	public String nom_fichier_sortie_;
	public OutputStream os_;

	public ParamSortie(File sortie)
	{
		sortie_ = sortie;
	}
	
	public ParamSortie(File sortie, int[] structures, String nom_fichier_sortie)
	{
		sortie_ = sortie;
		sous_dossier_ = (structures.length > 1);
		nom_fichier_sortie_ = nom_fichier_sortie;
	}
	
	public ParamSortie(OutputStream os)
	{
		os_ = os;
	}
	
	public File getFichierSortie()
	{
		return sortie_;
	}
	
	public String getNomFichierSortie()
	{
		return nom_fichier_sortie_;
	}
	
	public boolean getSousDossier()
	{
		return sous_dossier_;
	}
	
	public boolean getIsStream()
	{
		return (os_ != null);
	}
	
	public OutputStream getStream()
	{
		return os_;
	}
	
	public File construit(int structure, String extension)
	{
		return sous_dossier_ ? new File(sortie_, nom_fichier_sortie_+structure+extension) : sortie_;
	}

	public void close() {
		try {
			os_.close();
		} catch (IOException e) {
		}
	}
}
