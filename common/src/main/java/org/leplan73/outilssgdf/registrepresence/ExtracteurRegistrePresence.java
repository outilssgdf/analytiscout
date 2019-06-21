package org.leplan73.outilssgdf.registrepresence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class ExtracteurRegistrePresence {
	
	private Map<String, RegistrePresenceUnite> unites_ = new TreeMap<String, RegistrePresenceUnite>();
	
	public Collection<RegistrePresenceUnite> getUnites()
	{
		return unites_.values();
	}
	
	public void construitActivites(List<RegistrePresenceActivite> activites)
	{
		unites_.forEach((k,v) -> v.construitActivites(activites));
	}
	
	public int charge(final InputStream stream)
	{
		RegistrePresenceUnite unite = null;
		int anneeDebut = -1;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"UTF8"));
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(reader);
			
			String groupe = null;
			for (CSVRecord record : records) {
				if (record.size() == 2)
				{
					if (unite != null)
					{
						// Fin d'unité
						unite.complete(groupe);
						unites_.put(unite.getStructure(), unite);
						unite = null;
					}
					
					RegistrePresenceUnite nunite = new RegistrePresenceUnite(record.get(0));
					if (unites_.containsKey(nunite.getStructure()))
					{
						unite = unites_.get(nunite.getStructure());
					}
					else
					{
						unite = new RegistrePresenceUnite(record.get(0));
					}
					boolean est_groupe = unite.estGroupe();
					if (est_groupe)
					{
						groupe = unite.getNom();
					}
					else
					{
						RegistrePresenceUnite u = unites_.get(unite.code_groupe());
						groupe = u.getNom();
					}
				}
				else
				{
					if (unite != null)
					{
						int a = unite.charge(record);
						if (anneeDebut == -1)
							anneeDebut = a;
					}
				}
			}
			unite.complete(groupe);
			unites_.put(unite.getStructure(), unite);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return anneeDebut;
	}

	public void exportInfluxDb(File out) {
		PrintStream os;
		try {
			os = new PrintStream(new FileOutputStream(out));
			os.println("# DDL");
			os.println("CREATE DATABASE import");
			os.println();
			os.println("# DML");
			os.println("# CONTEXT-DATABASE: import");
			os.println();
			
			unites_.forEach((k,v) -> {
				String groupe;
				boolean est_groupe = v.estGroupe();
				if (est_groupe == false)
				{
					RegistrePresenceUnite u = unites_.get(v.code_groupe());
					groupe = u.getNom();
				}
				else
				{
					groupe = v.getNom();
				}
				v.exportInfluxDb(groupe,os);	
			});
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getActivites(List<RegistrePresenceActiviteHeure> activitesReel, List<RegistrePresenceActiviteHeure> activitesForfaitaire) {
		unites_.forEach((k,v) -> v.genereReel(activitesReel, activitesForfaitaire));
	}

	public void getActivitesCecChef(String chef, RegistrePresenceUnite unite, int anneeDebut, List<RegistrePresenceActiviteHeure> activites_cec) {
		unite.genereCecChef(chef, anneeDebut, activites_cec);
	}
}
