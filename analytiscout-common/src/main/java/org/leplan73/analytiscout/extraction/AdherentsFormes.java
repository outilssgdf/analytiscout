package org.leplan73.analytiscout.extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.leplan73.analytiscout.ExtracteurExtraHtml;
import org.leplan73.analytiscout.ExtracteurIndividusHtml;
import org.leplan73.analytiscout.extraction.AdherentForme.ExtraKey;

public class AdherentsFormes extends HashMap<Integer,AdherentForme>
{
	private static final long serialVersionUID = 1L;
	
	private List<AdherentForme> chefs_ = new ArrayList<AdherentForme>();
	
	public void addChef(int id, AdherentForme chef)
	{
		this.put(id, chef);
		chefs_.add(chef);
	}
	
	public void charge(ExtracteurIndividusHtml adherents, Map<ExtraKey, ExtracteurExtraHtml> extras)
	{
		adherents.getChefsList().forEach(chef ->
		{
			addChef(chef.getCode(), chef);
		});
	}
}
