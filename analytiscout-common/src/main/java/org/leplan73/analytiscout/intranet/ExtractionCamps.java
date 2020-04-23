package org.leplan73.analytiscout.intranet;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class ExtractionCamps extends ExtractionIntranet {

	public String extract(int structure, Date debut, Date fin, boolean brut) throws ClientProtocolException, IOException, JDOMException {
		
		DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
		String fdebut = DATE_TIME_FORMATTER.format(debut.toInstant());
		String ffin = DATE_TIME_FORMATTER.format(fin.toInstant());
		
		for (;;) {
			
			Document docViewstate = null;
			HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet() + "/Specialisation/Sgdf/camps/TableauDeBordCamp.aspx");
			httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.addHeader("Accept-Language", "fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
			CloseableHttpResponse responseViewState = httpclient.execute(httpget);

			HttpEntity entityViewstate = responseViewState.getEntity();
	       	String objViewstate = EntityUtils.toString(entityViewstate);
		    if (logger_.isDebugEnabled())
		    	logger_.debug(objViewstate);
		    docViewstate = Jsoup.parse(objViewstate);
			if (docViewstate == null)
				continue;
			viewstate = docViewstate.select("#__VIEWSTATE").first().val();
			String viewstategenerator = docViewstate.select("#__VIEWSTATEGENERATOR").first().val();
			String eventvalidation = docViewstate.select("#__EVENTVALIDATION").first().val();
			responseViewState.close();
			
			Map<Integer, Integer> structureMap = new TreeMap<Integer, Integer>();

	    	Integer ddStructure = null;
	    	Integer tbStructure = null;
	    	String tbAutoCompleteCode = null;
	    	
	       	// Extraction des codes structure "dd" internes (visible avec un profile "Groupe")
       		Element ddCodes = docViewstate.selectFirst("select[id=ctl00_MainContent__selecteur__ddStructure]");
       		if (ddCodes != null)
       		{
		       	Elements ddCodes2 = ddCodes.select("option");
		       	for (int i=0;i<ddCodes2.size();i++)
		       	{
		       		Element code = ddCodes2.get(i);
		       		if (code != null)
		       		{
			       		String va = code.text();
			       		if (va.compareTo("Toutes") != 0)
			       		{
				       		va = va.substring(0, va.indexOf(" - "));
				       		String value = code.attributes().get("value");
				       		structureMap.put(Integer.valueOf(va), Integer.valueOf(value));
			       		}
		       		}
		       	}
		       	ddStructure = (structure != ExtractionIntranet.STRUCTURE_TOUT) ? structureMap.get(structure) : null;
	       	}
	
	       	// Extraction des codes structure "tb" internes (visible avec un profile "Territoire")
	       	if (tbStructure == null && structure != ExtractionIntranet.STRUCTURE_TOUT)
	       	{
	       		HttpPost httppostStructures = new HttpPost(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/WebServices/AutoComplete.asmx/GetStructures");
	       		httppostStructures.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
	       		httppostStructures.addHeader("Content-Type","application/json; charset=UTF-8");
	       		httppostStructures.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
	       		
	       		String query = "{q: \""+structure+"\", id_token: \"undefined\"}";
	       		StringEntity JsonEntity = new StringEntity(query);
	    	    httppostStructures.setEntity(JsonEntity);
	    	    CloseableHttpResponse response = httpclient.execute(httppostStructures);
	    	    
	    	    HttpEntity entity = response.getEntity();
	    	    String obj = EntityUtils.toString(entity);
	    	    if (logger_.isDebugEnabled())
	    	    	logger_.debug(obj);
	           	response.close();
	       		
	       		Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(obj);
	       		if (jsonDocument != null)
	       		{
	       			tbAutoCompleteCode = JsonPath.read(jsonDocument,"$.d");
	       			jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(tbAutoCompleteCode);
	       			try
	       			{
	       				Object nodeId = JsonPath.read(jsonDocument,"$.[0].id");
	       				structureMap.put(structure, Integer.valueOf(nodeId.toString()));
	       			}
	       			catch(PathNotFoundException ex)
	       			{
	       				throw ex;
	       			}
	       		}
	       		tbStructure = structureMap.get(structure);
	       	}

			HttpPost httppost = new HttpPost(
					ExtractionIntranet.getIntranet() + "/Specialisation/Sgdf/camps/TableauDeBordCamp.aspx");
			httppost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httppost.addHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			httppost.addHeader("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
			httppost.addHeader("Cache-Control", "max-age=0");
			httppost.addHeader("DNT", "1");
			httppost.addHeader("Origin", "https://intranet.sgdf.fr");
			httppost.addHeader("Referer", "https://intranet.sgdf.fr/Specialisation/Sgdf/camps/TableauDeBordCamp.aspx");
			httppost.addHeader("Upgrade-Insecure-Requests", "1");

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(
					new BasicNameValuePair("ctl00_MainContent_ChercheurCamps1__pnlFormulaire_CurrentState", "false"));
			formparams.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$MainContent$ChercheurCamps1$_btnExporter"));
			formparams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
			formparams.add(new BasicNameValuePair("__LASTFOCUS", ""));
			formparams.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
			formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", viewstategenerator));
			formparams.add(new BasicNameValuePair("__EVENTVALIDATION", eventvalidation));
			formparams.add(new BasicNameValuePair("ctl00$_ddRechercheType", "0"));
			formparams
					.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalId", "ctl00__btnAvisUtilisateur__modal"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalFrameId",
					"ctl00__btnAvisUtilisateur__modalFrame"));
			formparams.add(
					new BasicNameValuePair("ctl00$_hfAvisUtilisateurUrl", "https://sgdf.limequery.net/725453?lang=fr"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateur", ""));
			formparams.add(new BasicNameValuePair("ctl00$_hfAlertesId", "150,149,137,135,133,119"));
			formparams.add(new BasicNameValuePair("ctl00$_navbar$_ddRechercheTypeXS", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$TypeSuivi", "_rbCampsPreparesParmisParticipantes"));
//			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_hidCodeStructure","" + structure));
//			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_txtAutoComplete","" + 1059985));
//			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_hiddenAutoComplete","[{\"id\":\"1059985\",\"name\":\"117540000 - TERRITOIRE PARIS SUD - PORTE DU SOLEIL\"}]"));

			if (structure != 0)
				formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_hidCodeStructure",""+structure));
			else
			{
				formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_hidCodeStructure",""));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_txtAutoComplete",""));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_hiddenAutoComplete",""));
			}
			if (ddStructure != null)
			{
				formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_ddStructure",""+ ddStructure));
			}
			if (tbStructure != null)
			{
				formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_tbCode",""+ tbStructure));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_txtAutoComplete",""+ tbStructure));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_hiddenAutoComplete",tbAutoCompleteCode));
			}
			
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_hdSelectionChanged", ""));
			formparams.add(new BasicNameValuePair("eo_version", "11.0.20.2"));
			formparams.add(new BasicNameValuePair("eo_style_keys", "/wFk"));
			formparams.add(new BasicNameValuePair("_eo_js_modules", ""));
			formparams.add(new BasicNameValuePair("_eo_obj_inst", ""));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_cbCumule", "on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddTypeStructure", "-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddSpecialite", "-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_tbCodePostal", ""));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_tbPays", ""));
			formparams
					.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddDepartementAdministratif", "-1"));
			formparams
					.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_dpbDateMin$_tbDate", fdebut));
			formparams
					.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_dpbDateMax$_tbDate", ffin));
			formparams.add(
					new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlDossierTransmisAuRGLetACCOPED", "0"));
			formparams
					.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlValidationDuRGLetACCOPED", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlValidationDuTerritoire", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlValiditeDesReglesJS", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlFicheJSTransmise", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlEtatGeneralDuDossier", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlVisasMarins", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlVisasPourLEtranger", "0"));
			formparams.add(new BasicNameValuePair("ctl00$_hidReferenceStatistiqueUtilisation", "-1"));
			formparams.add(new BasicNameValuePair("ctl00$_hfSuggestionAvisUtilisateur", "hidden"));
			formparams.add(new BasicNameValuePair("ctl00$_ddDelegations", "0"));

			HttpEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			httppost.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			boolean ret = checkContentType(response, "application/vnd.ms-excel");
			if (!ret)
				throw new IOException("Données invalide");
			entity = response.getEntity();
			String obj = EntityUtils.toString(entity);
			response.close();
			if (logger_.isDebugEnabled())
				logger_.debug(obj);
			Document doc = Jsoup.parse(obj);
			Elements tables = doc.select("table");
			String v = tables.html();
			if (v.indexOf("<td align=\"left\">") != -1) {
				continue;
			}
			return brut ? obj : v;
		}
	}
}
