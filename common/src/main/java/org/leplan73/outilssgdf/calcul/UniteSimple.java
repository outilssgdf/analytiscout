package org.leplan73.outilssgdf.calcul;

import org.leplan73.outilssgdf.Consts;

public class UniteSimple implements Comparable<Unite> {
	protected String nom_;
	protected int code_=999;
	protected String codeStructure_;
	
	public UniteSimple(String nom, String codeStructure, int fonction)
	{
		nom_ = nom;
		code_ = fonction;
		codeStructure_ = codeStructure;
	}
	
	public int getBesoindir()
	{
		return code_ > Consts.CODE_CHEFS_PIOK ? 0 : 1;
	}

	public void setCode(int code) {
		code_ = Math.min(code_,(code/10)*10);
	}
	
	public boolean getNonPrincipal()
	{
		return (getCodestructure().compareTo(getCodegroupe()) != 0);
	}
	
	public String getNom()
	{
		return nom_;
	}
	
	public int getCode()
	{
		return code_;
	}
	
	public String getCodestructure()
	{
		return codeStructure_;
	}
	
	public String getCodegroupe()
	{
		String codeGroupe = codeStructure_.substring(0, codeStructure_.length()-2);
		codeGroupe+="00";
		return codeGroupe;
	}
	
	public String getCodebranche()
	{
		String codeBranche = codeStructure_.substring(codeStructure_.length()-2, codeStructure_.length()-1);
		return codeBranche;
	}

	public int compareTo(String o) {
		return this.nom_.compareTo(o);
	}

	@Override
	public int compareTo(Unite o) {
		return this.nom_.compareTo(o.nom_);
	}
	
	@Override
	public String toString() {
		return nom_;
	}

}
