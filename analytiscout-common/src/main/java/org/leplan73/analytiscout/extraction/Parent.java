package org.leplan73.analytiscout.extraction;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.leplan73.analytiscout.Consts;

public class Parent extends TreeMap<Integer,Adherent>
{
	private static final long serialVersionUID = 1L;
	
	private String code_;
	private int type_;
	private Set<String> unitesEnfants_ = new HashSet<String>();
	
	public boolean init(ColonnesAdherents colonnes, Adherent adherent, boolean pere)
	{
		String nom = (String)adherent.get(pere ? colonnes.getNomPereId() : colonnes.getNomMereId());
		String prenom = (String)adherent.get(pere ? colonnes.getPrenomPereId() : colonnes.getPrenomMereId());

		type_ = pere ? Consts.PARENT_PERE : Consts.PARENT_MERE;
		
		if (nom != null && prenom != null)
		{
			if (nom.length() > 0 && prenom.length() > 0)
			{
				code_ = (nom + ":" + prenom);
				this.put(adherent.hashCode(), adherent);
				return true;
			}
		}
		return false;
	}

	public void fusionne(Parent parent) {
		parent.forEach((code, adherent) ->
		{
			this.put(code, adherent);
		});
	}
	
	public void complete()
	{
		this.forEach((code, adherent) ->
		{
			unitesEnfants_.add(adherent.getUnite());
		});
	}
	
	public Set<String> getUnitesEnfants()
	{
		return unitesEnfants_;
	}

	public String getCode() {
		return code_;
	}

	public int getType() {
		return type_;
	}
	
	public boolean afficheParentsVCard(ColonnesAdherents colonnes, Adherent adherent, Set<String> unitesEnfants, int type, PrintStream os) throws IOException {
		String nomPere = (String)adherent.get(colonnes.getNomPereId());
		String prenomPere = (String)adherent.get(colonnes.getPrenomPereId());
		String emailPere = (String)adherent.get(colonnes.getEmailPereId());
		String mobilePere = (String)adherent.get(colonnes.getMobilePereId());
		String nomMere = (String)adherent.get(colonnes.getNomMereId());
		String prenomMere = (String)adherent.get(colonnes.getPrenomMereId());
		String emailMere = (String)adherent.get(colonnes.getEmailMereId());
		String mobileMere = (String)adherent.get(colonnes.getMobileMereId());
		
		String unites = unitesEnfants.stream().collect(Collectors.joining(","));
		
		boolean ret = false;
		
		if ((type == Consts.PARENT_PERE || type == Consts.PARENT_PEREMERE) && ((emailPere != null && !emailPere.isEmpty()) || (mobilePere != null && !mobilePere.isEmpty())))
		{
			if (nomPere != null && prenomPere != null)
			{
				os.println("BEGIN:VCARD");
				os.println("VERSION:3.0");
				os.println("N:"+prenomPere + " " + nomPere+";;;");
				os.println("FN:"+prenomPere + " " + nomPere);
				if (emailPere != null && !emailPere.isEmpty()) os.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+emailPere.toLowerCase());
				if (mobilePere != null && !mobilePere.isEmpty()) os.println("TEL;CELL:"+mobilePere);
				os.println("CATEGORIES:Parents,"+unites);
				os.println("END:VCARD");
				ret=true;
			}
		}
		
		if ((type == Consts.PARENT_MERE || type == Consts.PARENT_PEREMERE) && ((emailMere != null && !emailMere.isEmpty()) || (mobileMere != null && !mobileMere.isEmpty())))
		{
			if (nomMere != null && prenomMere != null)
			{
				os.println("BEGIN:VCARD");
				os.println("VERSION:3.0");
				os.println("N:"+prenomMere + " " + nomMere+";;;");
				os.println("FN:"+prenomMere + " " + nomMere);
				if (emailMere != null && !emailMere.isEmpty()) os.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+emailMere.toLowerCase());
				if (mobilePere != null && !mobilePere.isEmpty()) os.println("TEL;CELL:"+mobileMere);
				os.println("CATEGORIES:Parents,"+unites);
				os.println("END:VCARD");
				ret=true;
			}
		}
		return ret;
	}
	
	public boolean afficheParentsEnfantVCard(ColonnesAdherents colonnes, Adherent adherent, String unite, int type, PrintStream os) throws IOException {
		String nom = (String)adherent.get(colonnes.getNomIndividuId());
		String prenom = (String)adherent.get(colonnes.getPrenomIndividuId());
		String nomPere = (String)adherent.get(colonnes.getNomPereId());
		String prenomPere = (String)adherent.get(colonnes.getPrenomPereId());
		String emailPere = (String)adherent.get(colonnes.getEmailPereId());
		String mobilePere = (String)adherent.get(colonnes.getMobilePereId());
		String nomMere = (String)adherent.get(colonnes.getNomMereId());
		String prenomMere = (String)adherent.get(colonnes.getPrenomMereId());
		String emailMere = (String)adherent.get(colonnes.getEmailMereId());
		String mobileMere = (String)adherent.get(colonnes.getMobileMereId());
		
		boolean ret = false;
		
		if ((type == Consts.PARENT_PERE || type == Consts.PARENT_PEREMERE) && ((emailPere != null && !emailPere.isEmpty()) || (mobilePere != null && !mobilePere.isEmpty())))
		{
			if (nomPere != null && prenomPere != null)
			{
				os.println("BEGIN:VCARD");
				os.println("VERSION:3.0");
				os.println("N:Papa de "+ prenom + " " + nom+";;;");
				os.println("FN:Papa de "+ prenom + " " + nom);
				if (emailPere != null && !emailPere.isEmpty()) os.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+emailPere.toLowerCase());
				if (mobilePere != null && !mobilePere.isEmpty()) os.println("TEL;CELL:"+mobilePere);
				os.println("CATEGORIES:Parents "+unite);
				os.println("END:VCARD");
				ret=true;
			}
		}
		
		if ((type == Consts.PARENT_MERE || type == Consts.PARENT_PEREMERE) && ((emailMere != null && !emailMere.isEmpty()) || (mobileMere != null && !mobileMere.isEmpty())))
		{
			if (nomMere != null && prenomMere != null)
			{
				os.println("BEGIN:VCARD");
				os.println("VERSION:3.0");
				os.println("N:Maman de "+ prenom + " " + nom+";;;");
				os.println("FN:Maman de "+ prenom + " " + nom);
				if (emailMere != null && !emailMere.isEmpty()) os.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+emailMere.toLowerCase());
				if (mobilePere != null && !mobilePere.isEmpty()) os.println("TEL;CELL:"+mobileMere);
				os.println("CATEGORIES:Parents "+unite);
				os.println("END:VCARD");
				ret=true;
			}
		}
		return ret;
	}
}

