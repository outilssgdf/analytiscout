package org.leplan73.outilssgdf.extraction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.Params;

public class AdherentForme extends Adherent {

	private static final String QUALIF_EST_TITULAIRE = "Qualifications.EstTitulaire";
	private static final String QUALIF_FIN_VALIDITE = "Qualifications.DateFinValidite";

	private static final String FORMATION_DATEFIN = "Formations.DateFin";
	private static final String FORMATION_ROLE = "Formations.Role";
	private static final String DIPLOME_DATE_OBTENTION = "Diplomes.DateObtention";
	
	private static final String PAS_DE_DATE = "Permanent";
	
	public AdherentForme(Adherent adherent) {
		super(adherent.colonnes_);
		
		data_ = adherent.data_;
		code_ = adherent.code_;
		codePapa_ = adherent.codePapa_;
		codeMaman_ = adherent.codeMaman_;
		unite_ = adherent.unite_;
		colonnes_ = adherent.colonnes_;
		age_ = adherent.age_;
		ageCamp_ = adherent.age_;
	}
	
	@Override
	public String getAgeok()
	{
		if (ageCamp_ > 0)
		{
			if (this.getFonction() < Consts.CODE_CHEFS_PIOK)
			{
				if (ageCamp_ < 19)
					return "Non";
				else
					return "Oui";
			}
			else
			{
				if (ageCamp_ < 21)
					return "Non";
				else
					return "Oui";
			}
		}
		return "";
	}
	
	public boolean getBafapotentiel()
	{
		if (getFormation_appro() == 0)
		{
			Formation f = getFormationNull("tech");
			if (f != null)
			{
				long fin = f.getDatefin().getTime() + 0;
				long diffFindDec = (new Date().getTime()-fin)/1000;
				diffFindDec/=(24*3600);
				return (diffFindDec < (365+365+365+183));
			}
			f = getFormationNull("bafa_general");
			if (f != null)
			{
				long fin = f.getDatefin().getTime() + 0;
				long diffFindDec = (new Date().getTime()-fin)/1000;
				diffFindDec/=(24*3600);
				return (diffFindDec < (365+365+365+183));
			}
		}
		return false;
	}
	
	private Map<String, Qualification> qualifs_ = new TreeMap<String, Qualification>();
	private Map<String, Formation> formations_ = new TreeMap<String, Formation>();
	private Map<String, ChefExtra> extras_ = new TreeMap<String, ChefExtra>();
	
	static public class ExtraKey implements Comparable<ExtraKey>
	{
		public String fichier_;
		public String nom_;
		public String type_;
		
		public ExtraKey(String fichier, String nom, String type)
		{
			fichier_ = fichier;
			nom_ = nom;
			type_ = type;
		}

		public boolean ifTout()
		{
			return (type_.compareTo("tout") == 0);
		}
		
		public boolean ifFormation()
		{
			return (type_.compareTo("formation") == 0);
		}
		
		public boolean ifQualif()
		{
			return (type_.compareTo("qualif") == 0);
		}
		
		public boolean ifDiplome()
		{
			return (type_.compareTo("diplome") == 0);
		}

		@Override
		public int compareTo(ExtraKey o) {
			return (type_+"|"+nom_).compareTo(o.type_+"|"+o.nom_);
		}
	}
	
	static public class ChefExtra
	{
		public String nom_;
		public AdherentForme dir_;
		public ColonnesAdherents colonnes2_;
		public boolean isQualif_;
		public boolean isFormation_;
		public boolean isDiplome_;
		
		public ChefExtra(ExtraKey key, AdherentForme dir, ColonnesAdherents colonnes)
		{
			nom_ = key.nom_;

			// Détections
			isQualif_ = key.ifQualif();
			isFormation_ = key.ifFormation();
			isDiplome_ = key.ifDiplome();
			
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
		
		public void complete()
		{
			dir_.complete();
		}
	};
	
	public class Qualification
	{
		private boolean titulaire_;
		private boolean estetaetetitulaire_;
		private boolean defini_;
		private String fin_validite_;
		private String nom_;
		
