package org.leplan73.outilssgdf.extraction;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Chef extends Adherent {

	private static final String QUALIFICATIONS_EST_TITULAIRE = "Qualifications.EstTitulaire";
	
	static public class ChefExtra
	{
		public String nom_;
		public Chef dir_;
		public Colonnes colonnes2_;
		
		public ChefExtra(String nom, Chef dir, Colonnes colonnes)
		{
			if (nom.contains("appro") == true)
			{
				nom_ = "appro";
			}
			else
				nom_ = nom;
			dir_ = dir;
			colonnes2_ = colonnes;
		}
		
		private String get(String nom)
		{
			return dir_ != null ? dir_.get(colonnes2_.get(nom)) : "";
		}
		
		@Override
		public String toString()
		{
			return nom_;
		}
	};
	
	public Chef(Adherent adherent) {
		super(adherent.colonnes_);
		
		data_ = adherent.data_;
		code_ = adherent.code_;
		codePapa_ = adherent.codePapa_;
		codeMaman_ = adherent.codeMaman_;
		unite_ = adherent.unite_;
		colonnes_ = adherent.colonnes_;
		age_ = adherent.age_;
		groupe_ = adherent.groupe_;
	}
	
	private Map<String, Qualification> qualifs_ = new TreeMap<String, Qualification>();
	public class Qualification
	{
		private boolean titulaire_;
		private boolean defini_;
		
		public Qualification(ChefExtra extra) {
			String titulaire = extra.get(QUALIFICATIONS_EST_TITULAIRE);
			titulaire_ = (titulaire != null && titulaire.compareTo("Oui") == 0);
			defini_ = true;
		}

		public Qualification() {
		}

		public boolean getOk()
		{
			return defini_;
		}
		
		public String getEsttitulaire()
		{
			return titulaire_ ? "Oui" : "Non";
		}
	}
	
	private Map<String, Formation> formations_ = new TreeMap<String, Formation>();
	public class Formation
	{
		private boolean titulaire_;
		
		public Formation()
		{
		}
		
		public Formation(ChefExtra extra) {
			String titulaire = extra.get(QUALIFICATIONS_EST_TITULAIRE);
			titulaire_ = (titulaire != null && titulaire.compareTo("Oui") == 0);
		}

		public boolean getOk()
		{
			return titulaire_;
		}
		
		public String getEsttitulaire()
		{
			return titulaire_ ? "Oui" : "Non";
		}
	}
	
	private Map<String, Diplome> diplomes_ = new TreeMap<String, Diplome>();
	public class Diplome
	{
		private boolean titulaire_;
		
		public Diplome(ChefExtra extra) {
			String titulaire = extra.get(QUALIFICATIONS_EST_TITULAIRE);
			titulaire_ = (titulaire != null && titulaire.compareTo("Oui") == 0);
		}

		public Diplome() {
		}

		public boolean getOk()
		{
			return titulaire_;
		}
		
		public String getEsttitulaire()
		{
			return titulaire_ ? "Oui" : "Non";
		}
	}
	
	private Map<String, ChefExtra> extras_ = new TreeMap<String, ChefExtra>();

	public void addExtras(List<ChefExtra> extras)
	{
		extras.forEach(extra ->
		{
			extras_.put(extra.nom_, extra);
			qualifs_.put(extra.nom_, new Qualification(extra));
			formations_.put(extra.nom_, new Formation(extra));
			diplomes_.put(extra.nom_, new Diplome(extra));
		});
	}
	
	public Qualification getQualif(String nom)
	{
		Qualification f = qualifs_.get(nom);
		if (f != null)
		{
			return f;
		}
		return new Qualification();
	}
	
	public Formation getFormation(String nom)
	{
		Formation f = formations_.get(nom);
		if (f != null)
		{
			return f;
		}
		return new Formation();
	}
	
	public Diplome getDiplome(String nom)
	{
		Diplome f = diplomes_.get(nom);
		if (f != null)
		{
			return f;
		}
		return new Diplome();
	}
	
	public int getFormation_apf()
	{
		return getFormations("afp");
	}
	
	public int getFormation_tech()
	{
		return getFormations("tech");
	}
	
	public int getFormation_appro()
	{
		return getFormations("appro_anim") + getFormations("appro_accueil") + getFormations("appro");
	}
	
	public boolean getFormation_appro_test()
	{
		return (getFormations("appro_anim") + getFormations("appro_accueil") + getFormations("appro")) > 0;
	}
	
	public int getQualif_animsf()
	{
		return getFormations("animsf");
	}
	
	public int getQualif_dirsf()
	{
		return getFormations("dirsf");
	}
	
	public int getDiplome_psc1()
	{
		return getFormations("psc1");
	}
	
	public int getDiplome_afps()
	{
		return getFormations("afps");
	}
	
	public int getDiplome_bafa()
	{
		return getFormations("bafa");
	}
	
	public int getDiplome_bafd()
	{
		return getFormations("bafd");
	}
	
	public int getFormation_staf()
	{
		return getFormations("staf");
	}
	
	public int getFormation_cham()
	{
		return getFormations("cham");
	}
	
	public int getDiplome_buchettes()
	{
		return getFormations("2buchettes") + getFormations("3buchettes") + getFormations("4buchettes");
	}
	
	public int getDiplome_buchettes2()
	{
		return getFormations("2buchettes");
	}
	
	public int getDiplome_buchettes3()
	{
		return getFormations("3buchettes");
	}
	
	public int getDiplome_buchettes4()
	{
		return getFormations("4buchettes");
	}
	
	private int getFormations(String nom)
	{
		ChefExtra extra = extras_.get(nom);
		if (extra != null)
		{
			String apf = extra.get(QUALIFICATIONS_EST_TITULAIRE);
			return apf.isEmpty() ? 0 : 1;
		}
		return 0;
	}

}
