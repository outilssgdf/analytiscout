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
import org.influxdb.InfluxDB;
import org.leplan73.outilssgdf.Consts;

public class ExtracteurRegistrePresence {
	
	private Map<String, RegistrePresenceUnite> unites_ = new TreeMap<String, RegistrePresenceUnite>();
	private String groupe_;
	
	public Collection<RegistrePresenceUnite> getUnites()
	{
		return unites_.values();
	}
	
	public String getGroupe()
	{
		return groupe_;
	}
	
	public void construitActivites(List<RegistrePresenceActivite> activites)
	{
		unites_.forEach((k,v) -> v.construitActivites(activites));
	}
	
	public int charge(final InputStream stream, boolean anonymiser) throws IOException
	{
		RegistrePresenceUnite premiereUnite = null;
		RegistrePresenceUnite unite = null;
		int anneeDebut = -1;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream,Consts.ENCODING_WINDOWS));
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(reader);
			
			String groupe = null;
			for (CSVRecord record : records) {
				if (record.size() == 2)
				{
					if (unite != null)
					{
						// Fin d'unité
						unite.complete(groupe);
						unites_.put(unite.getCodestructure(), unite);
						unite = null;
					}
					
					RegistrePresenceUnite nunite = new RegistrePresenceUnite(record.get(0));
					if (unites_.containsKey(nunite.getCodestructure()))
					{
						unite = unites_.get(nunite.getCodestructure());
						if (premiereUnite == null) premiereUnite = unite;
					}
					else
					{
						unite = new RegistrePresenceUnite(record.get(0));
						if (premiereUnite == null) premiereUnite = unite;
					}
					boolean est_groupe = unite.estGroupe();
					if (est_groupe)
					{
						groupe = unite.nomcomplet();
					}
					else
					{
						RegistrePresenceUnite u = unites_.get(unite.getGroupe().getCode());
						if (u != null) groupe = u.nomcomplet();
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
			unites_.put(unite.getCodestructure(), unite);
		} catch (IOException e) {
			throw e;
		} finally {
		}
		if (premiereUnite != null)
		{
			groupe_ = premiereUnite.getNom();
		}
		return anneeDebut;
	}

	public void exportInfluxDb(InfluxDB influxDB) {
		unites_.forEach((k,v) -> {
			String groupe;
			boolean est_groupe = v.estGroupe();
			if (est_groupe == false)
			{
				RegistrePresenceUnite u = unites_.get(v.getCodegroupe());
				groupe = u.getNom();
			}
			else
			{
				groupe = v.getNom();
			}
			v.exportInfluxDb(groupe,influxDB);	
		});
	}

	public void exportInfluxDb(File out) throws FileNotFoundException {
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
					RegistrePresenceUnite u = unites_.get(v.getCodegroupe());
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
			throw e;
		}
	}

	public void getActivites(List<RegistrePresenceActiviteHeure> activites) {
		unites_.forEach((k,v) -> v.genere(activites));
	}

	public void getActivitesCecChef(String chef, RegistrePresenceUnite unite, int anneeDebut, List<RegistrePresenceActiviteHeure> activites_cec) {
		unite.genereCecChef(chef, anneeDebut, activites_cec);
	}
}
