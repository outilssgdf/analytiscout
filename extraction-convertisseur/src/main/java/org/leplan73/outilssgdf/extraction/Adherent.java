package org.leplan73.outilssgdf.extraction;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.csv.CSVPrinter;
import org.leplan73.outilssgdf.Check;

public class Adherent extends Hashtable<Integer, String> {

	private static final long serialVersionUID = 1L;
	
	private int code_;
	private String codePapa_;
	private String codeMaman_;
	private String unite_;
	
	@Override
	public int hashCode()
	{
		return code_;
	}
	
	public String getUnite()
	{
		return unite_;
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
	
	public void init(Colonnes colonnes)
	{
		String code = this.get(colonnes.getCodeAdherentId());
		code_ = Integer.valueOf(code);
		unite_ = (String)this.get(colonnes.getUniteId());
	}
	
	public void check(Colonnes colonnes, Unite unite, List<Check> checks)
	{
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.nom()) != 0) return;

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
		
		if (indiv1Pere)checks.add(new Check(unite.nom(), nomIndividu + " : " + prenomIndividu, "mobileIndividu1 == mobilePere"));
		if (indiv1Mere) checks.add(new Check(unite.nom(), nomIndividu + " : " + prenomIndividu, "mobileIndividu1 == mobileMere"));
		if (indiv2Pere) checks.add(new Check(unite.nom(), nomIndividu + " : " + prenomIndividu, "mobileIndividu2 == mobilePere"));
		if (indiv2Mere) checks.add(new Check(unite.nom(), nomIndividu + " : " + prenomIndividu, "mobileIndividu2 == mobileMere"));
		
		if (indivPere1) checks.add(new Check(unite.nom(), nomIndividu + " : " + prenomIndividu, "emailIndividu1 == emailPere"));
		if (indivMere1) checks.add(new Check(unite.nom(), nomIndividu + " : " + prenomIndividu, "emailIndividu1 == emailMere"));
		if (indivPere2) checks.add(new Check(unite.nom(), nomIndividu + " : " + prenomIndividu, "emailIndividu2 == emailPere"));
		if (indivMere2) checks.add(new Check(unite.nom(), nomIndividu + " : " + prenomIndividu, "emailIndividu2 == emailMere"));
	}
	
	public boolean listeChefCvs(Colonnes colonnes, Unite unite, CSVPrinter os) throws IOException {
		String unitAdherent = (String)this.get(colonnes.getUniteId());
		if (unite != null && unitAdherent.compareTo(unite.nom()) != 0) return false;
		
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
		if (unite != null && unitAdherent.compareTo(unite.nom()) != 0) return;
		
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
		if (unite != null && unitAdherent.compareTo(unite.nom()) != 0) return;
		
		String emailPere = (String)this.get(colonnes.getEmailPereId());
		String emailMere = (String)this.get(colonnes.getEmailMereId());
		
		if (emailPere != null && !emailPere.isEmpty()) os.println(emailPere.toLowerCase());
		if (emailMere != null && !emailMere.isEmpty()) os.println(emailMere.toLowerCase());
	}

}
