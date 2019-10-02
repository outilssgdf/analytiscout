package org.leplan73.outilssgdf.alerte;

import org.leplan73.outilssgdf.extraction.Adherent;

public class Alerte {

	public static final String ALERTE_TYPE_AGE = "Age";
	public static final String ALERTE_TYPE_QUALIFICATION = "Qualification";
	
	public enum Severite
	{
		INCONNU,
		BAS,
		MOYEN,
		HAUT;
	}
	
	private Adherent adherent_;
	private String message_;
	private String type_;
	private Severite severite_ = Severite.INCONNU;
	
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
