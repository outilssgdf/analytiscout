package org.leplan73.analytiscout.formatage;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.calcul.Unites;
import org.leplan73.analytiscout.extraction.Adherent;
import org.leplan73.analytiscout.extraction.Adherents;
import org.leplan73.analytiscout.extraction.ColonnesAdherents;

public class VCardFormateur extends Formateur {
	
	private void listeVCardEmail(List<Email> emails, PrintStream out, Set<String> cvardEmails)
	{
		int index=1;
		for (Email email : emails)
		{
			if (cvardEmails.contains(email.adresse) && email.force == 0)
			{
				// Déjà ajouté, on ignore
				continue;
			}
			out.println("BEGIN:VCARD");
			out.println("VERSION:3.0");
			if (email.nom != null)
			{
				out.println("N:"+email.nom+";;;");
				out.println("FN:"+email.nom+";;;");
				out.println("TITLE:"+email.nom);
			}
			else
			{
				out.println("N:"+email.adresse+" "+index+";;;");
				out.println("FN:"+email.adresse+" "+index+";;;");
				out.println("TITLE:"+index);
			}
			out.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+email.adresse);
			out.println("CATEGORIES:"+email.categories.stream().collect(Collectors.joining(",")));
			out.println("END:VCARD");
			index++;
		}
	}
	
	private void listeEmail(ColonnesAdherents colonnes, Adherent adherent, List<Categorie> cats, List<Email> emails, PrintStream out, boolean ajouterGroupe, Set<String> cvardEmails) throws IOException {
		out.println("BEGIN:VCARD");
		out.println("VERSION:3.0");
		out.println("N:"+adherent.getNom()+";"+adherent.getPrenom()+";;;");
		out.println("FN:"+adherent.getPrenom()+" "+adherent.getNom());
		out.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+adherent.getEmailPersonnel());
		out.println("TITLE:"+adherent.getCode());
		StringBuilder sb = new StringBuilder();
		
		cvardEmails.add(adherent.getEmailPersonnel());
		
		int index = 0;
		
		// Categories customs
		for (Categorie cat : cats)
		{
			if (cat.membres != null && cat.membres.contains(adherent.getCode()))
			{
				if (index > 0)
					sb.append(",");
				sb.append(cat.nom);
				index++;
			}
			if (cat.codes != null && cat.codes.contains(adherent.getFonctioncomplet()))
			{
				if (index > 0)
					sb.append(",");
				sb.append(cat.nom);
				index++;
			}
			if (cat.codes != null && cat.codes.contains(adherent.getFonctionsecondairecomplet()))
			{
				if (index > 0)
					sb.append(",");
				sb.append(cat.nom);
				index++;
			}
			
			for (Email email : emails)
			{
				if (email.adresse.compareTo(adherent.getEmailPersonnel()) == 0)
				{
					for (String catEmail : email.categories)
					{
						if (catEmail.compareTo(cat.nom) == 0)
						{
							if (index > 0)
								sb.append(",");
							sb.append(catEmail);
							index++;
						}
					}
				}
				if (email.adresse.compareTo(adherent.getEmailProfessionnel()) == 0)
				{
					for (String catEmail : email.categories)
					{
						if (catEmail.compareTo(cat.nom) == 0)
						{
							if (index > 0)
								sb.append(",");
							sb.append(catEmail);
							index++;
						}
					}
				}
			}
		}

		if (ajouterGroupe)
		{
			// Ajout du groupe
			if (index > 0)
				sb.append(",");
			sb.append(adherent.getStructure());
		}
		
		String categories = sb.toString();
		if (categories.isEmpty() == false)
		{
			out.println("CATEGORIES:"+categories);
		}
		out.println("END:VCARD");
	}
	
	public void genereEmail(Unites unites, Adherents adherents, ColonnesAdherents colonnes, Properties props, PrintStream out) throws IOException {
		
		List<Categorie> cats = chargeCategories(props);
		List<Email> emails = chargeEmails(props);
		
		Set<String> cvardEmails = new TreeSet<String>();
		String ajouterGroupe = props.getProperty(Consts.VCARD_AJOUTER_GROUPE,"1");
		adherents.forEach((key,adherent) -> {
			try {
				listeEmail(colonnes, adherent, cats, emails, out, (ajouterGroupe.compareTo("1") == 0), cvardEmails);
			} catch (IOException e) {
			}
		});
		
		listeVCardEmail(emails, out, cvardEmails);
	}
}
