package org.leplan73.analytiscout.registrepresence;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.leplan73.analytiscout.calcul.Groupe;
import org.leplan73.analytiscout.calcul.UniteSimple;

public class RegistrePresenceActivite {
	private String description_;
	private UniteSimple unite_;
	private String type_;
	private String ddate_;
	private String fdate_;
	private String dheure_;
	private String fheure_;
	private Date debut_;
	private Date fin_;
	private Map<String, Integer> presencesChefs_ = new TreeMap<String, Integer>();
	private Map<String, Integer> presencesJeunes_ = new TreeMap<String, Integer>();
	private long dureeFortaitaire_;

	private final static SimpleDateFormat parser_ = new SimpleDateFormat("EEEEE dd/MM/yyyy HH:mm");

	public RegistrePresenceActivite(String type) {
		type_ = type;
	}

	public void dates(String ddate, String fdate) {
		ddate_ = ddate;
		fdate_ = fdate;
	}

	public void heures(String dheure, String fheure) {
		dheure_ = dheure;
		fheure_ = fheure;
	}

	public void setDescription(String description) {
		description_ = description;
	}

	public String getType() {
		return type_;
	}

	public String getDescription() {
		return description_;
	}

	public UniteSimple getUnite() {
		return unite_;
	}

	public String getDebut() {
		return ddate_ + " " + dheure_;
	}

