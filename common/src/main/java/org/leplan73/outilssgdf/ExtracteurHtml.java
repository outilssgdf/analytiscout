package org.leplan73.outilssgdf;

import java.io.ByteArrayInputStream;
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
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.calcul.Unite;
import org.leplan73.outilssgdf.calcul.Unites;
import org.leplan73.outilssgdf.extraction.Adherent;
import org.leplan73.outilssgdf.extraction.AdherentForme;
import org.leplan73.outilssgdf.extraction.AdherentForme.ChefExtra;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.Adherents;
import org.leplan73.outilssgdf.extraction.Colonnes;
import org.leplan73.outilssgdf.extraction.Parents;

public class ExtracteurHtml {
	
	protected Adherents adherents_;
	protected Parents parents_;
	protected Colonnes colonnes_;
	protected Unites unites_;
	protected String groupe_;
	protected boolean marins_;
	
	private Map<ExtraKey, ExtracteurExtraHtml> extras_;
	
	public ExtracteurHtml() throws ExtractionException, IOException, JDOMException {
	}
	
	public ExtracteurHtml(InputStream input, Map<ExtraKey, ExtracteurExtraHtml> extras, boolean age) throws ExtractionException, IOException, JDOMException {
		extras_ = extras;
		charge(input, age);
	}
	
