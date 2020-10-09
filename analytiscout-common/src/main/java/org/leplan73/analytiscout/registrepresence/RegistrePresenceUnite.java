package org.leplan73.analytiscout.registrepresence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.csv.CSVRecord;
import org.influxdb.InfluxDB;
import org.leplan73.analytiscout.Anonymizer;
import org.leplan73.analytiscout.calcul.UniteSimple;

public class RegistrePresenceUnite extends UniteSimple {

	private List<RegistrePresenceActivite> activites_ = new ArrayList<RegistrePresenceActivite>();
	private boolean animateurs_;
	private boolean jeunes_;
	
	public RegistrePresenceUnite(String nom) {
		super(nom);
	}

	public void complete(String groupe) {
		groupe_.setNom(groupe);
	}
	
	public void anonymiserStructure(Map<String, String> tableDeTraductionNoms, Map<String, String> tableDeTraductionCode, Map<String, String> tableDeTraductionNom, int codeStructure, Anonymizer anon) {
		super.anonymiserStructure(tableDeTraductionNoms, tableDeTraductionCode, codeStructure);
		if (tableDeTraductionNom != null) activites_.forEach(activite -> activite.anonymiser(tableDeTraductionNom, anon));
	}

	public void anonymiserNoms(Map<String, String> tableDeTraductionNom, Anonymizer anon) {
		activites_.forEach(activite -> activite.anonymiserNoms(tableDeTraductionNom, anon));
	}

	public void exportInfluxDb(String groupe, PrintStream os) {
		String prefix = "activite,unitecomplet="+nomcomplet_+",unite="+nom_+",structure="+codeStructure_+",code_groupe="+this.getCodegroupe()+",groupe="+groupe;
		activites_.forEach(v -> v.exportInfluxDbReel(prefix,os));
		activites_.forEach(v -> v.exportInfluxDbForfaitaire(prefix,os));
	}

	public void exportInfluxDb(String groupe, InfluxDB influxDB) {

		activites_.forEach(v -> v.exportInfluxDbReel(influxDB, "activites_reel", nomcomplet_, nom_, codeStructure_, this.getCodegroupe(), groupe));
		activites_.forEach(v -> v.exportInfluxDbForfaitaire(influxDB, "activites_forfaitaire", nomcomplet_, nom_, codeStructure_, this.getCodegroupe(), groupe));
	}
	
	@Override
	public String toString()
	{
		return nom_ + " / " + activites_.size();
	}
	
	public List<RegistrePresenceActivite> getActivites()
	{
		return activites_;
	}

	public Integer charge(CSVRecord record) {
		String nom = record.get(0);
		Integer anneeDebut = null;
		if (nom.compareTo("Activités") == 0)
		{
			for (int i=2;i<record.size();i++)
			{
				RegistrePresenceActivite activite = new RegistrePresenceActivite(record.get(i));
				activites_.add(activite);
			}
		}
		if (nom.compareTo("Volume horaire forfaitaire") == 0)
		{
			for (int i=2;i<record.size();i++)
			{
				int index = activites_.size()-record.size()+i+1;
				activites_.get(index).setDureeFortaitaire(record.get(i));
			}
		}
		if (nom.compareTo("Date de début et de fin") == 0)
		{
			for (int i=2;i<record.size();i++)
			{
				String sdates[] = record.get(i).split("-");
				if (sdates.length == 1)
				{
					String ddate = sdates[0];
					String fdate = ddate;
					
					int index = activites_.size()-record.size()+i;
					activites_.get(index).dates(ddate,fdate);
				}
				else if (sdates.length == 2)
				{
			        String ddate = sdates[0];
					String fdate = sdates[1].substring(1);

					int index = activites_.size()-record.size()+i;
					activites_.get(index).dates(ddate,fdate);
				}	
			}
		}
		if (nom.compareTo("Heure de début et de fin") == 0)
		{
			for (int i=2;i<record.size();i++)
			{
				String sdates[] = record.get(i).split("-");
				if (sdates.length == 1)
				{
					String ddate = sdates[0];
					String fdate = ddate;
					
					int index = activites_.size()-record.size()+i;
					activites_.get(index).heures(ddate,fdate);
				}
				else if (sdates.length == 2)
				{
			        String ddate = sdates[0];
					String fdate = sdates[1].substring(1);
					
					int index = activites_.size()-record.size()+i;
					activites_.get(index).heures(ddate,fdate);
				}	
			}
		}
		if (nom.compareTo("Volume horaire réel") == 0)
		{
			for (int i=2;i<record.size();i++)
			{
				int index = activites_.size()-record.size()+i;
				activites_.get(index).complete(this);
			}
			if (activites_.isEmpty() == false)
				anneeDebut = activites_.get(0).getDebutAnnee();
		}
		if (nom.compareTo("Volume horaire forfaitaire") == 0)
		{
			for (int i=2;i<record.size();i++)
			{
				int index = activites_.size()-record.size()+i;
				activites_.get(index).complete(this);
			}
			if (activites_.isEmpty() == false)
				anneeDebut = activites_.get(0).getDebutAnnee();
		}
		if (nom.compareTo("Animateurs") == 0)
		{
			animateurs_ = true;
		}
		if (nom.compareTo("Jeunes") == 0)
		{
			animateurs_ = false;
			jeunes_ = true;
		}
		if (nom.compareTo("Description de l'activité") == 0)
		{
			for (int i=2;i<record.size();i++)
			{
				int index = activites_.size()-record.size()+i;
				activites_.get(index).setDescription(record.get(i));
			}
		}
		if (nom.compareTo("Total jeunes par activité") == 0)
		{
			jeunes_ = false;
		}
		if (animateurs_)
		{
			for (int i=2;i<record.size();i++)
			{
				int index = activites_.size()-record.size()+i;
				activites_.get(index).ajoutChef(record.get(0), record.get(i));
			}
		}
		if (jeunes_)
		{
			for (int i=2;i<record.size();i++)
			{
				int index = activites_.size()-record.size()+i;
				activites_.get(index).ajoutJeune(record.get(0), record.get(i));
			}
		}
		return anneeDebut;
	}

	public void genere(List<RegistrePresenceActiviteHeure> activites) {
		activites_.forEach(v -> v.generer(this, codeStructure_, groupe_, activites));
	}

	public void construitActivites(List<RegistrePresenceActivite> activites) {
		activites_.forEach(v -> activites.add(v));
	}

	public void genereCecChef(String chef, int anneeDebut, List<RegistrePresenceActiviteHeure> activites_cec) {
		activites_.forEach(v -> v.genereCecChef(chef, this, codeStructure_, groupe_, anneeDebut, activites_cec));
	}

	public Set<String> getChefs() {
		Set<String> chefs = new TreeSet<String>();
		activites_.forEach(v -> v.getChefs(chefs));
		return chefs;
	}

	public void purger(int anneeFin) {
		activites_.removeIf((RegistrePresenceActivite activite) -> activite.getDebutAnnee() != anneeFin);
	}
}
