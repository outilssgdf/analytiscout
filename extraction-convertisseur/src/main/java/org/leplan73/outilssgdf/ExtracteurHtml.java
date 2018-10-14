package org.leplan73.outilssgdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.extraction.Adherent;
import org.leplan73.outilssgdf.extraction.Adherents;
import org.leplan73.outilssgdf.extraction.Chef;
import org.leplan73.outilssgdf.extraction.Chef.ChefExtra;
import org.leplan73.outilssgdf.extraction.Colonnes;
import org.leplan73.outilssgdf.extraction.Consts;
import org.leplan73.outilssgdf.extraction.Parents;
import org.leplan73.outilssgdf.extraction.Unite;
import org.leplan73.outilssgdf.extraction.Unites;

public class ExtracteurHtml {
	
	protected Adherents adherents_;
	protected Parents parents_;
	protected Colonnes colonnes_;
	protected Unites unites_;
	private Map<String, ExtracteurHtml> extras_;
	
	public ExtracteurHtml() throws ExtractionException, IOException, JDOMException {
	}
	
	public ExtracteurHtml(String fichier) throws ExtractionException, IOException, JDOMException {
		charge(fichier);
	}
	
	public ExtracteurHtml(String fichier, Map<String, ExtracteurHtml> extras) throws ExtractionException, IOException, JDOMException {
		extras_ = extras;
		charge(fichier);
	}

	public Adherents getAdherents()
	{
		return adherents_;
	}
	
	public List<Adherent> getAdherentsList() {
		List<Adherent> adhrerents = new ArrayList<Adherent>();
		adherents_.forEach((k,v) -> {
			adhrerents.add(v);
		});
		return adhrerents;
	}
	
	public List<Unite> getUnitesList() {
		List<Unite> unites = new ArrayList<Unite>();
		unites_.forEach((k,v) -> {
			unites.add(v);
		});
		return unites;
	}
	
	public List<Chef> getChefsList() {
		List<Chef> adhrerents = new ArrayList<Chef>();
		adherents_.forEach((k,v) -> {
			if (v.getChef() == 1)
				adhrerents.add((Chef)v);
		});
		return adhrerents;
	}
	
	public Unites getUnites()
	{
		return unites_;
	}
	
	public Parents getParents()
	{
		return parents_;
	}
	
	public Colonnes getColonnes()
	{
		return colonnes_;
	}
	
	public void charge(final String path) throws ExtractionException, IOException, JDOMException
	{
   		FileInputStream excelFile = new FileInputStream(new File(path));
   		charge(excelFile);
		excelFile.close();
	}
	
	private int construitColonnes(final InputStream stream) throws ExtractionException, JDOMException, IOException
	{
		SAXBuilder builder = new SAXBuilder();
		
		org.jdom2.Document docx = builder.build(stream);
		return construitColonnes(docx);
	}
	
	private int construitColonnes(final org.jdom2.Document docx) throws ExtractionException, JDOMException, IOException
	{
		XPathFactory xpfac = XPathFactory.instance();
		
        // Scan des colonnes
        colonnes_ = new Colonnes();
		XPathExpression<?> xpac = xpfac.compile("tbody/tr[1]/td/text()");
		 List<?> resultsc = xpac.evaluate(docx);
		 Iterator<?> firstCellIterator = resultsc.iterator();
		 int nbColumns = 0;
		 while (firstCellIterator.hasNext()) {
			 Object result = firstCellIterator.next();
				if(result instanceof Text)
				{
					Text resultElement = (Text) result;
					colonnes_.add(nbColumns++, resultElement.getText());
				}
		 }
		        
        // Calcul des codes
        colonnes_.calculCodes();
        
        return nbColumns;
	}
	
	private void complete(String groupe)
	{
		parents_ = adherents_.parents(colonnes_);
		parents_.complete();
		
		final String groupef = groupe;
		unites_ = new Unites();
		adherents_.forEach((code,ad) ->
		{
			ad.setGroupe(groupef);
			String unite = ad.getUnite();
			Unite uniteObj = unites_.computeIfAbsent(unite, k -> new Unite(unite,0));
			uniteObj.ajouter(ad.getJeune(), ad.getChef());
			uniteObj.setGroupe(groupef);
		});
		
		adherents_.forEach((code,ad) ->
		{
			if (ad.getChef() > 0)
			{
				Chef chef = (Chef)ad;
				String unite = ad.getUnite();
				Unite uniteObj = unites_.computeIfAbsent(unite, k -> new Unite(unite, ad.getFonction()));
				
				uniteObj.setFonction(ad.getFonction());
				
				boolean animsf = chef.getQualif("animsf").getOk();
				boolean dirsf = chef.getQualif("dirsf").getOk();
				
				boolean apf = chef.getFormation("apf").getOk();
				boolean tech = chef.getFormation("tech").getOk(); 
				boolean appro = chef.getFormation("appro").getOk();
				boolean appro_anim = chef.getFormation("appro_anim").getOk();
				boolean appro_accueil = chef.getFormation("appro_accueil").getOk();
				if (dirsf || appro || appro_accueil || appro_anim)
				{
					if (dirsf) uniteObj.addDirsf();
					else
					{
						if (appro) uniteObj.addAppro(true);
						if (appro_accueil) uniteObj.addAppro(false);
						if (appro_anim) uniteObj.addAppro(true);
					}
				}
				else
				{
					if (animsf) uniteObj.addAnimsf();
					else
					if (tech)
					{
						uniteObj.addTech();
					}
					else
					{
						if (apf) uniteObj.addApf();
						else uniteObj.addAutres();
					}
				}
				
				if (chef.getFormation("cham").getOk()) uniteObj.addCham();
				if (chef.getFormation("staf").getOk()) uniteObj.addStaf();
				
				if (chef.getDiplome("psc1").getOk()) uniteObj.addPsc1();
				if (chef.getDiplome("afps").getOk()) uniteObj.addAfps();
				if (chef.getDiplome("bafa").getOk()) uniteObj.addBafa();
				if (chef.getDiplome("bafd").getOk()) uniteObj.addBafd();
				if (chef.getDiplome("buchettes2").getOk()) uniteObj.addBuchettes();
				if (chef.getDiplome("buchettes3").getOk()) uniteObj.addBuchettes();
				if (chef.getDiplome("buchettes4").getOk()) uniteObj.addBuchettes();
			}
		});
	}
	
