package org.leplan73.outilssgdf.extraction;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AdherentForme extends Adherent {

	private static final String QUALIF_EST_TITULAIRE = "Qualifications.EstTitulaire";
	private static final String QUALIF_FIN_VALIDITE = "Qualifications.DateFinValidite";

	private static final String FORMATION_DATEFIN = "Formations.DateFin";
	private static final String DIPLOME_DATE_OBTENTION = "Diplomes.DateObtention";
	
	static public class ChefExtra
	{
		public String nom_;
		public AdherentForme dir_;
		public Colonnes colonnes2_;
		public boolean isQualif_;
		
		public ChefExtra(String nom, AdherentForme dir, Colonnes colonnes)
		{
			// Noms similaires
			if (nom.contains("appro") == true)
			{
				nom_ = "appro";
			}
			else
				nom_ = nom;
			if (nom.contains("apf") == true)
			{
				nom_ = "apf";
			}
			else
				nom_ = nom;

			// Détections
			if (nom.compareTo("dirsf") == 0)
				isQualif_ = true;
			if (nom.compareTo("animsf") == 0)
				isQualif_ = true;
			
			dir_ = dir;
			colonnes2_ = colonnes;
		}
		
		private String get(String nom)
		{
			return dir_ != null ? colonnes2_.get(nom) != null ? dir_.get(colonnes2_.get(nom)) : "" : "";
		}
		
		@Override
		public String toString()
		{
			return nom_;
		}
	};
	
	public AdherentForme(Adherent adherent) {
		super(adherent.colonnes_);
		
		data_ = adherent.data_;
		code_ = adherent.code_;
		codePapa_ = adherent.codePapa_;
		codeMaman_ = adherent.codeMaman_;
		unite_ = adherent.unite_;
		colonnes_ = adherent.colonnes_;
		age_ = adherent.age_;
	}
	
	private Map<String, Qualification> qualifs_ = new TreeMap<String, Qualification>();
	public class Qualification
	{
		private boolean titulaire_;
		private boolean defini_;
		private String fin_validite_;
		private String nom_;
		
		public Qualification(ChefExtra extra) {
			fin_validite_ = extra.get(QUALIF_FIN_VALIDITE) != null ? extra.get(QUALIF_FIN_VALIDITE).isEmpty() ? "Pas de date" : extra.get(QUALIF_FIN_VALIDITE) : "Pas de date";
			String titulaire = extra.get(QUALIF_EST_TITULAIRE);
			titulaire_ = (titulaire != null && titulaire.compareTo("Oui") == 0);
			defini_ = true;
			nom_ = extra.nom_;
		}

		public Qualification() {
			fin_validite_ = "";
		}

		public boolean getOk()
		{
			return defini_ && titulaire_;
		}

		public boolean getDefini()
		{
			return defini_;
		}

		public boolean getTitulaire()
		{
			return titulaire_;
		}
		
		public String getFinvalidite()
		{
			return fin_validite_;
		}
		
		public String getEsttitulaire()
		{
			return titulaire_ ? "Oui" : "Non";
		}
		
		@Override
		public String toString()
		{
			return "nom="+nom_+", titulaire="+titulaire_+", fin_validite="+fin_validite_;
		}
	}
	
	private Map<String, Formation> formations_ = new TreeMap<String, Formation>();
	public class Formation
	{
		private boolean titulaire_;
		private boolean defini_;
		private String datefin_;
		private String nom_;
		
		public Formation()
		{
		}
		
		public Formation(ChefExtra extra) {
			titulaire_ = true;
			datefin_ = extra.get(FORMATION_DATEFIN);
			nom_ = extra.nom_;
			defini_ = true;
		}

		public boolean getOk()
		{
			return titulaire_;
		}

		public boolean getDefini()
		{
			return defini_;
		}
		
		public String getDatefin()
		{
			return datefin_;
		}
		
		public String getEsttitulaire()
		{
			return titulaire_ ? "Oui" : "Non";
		}
		
		@Override
		public String toString()
		{
			return "nom="+nom_+", titulaire="+titulaire_+", datefin="+datefin_;
		}
	}
	
	private Map<String, Diplome> diplomes_ = new TreeMap<String, Diplome>();
	public class Diplome
	{
		private boolean titulaire_;
		private boolean defini_;
		private String dateobtention_;
		private String nom_;
		
		public Diplome(ChefExtra extra) {
			titulaire_ = true;
			dateobtention_ = extra.get(DIPLOME_DATE_OBTENTION);
			nom_ = extra.nom_;
			defini_ = true;
		}

		public Diplome() {
		}

		public boolean getOk()
		{
			return titulaire_;
		}

		public boolean getDefini()
		{
			return defini_;
		}
		
		public String getDateobtention()
		{
			return dateobtention_;
		}
		
		public String getEsttitulaire()
		{
			return titulaire_ ? "Oui" : "Non";
		}
		
		@Override
		public String toString()
		{
			return "nom="+nom_+", titulaire="+titulaire_+", dateobtention="+dateobtention_;
		}
	}
	
	private Map<String, ChefExtra> extras_ = new TreeMap<String, ChefExtra>();

	public void addExtras(List<ChefExtra> extras)
	{
		extras.forEach(extra ->
		{
			extras_.put(extra.nom_, extra);
			
			if (extra.isQualif_)
			{
				Qualification q = qualifs_.get(extra.nom_);
				if (q == null)
				{
					qualifs_.put(extra.nom_, new Qualification(extra));
				}
				else
					if (q.getTitulaire() == false)
					{
						qualifs_.put(extra.nom_, new Qualification(extra));
					}
			}
			formations_.put(extra.nom_, new Formation(extra));
			diplomes_.put(extra.nom_, new Diplome(extra));
		});
	}
	
	@Override
	public String toString()
	{
		return getPrenom() + " - " + getNom();
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
			String apf = extra.get(QUALIF_EST_TITULAIRE);
			return apf.isEmpty() ? 0 : 1;
		}
		return 0;
	}
}
