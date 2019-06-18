package org.leplan73.outilssgdf.calcul;

public class UniteSimple implements Comparable<UniteSimple> {
	protected String nomcomplet_;
	protected String nom_;
	protected int code_=999;
	protected String structure_;
	protected String code_groupe_;
	
	public UniteSimple(String nomcomplet)
	{
		nomcomplet_ = nomcomplet;
		nom_ = nomcomplet.substring(nomcomplet.indexOf(" - ")+3);
		structure_ = nomcomplet.substring(0,nomcomplet.indexOf(" - "));
		code_groupe_ = structure_.substring(0, structure_.length()-2);
		code_groupe_+="00";
	}
	
	public UniteSimple(String nom, String codeStructure, int fonction)
	{
		nom_ = nom;
		code_ = fonction;
		structure_ = codeStructure;
		code_groupe_ = structure_.substring(0, structure_.length()-2);
		code_groupe_+="00";
	}
	
	public boolean estGroupe()
	{
		return (structure_.compareTo(code_groupe_) == 0);
	}

	public void setCode(int code) {
		code_ = Math.min(code_,(code/10)*10);
	}
	
	public boolean getNonPrincipal()
	{
		return (getStructure().compareTo(getCodegroupe()) != 0);
	}
	
	public String getNom()
	{
		return nom_;
	}

	public String nomcomplet() {
		return nomcomplet_;
	}
	
	public int getCode()
	{
		return code_;
	}
	
	public String getStructure()
	{
		return structure_;
	}

	public String getCodestructure()
	{
		return structure_;
	}

	public String code_groupe() {
		return code_groupe_;
	}
	
	public String getCodegroupe()
	{
		String codeGroupe = structure_.substring(0, structure_.length()-2);
		codeGroupe+="00";
		return codeGroupe;
	}
	
	public String getCodebranche()
	{
		String codeBranche = structure_.substring(structure_.length()-2, structure_.length()-1);
		return codeBranche;
	}

	public int compareTo(String o) {
		return this.nom_.compareTo(o);
	}

	@Override
	public int compareTo(UniteSimple o) {
		return this.nom_.compareTo(o.nom_);
	}
	
	@Override
	public String toString() {
		return nom_;
	}
}
