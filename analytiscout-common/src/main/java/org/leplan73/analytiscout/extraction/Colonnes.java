package org.leplan73.analytiscout.extraction;

import java.util.Map;
import java.util.TreeMap;

public class Colonnes {

	static public String Individu_CourrielPersonnel = "Individu.CourrielPersonnel";
	static public String Individu_TelephonePortable1 = "Individu.TelephonePortable1";
	static public String Inscription_Delegations = "Inscription.Delegations";
	static public String Fonction_Code = "Fonction.Code";
	static public String Individu_DateNaissance = "Individu.DateNaissance";
	static public String Structure_CodeStructure = "Structure.CodeStructure";
	static public String Structure_Nom = "Structure.Nom";
	static public String Structure_Type = "Inscriptions.Type";
	static public String Individu_CodeAdherent = "Individu.CodeAdherent";
	static public String Individu_Nom = "Individu.Nom";
	static public String Individu_Prenom = "Individu.Prenom";
	static public String Individu_DroitImage = "Individu.DroitImage";
	static public String Mere_Nom = "Mere.Nom";
	static public String Mere_Prenom = "Mere.Prenom";
	static public String Pere_Nom = "Pere.Nom";
	static public String Pere_Prenom = "Pere.Prenom";
	static public String Pere_CourrielPersonnel = "Pere.CourrielPersonnel";
	static public String Mere_CourrielPersonnel = "Mere.CourrielPersonnel";
	static public String Pere_TelephonePortable1 = "Pere.TelephonePortable1";
	static public String Mere_TelephonePortable1 = "Mere.TelephonePortable1";
	
	private Map<Integer,String> map_ = new TreeMap<Integer,String>();

	public void add(int id, String nom) {
		map_.put(id,nom);
	}
	
	public String get(int id) {
		return map_.get(id);
	}
}