	public int getDebutAnnee() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(debut_);
		return calendar.get(Calendar.YEAR);
	}

	public long getDebutnum() {
		return debut_.getTime();
	}
	
	public Set<String> getChefs()
	{
		return presencesChefs_.keySet();
	}
	
	public double getParticipationchefs() {
		return (double)getPresencechefs() / (double)presencesChefs_.size();
	}
	
	public double getParticipationjeunes() {
		return (double)getPresencejeunes() / (double)presencesJeunes_.size();
	}
	
	public int getRemplissage() {
		return presencesChefs_.size() + presencesJeunes_.size();
	}
	
	public int getPresencechefs() {
		AtomicInteger n = new AtomicInteger();
		presencesChefs_.forEach((k,v) ->
		{
			if (v.intValue() == 1)
				n.incrementAndGet();
		});
		return n.get();
	}
	
	public int getPresencejeunes() {
		AtomicInteger n = new AtomicInteger();
		presencesJeunes_.forEach((k,v) ->
		{
			if (v.intValue() == 1)
				n.incrementAndGet();
		});
		return n.get();
	}

	public String getFin() {
		return fdate_ + " " + fheure_;
	}

	@Override
	public String toString() {
		return type_ + " / " + debut_.toString() + " / " + fin_.toString();
	}

	public void complete(UniteSimple unite) {
		try {
			unite_ = unite;
			synchronized(parser_)
			{
				debut_ = parser_.parse(ddate_ + " " + dheure_);
				fin_ = parser_.parse(fdate_ + " " + fheure_);
			}
		} catch (ParseException e) {
		}
	}

	public void ajoutChef(String nom, String presence) {
		if (presence.compareTo("X") == 0) {
			presencesChefs_.put(nom, 1);
		}
		if (presence.compareTo("") == 0) {
			presencesChefs_.put(nom, 0);
		}
	}

	public void ajoutJeune(String nom, String presence) {
		if (presence.compareTo("X") == 0) {
			presencesJeunes_.put(nom, 1);
		}
		if (presence.compareTo("") == 0) {
			presencesJeunes_.put(nom, 0);
		}
	}
	
	public void exportInfluxDbForfaitaire(String prefix, PrintStream os) {

		long debut = debut_.getTime()/1000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(debut*1000);
		presencesChefs_.forEach((k,v) -> {
			if (v== 1)
			{
				os.print(prefix.replaceAll("\\s", "-"));
				os.print(",nom=");
				os.print(type_.replaceAll("\\s", "-"));
				os.print(",personne=");
				os.print(k.replaceAll("\\s", "-"));
				os.print(",mois=");
				os.print(cal.get(Calendar.MONTH)+1);
				os.print(",chef=1 forfait="+dureeFortaitaire_+" ");
				os.print(debut);
				os.println();
			}
		});
		presencesJeunes_.forEach((k,v) -> {
			if (v== 1)
			{
				os.print(prefix.replaceAll("\\s", "-"));
				os.print(",nom=");
				os.print(type_.replaceAll("\\s", "-"));
				os.print(",personne=");
				os.print(k.replaceAll("\\s", "-"));
				os.print(",mois=");
				os.print(cal.get(Calendar.MONTH)+1);
				os.print(",chef=0 forfait="+dureeFortaitaire_+" ");
				os.print(debut);
				os.println();
			}
		});
	}

	public void exportInfluxDbReel(String prefix, PrintStream os) {
		long debut = debut_.getTime()/1000;
		long fin = fin_.getTime()/1000;
		
		AtomicLong t = new AtomicLong(debut);
		while(t.longValue() < fin)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(t.longValue()*1000);
			presencesChefs_.forEach((k,v) -> {
				os.print(prefix.replaceAll("\\s", "-"));
				os.print(",nom=");
				os.print(type_.replaceAll("\\s", "-"));
				os.print(",personne=");
				os.print(k.replaceAll("\\s", "-"));
				os.print(",mois=");
				os.print(cal.get(Calendar.MONTH)+1);
				os.print(",chef=1 presence="+v+" ");
				os.print(t.longValue());
				os.println();
			});
			presencesJeunes_.forEach((k,v) -> {
				os.print(prefix.replaceAll("\\s", "-"));
				os.print(",nom=");
				os.print(type_.replaceAll("\\s", "-"));
				os.print(",personne=");
				os.print(k.replaceAll("\\s", "-"));
				os.print(",mois=");
				os.print(cal.get(Calendar.MONTH)+1);
				os.print(",chef=0 presence="+v+" ");
				os.print(t.longValue());
				os.println();
			});
			t.addAndGet(3600);
		}
	}

	public void exportInfluxDbReel(InfluxDB influxDB, String serie, String nomcomplet, String unite, String structure, String code_groupe, String groupe) {
		long debut = debut_.getTime()/1000;
		long fin = fin_.getTime()/1000;
		
		AtomicLong t = new AtomicLong(debut);
		while(t.longValue() < fin)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(t.longValue()*1000);
			presencesChefs_.forEach((k,v) -> {
				Point point = Point.measurement(serie).time(t.longValue()*1000, TimeUnit.MILLISECONDS).tag("nomcomplet", nomcomplet).tag("unite", unite).tag("structure", structure).tag("code_groupe", code_groupe).tag("groupe", groupe)
						.tag("type", type_)
						.tag("personne", k)
						.tag("mois", ""+cal.get(Calendar.MONTH)+1)
						.tag("chef", "1")
						.addField("presence", v)
						.build();
				influxDB.write(point);
			});
			presencesJeunes_.forEach((k,v) -> {
				Point point = Point.measurement(serie).time(t.longValue()*1000, TimeUnit.MILLISECONDS).tag("nomcomplet", nomcomplet).tag("unite", unite).tag("structure", structure).tag("code_groupe", code_groupe).tag("groupe", groupe)
						.tag("type", type_)
						.tag("personne", k)
						.tag("mois", ""+cal.get(Calendar.MONTH)+1)
						.tag("chef", "0")
						.addField("presence", v)
						.build();
				influxDB.write(point);
			});
			t.addAndGet(3600);
		}
	}

	public void exportInfluxDbForfaitaire(InfluxDB influxDB, String serie, String nomcomplet, String unite, String structure, String code_groupe, String groupe) {
		long debut = debut_.getTime()/1000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(debut*1000);
		presencesChefs_.forEach((k,v) -> {
			if (v== 1)
			{
				Point point = Point.measurement(serie).time(debut*1000, TimeUnit.MILLISECONDS).tag("nomcomplet", nomcomplet).tag("unite", unite).tag("structure", structure).tag("code_groupe", code_groupe).tag("groupe", groupe)
						.tag("nom", "type_")
						.tag("personne", k)
						.tag("mois", ""+cal.get(Calendar.MONTH)+1)
						.tag("chef", "1")
						.addField("forfait", dureeFortaitaire_)
						.build();
				influxDB.write(point);
			}
		});
		presencesJeunes_.forEach((k,v) -> {
			if (v== 1)
			{
				Point point = Point.measurement(serie).time(debut*1000, TimeUnit.MILLISECONDS).tag("nomcomplet", nomcomplet).tag("unite", unite).tag("structure", structure).tag("code_groupe", code_groupe).tag("groupe", groupe)
						.tag("nom", "type_")
						.tag("personne", k)
						.tag("mois", ""+cal.get(Calendar.MONTH)+1)
						.tag("chef", "0")
						.addField("forfait", dureeFortaitaire_)
						.build();
				influxDB.write(point);
			}
		});
	}

	public void generer(UniteSimple unite, String structure, Groupe groupe, List<RegistrePresenceActiviteHeure> activites) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(debut_.getTime());
		presencesChefs_.forEach((nom,v) -> {
			if (v.intValue() == 1)
			{
				long d = (fin_.getTime() - debut_.getTime())/1000/3600;
				activites.add(new RegistrePresenceActiviteHeure(unite, structure, groupe, type_, nom, true, cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR), d, dureeFortaitaire_, description_));
			}
		});
		presencesJeunes_.forEach((nom,v) -> {
			if (v.intValue() == 1)
			{
				long d = (fin_.getTime() - debut_.getTime())/1000/3600;
				activites.add(new RegistrePresenceActiviteHeure(unite, structure, groupe, type_, nom, false, cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR), d, dureeFortaitaire_, description_));
			}
		});
	}

	public void genereCec(UniteSimple unite, String structure, Groupe groupe, int anneeDebut, List<RegistrePresenceActiviteHeure> activites_cec) {
		if (this.getDebutAnnee() == anneeDebut)
		{
			presencesChefs_.forEach((nom,v) -> {
				if (v.intValue() == 1)
				{
					long d = (fin_.getTime() - debut_.getTime())/1000/3600;
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(debut_.getTime());
					activites_cec.add(new RegistrePresenceActiviteHeure(unite, structure, groupe, type_, nom, true, cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR), d, dureeFortaitaire_, description_));
				}
			});
	}
	}

	public void genereCecChef(String chef, UniteSimple unite, String structure, Groupe groupe, int anneeDebut, List<RegistrePresenceActiviteHeure> activites_cec)
	{
		if (this.getDebutAnnee() == anneeDebut)
		{
			presencesChefs_.forEach((nom,v) -> {
				if (v.intValue() == 1 && nom.compareTo(chef) == 0)
				{
					long d = (fin_.getTime() - debut_.getTime())/1000/3600;
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(debut_.getTime());
					activites_cec.add(new RegistrePresenceActiviteHeure(unite, structure, groupe, type_, nom, true, cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR), d, dureeFortaitaire_, description_));
				}
			});
		}
	}

	public void getChefs(Set<String> chefs) {
		presencesChefs_.forEach((nom,v) -> {
			chefs.add(nom);
		});
	}

	public void setDureeFortaitaire(String duree) {
		dureeFortaitaire_ = Integer.parseInt(duree);
	}
	
	public long getDureeforfaitaire() {
		return dureeFortaitaire_;
	}
	
	public long getDureeforfaitairetotal() {
		return dureeFortaitaire_*(getPresencechefs()+getPresencejeunes());
	}
	
	public long getDureeforfaitairetotaljeune() {
		return dureeFortaitaire_*getPresencejeunes();
	}
	
	public long getDureereel() {
		long v = (fin_.getTime() - debut_.getTime())/1000/3600;
		return v;
	}
	
	public long getDureereeltotal() {
		long v = (fin_.getTime() - debut_.getTime())/1000/3600;
		return v*(getPresencechefs()+getPresencejeunes());
	}
	
	public long getDureereeltotaljeune() {
		long v = (fin_.getTime() - debut_.getTime())/1000/3600;
		return v*getPresencejeunes();
	}
}
