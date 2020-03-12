package org.leplan73.analytiscout.extraction;

import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVPrinter;
import org.leplan73.analytiscout.Check;
import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.Params;
import org.leplan73.analytiscout.alerte.Alerte;
import org.leplan73.analytiscout.alerte.Alertes;
import org.leplan73.analytiscout.calcul.Unite;

public class Adherent {

	protected Map<Integer, String> data_ = new Hashtable<Integer, String>();
	
	protected int code_;
	protected String codePapa_;
	protected String codeMaman_;
	protected String unite_;
	protected ColonnesAdherents colonnes_;
	protected double age_ = -1;
	protected double ageFinDec_ = -1;
	protected double ageCamp_ = -1;
	protected Date dn_;
	
	public Adherent(ColonnesAdherents colonnes)
	{
		colonnes_ = colonnes;
	}
	
	public void add(int key, String text)
	{
		data_.put(key, text);
	}
	
	public String get(Integer key) {
		return key != -1 ? data_.get(key) : null;
	}
	
	public void put(Integer key, String v) {
		data_.put(key,v);
	}
	
	@Override
	public int hashCode()
	{
		return code_;
	}
	
	@Override
	public String toString()
	{
		return getPrenom() + " - " + getNom();
	}
	
	public Integer getCode()
	{
		return code_;
	}
	
	public void setCode(int code)
	{
		code_ = code;
	}
	
	public String getCodetext()
	{
		return String.valueOf(code_);
	}
	
	public String getUnite()
	{
		return unite_;
	}
	
	public double getAge()
	{
		return age_;
	}
	
	public double getAgecamp()
	{
		return ageCamp_;
	}
	
	public boolean getAgecalcule()
	{
		return age_ > 0;
	}
	
	public String getDatedenaissance()
	{
		return this.get(colonnes_.getDatedeNaissanceId());
	}

	public void setNom(String nom) {
		this.put(colonnes_.getNomIndividuId(),nom);
		
	}

	public void setPrenom(String prenom) {
		this.put(colonnes_.getPrenomIndividuId(),prenom);
	}
	
	public String getNom()
	{
		return this.get(colonnes_.getNomIndividuId());
	}
	
	public String getPrenom()
	{
		return this.get(colonnes_.getPrenomIndividuId());
	}
	
	public String getEmailPersonnel()
	{
		return this.get(colonnes_.getEmailPersonnelIndividuId());
	}
	
	public String getEmailProfessionnel()
	{
		return this.get(colonnes_.getEmailProfessionelIndividuId());
	}
	
	public boolean getMarin()
	{
		String code = this.get(colonnes_.getFonctionCodeId());
		return code.endsWith("M");
	}
	
	public int getFonction()
	{
		return extraitCodeFonction();
	}
	
	public String getFonctioncomplet()
	{
		return this.get(colonnes_.getFonctionCodeId());
	}
	
	public String getFonctionsecondairecomplet()
	{
		String delegations = this.get(colonnes_.getDelegations());
		if (!delegations.isEmpty())
		{
			String[] parts = delegations.split(";");
			String fonctionSecondaire = parts[0];
			int index = fonctionSecondaire.indexOf(" -");
			if (index != -1)
			{
				return fonctionSecondaire.substring(0, index);
			}
		}
		return "";
	}
	
	public boolean getJeune()
	{
		int fonction = extraitCodeFonction();
		return fonction < Consts.CODE_RESPONSABLES ? true : false;
	}
	
	public boolean getCompa()
	{
		int fonctionSecondaire = extraitCodeFonctionSecondaire();
		if (fonctionSecondaire ==  Consts.CODE_COMPAS_T1T2 || fonctionSecondaire == Consts.CODE_COMPAS_T3)
			return true;
		int fonction = extraitCodeFonction();
		return (fonction ==  Consts.CODE_COMPAS_T1T2 || fonction == Consts.CODE_COMPAS_T3) ? true : false;
	}
	
