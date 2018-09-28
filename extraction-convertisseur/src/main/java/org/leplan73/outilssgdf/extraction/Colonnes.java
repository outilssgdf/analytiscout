package org.leplan73.outilssgdf.extraction;

import java.util.TreeMap;

import org.leplan73.outilssgdf.ExtractionException;

public class Colonnes extends TreeMap<Integer, String> {
	
	private static final long serialVersionUID = 1L;
	
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
	
	public void calculCodes() throws ExtractionException
	{
		this.forEach((key, value) ->
		{
			if (this.get(key).compareTo("Fonction.Code") == 0)
			{
				fonctionCodeId = key;
			}
			else if (this.get(key).compareTo("Individu.CodeAdherent") == 0)
			{
				codeAdherentId = key;
			}
			else if (this.get(key).compareTo("Structure.Nom") == 0)
			{
				uniteId = key;
			}
			else if (this.get(key).compareTo("Individu.Nom") == 0)
			{
				nomIndividuId = key;
			}
			else if (this.get(key).compareTo("Individu.Prenom") == 0)
			{
				prenomIndividuId = key;
			}
			else if (this.get(key).compareTo("Individu.TelephonePortable1") == 0)
			{
				mobileIndividu1 = key;
			}
			else if (this.get(key).compareTo("Individu.TelephonePortable2") == 0)
			{
				mobileIndividu2 = key;
			}
			else if (this.get(key).compareTo("Individu.CourrielPersonnel") == 0)
			{
				emailIndividuId = key;
			}
			else if (this.get(key).compareTo("Individu.CourrielProfessionnel") == 0)
			{
				emailIndividu2Id = key;
			}
			else if (this.get(key).compareTo("Pere.Nom") == 0)
			{
				nomPereId = key;
			}
			else if (this.get(key).compareTo("Pere.Prenom") == 0)
			{
				prenomPereId = key;
			}
			else if (this.get(key).compareTo("Pere.CourrielPersonnel") == 0)
			{
				emailPereId = key;
			}
			else if (this.get(key).compareTo("Pere.TelephonePortable1") == 0)
			{
				mobilePereId = key;
			}
			else if (this.get(key).compareTo("Mere.Nom") == 0)
			{
				nomMereId = key;
			}
			else if (this.get(key).compareTo("Mere.Prenom") == 0)
			{
				prenomMereId = key;
			}
			else if (this.get(key).compareTo("Mere.CourrielPersonnel") == 0)
			{
				emailMereId = key;
			}
			else if (this.get(key).compareTo("Mere.TelephonePortable1") == 0)
			{
				mobileMereId = key;
			}
		});
		
		// Quelques tests...
		if (codeAdherentId == -1) throw new ExtractionException("Erreur codeAdherentId");
		if (uniteId == -1) throw new ExtractionException("Erreur uniteId");
		if (nomIndividuId == -1) throw new ExtractionException("Erreur nomIndividuId");
		if (prenomIndividuId == -1) throw new ExtractionException("Erreur prenomIndividuId");
		if (emailIndividuId == -1) throw new ExtractionException("Erreur emailIndividuId");
		if (emailIndividu2Id == -1) throw new ExtractionException("Erreur emailIndividu2Id");
		if (mobileIndividu1 == -1) throw new ExtractionException("Erreur mobileIndividu1");
		if (mobileIndividu2 == -1) throw new ExtractionException("Erreur mobileIndividu2");
		if (nomPereId == -1) throw new ExtractionException("Erreur nomPereId");
		if (prenomPereId == -1) throw new ExtractionException("Erreur prenomPereId");
		if (emailPereId == -1) throw new ExtractionException("Erreur emailPereId");
		if (mobilePereId == -1) throw new ExtractionException("Erreur mobilePereId");
		if (nomMereId == -1) throw new ExtractionException("Erreur nomMereId");
		if (prenomMereId == -1) throw new ExtractionException("Erreur prenomMereId");
		if (emailMereId == -1) throw new ExtractionException("Erreur emailMereId");
		if (mobileMereId == -1) throw new ExtractionException("Erreur mobileMereId");
		if (fonctionCodeId == -1) throw new ExtractionException("Erreur fonctionCodeId");
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
}
