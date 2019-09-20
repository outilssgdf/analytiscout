package org.leplan73.outilssgdf.formatage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipOutputStream;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.calcul.Unite;
import org.leplan73.outilssgdf.calcul.Unites;
import org.leplan73.outilssgdf.extraction.Adherent;
import org.leplan73.outilssgdf.extraction.Adherents;
import org.leplan73.outilssgdf.extraction.ColonnesAdherents;
import org.leplan73.outilssgdf.extraction.Parent;
import org.leplan73.outilssgdf.extraction.Parents;
import org.leplan73.outilssgdf.outils.SmartStream;

public class ArchiveFormatteur {
	
	private void listeEmailFichier(ColonnesAdherents colonnes, Adherents adherents, Unite unite, File dir, String fichier, ZipOutputStream zout) throws IOException
	{
		SmartStream sstream = new SmartStream(dir, fichier, zout);
		PrintStream os = sstream.getStream();
		adherents.forEach((id,adherent) ->
		{
			adherent.listeEmail(colonnes, unite, os);
		});
		sstream.close();
	}
	
	private void listeEmailChefsFichier(ColonnesAdherents colonnes, Adherents adherents, Unite unite, File dir, String fichier, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, fichier, zout);
		PrintStream os = sstream.getStream();
		adherents.forEach((id,adherent) ->
		{
			adherent.listeEmailChef(colonnes, unite, os);
		});
		sstream.close();
	}

	private void listeChefsCsv(ColonnesAdherents colonnes, Adherents adherents, File dir, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, "maitrises.vcf", zout);
		PrintStream os = sstream.getStream();
		adherents.forEach((id,adherent) ->
		{
			try {
				adherent.listeChef(colonnes, null, os);
			} catch (IOException e) {
			}
		});
		os.flush();
		sstream.close();
	}

	private boolean listeParent(ColonnesAdherents colonnes, final Parent parent, final Unite unite, Adherents adherents, int type, PrintStream out) throws IOException {
		Map.Entry<Integer, Adherent> premierEnfant = parent.firstEntry();
		if (unite == null)
			return parent.afficheParentsVCard(colonnes, premierEnfant.getValue(), parent.getUnitesEnfants(), parent.getType(), out);
		
		AtomicInteger v = new AtomicInteger(0);
		parent.forEach((code, enfant) ->
		{
			if (unite.compareTo(enfant.getUnite()) == 0)
			{
				try {
					parent.afficheParentsVCard(colonnes, enfant, parent.getUnitesEnfants(), parent.getType(), out);
					v.set(1);
				} catch (IOException e) {
				}
			}
		});
		return (v.get() == 1);
	}
	
	private void listeParentEmail(ColonnesAdherents colonnes, final Parent parent, final Unite unite, Adherents adherents, PrintStream out) {
		parent.forEach((code, adherent) ->
		{
			adherent.listeEmail(colonnes, unite, out);
		});
	}

	private void listeParentsEmail(ColonnesAdherents colonnes, Adherents adherents, Parents parents, Unite unite, File dir, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, "parents.txt", zout);
		PrintStream os = sstream.getStream();
		parents.forEach((code,parent) -> listeParentEmail(colonnes, parent, unite, adherents, os));
		sstream.close();
	}
	
	private void listeEnfantsCVard(ColonnesAdherents colonnes, Adherents adherents, Parents parents, Unite unite, File dir, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, unite.getNom()+"_enfants.vcf", zout);
		PrintStream os = sstream.getStream();
		adherents.forEach((id,adherent) ->
		{
			if (unite == null || unite.compareTo(adherent.getUnite()) == 0)
				try {
					Parent p = parents.get(adherent.getCodePapa());
					if (p != null) p.afficheParentsEnfantVCard(colonnes, adherent, unite.getNom(), Consts.PARENT_PERE, os);
					Parent m = parents.get(adherent.getCodeMaman());
					if (m != null) m.afficheParentsEnfantVCard(colonnes, adherent, unite.getNom(), Consts.PARENT_MERE, os);
				} catch (IOException e) {
				}
		});
		sstream.close();
	}

	private void listeCsv(ColonnesAdherents colonnes, Adherents adherents, Parents parents, Unite unite, File dir, ZipOutputStream zout) throws IOException {
		SmartStream sstream = new SmartStream(dir, unite == null ? "tout.vcf" : unite.getNom()+"_parents.vcf", zout);
		PrintStream os = sstream.getStream();
		parents.forEach((code,parent) -> {
			try {
				listeParent(colonnes, parent, unite, adherents, Consts.PARENT_PEREMERE, os);
			} catch (IOException e) {
			}
		});
		sstream.close();
	}
	
	public void genereEmail(Unites unites, Parents parents, Adherents adherents, ColonnesAdherents colonnes, String chemin, ZipOutputStream zout) throws IOException {
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
				listeEnfantsCVard(colonnes, adherents, parents, v, dir, zout);
			} catch (IOException e) {
			}
		});
	}
}