	public boolean getChef()
	{
		int fonction = extraitCodeFonction();
		return fonction >= Consts.CODE_RESPONSABLES ? true : false;
	}
	
	public String getChamp(String nom)
	{
		return data_.get(colonnes_.get(nom));
	}
	
	public String getStructure()
	{
		return this.get(colonnes_.getStructureNom());
	}
	
	public String getCodestructure()
	{
		return this.get(colonnes_.getStructureCode());
	}
	
	public void setCodestructure(int code)
	{
		this.put(colonnes_.getStructureCode(), String.valueOf(code));
	}
	
	public String getDelegations()
	{
		return this.get(colonnes_.getDelegations());
	}
	
	public String getCodegroupe()
	{
		String codeStructure = this.get(colonnes_.getStructureCode());
		String codeGroupe = codeStructure.substring(0, codeStructure.length()-2);
		codeGroupe+="00";
		return codeGroupe;
	}
	
	public String getCodebranche()
	{
		String codeStructure = this.get(colonnes_.getStructureCode());
		String codeBranche = codeStructure.substring(codeStructure.length()-2, codeStructure.length()-1);
		return codeBranche;
	}
	
	public String getCodePapa()
	{
		return codePapa_;
	}
	
	public void setCodePapa(String codePapa)
	{
		codePapa_ = codePapa;
	}
	
	public String getCodeMaman()
	{
		return codeMaman_;
	}
	
	public void setCodeMaman(String codeMaman)
	{
		codeMaman_ = codeMaman;
	}
	
	private int extraitCodeFonction()
	{
		String code = this.get(colonnes_.getFonctionCodeId());
		char lcode = code.charAt(code.length()-1);
		if (lcode > 'A')
			code = code.substring(0, 3);
		return Integer.valueOf(code);
	}
	
	protected int extraitCodeFonctionSecondaire()
	{
		String code = this.getFonctionsecondairecomplet();
		if (code.isEmpty())
			return 0;
		char lcode = code.charAt(code.length()-1);
		if (lcode > 'A')
			code = code.substring(0, 3);
		return Integer.valueOf(code);
	}
	