	public void charge(List<InputStream> streams) throws ExtractionException, IOException, JDOMException
	{
		int nbColumns = streams.size() >= 1 ? construitColonnes(streams.get(0)) : 0;
		
		// Chargement des lignes d'adherents
        adherents_ = new Adherents();

        String groupe = null;
		
		for (InputStream stream : streams) 
		{
			stream.reset();
        	XPathFactory xpfac = XPathFactory.instance();
    		SAXBuilder builder = new SAXBuilder();
			org.jdom2.Document docx = builder.build(stream);
	        
	        XPathExpression<?> xpa = xpfac.compile("tbody/tr[position() > 1]/td");
	        
	        List<?> results = xpa.evaluate(docx);
	        
	        int index = 0;
	        Adherent adherent = null;
			Iterator<?> iter = results.iterator();
			while (iter.hasNext())
			{
				if (index % nbColumns == 0)
				{
					adherent = new Adherent(colonnes_);
				}
				
				Object result = iter.next();
				Element resultElement = (Element) result;
	            adherent.add(index % nbColumns, resultElement.getText());
	            index++;
	        	if (index % nbColumns == 0)
	        	{
	            	adherent.init();
					int code = adherent.getCode();
					if (adherent.getFonction() == Consts.CODE_VIOLETS)
					{
						groupe = adherent.getUnite();
					}
					if (adherent.getChef() == 1)
					{
						Chef chef = new Chef(adherent);
						chef.init();
						adherents_.put(adherent.getCode(), chef);
						
						List<ChefExtra> extras2 = new ArrayList<ChefExtra>();
						if (extras_ != null)
						{
							extras_.forEach((k,v) ->
							{
								Adherent qdir = v.getAdherents().get(code);
								if (qdir != null)
								{
									extras2.add(new ChefExtra(k, (Chef)qdir, v.getColonnes()));
								}
							});
							chef.addExtras(extras2);
						}
					}
					else
						adherents_.put(adherent.getCode(), adherent);
	        		adherent = null;
	        	}
			}
		}
		complete(groupe);
	}
	
	public void charge(final InputStream stream) throws ExtractionException, IOException, JDOMException
	{
		XPathFactory xpfac = XPathFactory.instance();
		SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document docx = builder.build(stream);
        
        int nbColumns = construitColonnes(docx);

        // Chargement des lignes d'adherents
        adherents_ = new Adherents();
        
        XPathExpression<?> xpa = xpfac.compile("tbody/tr[position() > 1]/td");
        
        List<?> results = xpa.evaluate(docx);
        
        int index = 0;
        String groupe = null;
        Adherent adherent = null;
		Iterator<?> iter = results.iterator();
		while (iter.hasNext())
		{
			if (index % nbColumns == 0)
			{
				adherent = new Adherent(colonnes_);
			}
			
			Object result = iter.next();
			Element resultElement = (Element) result;
            adherent.add(index % nbColumns, resultElement.getText());
            index++;
        	if (index % nbColumns == 0)
        	{
            	adherent.init();
				int code = adherent.getCode();
				if (adherent.getFonction() == Consts.CODE_VIOLETS)
				{
					groupe = adherent.getUnite();
				}
				if (adherent.getChef() == 1)
				{
					Chef chef = new Chef(adherent);
					chef.init();
					adherents_.put(adherent.getCode(), chef);
					
					List<ChefExtra> extras2 = new ArrayList<ChefExtra>();
					if (extras_ != null)
					{
						extras_.forEach((k,v) ->
						{
							Adherent qdir = v.getAdherents().get(code);
							if (qdir != null)
							{
								extras2.add(new ChefExtra(k, (Chef)qdir, v.getColonnes()));
							}
						});
						chef.addExtras(extras2);
					}
				}
				else
					adherents_.put(adherent.getCode(), adherent);
        		adherent = null;
        	}
		}
        
		complete(groupe);
	}
}
