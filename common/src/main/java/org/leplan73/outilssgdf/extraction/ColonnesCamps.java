package org.leplan73.outilssgdf.extraction;

import java.util.Map;
import java.util.TreeMap;

import org.leplan73.outilssgdf.ExtractionException;

public class ColonnesCamps {

	private Map<Integer, String> ids_ = new TreeMap<Integer, String>();
	private Map<String, Integer> map_ = new TreeMap<String, Integer>();
	
	private int Structuresorganisatrices = -1;
	private int StructuresParticipantes = -1;
	private int dates = -1;
	private int NomDuDirecteur = -1;
	private int Telephone = -1;
	private int Etat = -1;
	private int campMarin = -1;
	private int compagnon = -1;
	private int international = -1;
	private int campItinerant = -1;

	public void add(int id, String nom) {
		ids_.put(id, nom);
		map_.put(nom, id);
	}
	
	public void calculCodes() throws ExtractionException
	{
		ids_.forEach((key, value) ->
		{
			if (ids_.get(key).compareTo("Structures organisatrices") == 0)
			{
				Structuresorganisatrices = key;
			}
		});
	}
}
