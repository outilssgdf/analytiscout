package org.leplan73.outilssgdf.alerte;

import org.leplan73.outilssgdf.extraction.Adherent;

public class Alerte {

	public enum Type
	{
		INCONNU,
		BAS,
		MOYEN,
		HAUT;
	}
	
	private Adherent adherent_;
	private String message_;
	private Type type_ = Type.INCONNU;
	
	public Alerte(Adherent adherent, Type type, String message)
	{
		adherent_ = adherent;
		type_ = type;
		message_ = message;
	}
	
	public Adherent getAdherent()
	{
		return adherent_;
	}
	
	public Type getType()
	{
		return type_;
	}
	
	public String getMessage()
	{
		return message_;
	}
}
