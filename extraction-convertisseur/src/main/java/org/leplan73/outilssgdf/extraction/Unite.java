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
	
	public String getGroupe()
	{
		return groupe_;
	}

	private int qualifs_;
	public int getQualifieannee()
	{
		return qualifs_;
	}
	
	public boolean getQualifieanneeok()
	{
		return qualifs_ > 0 ? qualifs_ > 0 : false;
	}
	
	public boolean getStagiaireanneeok()
	{
		return stagiaireAnnee_ > 0 ? stagiaireAnnee_ > 0 : false;
	}
	
	public int getStagiaireanneetheorique()
	{
		return 0;
	}
	
	public int getQualifieanneetheorique()
	{
		return 1;
	}
	
	public boolean getDircampok()
	{
		return true;
	}
	
	public boolean getQualifiecampok()
	{
		return qualifs_ > 0 ? qualifs_ > 0 : false;
	}
	
	public boolean getStagiairecampok()
	{
		return stagiaireAnnee_ > 0 ? stagiaireAnnee_ > 0 : false;
	}

	private int stagiaireAnnee_;
	public int getStagiaireannee()
	{
		return stagiaireAnnee_;
	}

	private int autresAnnee_;
	public int getAutresannee()
	{
		return autresAnnee_;
	}

	private int dirs_;
	
	public int getDircamp()
	{
		return dirs_;
	}
	
	public int getQualifiecamp()
	{
		return qualifs_-1;
	}
	
	public int getStagiairecamp()
	{
		return stagiaireAnnee_;
	}
	
	public int getDircamptheorique()
	{
		return 1;
	}
	
	public int getQualifiecamptheorique()
	{
		return 0;
	}
	
	public int getStagiairecamptheorique()
	{
		return 0;
	}
	
	public int getAutrescamp()
	{
		return 0;
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

	private String groupe_;
	public void setGroupe(String groupe)
	{
		groupe_ = groupe;
	}

	public int getApf() {
		return apf_;
	}

	public void addApf() {
		this.apf_++;
		stagiaireAnnee_++;
	}

	public int getTech() {
		return tech_;
	}

	public void addTech() {
		this.tech_++;
		qualifs_++;
	}

	public int getAppro() {
		return appro_;
	}

	public void addAppro(boolean dir) {
		this.appro_++;
		if (dir)
		{
			dirs_++;
		}
		qualifs_++;
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
		this.autresAnnee_++;
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

	public int getAnimsf() {
		return animsf_;
	}

	public void addAnimsf() {
		this.animsf_++;
		qualifs_++;
	}

	public int getDirsf() {
		return dirsf_;
	}

	public void addDirsf() {
		this.dirsf_++;
		dirs_++;
		qualifs_++;
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
