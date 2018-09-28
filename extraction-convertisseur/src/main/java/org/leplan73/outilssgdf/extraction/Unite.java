package org.leplan73.outilssgdf.extraction;

public class Unite implements Comparable<Unite>
{
	private String nom_;
	
	public Unite(String nom)
	{
		nom_ = nom;
	}
	
	public String nom()
	{
		return nom_;
	}

	public int compareTo(String o) {
		return this.nom_.compareTo(o);
	}

	@Override
	public int compareTo(Unite o) {
		return this.nom_.compareTo(o.nom_);
	}
	
	@Override
	public String toString() {
		return nom_;
	}
}
