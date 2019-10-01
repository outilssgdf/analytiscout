package org.leplan73.outilssgdf.alerte;

import java.util.ArrayList;

import org.leplan73.outilssgdf.alerte.Alerte.Type;
import org.leplan73.outilssgdf.extraction.Adherent;

public class Alertes extends ArrayList<Alerte> {

	private static final long serialVersionUID = 1L;

	public void ajouter(Adherent adherent, Type type, String message)
	{
		this.add(new Alerte(adherent, type, message));
	}
}
