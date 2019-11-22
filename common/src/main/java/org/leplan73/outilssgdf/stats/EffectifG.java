package org.leplan73.outilssgdf.stats;
import java.util.Map;

import org.leplan73.outilssgdf.calcul.Groupe;

public class EffectifG {
	public String getGroupe() {
		return groupe.nomcomplet();
	}

	public int getJeunes(int annee) {
		return eff.get(annee).jeunes;
	}

	public int getResponsables(int annee) {
		return eff.get(annee).responsables;
	}

	public int getMembresassocies(int annee) {
		return eff.get(annee).membresAssocies;
	}

	public Groupe groupe;
	public Map<Integer, Effectifs> eff;
};
