package org.leplan73.outilssgdf;

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
	
	static private Date dateDebutCamp_;
	static private Date dateLimiteJeune_;
    
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
		
		if (m <= 6 || m >= 9)
		{
			// hors camp
			try {
				dateDebutCamp_ = simpleDateFormat.parse(m <= 6 ? "01/07/"+y : "01/07/"+(y+1));
				dateLimiteJeune_ = simpleDateFormat.parse(m <= 6 ? "31/12/"+(y-1) : "31/12/"+y);
			} catch (ParseException e) {
				logger_.error(e.getLocalizedMessage());
			}
		}
		else
		{
			try {
				dateDebutCamp_ = simpleDateFormat.parse("01/07/"+y);
			} catch (ParseException e) {
				logger_.error(e.getLocalizedMessage());
			}
		}
	}
	
	static public String get(String nom)
	{
		return this_.getProperty(nom);
	}
	
	static public String get(String nom, String defaut)
	{
		return this_.getProperty(nom, defaut);
	}

	public static Date getDateDebutCamp() {
		return dateDebutCamp_;
	}

	public static Date getDateLimiteJeune() {
		return dateLimiteJeune_;
	}
}
