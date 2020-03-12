package org.leplan73.analytiscout.camp;

import java.util.Hashtable;
import java.util.Map;

import org.leplan73.analytiscout.calcul.UniteSimple;

public class Chef {
	private Map<String, String> data_ = new Hashtable<String, String>();
	private UniteSimple unite_;

	public void add(String champ, String text) {
		data_.put(champ, text);
	}
	
	@Override
	public String toString()
	{
		return ""+data_.size();
	}

	public String get(String champ) {
		return data_.get(champ);
	}
	
	public UniteSimple getUnite() {
		return unite_;
	}
	
	public String getCodeadherent()
	{
		return get("Code adhérent");
	}
	
	public String getNom()
	{
		return get("Nom");
	}
	
	public String getPrenom()
	{
		return get("Prénom");
	}
	
	public void complete() {
		String structure = get("Structure appartenance");
		unite_ = new UniteSimple(structure);
	}
}
