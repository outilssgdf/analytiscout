package org.leplan73.analytiscout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.analytiscout.alerte.Alertes;
import org.leplan73.analytiscout.calcul.Global;
import org.leplan73.analytiscout.calcul.Unite;
import org.leplan73.analytiscout.calcul.Unites;
import org.leplan73.analytiscout.extraction.Adherent;
import org.leplan73.analytiscout.extraction.AdherentForme;
import org.leplan73.analytiscout.extraction.AdherentForme.ChefExtra;
import org.leplan73.analytiscout.extraction.AdherentForme.ExtraKey;
import org.leplan73.analytiscout.extraction.Adherents;
import org.leplan73.analytiscout.extraction.ColonnesAdherents;
import org.leplan73.analytiscout.extraction.Parents;

public class ExtracteurIndividusHtml {
	
	protected Adherents adherents_;
	protected Parents parents_;
	protected ColonnesAdherents colonnes_;
	protected Unites unites_;
	protected String groupe_;
	protected boolean marins_;
	
	private Map<ExtraKey, ExtracteurExtraHtml> extras_;

	public ExtracteurIndividusHtml() {}
	
	public ExtracteurIndividusHtml(ColonnesAdherents colonnes) {
		colonnes_ = colonnes;
		adherents_ = new Adherents();
	}
	
	public ExtracteurIndividusHtml(List<InputStream> inputs, Map<ExtraKey, ExtracteurExtraHtml> extras, boolean age, boolean anonymiser, Set<String> retirer_codes) throws ExtractionException, IOException, JDOMException {
		extras_ = extras;
		charge(inputs, age, anonymiser, retirer_codes);
	}
	
	public ExtracteurIndividusHtml(InputStream input, Map<ExtraKey, ExtracteurExtraHtml> extras, boolean age, boolean anonymiser, Set<String> retirer_codes) throws ExtractionException, IOException, JDOMException {
		extras_ = extras;
		charge(input, age, anonymiser, retirer_codes);
	}

	public Map<String, ExtracteurIndividusHtml> genereGroupes() {
		Map<String, ExtracteurIndividusHtml> groupes = new HashMap<String,ExtracteurIndividusHtml>();
		adherents_.forEach((code,adherent) ->
		{
			String groupe = adherent.getCodegroupe();
			ExtracteurIndividusHtml individus = groupes.get(groupe);
			if (individus == null)
			{
				individus = new ExtracteurIndividusHtml(this.colonnes_);
				groupes.put(groupe, individus);
			}
			individus.ajouterAdherent(adherent);
		});
		
		groupes.forEach((k,v) -> {
			v.complete();
		});
		return groupes;
	}

