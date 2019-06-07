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
	
	public void charge(final InputStream stream)
	{
		RegistrePresenceUnite unite = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"ISO-8859-1"));
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(reader);
			
			String groupe = null;
			for (CSVRecord record : records) {
				if (record.size() == 2)
				{
					if (unite != null)
					{
						unite.complete(groupe);
						unites_.put(unite.structure(), unite);
					}
					unite = new RegistrePresenceUnite(record.get(0));
					boolean est_groupe = unite.estGroupe();
					if (est_groupe)
					{
						groupe = unite.nom_court();
					}
					else
					{
						RegistrePresenceUnite u = unites_.get(unite.code_groupe());
						groupe = u.nom_court();
					}
				}
				else
				{
					if (unite != null)
					{
						unite.charge(record);
					}
				}
			}
			unite.complete(groupe);
			unites_.put(unite.structure(), unite);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void exportInfluxDb() {
		PrintStream os;
		try {
			os = new PrintStream(new FileOutputStream(new File("C:\\dev\\outilssgdf_data\\export.txt")));
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
					groupe = u.nom_court();
				}
				else
				{
					groupe = v.nom_court();
				}
				v.exportInfluxDb(groupe,os);	
			});
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getActivites(List<RegistrePresenceActiviteHeure> activites, List<RegistrePresenceActiviteHeure> activites_jeunes, List<RegistrePresenceActiviteHeure> activites_chefs) {
		unites_.forEach((k,v) -> v.genere(activites, activites_jeunes, activites_chefs));
	}
}
