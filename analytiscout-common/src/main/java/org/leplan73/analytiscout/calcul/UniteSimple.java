package org.leplan73.analytiscout.calcul;

import java.util.Map;

public class UniteSimple implements Comparable<UniteSimple> {
	protected String nomcomplet_;
	protected String nom_;
	protected int codeFonction_=999;
	protected String codeStructure_;
	protected Groupe groupe_;
	protected int montees_;
	
	public UniteSimple(String nomcomplet)
	{
		changeNomcomplet(nomcomplet);
	}
	
	public UniteSimple(String nom, String codeStructure, int fonction)
	{
		nom_ = nom;
		codeFonction_ = fonction;
		codeStructure_ = codeStructure;
		groupe_ = new Groupe(codeStructure);
	}
	
	private void changeNomcomplet(String nomcomplet)
	{
		if (nomcomplet.isEmpty() == false) {
			nomcomplet_ = nomcomplet;
			nom_ = nomcomplet.substring(nomcomplet.indexOf(" - ")+3);
			codeStructure_ = nomcomplet.substring(0,nomcomplet.indexOf(" - "));
			groupe_ = new Groupe(nomcomplet);
		}
	}

	public void anonymiserStructure(Map<String, String> tableDeTraductionNoms, Map<String, String> tableDeTraductionCode, int codeStructure) {
		changeNomcomplet(codeStructure + " - " + tableDeTraductionNoms.getOrDefault(nom_, nom_));
	}
	
	public void ajouterMontees() {
		montees_++;
	}
	
	public int getMontees() {
		return montees_;
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
	
	public int getTribranche()
	{
		String branche = this.getBranche();
		if (branche.compareTo("F") == 0)
			return 0;
		if (branche.compareTo("LJ") == 0)
			return 1;
		if (branche.compareTo("SG") == 0)
			return 2;
		if (branche.compareTo("PC") == 0)
			return 3;
		if (branche.compareTo("C") == 0)
			return 4;
		if (branche.compareTo("VDL") == 0)
			return 5;
		return 999;
	}
	
	public String getBranche()
	{
		String unite = this.getCodebranche();
		if (unite.compareTo("7") == 0)
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
		return "R";
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
