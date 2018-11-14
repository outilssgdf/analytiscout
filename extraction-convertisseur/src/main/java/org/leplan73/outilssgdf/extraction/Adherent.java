package org.leplan73.outilssgdf.extraction;

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
import org.leplan73.outilssgdf.Check;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.calcul.Unite;

public class Adherent {

	protected Map<Integer, String> data_ = new Hashtable<Integer, String>();
	
	protected int code_;
	protected String codePapa_;
	protected String codeMaman_;
	protected String unite_;
	protected Colonnes colonnes_;
	protected long age_ = -1;
	protected long ageFinDec_ = -1;
	protected long ageCamp_ = -1;
	
	public Adherent(Colonnes colonnes)
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
	
	@Override
	public int hashCode()
	{
		return code_;
	}
	
	public Integer getCode()
	{
		return code_;
	}
	
	public String getUnite()
	{
		return unite_;
	}
	
	public long getAge()
	{
		return age_;
	}
	
	public String getDatedenaissance()
	{
		return this.get(colonnes_.getDatedeNaissanceId());
	}
	
	public String getNom()
	{
		return this.get(colonnes_.getNomIndividuId());
	}
	
	public String getPrenom()
	{
		return this.get(colonnes_.getPrenomIndividuId());
	}
	
	public boolean getMarin()
	{
		String code = this.get(colonnes_.getFonctionCodeId());
		return code.endsWith("M");
	}
	
	public int getFonction()
	{
		return extraitCode();
	}
	
	public String getFonctioncomplet()
	{
		return this.get(colonnes_.getFonctionCodeId());
	}
	
	public int getJeune()
	{
		int fonction = extraitCode();
		return fonction < Consts.CODE_RESPONSABLES ? 1 : 0;
	}
	
	public int getCompa()
	{
		int fonction = extraitCode();
		return (fonction ==  Consts.CODE_COMPAS_T1T2 || fonction == Consts.CODE_COMPAS_T3) ? 1 : 0;
	}
	
	public int getChef()
	{
		int fonction = extraitCode();
		return fonction >= Consts.CODE_RESPONSABLES ? 1 : 0;
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
	
	private int extraitCode()
	{
		String code = this.get(colonnes_.getFonctionCodeId());
		if (code.endsWith("M"))
			code = code.substring(0, 3);
		if (code.endsWith("L"))
			code = code.substring(0, 3);
		return Integer.valueOf(code);
	}
	
	public void init()
	{
		String code = this.get(colonnes_.getCodeAdherentId());
		code_ = Integer.valueOf(code);
		unite_ = (String)this.get(colonnes_.getUniteId());
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			String date = get(colonnes_.getDatedeNaissanceId());
			if (date != null)
			{
				Date dn = simpleDateFormat.parse(date);
				long aj = Instant.now().toEpochMilli();
				long diff = ((aj - dn.getTime())/1000);
				diff = diff/(3600*365*24);
				age_ = diff;
				
				Date debutFindDec = simpleDateFormat.parse(Consts.DATE_LIMITE_JEUNE);
				long diffFindDec = ((debutFindDec.getTime() - dn.getTime())/1000);
				diffFindDec = diffFindDec/(3600*365*24);
				ageFinDec_ = diffFindDec;
				
				Date debutCamp = simpleDateFormat.parse(Consts.DATE_DEBUT_CAMP);
				long diffCamp = ((debutCamp.getTime() - dn.getTime())/1000);
				diffCamp = diffCamp/(3600*365*24);
				ageCamp_ = diffCamp;
			}
		} catch (ParseException e) {
		}
	}
	
	public String getAgeok()
	{
		if (this.getFonction() < Consts.CODE_COMPAS)
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
		}
		return "Oui";
	}
	
	public void check(Colonnes colonnes, Unite unite, List<Check> checks)
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
		String emailIndividu1 = (String)this.get(colonnes.getEmailIndividuId());
		String emailIndividu2 = (String)this.get(colonnes.getEmailIndividu2Id());
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
	
	public boolean listeChefCvs(Colonnes colonnes, Unite unite, CSVPrinter os) throws IOException {
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.getNom()) != 0) return false;
		
		int code = Integer.parseInt((String)this.get(colonnes.getFonctionCodeId()));
		if (code >= Consts.CODE_RESPONSABLES)
		{
			String nomIndividu = (String)this.get(colonnes.getNomIndividuId());
			String prenomIndividu = (String)this.get(colonnes.getPrenomIndividuId());
			String mobileIndividu1 = (String)this.get(colonnes.getMobileIndividu1Id());
			String emailIndividu = (String)this.get(colonnes.getEmailIndividuId());
			
			if (code >= Consts.CODE_VIOLETS || code == Consts.CODE_RESPONSABLE_FARFADETS || code == Consts.CODE_PARENTS_FARFADETS)
			{
				if (nomIndividu != null && prenomIndividu != null)
				{
					os.print(prenomIndividu + " " + nomIndividu);
					os.print(prenomIndividu);
					os.print(nomIndividu);
					if (code == Consts.CODE_RESPONSABLE_FARFADETS)
					{
						os.print("Direction ::: Parents Farfadets");
					}
					else
					{
						if (code == Consts.CODE_PARENTS_FARFADETS)
							os.print("Parents Farfadets");
						else
							os.print("Direction");
					}
					os.print(emailIndividu != null ? (emailIndividu.toLowerCase()) : "");
					if (mobileIndividu1 != null)
					{
						os.print("Mobile");
						os.print(mobileIndividu1);
					}
					else
					{
						os.print("");
						os.print("");
					}
				}
				return true;
			}
			else
			{
				if (nomIndividu != null && prenomIndividu != null)
				{
					os.print(prenomIndividu + " " + nomIndividu);
					os.print(prenomIndividu);
					os.print(nomIndividu);
					os.print("Maitrises ::: Chefs " + unitAdherent);
					os.print(emailIndividu != null ? (emailIndividu.toLowerCase()) : "");
					if (mobileIndividu1 != null)
					{
						os.print("Mobile");
						os.print(mobileIndividu1);
					}
					else
					{
						os.print("");
						os.print("");
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void listeEmailChef(Colonnes colonnes, Unite unite, PrintStream os) {
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.getNom()) != 0) return;
		
		int code = Integer.parseInt((String)this.get(colonnes.getFonctionCodeId()));
		if (code >= Consts.CODE_RESPONSABLES)
		{
			String emailIndividu = (String)this.get(colonnes.getEmailIndividuId());
			if (emailIndividu != null && !emailIndividu.isEmpty())
				os.println(emailIndividu.toLowerCase());
		}
	}

	public void listeEmail(Colonnes colonnes, Unite unite, PrintStream os) {
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.getNom()) != 0) return;
		
		String emailPere = (String)this.get(colonnes.getEmailPereId());
		String emailMere = (String)this.get(colonnes.getEmailMereId());
		
		if (emailPere != null && !emailPere.isEmpty()) os.println(emailPere.toLowerCase());
		if (emailMere != null && !emailMere.isEmpty()) os.println(emailMere.toLowerCase());
	}

}