	private void ajouterAdherent(Adherent adherent) {
		adherents_.put(adherent.getCode(), adherent);
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
			if (v.getChef())
				adherents.add((AdherentForme)v);
		});
		return adherents;
	}
	
	public List<AdherentForme> getCompasList() {
		List<AdherentForme> adherents = new ArrayList<AdherentForme>();
		adherents_.forEach((k,v) -> {
			if (v.getCompa() == true && v.getChef() == false)
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
	
	public ColonnesAdherents getColonnes()
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
	
	private int construitColonnes(final org.jdom2.Document docx) throws ExtractionException, JDOMException, IOException
	{
		XPathFactory xpfac = XPathFactory.instance();
		
        // Scan des colonnes
        colonnes_ = new ColonnesAdherents();
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
		adherents_.forEach((code,adherent) ->
		{
			if (adherent.getChef())
			{
				AdherentForme chef = (AdherentForme)adherent;
				chef.finalise();
			}
		});
		
		parents_ = adherents_.parents(colonnes_);
		parents_.complete();
		
		unites_ = new Unites();
		
		AtomicInteger codeMax = new AtomicInteger();
		adherents_.forEach((code,adherent) ->
		{
			String unite = adherent.getUnite();
			Unite uniteObj = unites_.computeIfAbsent(unite, k -> new Unite(unite, adherent.getCodestructure(), adherent.getFonction()));
			uniteObj.ajouter(adherent.getJeune(), adherent.getChef());

			if (adherent.getFonction() >= Consts.CODE_VIOLETS)
			{
				if (adherent.getFonction() > codeMax.get())
				{
					codeMax.set(adherent.getFonction());
					groupe_ = adherent.getUnite();
				}
			}
			if (adherent.getMarin())
			{
				marins_ = true;
			}
		});
		
		adherents_.forEach((code,ad) ->
		{
			String unite = ad.getUnite();
			Unite uniteObj = unites_.computeIfAbsent(unite, k -> new Unite(unite, ad.getCodestructure(), ad.getFonction()));
			uniteObj.setCode(ad.getFonction());
			
			if (ad.getChef())
			{
				AdherentForme chef = (AdherentForme)ad;

				boolean dirsf = chef.getQualif("dirsf").getDefini();
				boolean dirsfQualifie = chef.getQualif("dirsf").getDefini() && chef.getQualif("dirsf").getTitulaire();
				boolean dirsfNonQualifie = chef.getQualif("dirsf").getDefini() && !chef.getQualif("dirsf").getTitulaire();
				boolean animsfQualifie = chef.getQualif("animsf").getDefini() && chef.getQualif("animsf").getTitulaire();
				boolean animsfNonQualifie = chef.getQualif("animsf").getDefini() && !chef.getQualif("animsf").getTitulaire();
				
				boolean compa = chef.getCompa();
				
				boolean apf = chef.getFormation("apf").getOk();
				boolean apf_chefs = chef.getFormation("apf_chefs").getOk();
				boolean apf_rg = chef.getFormation("apf_rg").getOk();
				boolean apf_sp = chef.getFormation("apf_sp").getOk();
				boolean apf_rldr = chef.getFormation("apf_rldr").getOk();
				boolean apf_aavsc = chef.getFormation("apf_aavsc").getOk();
				
				boolean tech = chef.getFormation("tech").getOk(); 
				boolean appro = chef.getFormation("appro").getOk();
				boolean appro_anim = chef.getFormation("appro_anim").getOk();
				boolean appro_accueil = chef.getFormation("appro_accueil").getOk();

				boolean module_appro_accueil_scoutisme = chef.getFormation("module_appro_accueil_scoutisme").getOk();
				boolean module_animateur_scoutisme_campisme = chef.getFormation("module_animateur_scoutisme_campisme").getOk();
				boolean module_appro_surveillant_baignade = chef.getFormation("module_appro_surveillant_baignade").getOk();
				
				boolean aqualif = false;
				boolean aqualifsf = false;
				
				boolean responsable_farfadets = chef.getFormation("responsable_farfadets").getOk();
				if (responsable_farfadets)
				{
					uniteObj.addResponsablefarfadets();
				}
				
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
				if ((animsfNonQualifie || dirsfNonQualifie) && (!animsfQualifie && !dirsfQualifie))
				{
					uniteObj.addStagiaire();
					aqualif=true;
				}
				if (appro)
				{
					uniteObj.addAppro();
					aqualif=true;
				}
				if (module_appro_accueil_scoutisme)
				{
					uniteObj.addModuleApproAccueilScoutisme();
				}
				if (module_animateur_scoutisme_campisme)
				{
					uniteObj.addModuleAnimateurScoutismeCampisme();
				}
				if (module_appro_surveillant_baignade)
				{
					uniteObj.addModuleApproSurveillantBaignade();
				}
				if (appro_accueil)
				{
					uniteObj.addApproAccueil();
					aqualif=true;
				}
				if (appro_anim)
				{
					uniteObj.addApproAnim();
					aqualif=true;
				}
				if (tech)
				{
					uniteObj.addTech();
					aqualif=true;
				}
				if (apf||apf_chefs||apf_rg)
				{
					uniteObj.addApf();
					aqualif=true;
				}
				if (apf_sp) uniteObj.addApf();;
				if (apf_rldr) uniteObj.addApf();
				if (apf_aavsc) uniteObj.addApf();
				
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
	
	private void anonymiserDonnees() {
		Anonymizer anon = new Anonymizer();
		anon.init();
		
		Map<String, String> tableDeTraductionNoms = new TreeMap<String, String>();
		Map<String, String> tableDeTraductionCode = new TreeMap<String, String>();
		
		Map<String, List<String>> unites = new TreeMap<String, List<String>>();
		adherents_.forEach((id, adherent) ->
		{
			String c = adherent.getBranche() + "-" + adherent.getCodegroupe();
			List<String> us = unites.get(c);
			if (us == null)
			{
				us = new ArrayList<String>();
				unites.put(c, us);
			}
			int index = us.indexOf(adherent.getCodestructure());
			if (index == -1)
				us.add(adherent.getCodestructure());
		});
		
		AtomicInteger groupeId = new AtomicInteger();
		adherents_.forEach((id, adherent) ->
		{
			String groupe = adherent.getCodegroupe();
			String unite = adherent.getUnite();
			
			if (tableDeTraductionNoms.containsKey(unite) == false)
			{
				if (unite.startsWith("TERRITOIRE "))
				{
					unite = "TERRITOIRE "+ "UNIVERS";
					tableDeTraductionNoms.put(adherent.getUnite(), unite);
				}
				else if (unite.startsWith("GROUPE "))
				{
					unite = "GROUPE A"+ groupeId.incrementAndGet();
					tableDeTraductionNoms.put(adherent.getUnite(), unite);
					tableDeTraductionCode.put(groupe, unite);
				}
			}
		});
		
		adherents_.forEach((id, adherent) ->
		{
			String unite = adherent.getUnite();
			String c = adherent.getBranche() + "-" + adherent.getCodegroupe();
			
			if (unite.startsWith("RÉSEAU IMPEESA"))
			{
				unite = "RÉSEAU IMPEESA "+ tableDeTraductionCode.get(adherent.getCodegroupe());
				tableDeTraductionNoms.put(adherent.getUnite(), unite);
			}
			else
			{
				String branche = adherent.getBranche();
				List<String> us = unites.get(c);
				if (branche.compareTo("F") == 0)
				{
					if (us.size() == 1)
					{
						tableDeTraductionNoms.put(adherent.getUnite(), "FARFADETS "+tableDeTraductionCode.get(adherent.getCodegroupe()));
					}
					else
					{
						int index = us.indexOf(adherent.getCodestructure());
						tableDeTraductionNoms.put(adherent.getUnite(), "FARFADETS "+tableDeTraductionCode.get(adherent.getCodegroupe())+" UNITE "+(index+1));
					}
				}
				if (branche.compareTo("LJ") == 0)
				{
					if (us.size() == 1)
					{
						tableDeTraductionNoms.put(adherent.getUnite(), "LOUVETEAUX JEANNETTES "+tableDeTraductionCode.get(adherent.getCodegroupe()));
					}
					else
					{
						int index = us.indexOf(adherent.getCodestructure());
						tableDeTraductionNoms.put(adherent.getUnite(), "LOUVETEAUX JEANNETTES "+tableDeTraductionCode.get(adherent.getCodegroupe())+" UNITE "+(index+1));
					}
				}
				if (branche.compareTo("SG") == 0)
				{
					if (us.size() == 1)
					{
						tableDeTraductionNoms.put(adherent.getUnite(), "SCOUTS GUIDES "+tableDeTraductionCode.get(adherent.getCodegroupe()));
					}
					else
					{
						int index = us.indexOf(adherent.getCodestructure());
						tableDeTraductionNoms.put(adherent.getUnite(), "SCOUTS GUIDES "+tableDeTraductionCode.get(adherent.getCodegroupe())+" UNITE "+(index+1));
					}
				}
				if (branche.compareTo("PC") == 0)
				{
					if (us.size() == 1)
					{
						tableDeTraductionNoms.put(adherent.getUnite(), "PIONNNERS CARAVELLES "+tableDeTraductionCode.get(adherent.getCodegroupe()));
					}
					else
					{
						int index = us.indexOf(adherent.getCodestructure());
						tableDeTraductionNoms.put(adherent.getUnite(), "PIONNNERS CARAVELLES "+tableDeTraductionCode.get(adherent.getCodegroupe())+" UNITE "+(index+1));
					}
				}
				if (branche.compareTo("C") == 0)
				{
					if (us.size() == 1)
					{
						tableDeTraductionNoms.put(adherent.getUnite(), "COMPAGNONS "+tableDeTraductionCode.get(adherent.getCodegroupe()));
					}
					else
					{
						int index = us.indexOf(adherent.getCodestructure());
						tableDeTraductionNoms.put(adherent.getUnite(), "COMPAGNONS "+tableDeTraductionCode.get(adherent.getCodegroupe())+" UNITE "+(index+1));
					}
				}
			}
		}
		);

		AtomicInteger ai = new AtomicInteger(100000000);
		List<Adherent> adds = new ArrayList<Adherent>();
		adherents_.forEach((id, adherent) ->
		{
			adds.add(adherent);
			
			int code = ai.incrementAndGet();
			adherent.setCode(code);
			adherent.setNom(anon.prochainNom());
			adherent.setPrenom(anon.prochainPrenom());
			
			// Nom de la structure
			adherent.anonymiserStructure(tableDeTraductionNoms, tableDeTraductionCode);
			
			// Code structure
			int codeStructure = Integer.parseInt(adherent.getCodestructure());
			codeStructure+=100000000;
			adherent.setCodestructure(codeStructure);
		});
		adherents_.clear();
		adds.forEach(adherent-> adherents_.put(adherent.getCode(), adherent));
	}
	
	public void charge(List<InputStream> streams, boolean age, boolean anonymiser, Set<String> retirer_codes) throws ExtractionException, IOException, JDOMException
	{
        adherents_ = new Adherents();
		for (InputStream stream : streams) 
		{
			chargeStream(stream, age, retirer_codes);
		}
		if (anonymiser) {
			anonymiserDonnees();
		}
		complete();
	}
	
	public void charge(final InputStream stream, boolean age, boolean anonymiser) throws ExtractionException, IOException, JDOMException
	{
		charge(stream, age,anonymiser, null);
	}
	
	public void charge(final InputStream stream, boolean age, boolean anonymiser, Set<String> retirer_codes) throws ExtractionException, IOException, JDOMException
	{
        adherents_ = new Adherents();
		chargeStream(stream, age, retirer_codes);
		if (anonymiser) {
			anonymiserDonnees();
		}
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
						ChefExtra ex = new ChefExtra(key, adherent, map.getColonnes());
						extra.add(ex);
					}
				});
			});
			return extra;
		}
		return null;
	}
	
	private void chargeStream(final InputStream stream, boolean age, Set<String> retirer_codes) throws JDOMException, IOException, ExtractionException
	{
		XPathFactory xpfac = XPathFactory.instance();
		SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document docx = builder.build(stream);
        
        int nbColumns = construitColonnes(docx);
        
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
				
				if (retirer_codes != null)
            	{
            		if (retirer_codes.contains(adherent.getFonctioncomplet()) == true)
            		{
            			continue;
            		}
            	}
				
				if (adherent.getChef())
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
				else if (adherent.getCompa())
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
			if (ad.getChef())
			{
				AdherentForme chef = (AdherentForme)ad;
				boolean dirsfdef = chef.getQualif("dirsf").getDefini();
				boolean animsfQualifie = chef.getQualif("animsf").getDefini() && chef.getQualif("animsf").getTitulaire();
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

	public void construitsAlertes(Alertes alertes, boolean jeunes, boolean age) {
		adherents_.forEach((code,ad) ->
		{
			ad.construitsAlertes(alertes, jeunes, age);
		});
	}
}
