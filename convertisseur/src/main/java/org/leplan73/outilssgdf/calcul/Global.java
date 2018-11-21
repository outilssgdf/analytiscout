package org.leplan73.outilssgdf.calcul;

public class Global {

	private String groupe_;
	private boolean marins_;
	
	public Global(String groupe, boolean marins)
	{
		groupe_ = groupe;
		marins_ = marins;
	}
	
	public String getGroupe()
	{
		return groupe_;
	}
	
	public String getMarins()
	{
		return marins_ ? "Oui" : "Non";
	}
	
	private int rgdirsf_;
	private int dirsf_;
	
	public void addRgdirsf()
	{
		boolean ret = (rgdirsf_ == 0);
		if (ret == true)
		{
			rgdirsf_++;
		}
		else
		{
			dirsf_++;
		}
	}
	
	public int getRgdirsf()
	{
		return rgdirsf_;
	}
	
	public int getDirsf()
	{
		return dirsf_;
	}

	private int animsfqualifie_;
	public void addAnimsfQualifie() {
		animsfqualifie_++;
	}
	
	public int getAnimsfqualifie()
	{
		return animsfqualifie_;
	}
}
