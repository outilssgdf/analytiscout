package org.leplan73.outilssgdf.registrepresence;

import java.text.SimpleDateFormat;

import org.leplan73.outilssgdf.calcul.Groupe;
import org.leplan73.outilssgdf.calcul.UniteSimple;

public class RegistrePresenceActiviteHeure {
	private String type_;
	private UniteSimple unite_;
	private boolean chef_;
	private int mois_;
	private int annee_;
	private int moisactivite_;
	private long dureeReel_ = 1;
	private long dureeForfaitaire_ = 1;
	private String description_;
	private String nomPersonne_;

	final static SimpleDateFormat parser_ = new SimpleDateFormat("EEEEE dd/MM/yyyy HH:mm");
	
	static final int moisactivites_[]= {5,6,7,8,9,10,11,12,1,2,3,4};

	public RegistrePresenceActiviteHeure(UniteSimple unite, String structure, Groupe groupe, String type, String nomPersonne, boolean chef, int mois, int annee, long dureeReel, long dureeForfaitaire, String description) {
		unite_ = unite;
		type_ = type;
		chef_ = chef;
		mois_ = mois;
		annee_ = annee;
		moisactivite_ = moisactivites_[mois-1];
		dureeReel_ = dureeReel;
		dureeForfaitaire_ = dureeForfaitaire;
		description_ = description;
		nomPersonne_ = nomPersonne;
	}

	public String getDescription() {
		return description_;
	}

	public String getType() {
		return type_;
	}
	
	public UniteSimple getUnite() {
		return unite_;
	}
	
	public String getNom() {
		return nomPersonne_;
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
	
	public long getDureereel() {
		return dureeReel_;
	}
	
	public long getDureeforfaitaire() {
		return dureeForfaitaire_;
	}
}
