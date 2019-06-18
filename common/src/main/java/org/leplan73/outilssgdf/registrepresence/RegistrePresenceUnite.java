package org.leplan73.outilssgdf.registrepresence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.csv.CSVRecord;
import org.leplan73.outilssgdf.calcul.UniteSimple;

public class RegistrePresenceUnite extends UniteSimple {

	private String groupe_;
	private List<RegistrePresenceActivite> activites_ = new ArrayList<RegistrePresenceActivite>();
	private boolean animateurs_;
	private boolean jeunes_;
	
	public RegistrePresenceUnite(String nom) {
		super(nom);
	}

	public void complete(String groupe) {
		groupe_ = groupe;
	}
	
	public void exportInfluxDb(String groupe, PrintStream os) {
		String prefix = "activite,unitecomplet="+nomcomplet_+",unite="+nom_+",structure="+structure_+",code_groupe="+code_groupe_+",groupe="+groupe;
		activites_.forEach(v -> v.exportInfluxDbReel(prefix,os));
		activites_.forEach(v -> v.exportInfluxDbForfaitaire(prefix,os));
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

	public int charge(CSVRecord record) {
		String nom = record.get(0);
		int anneeDebut = -1;
		if (nom.compareTo("Activités") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				RegistrePresenceActivite activite = new RegistrePresenceActivite(record.get(i));
				activites_.add(activite);
			}
		}
		if (nom.compareTo("Volume horaire forfaitaire") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				int index = activites_.size()-record.size()+i+1;
				activites_.get(index).setDureeFortaitaire(record.get(i));
			}
		}
		if (nom.compareTo("Date de début et de fin") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				String sdates[] = record.get(i).split("-");
				if (sdates.length == 1)
				{
					String ddate = sdates[0];
					String fdate = ddate;
					
					int index = activites_.size()-record.size()+i+1;
					activites_.get(index).dates(ddate,fdate);
				}
				else if (sdates.length == 2)
				{
			        String ddate = sdates[0];
					String fdate = sdates[1].substring(1);

					int index = activites_.size()-record.size()+i+1;
					activites_.get(index).dates(ddate,fdate);
				}	
			}
		}
		if (nom.compareTo("Heure de début et de fin") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				String sdates[] = record.get(i).split("-");
				if (sdates.length == 1)
				{
					String ddate = sdates[0];
					String fdate = ddate;
					
					int index = activites_.size()-record.size()+i+1;
					activites_.get(index).heures(ddate,fdate);
				}
				else if (sdates.length == 2)
				{
			        String ddate = sdates[0];
					String fdate = sdates[1].substring(1);
					
					int index = activites_.size()-record.size()+i+1;
					activites_.get(index).heures(ddate,fdate);
				}	
			}
		}
		if (nom.compareTo("Volume horaire réel") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				int index = activites_.size()-record.size()+i+1;
				activites_.get(index).complete(nom_);
			}
			anneeDebut = activites_.get(2).getDebutAnnee();
		}
		if (nom.compareTo("Volume horaire forfaitaire") == 0)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				int index = activites_.size()-record.size()+i+1;
				activites_.get(index).complete(nom_);
			}
			anneeDebut = activites_.get(2).getDebutAnnee();
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
			for (int i=2;i<record.size()-1;i++)
			{
				int index = activites_.size()-record.size()+i+1;
				activites_.get(index).setDescription(record.get(i));
			}
		}
		if (nom.compareTo("Total jeunes par activité") == 0)
		{
			jeunes_ = false;
		}
		if (animateurs_)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				int index = activites_.size()-record.size()+i+1;
				activites_.get(index).ajoutChef(record.get(0), record.get(i));
			}
		}
		if (jeunes_)
		{
			for (int i=2;i<record.size()-1;i++)
			{
				int index = activites_.size()-record.size()+i+1;
				activites_.get(index).ajoutJeune(record.get(0), record.get(i));
			}
		}
		return anneeDebut;
	}

	public void genereReel(List<RegistrePresenceActiviteHeure> activites, List<RegistrePresenceActiviteHeure> activitesForfaitaire, List<RegistrePresenceActiviteHeure> activites_jeunes, List<RegistrePresenceActiviteHeure> activites_chefs) {
		activites_.forEach(v -> v.generer(nomcomplet_, nom_, structure_, code_groupe_, groupe_, activites, activitesForfaitaire, activites_jeunes, activites_chefs));
	}

	public void genereCec(int anneeDebut, List<RegistrePresenceActiviteHeure> activites_heures_cec, List<RegistrePresenceActiviteHeure> activites_cec) {
		activites_.forEach(v -> v.genereCec(nomcomplet_, nom_, structure_, code_groupe_, groupe_, anneeDebut, activites_heures_cec, activites_cec));
	}

	public void construitActivites(List<RegistrePresenceActivite> activites) {
		activites_.forEach(v -> activites.add(v));
	}

	public void genereCecChef(String chef, int anneeDebut, List<RegistrePresenceActiviteHeure> activites_cec) {
		activites_.forEach(v -> v.genereCecChef(chef, nomcomplet_, nom_, structure_, code_groupe_, groupe_, anneeDebut, activites_cec));
	}

	public Set<String> getChefs() {
		Set<String> chefs = new TreeSet<String>();
		activites_.forEach(v -> v.getChefs(chefs));
		return chefs;
	}
}
