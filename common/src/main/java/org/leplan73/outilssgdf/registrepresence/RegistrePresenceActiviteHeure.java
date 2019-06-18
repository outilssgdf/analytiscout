package org.leplan73.outilssgdf.registrepresence;

import java.text.SimpleDateFormat;

public class RegistrePresenceActiviteHeure {
	private String type_;
	private String unite_;
	private String unite_court_;
	private String nom_;
	private String structure_;
	private String groupe_;
	private String code_groupe_;
	private boolean chef_;
	private int mois_;
	private int annee_;
	private int moisactivite_;
	private int duree_ = 1;

	final static SimpleDateFormat parser_ = new SimpleDateFormat("EEEEE dd/MM/yyyy HH:mm");
	
	static final int moisactivites_[]= {5,6,7,8,9,10,11,12,1,2,3,4};

	public RegistrePresenceActiviteHeure(String unite, String unite_court, String structure, String groupe, String code_groupe, String type, String nom, boolean chef, int mois, int annee, int duree) {
		unite_ = unite;
		type_ = type;
		nom_ = nom;
		unite_court_ = unite_court;
		structure_ = structure;
		groupe_ = groupe;
		code_groupe_ = code_groupe;
		chef_ = chef;
		mois_ = mois;
		annee_ = annee;
		moisactivite_ = moisactivites_[mois-1];
		duree_ = duree;
	}

	public String getType() {
		return type_;
	}
	
	public String getUnite() {
		return unite_;
	}
	
	public String getUnitecourt() {
		return unite_court_;
	}
	
	public String getStructure() {
		return structure_;
	}
	
	public String getGroupe() {
		return groupe_;
	}
	
	public String getCodegroupe() {
		return code_groupe_;
	}
	
	public String getNom() {
		return nom_;
	}
	
	public boolean getChef() {
		return chef_;
	}
	
	public int getAnnee() {
		return annee_;
	}
	
	public int getMois() {
		return mois_;
	}
	
	public int getMoisactivite() {
		return moisactivite_;
	}
	
	public int getHeurechef() {
		return chef_ ? 1 : 0;
	}
	
	public int getHeure() {
		return 1;
	}
	
	public int getDuree() {
		return duree_;
	}
}
