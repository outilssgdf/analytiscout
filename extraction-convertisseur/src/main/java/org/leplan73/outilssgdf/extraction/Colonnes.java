package org.leplan73.outilssgdf.extraction;

import java.util.Map;
import java.util.TreeMap;

import org.leplan73.outilssgdf.ExtractionException;

public class Colonnes {

	private Map<Integer, String> ids_ = new TreeMap<Integer, String>();
	private Map<String, Integer> map_ = new TreeMap<String, Integer>();
	
	private int codeAdherentId = -1;
	private int uniteId = -1;
	private int nomIndividuId = -1;
	private int emailIndividuId = -1;
	private int emailIndividu2Id = -1;
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

	private int stuctureNom = -1;

	public void add(int id, String nom) {
		ids_.put(id, nom);
		map_.put(nom, id);
	}
	
	public void calculCodes() throws ExtractionException
	{
		ids_.forEach((key, value) ->
		{
			if (ids_.get(key).compareTo("Structure.Nom") == 0)
			{
				stuctureNom = key;
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
				emailIndividuId = key;
			}
			else if (ids_.get(key).compareTo("Individu.CourrielProfessionnel") == 0)
			{
				emailIndividu2Id = key;
			}
			else if (ids_.get(key).compareTo("Individu.DateNaissance") == 0)
			{
				datedenaissanceId = key;
			}
			
		});
		
		// Quelques tests...
		if (codeAdherentId == -1) throw new ExtractionException("Erreur codeAdherentId");
		if (uniteId == -1) throw new ExtractionException("Erreur uniteId");
		if (nomIndividuId == -1) throw new ExtractionException("Erreur nomIndividuId");
		if (prenomIndividuId == -1) throw new ExtractionException("Erreur prenomIndividuId");
		if (bureauIndividu == -1) throw new ExtractionException("Erreur bureauIndividuId");
		if (emailIndividuId == -1) throw new ExtractionException("Erreur emailIndividuId");
		if (emailIndividu2Id == -1) throw new ExtractionException("Erreur emailIndividu2Id");
		if (mobileIndividu1 == -1) throw new ExtractionException("Erreur mobileIndividu1");
		if (mobileIndividu2 == -1) throw new ExtractionException("Erreur mobileIndividu2");
		if (fonctionCodeId == -1) throw new ExtractionException("Erreur fonctionCodeId");
		if (datedenaissanceId == -1) throw new ExtractionException("Erreur datedenaissanceId");
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

	public int getEmailIndividuId() {
		return emailIndividuId;
	}

	public int getEmailIndividu2Id() {
		return emailIndividu2Id;
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

	public int getStructureNom() {
		return stuctureNom ;
	}
}
