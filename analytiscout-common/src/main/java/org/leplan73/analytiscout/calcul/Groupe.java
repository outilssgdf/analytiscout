package org.leplan73.analytiscout.calcul;

import java.util.Map;

public class Groupe implements Comparable<Groupe> {
	protected String nomcomplet_;
	protected String nom_;
	protected String code_;
	
	public Groupe(String nom)
	{
		int index = nom.indexOf(" - ");
		if (index == -1)
		{
			code_ = nom.substring(0,nom.length()-2);
			code_ = code_ + "00";
		}
		else
		{
			nomcomplet_ = nom;
			nom_ = nom.substring(nom.indexOf(" - ")+3);
			code_ = nom.substring(0,nom.indexOf(" - ")-2);
			code_ = code_ + "00";
		}
	}
	
	public String getCode()
	{
		return code_;
	}
	
	public String getNom()
	{
		return nom_;
	}

	public String nomcomplet() {
		return nomcomplet_;
	}
	
	@Override
	public String toString() {
		return nomcomplet_;
	}

	public void setNom(String groupe) {
		nomcomplet_ = groupe;
		nom_ = groupe.substring(groupe.indexOf(" - ")+3);
		code_ = groupe.substring(0,groupe.indexOf(" - ")-2);
		code_ = code_ + "00";
	}

	@Override
	public int compareTo(Groupe o) {
		return code_.compareTo(o.code_);
	}
}
