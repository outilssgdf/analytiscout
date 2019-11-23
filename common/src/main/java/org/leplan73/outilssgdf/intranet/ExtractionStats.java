package org.leplan73.outilssgdf.intranet;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.leplan73.outilssgdf.calcul.Groupe;
import org.leplan73.outilssgdf.stats.Effectifs;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class ExtractionStats extends ExtractionIntranet {

	public Map<Groupe,Map<Integer, Effectifs>> extract(int structure) throws IOException
	{
		Map<Groupe,Map<Integer, Effectifs>> effectifs = new TreeMap<Groupe,Map<Integer,Effectifs>>();
		
		HtmlAnchor a = page.getAnchorByHref("/Specialisation/Sgdf/Pilotage/Adherents/EffectifTotalCategorieMembre.aspx");
		HtmlPage page3 = a.click(false, false, false, false, true, false);
		
		DomElement b = page3.getElementById("ctl00_ctl00_MainContent_EntreeContent__entree__pilotageEntree__btnPopupStructure__btnPopup__button");
		page3 = b.click();
		
		HtmlTextInput te = (HtmlTextInput)page3.getElementById("ctl00_Popup__recherche__tbCodeStructure");
		te.setValueAttribute(formatStructure(structure));
		
		a = (HtmlAnchor)page3.getElementById("ctl00_Popup__recherche__btnRechercher");
		page3 = a.click();
		
		b = page3.getElementById("ctl00_Popup__recherche__pnlResultats").getFirstElementChild().getFirstElementChild().getFirstElementChild().getFirstElementChild();
		Iterable<DomElement> nodes4 = b.getChildElements();
		int index4=0;
		
		boolean structureOk = false;
		for (DomNode n4 : nodes4)
		{
			if (index4 != 0)
			{
				DomNode n411 = null;
				HtmlRadioButtonInput radio = null;
				DomNodeList<DomNode> nodes41 = n4.getChildNodes();
				int nb41 = nodes41.getLength();
				for (int j=0;j<nb41;j++)
				{
					n411 = nodes41.get(j).getFirstChild();
					if (n411 != null)
					{
						DomNode n4111 = n411.getNextSibling();
						if (n4111 == null)
						{
							n4111 = n411.getFirstChild();
						}
						if (n4111 instanceof HtmlRadioButtonInput)
						{
							radio = (HtmlRadioButtonInput)n4111;
							radio.click();
							structureOk = true;
							break;
						}
					}
				}
				if (structureOk)
					break;
			}
			index4++;
		}
		
		b = page3.getElementById("ctl00_Popup__recherche__btnValider");
		page3 = b.click();
		
		b = page3.getElementById("ctl00_ctl00_MainContent_EntreeContent__entree__pilotageEntree__rbSaisonFinSaison");
		page3 = b.click();
		
		b = page3.getElementById("ctl00_ctl00_MainContent__btnValider");
		page3 = b.click();
		
		b = page3.getElementById("ctl00_ctl00_MainContent_DivsContent__table__gvResultatLibelle").getFirstElementChild();
		Iterable<DomElement> nodes2 = b.getChildElements();
		int nn = b.getChildElementCount();
		int index=0;
		Map<Integer, Groupe> indexGroupes = new TreeMap<Integer,Groupe>();
		for (DomNode n : nodes2)
		{
			if (index != 0 && index != 1 && index != nn-1)
			{
				Groupe g = new Groupe(n.asText());
				indexGroupes.put(index,  g);
				
				Map<Integer,Effectifs> eff = new TreeMap<Integer,Effectifs>();
				effectifs.put(g, eff);
			}
			index++;
		}
		
		b = page3.getElementById("ctl00_ctl00_MainContent_DivsContent__table__gvResultat").getFirstElementChild();
		nodes2 = b.getChildElements();
		nn = b.getChildElementCount();
		index=0;
		for (DomNode n : nodes2)
		{
			if (index != 0 && index != 1 && index != nn-1)
			{
				DomNodeList<DomNode> nodes3 = n.getChildNodes();
				int nbAnnees = nodes3.getLength()/4;
				for (int j=0;j<nbAnnees;j++)
				{
					int nr = Integer.parseInt(nodes3.get(4*j).getNextSibling().asText());
					int nj = Integer.parseInt(nodes3.get(4*j+1).getNextSibling().asText());
					int nma = Integer.parseInt(nodes3.get(4*j+2).getNextSibling().asText());
					Groupe groupe = indexGroupes.get(index);
					
					Map<Integer, Effectifs> effs = effectifs.get(groupe);
					Effectifs eff = effs.get(2019-j);
					if (eff == null)
					{
						eff = new Effectifs();
						eff.responsables = nr;
						eff.jeunes = nj;
						eff.membresAssocies = nma;
						effs.put(2019-j, eff);
					}
				}
			}
			index++;
		}
		return effectifs;
	}
}
