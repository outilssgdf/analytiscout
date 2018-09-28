package org.leplan73.outilssgdf.extraction;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVPrinter;

public class Parent extends TreeMap<Integer,Adherent>
{
	private static final long serialVersionUID = 1L;
	
	private String code_;
	private int type_;
	private Set<String> unitesEnfants_ = new HashSet<String>();
	
	public boolean init(Colonnes colonnes, Adherent adherent, boolean pere)
	{
		String nom = (String)adherent.get(pere ? colonnes.getNomPereId() : colonnes.getNomMereId());
		String prenom = (String)adherent.get(pere ? colonnes.getPrenomPereId() : colonnes.getPrenomMereId());

		type_ = pere ? Consts.PARENT_PERE : Consts.PARENT_MERE;
		
		if (nom != null && prenom != null)
		{
			code_ = (nom + ":" + prenom);
			this.put(adherent.hashCode(), adherent);
			return true;
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
	
	public boolean afficheParentsCvs(Colonnes colonnes, Adherent adherent, Set<String> unitesEnfants, int type, CSVPrinter os) throws IOException {
		String nomPere = (String)adherent.get(colonnes.getNomPereId());
		String prenomPere = (String)adherent.get(colonnes.getPrenomPereId());
		String emailPere = (String)adherent.get(colonnes.getEmailPereId());
		String mobilePere = (String)adherent.get(colonnes.getMobilePereId());
		String nomMere = (String)adherent.get(colonnes.getNomMereId());
		String prenomMere = (String)adherent.get(colonnes.getPrenomMereId());
		String emailMere = (String)adherent.get(colonnes.getEmailMereId());
		String mobileMere = (String)adherent.get(colonnes.getMobileMereId());
		
		String unites = unitesEnfants.stream().collect(Collectors.joining(" ::: "));
		
		boolean ret = false;
		
		if ((type == Consts.PARENT_PERE || type == Consts.PARENT_PEREMERE) && ((emailPere != null && !emailPere.isEmpty()) || (mobilePere != null && !mobilePere.isEmpty())))
		{
			if (nomPere != null && prenomPere != null)
			{
				os.print(prenomPere + " " + nomPere);
				os.print(prenomPere);
				os.print(nomPere);
				os.print("Parents ::: " + unites);
				os.print(emailPere != null ? (emailPere.toLowerCase()) : "");
				if (mobilePere != null)
				{
					os.print("Mobile");
					os.print(mobilePere);
				}
				else
				{
					os.print("");
					os.print("");
				}
				
				ret=true;
			}
		}
		
		if ((type == Consts.PARENT_MERE || type == Consts.PARENT_PEREMERE) && ((emailMere != null && !emailMere.isEmpty()) || (mobileMere != null && !mobileMere.isEmpty())))
		{
			if (nomMere != null && prenomMere != null)
			{
				os.print(prenomMere + " " + nomMere);
				os.print(prenomMere);
				os.print(nomMere);
				os.print("Parents ::: " + unites);
				os.print(emailMere != null ? (emailMere.toLowerCase()) : "");
				if (mobileMere != null)
				{
					os.print("Mobile");
					os.print(mobileMere);
				}
				else
				{
					os.print("");
					os.print("");
				}
				
				ret=true;
			}
		}
		
		return ret;
	}
	
	public boolean afficheParentsEnfantCvs(Colonnes colonnes, Adherent adherent, String unite, int type, CSVPrinter os) throws IOException {
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
				os.print("Papa de "+ prenom + " " + nom);
				os.print(prenom);
				os.print(nom);
				os.print("Parents ::: " + unite);
				os.print(emailPere != null ? (emailPere.toLowerCase()) : "");
				if (mobilePere != null)
				{
					os.print("Mobile");
					os.print(mobilePere);
				}
				else
				{
					os.print("");
					os.print("");
				}
				
				ret=true;
			}
		}
		
		if ((type == Consts.PARENT_MERE || type == Consts.PARENT_PEREMERE) && ((emailMere != null && !emailMere.isEmpty()) || (mobileMere != null && !mobileMere.isEmpty())))
		{
			if (nomMere != null && prenomMere != null)
			{
				os.print("Maman de "+ prenom + " " + nom);
				os.print(prenom);
				os.print(nom);
				os.print("Parents ::: " + unite);
				os.print(emailMere != null ? (emailMere.toLowerCase()) : "");
				if (mobileMere != null)
				{
					os.print("Mobile");
					os.print(mobileMere);
				}
				else
				{
					os.print("");
					os.print("");
				}
				
				ret=true;
			}
		}
		
		return ret;
	}
}

