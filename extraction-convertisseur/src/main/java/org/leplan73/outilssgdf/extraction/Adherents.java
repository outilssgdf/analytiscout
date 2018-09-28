package org.leplan73.outilssgdf.extraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.leplan73.outilssgdf.Check;

public class Adherents extends HashSet<Adherent> {
	
	private static final long serialVersionUID = 1L;
	
	public Set<Unite> unites(Colonnes colonnes)
	{
		Set<Unite> unites = new TreeSet<Unite>();
		this.forEach(adherent ->
		{
			int idUnite = colonnes.getUniteId();
			String unite = adherent.get(idUnite);
			unites.add(new Unite(unite));
		});
		return unites;
	}

	public void check(Colonnes colonnes, final Unite unite, List<Check> checks) {
		this.forEach(adherent ->
		{
			if (!adherent.isEmpty())
			adherent.check(colonnes, unite, checks);
		});
	}

	public Parents parents(Colonnes colonnes) {
		Parents parents = new Parents();
		this.forEach(adherent ->
		{
			Parent pere = new Parent();
			if (pere.init(colonnes, adherent, true))
			{
				String code = pere.getCode();
				if (parents.containsKey(code) == false)
				{
					parents.put(pere.getCode(), pere);
				}
				else
				{
					parents.get(code).fusionne(pere);
				}
			}
			adherent.setCodePapa(pere.getCode());
			
			Parent mere = new Parent();
			if (mere.init(colonnes, adherent, false))
			{
				String code = mere.getCode();
				if (parents.containsKey(code) == false)
				{
					parents.put(mere.getCode(), mere);
				}
				else
				{
					parents.get(code).fusionne(mere);
				}
			}
			adherent.setCodeMaman(mere.getCode());
		});
		return parents;
	}
}
