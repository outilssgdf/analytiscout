package org.leplan73.analytiscout.alerte;

import org.leplan73.analytiscout.extraction.Adherent;

public class Alerte {

	public static final String ALERTE_TYPE_AGE = "Age";
	public static final String ALERTE_TYPE_QUALIFICATION = "Qualification";
	public static final String ALERTE_TYPE_JS = "JS";
	public static final String ALERTE_TYPE_INSCRIPTION = "Inscription";
	
	public enum Severite
	{
		INCONNUE,
		BASSE,
		MOYENNE,
		HAUTE;
	}
	
	private Adherent adherent_;
	private String message_;
	private String type_;
	private Severite severite_ = Severite.INCONNUE;
	
	public Alerte(Adherent adherent, Severite severite, String type, String message)
	{
		adherent_ = adherent;
		severite_ = severite;
		type_ = type;
		message_ = message;
	}
	
	public Adherent getAdherent()
	{
		return adherent_;
	}
	
	public Severite getSeverite()
	{
		return severite_;
	}
	
	public int getSeveritenum()
	{
		return severite_.ordinal();
	}
	
	public String getType()
	{
		return type_;
	}
	
	public String getMessage()
	{
		return message_;
	}
}