	public ExtracteurHtml(File fichier, Map<ExtraKey, ExtracteurExtraHtml> extras, boolean age) throws ExtractionException, IOException, JDOMException {
		extras_ = extras;
		charge(fichier, age);
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
	
	public List<AdherentForme> getChefsList() {
		List<AdherentForme> adherents = new ArrayList<AdherentForme>();
		adherents_.forEach((k,v) -> {
			if (v.getChef() == 1)
				adherents.add((AdherentForme)v);
		});
		return adherents;
	}
	
	public List<AdherentForme> getCompasList() {
		List<AdherentForme> adherents = new ArrayList<AdherentForme>();
		adherents_.forEach((k,v) -> {
			if (v.getCompa() == 1)
				adherents.add((AdherentForme)v);
		});
		return adherents;
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
	
	public String getGroupe()
	{
		return groupe_;
	}
	
	public boolean getMarins()
	{
		return marins_;
	}
	
	public void charge(final String donnnes, boolean age) throws ExtractionException, IOException, JDOMException
	{
   		ByteArrayInputStream excelFile = new ByteArrayInputStream(donnnes.getBytes());
   		charge(excelFile, age);
		excelFile.close();
	}
	
	public void charge(final File fichier, boolean age) throws ExtractionException, IOException, JDOMException
	{
   		FileInputStream excelFile = new FileInputStream(fichier);
   		charge(excelFile, age);
		excelFile.close();
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
	
	private void complete()
	{
		parents_ = adherents_.parents(colonnes_);
		parents_.complete();
		
		unites_ = new Unites();
		adherents_.forEach((code,ad) ->
		{
			String unite = ad.getUnite();
			Unite uniteObj = unites_.computeIfAbsent(unite, k -> new Unite(unite, ad.getCodestructure(), ad.getFonction()));
			uniteObj.ajouter(ad.getJeune(), ad.getChef());
		});
		
		adherents_.forEach((code,ad) ->
		{
			String unite = ad.getUnite();
			Unite uniteObj = unites_.computeIfAbsent(unite, k -> new Unite(unite, ad.getCodestructure(), ad.getFonction()));
			uniteObj.setCode(ad.getFonction());
			
			if (ad.getChef() > 0)
			{
				AdherentForme chef = (AdherentForme)ad;

				boolean dirsf = chef.getQualif("dirsf").getDefini();
				boolean dirsfQualifie = chef.getQualif("dirsf").getDefini() && chef.getQualif("dirsf").getTitulaire();
				boolean dirsfNonQualifie = chef.getQualif("dirsf").getDefini() && !chef.getQualif("dirsf").getTitulaire();
				boolean animsfQualifie = chef.getQualif("animsf").getDefini() && chef.getQualif("animsf").getTitulaire();
				boolean animsfNonQualifie = chef.getQualif("animsf").getDefini() && !chef.getQualif("animsf").getTitulaire();
				
				boolean compa = chef.getCompa()  == 1 ? true : false;
				
				boolean apf = chef.getFormation("apf").getOk();
				boolean tech = chef.getFormation("tech").getOk(); 
				boolean appro = chef.getFormation("appro").getOk();
				boolean appro_anim = chef.getFormation("appro_anim").getOk();
				boolean appro_accueil = chef.getFormation("appro_accueil").getOk();
				
				boolean aqualif = false;
				boolean aqualifsf = false;
				
				if (dirsf)
				{
					if (uniteObj.getDirsfqnonq() == 0)
					{
						uniteObj.addDirsfsqnonq();
					}
				}
				if (dirsfQualifie)
				{
					if (uniteObj.getDirsf() == 0)
					{
						uniteObj.addDirsf();
						aqualif=true;
						aqualifsf=true;
					}
					else
					{
						animsfQualifie = true;
						aqualifsf = false;
					}
				}
				if (animsfQualifie && !aqualifsf && !compa)
				{
					uniteObj.addAnimsf();
					aqualif=true;
				}
				if ((animsfNonQualifie || dirsfNonQualifie) && !animsfQualifie)
				{
					uniteObj.addStagiaire();
					aqualif=true;
				}
				if (appro)
				{
					uniteObj.addAppro(true);
					aqualif=true;
				}
				if (appro_accueil)
				{
					uniteObj.addAppro(false);
					aqualif=true;
				}
				if (appro_anim)
				{
					uniteObj.addAppro(true);
					aqualif=true;
				}
				if (tech)
				{
					uniteObj.addTech();
					aqualif=true;
				}
				if (apf)
				{
					uniteObj.addApf();
					aqualif=true;
				}
				if (!aqualif) uniteObj.addAutres();
				
				if (!compa && (animsfQualifie || dirsfQualifie))
				{
					uniteObj.addQualifannee();
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
	
	public void charge(List<InputStream> streams, boolean age) throws ExtractionException, IOException, JDOMException
	{
		// Chargement des lignes d'adherents
        adherents_ = new Adherents();

		for (InputStream stream : streams) 
		{
			chargeStream(stream, age);
		}
		complete();
	}
	
	public void charge(final InputStream stream, boolean age) throws ExtractionException, IOException, JDOMException
	{
		chargeStream(stream, age);
		complete();
	}
	
	private List<ChefExtra> extra(int code)
	{
		if (extras_ != null)
		{
			List<ChefExtra> extra = new ArrayList<ChefExtra>();
			extras_.forEach((key,map) -> 
			{
				List<AdherentForme> adherents = map.getAdherents();
				adherents.forEach(adherent ->
				{
					if (adherent.getCode() == code)
					{
						extra.add(new ChefExtra(key, adherent, map.getColonnes()));
					}
				});
			});
			return extra;
		}
		return null;
	}
	
	private void chargeStream(final InputStream stream, boolean age) throws JDOMException, IOException, ExtractionException
	{
		XPathFactory xpfac = XPathFactory.instance();
		SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document docx = builder.build(stream);
        
        int nbColumns = construitColonnes(docx);

        // Chargement des lignes d'adherents
        adherents_ = new Adherents();
        
        XPathExpression<?> xpa = xpfac.compile("tbody/tr[position() > 1]/td");
        
        List<?> results = xpa.evaluate(docx);
        
        int codeMax = -1;
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
            	adherent.init(age);
				int code = adherent.getCode();
				if (adherent.getFonction() >= Consts.CODE_VIOLETS)
				{
					if (adherent.getFonction() > codeMax)
					{
						codeMax = adherent.getFonction();
						groupe_ = adherent.getUnite();
					}
				}
				if (adherent.getMarin())
				{
					marins_ = true;
				}
				if (adherent.getChef() == 1)
				{
					AdherentForme chef = new AdherentForme(adherent);
					chef.init(age);
					adherents_.put(adherent.getCode(), chef);
					
					List<ChefExtra> extras2 = extra(code);
					if (extras_ != null)
					{
						chef.addExtras(extras2);
					}
				}
				else if (adherent.getCompa() == 1)
				{
					AdherentForme compa = new AdherentForme(adherent);
					compa.init(age);
					adherents_.put(adherent.getCode(), compa);
					
					List<ChefExtra> extras2 = extra(code);
					if (extras_ != null)
					{
						compa.addExtras(extras2);
					}
				}
				else
					adherents_.put(adherent.getCode(), adherent);
        		adherent = null;
        	}
		}
	}

	public void calculGlobal(Global global)
	{
		adherents_.forEach((code,ad) ->
		{
			String unite = ad.getUnite();
			if (ad.getChef() > 0)
			{
				AdherentForme chef = (AdherentForme)ad;
				boolean dirsfdef = chef.getQualif("dirsf").getDefini();
				boolean dirsfQualifie = chef.getQualif("dirsf").getOk();
				
				boolean animsfQualifie = chef.getQualif("animsf").getDefini() && chef.getQualif("animsf").getTitulaire();
				boolean animsfNonQualifie = chef.getQualif("animsf").getDefini() && !chef.getQualif("animsf").getTitulaire();
				
				boolean apf = chef.getFormation("apf").getOk();
				boolean tech = chef.getFormation("tech").getOk(); 
				boolean appro = chef.getFormation("appro").getOk();
				boolean appro_anim = chef.getFormation("appro_anim").getOk();
				boolean appro_accueil = chef.getFormation("appro_accueil").getOk();
				
				if (dirsfdef)
				{
					global.addRgdirsf();
				}
				if (animsfQualifie)
				{
					global.addAnimsfQualifie();
				}
			}
		});
	}
}
