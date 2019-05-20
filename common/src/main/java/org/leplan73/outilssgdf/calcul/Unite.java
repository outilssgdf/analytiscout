package org.leplan73.outilssgdf.calcul;

import org.leplan73.outilssgdf.Consts;

public class Unite implements Comparable<Unite>
{
	private String nom_;
	private int jeunes_;
	private int chefs_;
	private int code_=999;
	private String codeStructure_;
	
	private int apf_;
	private int tech_;
	private int appro_;
	private int appro_accueil_;
	private int appro_anim_;
	private int psc1_;
	private int afps_;
	private int bafa_;
	private int bafd_;
	private int animsf_;
	private int dirsfqnonq_;
	private int dirsf_;
	private int cham_;
	private int staf_;
	private int buchettes_;
	private int module_appro_accueil_scoutisme_;
	private int module_animateur_scoutisme_campisme_;
	private int module_appro_surveillant_baignade_;
	
	public boolean getAvecdesjeunes()
	{
		return jeunes_ > 0;
	}
	
	public double getRatio()
	{
		return (double)chefs_ / (double)jeunes_;
	}
	
	public int getJeunes()
	{
		return jeunes_;
	}
	
	public int getChefs()
	{
		return chefs_;
	}
	
	public int getCode()
	{
		return code_;
	}
	
	public void ajouter(int jeune, int chef)
	{
		jeunes_+=jeune;
		chefs_+=chef;
	}
	
	public Unite(String nom, String codeStructure, int fonction)
	{
		nom_ = nom;
		code_ = fonction;
		codeStructure_ = codeStructure;
	}
	
	public boolean getNonPrincipal()
	{
		return (getCodestructure().compareTo(getCodegroupe()) != 0);
	}
	
	public String getNom()
	{
		return nom_;
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
	
	public int getBesoindir()
	{
		return code_ > Consts.CODE_CHEFS_PIOK ? 0 : 1;
	}

	private int qualifs_;
	public int getQualifieannee()
	{
		return qualifs_;
	}

	private int autresCamp_;
	public int getAutrescamp()
	{
		return autresCamp_;
	}

	private int stagiaires_;
	public int getStagiairesf()
	{
		return stagiaires_;
	}
	
	public int getToutsf()
	{
		return qualifs_+stagiaires_;
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

	public int getApf() {
		return apf_;
	}

	public void addStagiaire() {
		stagiaires_++;
	}

	public void addApf() {
		this.apf_++;
	}

	public int getTech() {
		return tech_;
	}

	public void addTech() {
		this.tech_++;
	}

	public int getAppro() {
		return appro_;
	}

	public int getModule_appro_accueil_scoutisme() {
		return module_appro_accueil_scoutisme_;
	}

	public int getModule_animateur_scoutisme_campisme() {
		return module_animateur_scoutisme_campisme_;
	}

	public int getModule_appro_surveillant_baignade() {
		return module_appro_surveillant_baignade_;
	}
	
	public void addModuleApproAccueilScoutisme() {
		this.module_appro_accueil_scoutisme_++;
	}
	
	public void addModuleAnimateurScoutismeCampisme() {
		this.module_animateur_scoutisme_campisme_++;
	}
	
	public void addModuleApproSurveillantBaignade() {
		this.module_appro_surveillant_baignade_++;
	}

	public void addApproAccueil() {
		this.appro_accueil_++;
	}

	public int getAppro_accueil() {
		return appro_accueil_;
	}

	public void addApproAnim() {
		this.appro_anim_++;
	}

	public int getAppro_anim() {
		return appro_anim_;
	}

	public void addAppro() {
		this.appro_++;
	}

	public int getPsc1afps() {
		return psc1_ + afps_;
	}

	public int getPsc1() {
		return psc1_;
	}

	public void addPsc1() {
		this.psc1_++;
	}

	public int getAfps() {
		return afps_;
	}

	public void addAutres() {
		this.autresCamp_++;
	}

	public void addAfps() {
		this.afps_++;
	}

	public int getBafa() {
		return bafa_;
	}

	public void addBafa() {
		this.bafa_++;
	}

	public int getBafd() {
		return bafd_;
	}

	public void addBafd() {
		this.bafd_++;
	}

	public int getQualifiesf() {
		return animsf_;
	}

	public int getAnimsf() {
		return animsf_;
	}

	public void addAnimsf() {
		this.animsf_++;
	}

	public void addQualifannee() {
		this.qualifs_++;
	}

	public int getDirsf() {
		return dirsf_;
	}

	public int getDirsfqnonq() {
		return dirsfqnonq_;
	}

	public void addDirsfsqnonq() {
		this.dirsfqnonq_++;
	}

	public void addDirsf() {
		this.dirsf_++;
	}

	public int getCham() {
		return cham_;
	}

	public void addCham() {
		this.cham_++;
	}

	public int getStaf() {
		return staf_;
	}

	public void addStaf() {
		this.staf_++;
	}

	public int getBuchettes() {
		return buchettes_;
	}

	public void addBuchettes() {
		this.buchettes_++;
	}

	public void setCode(int code) {
		code_ = Math.min(code_,(code/10)*10);
	}
}
