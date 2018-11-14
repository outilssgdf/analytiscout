package org.leplan73.outilssgdf.formatage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.calcul.Unite;
import org.leplan73.outilssgdf.calcul.Unites;
import org.leplan73.outilssgdf.extraction.Adherent;
import org.leplan73.outilssgdf.extraction.Adherents;
import org.leplan73.outilssgdf.extraction.Colonnes;
import org.leplan73.outilssgdf.extraction.Parent;
import org.leplan73.outilssgdf.extraction.Parents;
import org.leplan73.outilssgdf.outils.SmartStream;

public class GmailCsvFormatteur {
	
	protected void listeEmailFichier(Colonnes colonnes, Adherents adherents, Unite unite, File dir, String fichier, ZipOutputStream zout) throws IOException
	{
		SmartStream sstream = new SmartStream(dir, fichier, zout);
		PrintStream os = sstream.getStream();
		adherents.forEach((id,adherent) ->
		{
			adherent.listeEmail(colonnes, unite, os);
		});
		sstream.close();
	}
	
	protected void listeEmailChefsFichier(Colonnes colonnes, Adherents adherents, Unite unite, File dir, String fichier, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, fichier, zout);
		PrintStream os = sstream.getStream();
		adherents.forEach((id,adherent) ->
		{
			adherent.listeEmailChef(colonnes, unite, os);
		});
		sstream.close();
	}

	protected void listeChefsCsv(Colonnes colonnes, Adherents adherents, File dir, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, "maitrises.csv", zout);
		PrintStream os = sstream.getStream();
		
		final CSVPrinter printer = CSVFormat.DEFAULT.withHeader("Name","Given Name","Family Name","Group Membership","E-mail 1 - Value","Phone 1 - Type","Phone 1 - Value").print(os);
		adherents.forEach((id,adherent) ->
		{
			try {
				if (adherent.listeChefCvs(colonnes, null, printer))
					printer.println();
			} catch (IOException e) {
			}
		});
		printer.flush();
		sstream.close();
	}

	protected boolean listeParent(Colonnes colonnes, final Parent parent, final Unite unite, Adherents adherents, int type, CSVPrinter out) throws IOException {
		Map.Entry<Integer, Adherent> premierEnfant = parent.firstEntry();
		if (unite == null || unite.compareTo(premierEnfant.getValue().getUnite()) == 0)
			return parent.afficheParentsCvs(colonnes, premierEnfant.getValue(), parent.getUnitesEnfants(), parent.getType(), out);
		return false;
	}
	
	private void listeParentEmail(Colonnes colonnes, final Parent parent, final Unite unite, Adherents adherents, PrintStream out) {
		parent.forEach((code, adherent) ->
		{
			adherent.listeEmail(colonnes, unite, out);
		});
	}

	protected void listeParentsEmail(Colonnes colonnes, Adherents adherents, Parents parents, Unite unite, File dir, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, "parents.txt", zout);
		PrintStream os = sstream.getStream();
		parents.forEach((code,parent) -> listeParentEmail(colonnes, parent, unite, adherents, os));
		sstream.close();
	}
	
	protected void listeEnfantsCsv(Colonnes colonnes, Adherents adherents, Parents parents, Unite unite, File dir, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, unite.getNom()+"_enfants.csv", zout);
		PrintStream os = sstream.getStream();
		final CSVPrinter out = CSVFormat.DEFAULT.withHeader("Name","Given Name","Family Name","Group Membership","E-mail 1 - Value","Phone 1 - Type","Phone 1 - Value").print(os);
		
		adherents.forEach((id,adherent) ->
		{
			if (unite == null || unite.compareTo(adherent.getUnite()) == 0)
				try {
					Parent p = parents.get(adherent.getCodePapa());
					if (p != null && p.afficheParentsEnfantCvs(colonnes, adherent, unite.getNom(), Consts.PARENT_PERE, out)) out.println();
					Parent m = parents.get(adherent.getCodeMaman());
					if (m != null && m.afficheParentsEnfantCvs(colonnes, adherent, unite.getNom(), Consts.PARENT_MERE, out)) out.println();
				} catch (IOException e) {
				}
		});
		sstream.close();
	}

	protected void listeCsv(Colonnes colonnes, Adherents adherents, Parents parents, Unite unite, File dir, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, unite == null ? "tout.csv" : unite.getNom()+"_parents.csv", zout);
		PrintStream os = sstream.getStream();
		final CSVPrinter out = CSVFormat.DEFAULT.withHeader("Name","Given Name","Family Name","Group Membership","E-mail 1 - Value","Phone 1 - Type","Phone 1 - Value").print(os);
		parents.forEach((code,parent) -> {
			try {
				if (listeParent(colonnes, parent, unite, adherents, Consts.PARENT_PEREMERE, out)) out.println();
			} catch (IOException e) {
			}
		});
		sstream.close();
	}
	
	public void genereEmail(Unites unites, Parents parents, Adherents adherents, Colonnes colonnes, String chemin, ZipOutputStream zout) throws IOException {
		final File dir = chemin != null ? new File(chemin) : null;

		// Génération de fichiers texte d'adresses email
		listeChefsCsv(colonnes, adherents, dir, zout);
		listeParentsEmail(colonnes, adherents, parents, null, dir, zout);
		
		// Génération de fichiers texte d'adresses email par unité
		unites.forEach((unite,v) ->
		{
			try {
				listeEmailFichier(colonnes, adherents, v, dir, "parents-"+unite+".txt", zout);
				listeEmailChefsFichier(colonnes, adherents, v, dir, "chefs-"+unite+".txt", zout);
			} catch (IOException e) {
			}
		});
		
		// Export CVS total
		listeCsv(colonnes, adherents, parents, null, dir, zout);
		
		// Export CVS par unité
		unites.forEach((unite,v) ->
		{
			try {
				listeCsv(colonnes, adherents, parents, v, dir, zout);
				listeEnfantsCsv(colonnes, adherents, parents, v, dir, zout);
			} catch (IOException e) {
			}
		});
	}
}
