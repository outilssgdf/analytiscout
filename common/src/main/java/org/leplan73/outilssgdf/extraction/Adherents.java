package org.leplan73.outilssgdf.extraction;

import java.util.HashMap;
import java.util.List;

import org.leplan73.outilssgdf.Check;
import org.leplan73.outilssgdf.calcul.Unite;

public class Adherents extends HashMap<Integer,Adherent> {
	
	private static final long serialVersionUID = 1L;
	
	public void check(Colonnes colonnes, final Unite unite, List<Check> checks) {
		this.forEach((id,adherent) ->
		{
			adherent.check(colonnes, unite, checks);
		});
	}

	public Parents parents(Colonnes colonnes) {
		Parents parents = new Parents();
		this.forEach((id,adherent) ->
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
