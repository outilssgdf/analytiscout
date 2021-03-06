package org.leplan73.analytiscout.calcul;

import org.leplan73.analytiscout.Consts;

public class Unite extends UniteSimple
{
	private int jeunes_;
	private int chefs_;
	
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
	private int responsable_farfadets_;
	
	public Unite(String nom, String codeStructure, int fonction) {
		super(nom, codeStructure, fonction);
	}
	
	public boolean getAvecdesjeunes()
	{
		return jeunes_ > 0;
	}
	
	public int getBesoindir()
	{
		return codeFonction_ > Consts.CODE_CHEFS_PIOK ? 0 : 1;
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
	
	public void ajouter(boolean jeune, boolean chef)
	{
		jeunes_+=jeune ? 1 : 0;
		chefs_+=chef ? 1 : 0;
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

	public int getApf() {
		return apf_;
	}
	
	public int getResponsablefarfadets() {
		return responsable_farfadets_;
	}
	
	public void addResponsablefarfadets() {
		this.responsable_farfadets_++;
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
}
