package org.leplan73.analytiscout.registrepresence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.influxdb.InfluxDB;
import org.leplan73.analytiscout.Anonymizer;
import org.leplan73.analytiscout.Consts;

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
		
		if (anonymiser)
		{
			Anonymizer anon = new Anonymizer();
			anon.init();
			
			Map<String, String> tableDeTraductionNoms = new TreeMap<String, String>();
			Map<String, String> tableDeTraductionCode = new TreeMap<String, String>();
			
			Map<String, List<String>> unites = new TreeMap<String, List<String>>();
			unites_.forEach((code, activiteUnite) ->
			{
				String c = activiteUnite.getBranche() + "-" + activiteUnite.getCodegroupe();
				List<String> us = unites.get(c);
				if (us == null)
				{
					us = new ArrayList<String>();
					unites.put(c, us);
				}
				int index = us.indexOf(activiteUnite.getCodestructure());
				if (index == -1)
					us.add(activiteUnite.getCodestructure());
			});
			
			AtomicInteger groupeId = new AtomicInteger();
			
			unites_.forEach((id, activiteUnite) ->
			{
				String structure = activiteUnite.getCodegroupe();
				String uniteIt = activiteUnite.getNom();
				
				if (tableDeTraductionNoms.containsKey(uniteIt) == false)
				{
					if (uniteIt.startsWith("TERRITOIRE "))
					{
						uniteIt = "TERRITOIRE "+ "UNIVERS";
						tableDeTraductionNoms.put(activiteUnite.getNom(), uniteIt);
					}
					else if (uniteIt.startsWith("GROUPE "))
					{
						uniteIt = "GROUPE A"+ groupeId.incrementAndGet();
						tableDeTraductionNoms.put(activiteUnite.getNom(), uniteIt);
						tableDeTraductionCode.put(structure, uniteIt);
					}
				}
			});
			
			unites_.forEach((id, activiteUnite) ->
			{
				String structure = activiteUnite.getNom();
				String c = activiteUnite.getBranche() + "-" + activiteUnite.getCodegroupe();
				
				if (structure.startsWith("RÉSEAU IMPEESA"))
				{
					structure = "RÉSEAU IMPEESA "+ tableDeTraductionCode.get(activiteUnite.getCodegroupe());
					tableDeTraductionNoms.put(activiteUnite.getNom(), structure);
				}
				else
				{
					String branche = activiteUnite.getBranche();
					List<String> us = unites.get(c);
					if (branche.compareTo("F") == 0)
					{
						if (us.size() == 1)
						{
							tableDeTraductionNoms.put(activiteUnite.getNom(), "FARFADETS "+tableDeTraductionCode.get(activiteUnite.getCodegroupe()));
						}
						else
						{
							int index = us.indexOf(activiteUnite.getCodestructure());
							tableDeTraductionNoms.put(activiteUnite.getNom(), "FARFADETS "+tableDeTraductionCode.get(activiteUnite.getCodegroupe())+" UNITE "+(index+1));
						}
					}
					if (branche.compareTo("LJ") == 0)
					{
						if (us.size() == 1)
						{
							tableDeTraductionNoms.put(activiteUnite.getNom(), "LOUVETEAUX JEANNETTES "+tableDeTraductionCode.get(activiteUnite.getCodegroupe()));
						}
						else
						{
							int index = us.indexOf(activiteUnite.getCodestructure());
							tableDeTraductionNoms.put(activiteUnite.getNom(), "LOUVETEAUX JEANNETTES "+tableDeTraductionCode.get(activiteUnite.getCodegroupe())+" UNITE "+(index+1));
						}
					}
					if (branche.compareTo("SG") == 0)
					{
						if (us.size() == 1)
						{
							tableDeTraductionNoms.put(activiteUnite.getNom(), "SCOUTS GUIDES "+tableDeTraductionCode.get(activiteUnite.getCodegroupe()));
						}
						else
						{
							int index = us.indexOf(activiteUnite.getCodestructure());
							tableDeTraductionNoms.put(activiteUnite.getNom(), "SCOUTS GUIDES "+tableDeTraductionCode.get(activiteUnite.getCodegroupe())+" UNITE "+(index+1));
						}
					}
					if (branche.compareTo("PC") == 0)
					{
						if (us.size() == 1)
						{
							tableDeTraductionNoms.put(activiteUnite.getNom(), "PIONNNERS CARAVELLES "+tableDeTraductionCode.get(activiteUnite.getCodegroupe()));
						}
						else
						{
							int index = us.indexOf(activiteUnite.getCodestructure());
							tableDeTraductionNoms.put(activiteUnite.getNom(), "PIONNNERS CARAVELLES "+tableDeTraductionCode.get(activiteUnite.getCodegroupe())+" UNITE "+(index+1));
						}
					}
					if (branche.compareTo("C") == 0)
					{
						if (us.size() == 1)
						{
							tableDeTraductionNoms.put(activiteUnite.getNom(), "COMPAGNONS "+tableDeTraductionCode.get(activiteUnite.getCodegroupe()));
						}
						else
						{
							int index = us.indexOf(activiteUnite.getCodestructure());
							tableDeTraductionNoms.put(activiteUnite.getNom(), "COMPAGNONS "+tableDeTraductionCode.get(activiteUnite.getCodegroupe())+" UNITE "+(index+1));
						}
					}
				}
			}
			);
			
			int codePremiereUnite = Integer.parseInt(premiereUnite.getCodestructure());
			codePremiereUnite+=100000000;
			
			premiereUnite.anonymiserStructure(tableDeTraductionNoms, tableDeTraductionCode, codePremiereUnite, anon);
			
			List<RegistrePresenceUnite> adds = new ArrayList<RegistrePresenceUnite>();
			unites_.forEach((id, activiteUnite) ->
			{
				adds.add(activiteUnite);
				
				// Code structure
				int codeStructure = Integer.parseInt(activiteUnite.getCodestructure());
				codeStructure+=100000000;
				
				// Nom de la structure
				activiteUnite.anonymiserStructure(tableDeTraductionNoms, tableDeTraductionCode, codeStructure, anon);
			});
			unites_.clear();
			adds.forEach(activiteUnite-> unites_.put(activiteUnite.getNom(), activiteUnite));
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

	public void purger(int anneeFin) {
		unites_.forEach((k,v) -> v.purger(anneeFin));
	}
}
