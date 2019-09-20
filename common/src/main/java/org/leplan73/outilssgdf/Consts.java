package org.leplan73.outilssgdf;

public class Consts {

	public static final String STRUCTURE_NATIONAL = "000000000";
	public static final String ENCODING_WINDOWS = "Windows-1252";
	public static final String ENCODING_UTF8 = "UTF-8";
	
	public static final String FICHIER_CONF_EXPORT = "generateur.properties";
	
	static final public int PARENT_PERE = 1;
	static final public int PARENT_MERE = 2;
	static final public int PARENT_PEREMERE = PARENT_PERE|PARENT_MERE;

	static final public int CODE_LJ = 110;
	static final public int CODE_CHEFS_LJ = 210;
	static final public int CODE_SG = 120;
	static final public int CODE_CHEFS_SG = 220;
	static final public int CODE_PIOK = 130;
	static final public int CODE_FARFADETS = 170;
	static final public int CODE_CHEFS_PIOK = 230;
	static final public int CODE_COMPAS = 240;
	static final public int CODE_RESPONSABLES = 180;
	static final public int CODE_RESPONSABLE_FARFADETS = 270;
	static final public int CODE_PARENTS_FARFADETS = 271;
	static final public int CODE_VIOLETS = 300;
	static final public int CODE_COMPAS_T1T2 = 140;
	static final public int CODE_COMPAS_T3 = 141;
	
	static final public String GROUPE_PARENTS = "Parents";
	
	static final public String DATE_DEBUT_CAMP = "01/07/2019";
	static final public String DATE_LIMITE_JEUNE = "31/12/2018";
	
	public static final String FENETRE_PRINCIPALE_X = "fenetre.principale.x";
	public static final String FENETRE_PRINCIPALE_Y = "fenetre.principale.y";
	
	public static final String FENETRE_ANALYSEUR_X = "fenetre.analyseur.x";
	public static final String FENETRE_ANALYSEUR_Y = "fenetre.analyseur.y";
	
	public static final String FENETRE_ANALYSEURENLIGNE_X = "fenetre.analyseurenligne.x";
	public static final String FENETRE_ANALYSEURENLIGNE_Y = "fenetre.analyseurenligne.y";
	
	public static final String FENETRE_EXTRACTEUR_X = "fenetre.extracteur.x";
	public static final String FENETRE_EXTRACTEUR_Y = "fenetre.extracteur.y";
	
	public static final String FENETRE_EXTRACTEURBATCH_X = "fenetre.extracteurbatch.x";
	public static final String FENETRE_EXTRACTEURBATCH_Y = "fenetre.extracteurbatch.y";
	
	public static final String FENETRE_GENERATEUR_X = "fenetre.generateur.x";
	public static final String FENETRE_GENERATEUR_Y = "fenetre.generateur.y";
	
	public static final String INTRANET_IDENTIFIANT = "intranet.identifiant";
	public static final String INTRANET_MOTDEPASSE = "intranet.motdepasse";
	public static final String INTRANET_MEMORISER = "intranet.memoriser";
	public static final String INTRANET_STRUCTURE = "intranet.structure";
	
	public static final String VCARD_CATEGORIE_NOM = "categorie.nom.";
	public static final String VCARD_CATEGORIE_CODE = "categorie.code.";
	public static final String VCARD_CATEGORIE_MEMBRES = "categorie.membres.";
	
	public static final String VCARD_AJOUTER_GROUPE = "categorie.ajouter.groupe";
}
