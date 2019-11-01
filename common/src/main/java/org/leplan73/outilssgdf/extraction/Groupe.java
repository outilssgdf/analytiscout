package org.leplan73.outilssgdf.extraction;

import org.leplan73.outilssgdf.ExtracteurIndividusHtml;

public class Groupe {
	private ExtracteurIndividusHtml individus_;
	private AdherentsFormes compas_;
	
	public Groupe(ExtracteurIndividusHtml individus, AdherentsFormes compas) {
		individus_ = individus;
		compas_ = compas;
	}

	public ExtracteurIndividusHtml getIndividus() {
		return individus_;
	}

	public AdherentsFormes getCompas() {
		return compas_;
	}
}
