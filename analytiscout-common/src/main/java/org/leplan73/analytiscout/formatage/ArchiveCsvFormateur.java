package org.leplan73.analytiscout.formatage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.calcul.Unites;
import org.leplan73.analytiscout.extraction.Adherent;
import org.leplan73.analytiscout.extraction.Adherents;
import org.leplan73.analytiscout.extraction.ColonnesAdherents;
import org.leplan73.analytiscout.outils.SmartStream;

public class ArchiveCsvFormateur extends Formateur {

	public void genereEmail(Unites unites, Adherents adherents, ColonnesAdherents colonnes, Properties properties, ZipOutputStream zout) throws IOException
	{
		String sajouterGroupe = properties.getProperty(Consts.VCARD_AJOUTER_GROUPE,"1");
		boolean ajouterGroupe = (sajouterGroupe.compareTo("1") == 0);
		
		List<Categorie> cats = chargeCategories(properties);
		List<Email> emails = chargeEmails(properties);
		
		if (ajouterGroupe)
		{
			adherents.forEach((key,adherent) -> {
				String unite = adherent.getUnite();
				
				boolean trouve = false;
				for (Categorie categorie : cats)
				{
					if (categorie.nom.compareTo(unite) == 0)
					{
						trouve = true;
					}
				}
				if (!trouve)
				{
					cats.add(new Categorie(unite));
				}
			});
		}
		
		for (Categorie categorie : cats)
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PrintStream out = new PrintStream(bos);
			
			Set<String> cvardEmails = new TreeSet<String>();
			adherents.forEach((key,adherent) -> {
				try {
					listeEmail(colonnes, adherent, categorie, emails, ajouterGroupe, cvardEmails);
				} catch (IOException e) {
				}
			});

			cvardEmails.forEach(email -> out.println(email));
			ecritStream(bos, null, categorie.nom.replaceAll("/", "-")+".csv", zout);
		}
	}
	
	private void listeEmail(ColonnesAdherents colonnes, Adherent adherent, Categorie cat, List<Email> emails, boolean ajouterGroupe, Set<String> cvardEmails) throws IOException {
		
		if (adherent.getEmailPersonnel().isEmpty()) return;
		
		// Categories customs
		if (cat.membres != null && cat.membres.contains(adherent.getCode()))
		{
			cvardEmails.add(adherent.getEmailPersonnel().toLowerCase());
		}
		if (cat.codes != null && cat.codes.contains(adherent.getFonctioncomplet()))
		{
			cvardEmails.add(adherent.getEmailPersonnel().toLowerCase());
		}
		if (cat.codes_regexp != null) {
			cat.codes_regexp.forEach(re -> {
				Pattern pattern = Pattern.compile(re);
				Matcher matcher = pattern.matcher(adherent.getFonctioncomplet());
				if (matcher.find()) {
					cvardEmails.add(adherent.getEmailPersonnel().toLowerCase());
				}
			});
		}
		
		if (ajouterGroupe)
		{
			if (cat.nom.compareTo(adherent.getUnite()) == 0)
			{
				cvardEmails.add(adherent.getEmailPersonnel().toLowerCase());
			}
		}
		
		for (Email email : emails)
		{
			if (adherent.getEmailPersonnel() != null && email.adresse.compareTo(adherent.getEmailPersonnel()) == 0)
			{
				for (String catEmail : email.categories)
				{
					if (catEmail.compareTo(cat.nom) == 0)
					{
						cvardEmails.add(adherent.getEmailPersonnel().toLowerCase());
					}
				}
			}
			if (adherent.getEmailProfessionnel() != null && email.adresse.compareTo(adherent.getEmailProfessionnel()) == 0)
			{
				for (String catEmail : email.categories)
				{
					if (catEmail.compareTo(cat.nom) == 0)
					{
						cvardEmails.add(adherent.getEmailPersonnel().toLowerCase());
					}
				}
			}
		}
	}
	
	private void ecritStream(ByteArrayOutputStream bos, File dir, String nom, ZipOutputStream zout) throws IOException
	{
		if (bos.size() > 0)
		{
			SmartStream sstream = new SmartStream(dir, nom, zout);
			sstream.write(bos);
			sstream.close();
		}
	}
}
