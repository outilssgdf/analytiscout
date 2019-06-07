package org.leplan73.outilssgdf.extraction;

import java.util.Map;
import java.util.TreeMap;

public class ColonnesCamps {

	private Map<Integer,String> map_ = new TreeMap<Integer,String>();

	public void add(int id, String nom) {
		map_.put(id,nom);
	}
	
	public String get(int id) {
		return map_.get(id);
	}
}
