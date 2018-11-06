package org.leplan73.outilssgdf.extraction;

public class Unite implements Comparable<Unite>
{
	private String nom_;
	private int jeunes_;
	private int chefs_;
	private int fonction_;
	
	private int apf_;
	private int tech_;
	private int appro_;
	private int psc1_;
	private int afps_;
	private int bafa_;
	private int bafd_;
	private int animsf_;
	private int dirsf_;
	private int cham_;
	private int staf_;
	private int buchettes_;
	
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
	
	public int getFonction()
	{
		return fonction_;
	}
	
	public void ajouter(int jeune, int chef)
	{
		jeunes_+=jeune;
		chefs_+=chef;
	}
	
	public Unite(String nom, int fonction)
	{
		nom_ = nom;
		fonction_ = fonction;
	}
	
	public String getNom()
	{
		return nom_;
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

	public void addAppro(boolean dir) {
		this.appro_++;
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

	public void addAnimsf() {
		this.animsf_++;
	}

	public void addQualifAnnee() {
		this.qualifs_++;
	}

	public int getDirsf() {
		return dirsf_;
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

	public void setFonction(int fonction) {
		fonction_ = fonction;
	}
}
