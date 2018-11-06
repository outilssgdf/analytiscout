package org.leplan73.outilssgdf.extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurHtml;

public class AdherentFormes extends HashMap<Integer,AdherentForme>
{
	private static final long serialVersionUID = 1L;
	
	private List<AdherentForme> chefs_ = new ArrayList<AdherentForme>();
	
	public void addChef(int id, AdherentForme chef)
	{
		this.put(id, chef);
		chefs_.add(chef);
	}
	
	public void charge(ExtracteurHtml adherents, Map<String, ExtracteurExtraHtml> extras)
	{
		adherents.getChefsList().forEach(chef ->
		{
			addChef(chef.getCode(), chef);
		});
	}
}
