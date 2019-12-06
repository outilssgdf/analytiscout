package org.leplan73.outilssgdf.camp;

public class ChefCamp {
	private Chef chef;
	private Camp camp;

	public ChefCamp(Chef v, Camp k) {
		chef = v;
		camp = k;
	}
	
	public Chef getChef() { return chef; }
	public Camp getCamp() { return camp; }
}
