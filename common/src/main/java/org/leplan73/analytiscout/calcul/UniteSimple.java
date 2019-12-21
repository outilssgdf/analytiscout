package org.leplan73.analytiscout.calcul;

public class UniteSimple implements Comparable<UniteSimple> {
	protected String nomcomplet_;
	protected String nom_;
	protected int codeFonction_=999;
	protected String codeStructure_;
	protected Groupe groupe_;
	
	public UniteSimple(String nomcomplet)
	{
		if (nomcomplet.isEmpty() == false) {
			nomcomplet_ = nomcomplet;
			nom_ = nomcomplet.substring(nomcomplet.indexOf(" - ")+3);
			codeStructure_ = nomcomplet.substring(0,nomcomplet.indexOf(" - "));
			groupe_ = new Groupe(nomcomplet);
		}
	}
	
	public UniteSimple(String nom, String codeStructure, int fonction)
	{
		nom_ = nom;
		codeFonction_ = fonction;
		codeStructure_ = codeStructure;
		groupe_ = new Groupe(codeStructure);
	}
	
	public boolean estGroupe()
	{
		return (codeStructure_.compareTo(groupe_.getCode()) == 0);
	}

	public void setCode(int code) {
		codeFonction_ = Math.min(codeFonction_,(code/10)*10);
	}
	
	public boolean getNonPrincipal()
	{
		return (codeStructure_.compareTo(getCodegroupe()) != 0);
	}
	
	public String getNom()
	{
		return nom_;
	}
	
	public Groupe getGroupe()
	{
		return groupe_;
	}

	public String nomcomplet() {
		return nomcomplet_;
	}
	
	public int getCode()
	{
		return codeFonction_;
	}

	public String getCodestructure()
	{
		return codeStructure_;
	}
	
	public String getCodegroupe()
	{
		return groupe_.getCode();
	}
	
	public String getBranche()
	{
		String unite = this.getCodebranche();
		if (unite.compareTo("FARFADET") == 5)
		{
			return "F";
		}
		if (unite.compareTo("1") == 0)
		{
			return "LJ";
		}
		if (unite.compareTo("2") == 0)
		{
			return "SG";
		}
		if (unite.compareTo("3") == 0)
		{
			return "PC";
		}
		if (unite.compareTo("4") == 0)
		{
			return "C";
		}
		return "T";
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
	public int compareTo(UniteSimple o) {
		return this.nom_.compareTo(o.nom_);
	}
	
	@Override
	public String toString() {
		return nom_;
	}
}
