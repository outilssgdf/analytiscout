package org.leplan73.analytiscout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Params extends Properties {
	
	private static final long serialVersionUID = 1L;
	
	static private Params this_;
	static private Logger logger_;
	
	static private Date dateDebutSaison_;
	static private Date dateFinSaison_;
	static private Date dateDebutCamp_;
	static private Date dateLimiteJeune_;
	static private Date dateLimiteJeuneSuivant_;
    
	static {
		this_ = new Params();
		logger_ = LoggerFactory.getLogger(Params.class);
	}
	
	static public void init()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.MONTH)+1;
		int y = cal.get(Calendar.YEAR);
		logger_.debug("Mois="+m+"/Année="+y);

		try {
			dateDebutSaison_ = simpleDateFormat.parse("01/09/"+(y-1));
			dateFinSaison_ = simpleDateFormat.parse("31/08/"+y);
		} catch (ParseException e) {
			logger_.error(e.getLocalizedMessage());
		}
		
		if (m <= 6 || m >= 9)
		{
			// hors camp
			try {
				dateDebutCamp_ = simpleDateFormat.parse(m <= 6 ? "01/07/"+y : "01/07/"+(y+1));
				dateLimiteJeune_ = simpleDateFormat.parse(m <= 6 ? "31/12/"+(y-1) : "31/12/"+y);
				dateLimiteJeuneSuivant_ = simpleDateFormat.parse(m <= 6 ? "31/12/"+(y-1+1) : "31/12/"+(y+1));
			} catch (ParseException e) {
				logger_.error(e.getLocalizedMessage());
			}
		}
		else
		{
			// pendant la période des camps
			try {
				dateDebutCamp_ = simpleDateFormat.parse("01/07/"+y);
				dateLimiteJeune_ = simpleDateFormat.parse("31/12/"+(y-1));
				dateLimiteJeuneSuivant_ = simpleDateFormat.parse("31/12/"+(y-1+1));
			} catch (ParseException e) {
				logger_.error(e.getLocalizedMessage());
			}
		}
	}
	
	static public void set(String nom, String value)
	{
		this_.put(nom, value);
	}
	
	static public String get(String nom)
	{
		return this_.getProperty(nom);
	}
	
	static public String get(String nom, String defaut)
	{
		return this_.getProperty(nom, defaut);
	}
	
	static public boolean getb(String nom, boolean defaut)
	{
		String v = this_.getProperty(nom, String.valueOf(defaut));
		return Boolean.parseBoolean(v);
	}

	public static Date getDateDebutSaison() {
		return dateDebutSaison_;
	}

	public static Date getDateFinSaison() {
		return dateFinSaison_;
	}

	public static Date getDateDebutCamp() {
		return dateDebutCamp_;
	}

	public static Date getDateLimiteJeune() {
		return dateLimiteJeune_;
	}

	public static Date getDateLimiteJeuneSuivant() {
		return dateLimiteJeuneSuivant_;
	}
}
