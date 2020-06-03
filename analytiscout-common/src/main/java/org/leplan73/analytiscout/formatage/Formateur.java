package org.leplan73.analytiscout.formatage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.leplan73.analytiscout.Consts;

public class Formateur {

	class Categorie
	{
		public Categorie() {
		}

		public Categorie(String unite) {
			this.nom = unite;
		}
		
		String nom;
		Set<Integer> membres;
		Set<String> codes;
		Set<String> codes_regexp;
	}
	
	class Email
	{
		String adresse;
		String nom;
		int force;
		Set<String> categories;
	}
	
	protected List<Email> chargeEmails(Properties props)
	{
		List<Email> emails = new ArrayList<Email>();
		
		int index = 1;
		for (;;) {
			String adresse = props.getProperty(Consts.VCARD_EMAILS_ADRESSE+index);
			if (adresse == null) {
				break;
			}
			String categories = props.getProperty(Consts.VCARD_EMAILS_CATEGORIES+index);
			String force = props.getProperty(Consts.VCARD_EMAILS_FORCE+index,"0");
			Email email = new Email();
			email.adresse = adresse;
			email.force = Integer.parseInt(force);
			String nom = props.getProperty(Consts.VCARD_EMAILS_NOM+index);
			if (nom != null) {
				email.nom = nom;
			}
			if (categories != null)
			{
				email.categories = new HashSet<String>();
				String categoriess[] = categories.split(",");
				for (String categorie : categoriess)
				{
					email.categories.add(categorie);
				}
			}
			emails.add(email);
			index++;
		}
		return emails;
	}
	
	protected List<Categorie> chargeCategories(Properties props)
	{
		List<Categorie> cats = new ArrayList<Categorie>();
		
		int index = 1;
		for (;;) {
			String nom = props.getProperty(Consts.VCARD_CATEGORIE_NOM+index);
			if (nom == null) {
				break;
			}
			String membresp = props.getProperty(Consts.VCARD_CATEGORIE_MEMBRES+index);
			Categorie categorie = new Categorie();
			categorie.nom = nom;
			if (membresp != null)
			{
				categorie.membres = new HashSet<Integer>();
				String membress[] = membresp.split(",");
				for (String membres : membress)
				{
					Integer code = Integer.parseInt(membres);
					categorie.membres.add(code);
				}
			}
			String codesp = props.getProperty(Consts.VCARD_CATEGORIE_CODE+index);
			if (codesp != null)
			{
				categorie.codes = new HashSet<String>();
				String codes[] = codesp.split(",");
				for (String code : codes)
				{
					categorie.codes.add(code);
				}
			}
			String codes_regexpp = props.getProperty(Consts.VCARD_CATEGORIE_CODE_REGEXP+index);
			if (codes_regexpp != null)
			{
				categorie.codes_regexp = new HashSet<String>();
				String codes_regexp[] = codes_regexpp.split(",");
				for (String code_regexp : codes_regexp)
				{
					categorie.codes_regexp.add(code_regexp);
				}
			}
			cats.add(categorie);
			index++;
		}
		return cats;
	}
	
}
