package org.leplan73.outilssgdf.extraction;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.leplan73.outilssgdf.ExtractionException;

public class ColonnesAdherents {

	private Map<Integer, String> ids_ = new TreeMap<Integer, String>();
	private Map<String, Integer> map_ = new TreeMap<String, Integer>();
	
	private int codeAdherentId = -1;
	private int uniteId = -1;
	private int nomIndividuId = -1;
	private int emailPersonnelIndividuId = -1;
	private int emailProfessionelIndividuId = -1;
	private int prenomIndividuId = -1;
	private int mobileIndividu1 = -1;
	private int mobileIndividu2 = -1;
	private int nomPereId = -1;
	private int prenomPereId = -1;
	private int emailPereId = -1;
	private int mobilePereId = -1;
	private int nomMereId = -1;
	private int prenomMereId = -1;
	private int emailMereId = -1;
	private int mobileMereId = -1;
	private int fonctionCodeId = -1;
	private int bureauIndividu = -1;
	private int datedenaissanceId = -1;
	private int droitImage = -1;
	private int diplomeJs = -1;
	private int diplomeDetailsJs = -1;
	private int qualiteJs = -1;
	private int stuctureNom = -1;
	private int structureCode = -1;
	private int delegations = -1;

	public void add(int id, String nom) {
		ids_.put(id, nom);
		map_.put(nom, id);
	}
	
	public void calculCodes() throws ExtractionException
	{
		ids_.forEach((key, value) ->
		{
			if (ids_.get(key).compareTo("IntervenantJS.DiplomeJS") == 0)
			{
				diplomeJs = key;
			}
			if (ids_.get(key).compareTo("IntervenantJS.DiplomeDetailJS") == 0)
			{
				diplomeDetailsJs = key;
			}
			if (ids_.get(key).compareTo("IntervenantJS.QualiteJS") == 0)
			{
				qualiteJs = key;
			}
			if (ids_.get(key).compareTo("Individu.DroitImage") == 0)
			{
				droitImage = key;
			}
			if (ids_.get(key).compareTo("Inscription.Delegations") == 0)
			{
				delegations = key;
			}
			if (ids_.get(key).compareTo("Structure.Nom") == 0)
			{
				stuctureNom = key;
			}
			if (ids_.get(key).compareTo("Structure.CodeStructure") == 0)
			{
				structureCode = key;
			}
			if (ids_.get(key).compareTo("Fonction.Code") == 0)
			{
				fonctionCodeId = key;
			}
			else if (ids_.get(key).compareTo("Individu.CodeAdherent") == 0)
			{
				codeAdherentId = key;
			}
			else if (ids_.get(key).compareTo("Structure.Nom") == 0)
			{
				uniteId = key;
			}
			else if (ids_.get(key).compareTo("Individu.Nom") == 0)
			{
				nomIndividuId = key;
			}
			else if (ids_.get(key).compareTo("Individu.Prenom") == 0)
			{
				prenomIndividuId = key;
			}
			else if (ids_.get(key).compareTo("Individu.TelephonePortable1") == 0)
			{
				mobileIndividu1 = key;
			}
			else if (ids_.get(key).compareTo("Individu.TelephonePortable2") == 0)
			{
				mobileIndividu2 = key;
			}
			else if (ids_.get(key).compareTo("Individu.TelephoneBureau") == 0)
			{
				bureauIndividu = key;
			}
			else if (ids_.get(key).compareTo("Individu.CourrielPersonnel") == 0)
			{
				emailPersonnelIndividuId = key;
			}
			else if (ids_.get(key).compareTo("Individu.CourrielProfessionnel") == 0)
			{
				emailProfessionelIndividuId = key;
			}
			else if (ids_.get(key).compareTo("Individu.DateNaissance") == 0)
			{
				datedenaissanceId = key;
			}
			else if (ids_.get(key).compareTo("Pere.Nom") == 0)
			{
				nomPereId = key;
			}
			else if (ids_.get(key).compareTo("Pere.Prenom") == 0)
			{
				prenomPereId = key;
			}
			else if (ids_.get(key).compareTo("Pere.CourrielPersonnel") == 0)
			{
				emailPereId = key;
			}
			else if (ids_.get(key).compareTo("Pere.TelephonePortable1") == 0)
			{
				mobilePereId = key;
			}
			else if (ids_.get(key).compareTo("Mere.Nom") == 0)
			{
				nomMereId = key;
			}
			else if (ids_.get(key).compareTo("Mere.Prenom") == 0)
			{
				prenomMereId = key;
			}
			else if (ids_.get(key).compareTo("Mere.CourrielPersonnel") == 0)
			{
				emailMereId = key;
			}
			else if (ids_.get(key).compareTo("Mere.TelephonePortable1") == 0)
			{
				mobileMereId = key;
			}
		});
		
		// Quelques tests...
		if (codeAdherentId == -1) throw new ExtractionException("Erreur codeAdherentId");
		if (uniteId == -1) throw new ExtractionException("Erreur uniteId");
		if (nomIndividuId == -1) throw new ExtractionException("Erreur nomIndividuId");
		if (prenomIndividuId == -1) throw new ExtractionException("Erreur prenomIndividuId");
		if (fonctionCodeId == -1) throw new ExtractionException("Erreur fonctionCodeId");
	}

	public int getDatedeNaissanceId() {
		return datedenaissanceId;
	}

	public int getFonctionCodeId() {
		return fonctionCodeId;
	}

	public int getCodeAdherentId() {
		return codeAdherentId;
	}

	public int getUniteId() {
		return uniteId;
	}

	public int getNomPereId() {
		return nomPereId;
	}

	public int getPrenomPereId() {
		return prenomPereId;
	}
	
	public int getNomIndividuId() {
		return nomIndividuId;
	}

	public int getPrenomIndividuId() {
		return prenomIndividuId;
	}

	public int getEmailPersonnelIndividuId() {
		return emailPersonnelIndividuId;
	}

	public int getEmailProfessionelIndividuId() {
		return emailProfessionelIndividuId;
	}

	public int getEmailPereId() {
		return emailPereId;
	}

	public int getMobilePereId() {
		return mobilePereId;
	}

	public int getNomMereId() {
		return nomMereId;
	}

	public int getPrenomMereId() {
		return prenomMereId;
	}

	public int getEmailMereId() {
		return emailMereId;
	}

	public int getMobileMereId() {
		return mobileMereId;
	}

	public int getMobileIndividu1Id() {
		return mobileIndividu1;
	}

	public int getMobileIndividu2Id() {
		return mobileIndividu2;
	}

	public int getBureauIndividu() {
		return bureauIndividu;
	}
	
	public Integer get(String code)
	{
		return map_.get(code);
	}
	
	public String getNom(int code)
	{
		return ids_.get(code);
	}

	public int getStructureNom() {
		return stuctureNom;
	}

	public int getStructureCode() {
		return structureCode;
	}

	public int getDelegations() {
		return delegations;
	}

	public int getDroitImage() {
		return droitImage;
	}

	public int getDiplomejsId() {
		return diplomeJs;
	}

	public int getDiplomedetailsjs() {
		return diplomeDetailsJs;
	}

	public int getQualitejs() {
		return qualiteJs;
	}

	public Set<String> noms() {
		return map_.keySet();
	}

	public Set<Integer> ids() {
		return ids_.keySet();
	}
}
