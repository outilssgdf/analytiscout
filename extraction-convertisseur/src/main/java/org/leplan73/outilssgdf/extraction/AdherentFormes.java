package org.leplan73.outilssgdf.extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.extraction.AdherentForme.ChefExtra;

public class AdherentFormes extends HashMap<Integer,AdherentForme>
{
	private static final long serialVersionUID = 1L;
	
	private List<AdherentForme> chefs_ = new ArrayList<AdherentForme>();
	
	public void addChef(int id, AdherentForme chef)
	{
		this.put(id, chef);
		chefs_.add(chef);
	}
	
	public void charge(ExtracteurHtml adherents, Map<String, ExtracteurHtml> extras)
	{
		adherents.getChefsList().forEach(chef ->
		{
			List<ChefExtra> extras2 = new ArrayList<ChefExtra>();
			if (extras != null)
			{
				extras.forEach((k,v) ->
				{
					AdherentForme qdir = (AdherentForme)v.getAdherents().get(chef.getCode());
					extras2.add(new ChefExtra(k, qdir, v.getColonnes()));
				});
			}
			addChef(chef.getCode(), chef);
		});
	}
}
