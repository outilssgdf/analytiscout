package org.leplan73.outilssgdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ParamEntree {

	public File entree_;
	public boolean sous_dossier_;
	public InputStream is_;

	public ParamEntree(File entree)
	{
		entree_ = entree;
	}
	
	public ParamEntree(File entree, int[] structures)
	{
		entree_ = entree;
		sous_dossier_ = (structures.length > 1);
	}
	
	public ParamEntree(InputStream is)
	{
		is_ = is;
	}
	
	public File getFichierSortie()
	{
		return entree_;
	}
	
	public boolean getSousDossier()
	{
		return sous_dossier_;
	}
	
	public boolean getIsStream()
	{
		return (is_ != null);
	}
	
	public InputStream getStream()
	{
		return is_;
	}
	
	public File construit(int structure)
	{
		return sous_dossier_ ? new File(entree_,""+structure) : entree_;
	}

	public void close() {
		try {
			is_.close();
		} catch (IOException e) {
		}
	}
}
