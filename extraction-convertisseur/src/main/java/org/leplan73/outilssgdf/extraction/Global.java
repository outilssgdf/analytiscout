package org.leplan73.outilssgdf.extraction;

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
		return marins_ ? "Oui" : "";
	}
}
