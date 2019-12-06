package org.leplan73.outilssgdf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.leplan73.outilssgdf.outils.FichierSortie;
import org.leplan73.outilssgdf.outils.Structure;

public class ParamSortie {

	public File sortie_;
	public boolean sous_dossier_;
	public String nom_fichier_sortie_;
	public OutputStream os_;

	public ParamSortie(File sortie)
	{
		sortie_ = sortie;
	}
	
	public ParamSortie(File sortie, boolean sous_dossier , String nom_fichier_sortie)
	{
		sortie_ = sortie;
		sous_dossier_ = sous_dossier;
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

	public void setSousDossier() {
		sous_dossier_ = true;
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
	
	public File construit(String structure, String nom, String extension)
	{
		return sous_dossier_ ? new FichierSortie(sortie_, nom_fichier_sortie_+structure+"-"+nom+extension) : sortie_;
	}
	
	public File construit(int structure, String extension)
	{
		return sous_dossier_ ? new FichierSortie(sortie_, nom_fichier_sortie_+Structure.formatStructure(structure)+extension) : sortie_;
	}

	public void close() {
		try {
			os_.close();
		} catch (IOException e) {
		}
	}
}