	public void init(boolean age)
	{
		String code = this.get(colonnes_.getCodeAdherentId());
		code_ = Integer.valueOf(code);
		unite_ = (String)this.get(colonnes_.getUniteId());
		
		if (age)
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				String date = get(colonnes_.getDatedeNaissanceId());
				if (date != null)
				{
					dn_ = simpleDateFormat.parse(date);
					long aj = Instant.now().toEpochMilli();
					float diff = ((aj - dn_.getTime())/1000);
					diff = diff/(3600*365*24);
					age_ = diff;
					
					Date debutFindDec = Params.getDateLimiteJeune();
					double diffFindDec = ((debutFindDec.getTime() - dn_.getTime())/1000);
					diffFindDec = diffFindDec/(3600*365.25*24);
					ageFinDec_ = diffFindDec;
					
					Date debutCamp = Params.getDateDebutCamp();
					double diffCamp = ((debutCamp.getTime() - dn_.getTime())/1000);
					diffCamp = diffCamp/(3600*365.25*24);
					ageCamp_ = diffCamp;
				}
			} catch (ParseException e) {
			}
		}
	}

	public void anonymiserStructure(Map<String, String> tableDeTraductionNoms, Map<String, String> tableDeTraductionCode) {
		unite_ = tableDeTraductionNoms.getOrDefault(unite_, unite_);
	}
	
	private String calculBranche()
	{
		String unite = this.getUnite();
		if (unite.contains("FARFADET"))
		{
			return "F";
		}
		if (unite.contains("LOUVETEAU") || unite.contains("JEANNETTE") || unite.contains("MOUSSAILLON"))
		{
			return "LJ";
		}
		if (unite.contains("SCOUT") || unite.contains("GUIDE") || unite.contains("MOUSSE"))
		{
			return "SG";
		}
		if (unite.contains("PIONNIER") || unite.contains("CARAVELLE") || unite.contains("MARIN"))
		{
			return "PC";
		}
		if (unite.contains("COMPAGNON"))
		{
			return "C";
		}
		return "R";
	}
	
	public String getBranche()
	{
		String brancheCalcule = calculBranche();
		if (this.getJeune())
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				String date = get(colonnes_.getDatedeNaissanceId());
				Date dn = simpleDateFormat.parse(date);
				Date debutFindDec = Params.getDateLimiteJeune();
				double diffFindDec = ((debutFindDec.getTime() - dn.getTime())/1000);
				diffFindDec = diffFindDec/(3600*365.25*24);
				if (diffFindDec < 8)
				{
					if (brancheCalcule.compareTo("F") != 0)
						return brancheCalcule;
					return "F";
				}
				if (diffFindDec < 11)
				{
					if (brancheCalcule.compareTo("LJ") != 0)
						return brancheCalcule;
					return "LJ";
				}
				if (diffFindDec < 14)
				{
					if (brancheCalcule.compareTo("SG") != 0)
						return brancheCalcule;
					return "SG";
				}
				if (diffFindDec < 17)
				{
					if (brancheCalcule.compareTo("PC") != 0)
						return brancheCalcule;
					return "PC";
				}
				if (diffFindDec < 22)
				{
					if (brancheCalcule.compareTo("C") != 0)
						return brancheCalcule;
					return "C";
				}
			} catch (ParseException e) {
			}
			return "R";
		}
		return brancheCalcule;
	}
	
	public String getBrancheanneeprochaine()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			String date = get(colonnes_.getDatedeNaissanceId());
			Date dn = simpleDateFormat.parse(date);
			Date debutFindDec = Params.getDateLimiteJeune();
			double diffFindDec = ((debutFindDec.getTime() - dn.getTime())/1000);
			diffFindDec = diffFindDec/(3600*365.25*24);
			Date debutFindDecN1 = Params.getDateLimiteJeuneSuivant();
			double diffFindDecN1 = ((debutFindDecN1.getTime() - dn.getTime())/1000);
			diffFindDecN1 = diffFindDecN1/(3600*365.25*24);
					
			if (diffFindDecN1 < 8)
			{
				return "F";
			}
			if (diffFindDecN1 < 11)
			{
				return "LJ";
			}
			if (diffFindDecN1 < 14)
			{
				return "SG";
			}
			if (diffFindDecN1 < 17)
			{
				return "PC";
			}
			if (diffFindDecN1 < 22)
			{
				return "C";
			}
		} catch (ParseException e) {
		}
		return "R";
	}
	
	public String getDroitimageok()
	{
		String droit = this.get(colonnes_.getDroitImage());
		if (!droit.isEmpty())
		{
			return droit;
		}
		return "Oui";
	}
	
	public String getDroitinformationsok()
	{
		String droit = this.get(colonnes_.getDroitInformations());
		if (!droit.isEmpty())
		{
			return droit;
		}
		return "Oui";
	}
	
	public boolean getAgeokb()
	{
		if (ageCamp_ > 0)
		{
			switch (this.getFonction())
			{
				case Consts.CODE_FARFADETS:
					if (ageFinDec_ < 6) return false;
				break;
				case Consts.CODE_LJ:
					if (ageFinDec_ < 8) return false;
				break;
				case Consts.CODE_SG:
					if (ageFinDec_ < 11) return false;
				break;
				case Consts.CODE_PIOK:
					if (ageFinDec_ < 14) return false;
				break;
			}
		}
		return true;
	}
	
	public String getAgeok()
	{
		if (ageCamp_ > 0)
		{
			switch (this.getFonction())
			{
				case Consts.CODE_FARFADETS:
					if (ageFinDec_ < 6) return "Non";
				break;
				case Consts.CODE_LJ:
					if (ageFinDec_ < 8) return "Non";
				break;
				case Consts.CODE_SG:
					if (ageFinDec_ < 11) return "Non";
				break;
				case Consts.CODE_PIOK:
					if (ageFinDec_ < 14) return "Non";
				break;
			}
			return "Oui";
		}
		else
			return "";
	}
	
	public boolean getAgeokcampb()
	{
		if (ageCamp_ > 0)
		{
			switch (this.getFonction())
			{
				case Consts.CODE_FARFADETS:
					if (ageCamp_ < 6) return false;
				break;
				case Consts.CODE_LJ:
					if (ageCamp_ < 8) return false;
				break;
				case Consts.CODE_SG:
					if (ageCamp_ < 11) return false;
				break;
				case Consts.CODE_PIOK:
					if (ageCamp_ < 14) return false;
				break;
			}
		}
		return true;
	}
	
	public String getAgeokcamp()
	{
		if (ageCamp_ > 0)
		{
			switch (this.getFonction())
			{
				case Consts.CODE_FARFADETS:
					if (ageCamp_ < 6) return "Non";
				break;
				case Consts.CODE_LJ:
					if (ageCamp_ < 8) return "Non";
				break;
				case Consts.CODE_SG:
					if (ageCamp_ < 11) return "Non";
				break;
				case Consts.CODE_PIOK:
					if (ageCamp_ < 14) return "Non";
				break;
			}
			return "Oui";
		}
		else
			return "";
	}
	
	public String getAge18ansokcamp()
	{
		if (ageCamp_ > 0)
		{
			if (this.getFonction() < Consts.CODE_ACCOMPAGNATEUR_COMPAS)
			{
				return ageCamp_ >= 18 ? "Oui" : "Non";
			}
			return "Oui";
		}
		else
			return "";
	}
	
	public void check(ColonnesAdherents colonnes, Unite unite, List<Check> checks)
	{
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.getNom()) != 0) return;

		String nomIndividu = (String)this.get(colonnes.getNomIndividuId());
		String prenomIndividu = (String)this.get(colonnes.getPrenomIndividuId());
		
		String mobileIndividu1 = (String)this.get(colonnes.getMobileIndividu1Id());
		String mobileIndividu2 = (String)this.get(colonnes.getMobileIndividu2Id());
		String bureauIndividu = (String)this.get(colonnes.getBureauIndividu());
		String mobilePere = (String)this.get(colonnes.getMobilePereId());
		String mobileMere = (String)this.get(colonnes.getMobileMereId());
		String emailIndividu1 = (String)this.get(colonnes.getEmailPersonnelIndividuId());
		String emailIndividu2 = (String)this.get(colonnes.getEmailProfessionelIndividuId());
		String emailPere = (String)this.get(colonnes.getEmailPereId());
		String emailMere = (String)this.get(colonnes.getEmailMereId());
		
		boolean indiv1Pere = mobilePere != null && mobileIndividu1 != null ? mobileIndividu1.compareTo(mobilePere) == 0 : false;
		boolean indiv1Mere = mobileMere != null && mobileIndividu1 != null ? mobileIndividu1.compareTo(mobileMere) == 0 : false;
		boolean indiv2Pere = mobilePere != null && mobileIndividu2 != null ? mobileIndividu2.compareTo(mobilePere) == 0 : false;
		boolean indiv2Mere = mobileMere != null && mobileIndividu2 != null ? mobileIndividu2.compareTo(mobileMere) == 0 : false;
		boolean indivPere1 = emailPere != null && emailIndividu1 != null ? emailIndividu1.compareToIgnoreCase(emailPere) == 0 : false;
		boolean indivMere1 = emailMere != null && emailIndividu1 != null ? emailIndividu1.compareToIgnoreCase(emailMere) == 0 : false;
		boolean indivPere2 = emailPere != null && emailIndividu2 != null ? emailIndividu2.compareToIgnoreCase(emailPere) == 0 : false;
		boolean indivMere2 = emailMere != null && emailIndividu2 != null ? emailIndividu2.compareToIgnoreCase(emailMere) == 0 : false;
		
		if (indiv1Pere)checks.add(new Check(unite.getNom(), nomIndividu + " : " + prenomIndividu, "mobileIndividu1 == mobilePere"));
		if (indiv1Mere) checks.add(new Check(unite.getNom(), nomIndividu + " : " + prenomIndividu, "mobileIndividu1 == mobileMere"));
		if (indiv2Pere) checks.add(new Check(unite.getNom(), nomIndividu + " : " + prenomIndividu, "mobileIndividu2 == mobilePere"));
		if (indiv2Mere) checks.add(new Check(unite.getNom(), nomIndividu + " : " + prenomIndividu, "mobileIndividu2 == mobileMere"));
		
		if (indivPere1) checks.add(new Check(unite.getNom(), nomIndividu + " : " + prenomIndividu, "emailIndividu1 == emailPere"));
		if (indivMere1) checks.add(new Check(unite.getNom(), nomIndividu + " : " + prenomIndividu, "emailIndividu1 == emailMere"));
		if (indivPere2) checks.add(new Check(unite.getNom(), nomIndividu + " : " + prenomIndividu, "emailIndividu2 == emailPere"));
		if (indivMere2) checks.add(new Check(unite.getNom(), nomIndividu + " : " + prenomIndividu, "emailIndividu2 == emailMere"));
	}
	
	public boolean listeChef(ColonnesAdherents colonnes, Unite unite, PrintStream os) throws IOException {
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.getNom()) != 0) return false;
		
		int code = this.getFonction();
		if (code >= Consts.CODE_RESPONSABLES)
		{
			String nomIndividu = (String)this.get(colonnes.getNomIndividuId());
			String prenomIndividu = (String)this.get(colonnes.getPrenomIndividuId());
			String mobileIndividu1 = (String)this.get(colonnes.getMobileIndividu1Id());
			String emailIndividu = (String)this.get(colonnes.getEmailPersonnelIndividuId());
			
			if (code >= Consts.CODE_VIOLETS || code == Consts.CODE_RESPONSABLE_FARFADETS || code == Consts.CODE_PARENTS_FARFADETS)
			{
				if (nomIndividu != null && prenomIndividu != null)
				{
					os.println("BEGIN:VCARD");
					os.println("VERSION:3.0");
					os.println("N:"+nomIndividu+";"+prenomIndividu+";;;");
					os.println("FN:"+prenomIndividu + " " + nomIndividu);
					if (code == Consts.CODE_RESPONSABLE_FARFADETS)
					{
						os.println("CATEGORIES:Direction,Parents Farfadets");
					}
					else
					{
						if (code == Consts.CODE_PARENTS_FARFADETS)
							os.println("CATEGORIES:Parents Farfadets");
						else
							os.println("CATEGORIES:Direction");
					}
					if (emailIndividu != null && !emailIndividu.isEmpty()) os.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+emailIndividu.toLowerCase());
					if (mobileIndividu1 != null && !mobileIndividu1.isEmpty()) os.println("TEL;CELL:"+mobileIndividu1);
				}
				os.println("END:VCARD");
				return true;
			}
			else
			{
				if (nomIndividu != null && prenomIndividu != null)
				{
					os.println("BEGIN:VCARD");
					os.println("VERSION:3.0");
					os.println("N:"+nomIndividu+";"+prenomIndividu+";;;");
					os.println("FN:"+prenomIndividu + " " + nomIndividu);
					os.println("CATEGORIES:Maitrises,Chefs " + unitAdherent);
					if (emailIndividu != null && !emailIndividu.isEmpty()) os.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+emailIndividu.toLowerCase());
					if (mobileIndividu1 != null && !mobileIndividu1.isEmpty()) os.println("TEL;CELL:"+mobileIndividu1);
					os.println("END:VCARD");
					return true;
				}
			}
		}
		return false;
	}
	
	public void listeEmailChef(ColonnesAdherents colonnes, Unite unite, PrintStream os) {
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.getNom()) != 0) return;
		
		int code = this.getFonction();
		if (code >= Consts.CODE_RESPONSABLES)
		{
			String emailIndividu = (String)this.get(colonnes.getEmailPersonnelIndividuId());
			if (emailIndividu != null && !emailIndividu.isEmpty())
			{
				os.println(emailIndividu.toLowerCase());
			}
		}
	}

	public void listeEmail(ColonnesAdherents colonnes, Unite unite, PrintStream os) {
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.getNom()) != 0) return;
		
		String emailPere = (String)this.get(colonnes.getEmailPereId());
		String emailMere = (String)this.get(colonnes.getEmailMereId());
		
		if (emailPere != null && !emailPere.isEmpty())
			os.println(emailPere.toLowerCase());
		if (emailMere != null && !emailMere.isEmpty())
			os.println(emailMere.toLowerCase());
	}
	
	private Date dateAge(int age)
	{
		double d = dn_.getTime()+age*3600*365.25*24*1000;
		Date dd = new Date((long)Math.floor(d));
		return dd;
	}

	public void construitsAlertes(Alertes alertes, boolean jeunes, boolean age) {
		if (jeunes == false)
		{
			return;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		switch (this.getFonction())
		{
			case Consts.CODE_FARFADETS:
				if (age && this.getAge() < 6)
					alertes.ajouter(this, Alerte.Severite.HAUTE, Alerte.ALERTE_TYPE_AGE, "Farfadet n'ayant pas encore 6 ans à ce jour, pas d'activité avant le "+simpleDateFormat.format(dateAge(6)));
			break;
			case Consts.CODE_LJ:
				if (age && this.getAgeokb() == false)
					alertes.ajouter(this, Alerte.Severite.MOYENNE, Alerte.ALERTE_TYPE_AGE, "Pas 8 ans au 31 décembre prochain, il/elle les aura le "+simpleDateFormat.format(dateAge(8)));
			break;
			case Consts.CODE_SG:
				if (age && this.getAgeokcampb() == false)
					alertes.ajouter(this, Alerte.Severite.HAUTE, Alerte.ALERTE_TYPE_AGE, "Pas 11 ans au 1er juillet prochain, il/elle les aura le "+simpleDateFormat.format(dateAge(11)));
				else if (age && this.getAgeokb() == false)
					alertes.ajouter(this, Alerte.Severite.MOYENNE, Alerte.ALERTE_TYPE_AGE, "Pas 11 ans au 31 décembre prochain, il/elle les aura le "+simpleDateFormat.format(dateAge(11)));
			break;
			case Consts.CODE_PIOK:
				if (age && this.getAgeokcampb() == false)
					alertes.ajouter(this, Alerte.Severite.HAUTE, Alerte.ALERTE_TYPE_AGE, "Pas 14 ans au 1er juillet prochain, il/elle les aura le "+simpleDateFormat.format(dateAge(14)));
				else if (age && this.getAgeokb() == false)
					alertes.ajouter(this, Alerte.Severite.MOYENNE, Alerte.ALERTE_TYPE_AGE, "Pas 14 ans au 31 décembre prochain, il/elle les aura le "+simpleDateFormat.format(dateAge(14)));
			break;
		}
	}

	public void liste(int id, String nom, CSVPrinter out, String groupe) throws IOException {
		if (id != -1)
		{
			out.print(id);
			out.print(nom);
		}
		data_.forEach((key,value) -> {
			try {
				String n = colonnes_.getNom(key);
				if (id != -1)
				{
					if (n.startsWith("Formations") || n.startsWith("Qualifications") || n.startsWith("Diplomes") || n.startsWith("Individu.CodeAdherent"))
					{
						if (n.compareTo("Qualifications.DateFinValidite") == 0 || n.compareTo("Diplomes.DateObtention") == 0 || n.compareTo("Formations.DateFin") == 0)
						{
							String parts[] = value.split("/");
							if (parts.length==3)
								out.print(parts[2]+"-"+parts[1]+"-"+parts[0]);
							else
								out.print(value);
						}
						else
							out.print(value);
					}
				}
				else
				{
					out.print(value);
				}
			} catch (IOException e) {
			}
		});
		if (id == -1)
		{
			out.print(getCodegroupe());
			out.print(groupe);
		}
		out.println();
	}
}
