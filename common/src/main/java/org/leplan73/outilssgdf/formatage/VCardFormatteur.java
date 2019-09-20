package org.leplan73.outilssgdf.formatage;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.calcul.Unites;
import org.leplan73.outilssgdf.extraction.Adherent;
import org.leplan73.outilssgdf.extraction.Adherents;
import org.leplan73.outilssgdf.extraction.ColonnesAdherents;

public class VCardFormatteur {
	
	class Categorie
	{
		String nom;
		Set<Integer> membres;
		Set<String> codes;
	}
	
	
	private List<Categorie> chargeCategories(Properties modele)
	{
		List<Categorie> cats = new ArrayList<Categorie>();
		
		int index = 1;
		for (;;) {
			String nom = modele.getProperty(Consts.VCARD_CATEGORIE_NOM+"." + index);
			if (nom == null) {
				break;
			}
			String membresp = modele.getProperty(Consts.VCARD_CATEGORIE_MEMBRES+"." + index);
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
			String codesp = modele.getProperty(Consts.VCARD_CATEGORIE_CODE+"." + index);
			if (codesp != null)
			{
				categorie.codes = new HashSet<String>();
				String codes[] = codesp.split(",");
				for (String code : codes)
				{
					categorie.codes.add(code);
				}
			}
			cats.add(categorie);
			index++;
		}
		return cats;
	}
	
	
	private void listeVCard(ColonnesAdherents colonnes, Adherent adherent, List<Categorie> cats, PrintStream out, boolean ajouterGroupe) throws IOException {
		out.println("BEGIN:VCARD");
		out.println("VERSION:3.0");
		out.println("N:"+adherent.getNom()+";"+adherent.getPrenom()+";;;");
		out.println("FN:"+adherent.getPrenom()+" "+adherent.getNom());
		out.println("EMAIL;TYPE=INTERNET;TYPE=HOME:"+adherent.getEmail());
		StringBuilder sb = new StringBuilder();
		
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
	
	public void genereEmail(Unites unites, Adherents adherents, ColonnesAdherents colonnes, Properties modele, PrintStream out) throws IOException {
		
		List<Categorie> cats = chargeCategories(modele);
		
		String ajouterGroupe = modele.getProperty(Consts.VCARD_AJOUTER_GROUPE,"1");
		
		adherents.forEach((key,adherent) -> {
			try {
				listeVCard(colonnes, adherent, cats, out, (ajouterGroupe.compareTo("1") == 0));
			} catch (IOException e) {
			}
		});
	}
}