		public Qualification(ChefExtra extra) {
			fin_validite_ = extra.get(QUALIF_FIN_VALIDITE) != null ? extra.get(QUALIF_FIN_VALIDITE).isEmpty() ? PAS_DE_DATE : extra.get(QUALIF_FIN_VALIDITE) : PAS_DE_DATE;
			String titulaire = extra.get(QUALIF_EST_TITULAIRE);
			titulaire_ = (titulaire != null && titulaire.compareTo("Oui") == 0);
			estetaetetitulaire_ = titulaire_;
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

		public boolean getPasok()
		{
			return defini_ && !titulaire_;
		}

		public boolean getDefini()
		{
			return defini_;
		}

		public boolean getTitulaire()
		{
			return titulaire_;
		}

		public boolean getAetaetetitulaire()
		{
			return estetaetetitulaire_;
		}
		
		public Object getFinvalidite()
		{
			if (fin_validite_.compareTo(PAS_DE_DATE) != 0)
			{
				try
				{
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					Date date = df.parse(fin_validite_);
					return date;
				} catch (ParseException e) {
				}
			}
			else
			{
				return PAS_DE_DATE;
			}
			return null;
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
		
		public void complete()
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try
			{
				if (fin_validite_.compareTo(PAS_DE_DATE) != 0)
				{
					Date date = df.parse(fin_validite_);
					if (date.before(Params.getDateDebutCamp()))
					{
						defini_ = false;
						titulaire_ = false;
					}
				}
			} catch (ParseException e) {
			}
		}
	}
	
	public class Formation
	{
		private boolean titulaire_;
		private boolean defini_;
		private String datefin_;
		private Date date_fin_validite_;
		private String nom_;
		
		public Formation()
		{
		}
		
		public Formation(ChefExtra extra) {
			titulaire_ = true;
			datefin_ = extra.get(FORMATION_DATEFIN);
			nom_ = extra.nom_;
			defini_ = true;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				if (datefin_ != null)
					date_fin_validite_ = simpleDateFormat.parse(extra.get(FORMATION_DATEFIN));
				} catch (ParseException e) {
			}
		}

		public boolean getOk()
		{
			return titulaire_;
		}

		public boolean getDefini()
		{
			return defini_;
		}
		
		public Date getDatefin()
		{
			return date_fin_validite_;
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
		
		public void complete()
		{
		}
	}
	
	private Map<String, Diplome> diplomes_ = new TreeMap<String, Diplome>();
	public class Diplome
	{
		private boolean titulaire_;
		private boolean defini_;
		private Date dateobtention_;
		private String nom_;
		
		public Diplome(ChefExtra extra) {
			titulaire_ = true;
			String dateobtention = extra.get(DIPLOME_DATE_OBTENTION);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				if (dateobtention != null)
					dateobtention_ = simpleDateFormat.parse(dateobtention);
				} catch (ParseException e) {
			}
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
		
		public Date getDateobtention()
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
		
		public void complete()
		{
		}
	}

	public void addExtras(List<ChefExtra> extras)
	{
		extras.forEach(extra ->
		{
			boolean ajouter = true; 
			if (extra.isQualif_)
			{
				extras_.put(extra.nom_, extra);
				Qualification q = qualifs_.get(extra.nom_);
				if (q == null)
				{
					qualifs_.put(extra.nom_, q = new Qualification(extra));
				}
				else
					if (q.getTitulaire() == false)
					{
						qualifs_.put(extra.nom_, q = new Qualification(extra));
					}
				q.complete();
			}
			else
			{
				if (extra.isFormation_)
				{
					String role = extra.get(FORMATION_ROLE);
					if (role.isEmpty() || role.compareTo("0") == 0)
					{
						extras_.put(extra.nom_, extra);
					}
					else
						ajouter = false;
				}
				else
					extras_.put(extra.nom_, extra);
			}
			if (ajouter)
			{
				Formation f = null;
				formations_.put(extra.nom_, f = new Formation(extra));
				f.complete();
				Diplome d = null;
				diplomes_.put(extra.nom_, d = new Diplome(extra));
				d.complete();
			}
		});
	}
	
	public void complete() {
		qualifs_.forEach((k,v) ->
		{
			v.complete();
		});
		diplomes_.forEach((k,v) ->
		{
			v.complete();
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
	
	private Formation getFormationNull(String nom)
	{
		Formation f = formations_.get(nom);
		if (f != null)
		{
			return f;
		}
		return null;
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
	
	public int getQualiftitulaire()
	{
		return (getQualif("dirsf").getOk()||getQualif("animsf").getOk()) ? 1 : 0;
	}
	
	public int getQualifs()
	{
		boolean qualifs = getQualif("dirsf").getOk() || getQualif("dirsf").getPasok()||getQualif("animsf").getOk()||getQualif("animsf").getPasok();
		return qualifs ? 1 : 0;
	}
	
	public int getQualif_animsf()
	{
		return getQualifs("animsf");
	}
	
	public int getQualif_dirsf()
	{
		return getQualifs("dirsf");
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
			String fin = extra.get(FORMATION_DATEFIN);
			return fin.isEmpty() ? 0 : 1;
		}
		return 0;
	}
	
	private int getQualifs(String nom)
	{
		ChefExtra extra = extras_.get(nom);
		if (extra != null)
		{
			String fin = extra.get(QUALIF_FIN_VALIDITE);
			return fin.isEmpty() ? 0 : 1;
		}
		return 0;
	}
}
