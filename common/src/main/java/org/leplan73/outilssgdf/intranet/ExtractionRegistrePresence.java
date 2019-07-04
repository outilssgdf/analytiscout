package org.leplan73.outilssgdf.intranet;

import java.io.IOException;
import java.util.ArrayList;
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

public class ExtractionRegistrePresence extends ExtractionIntranet {

	static class AjaxE
	{
		public AjaxE(int taille, String type, String nom, String donnees) {
			taille_ = taille;
			type_ = type;
			nom_ = nom;
			donnees_ = donnees;
		}
		public int taille_;
		public String type_;
		public String nom_;
		public String donnees_;
		
		@Override
		public String toString()
		{
			return nom_;
		}
	}
	
	static private List<AjaxE> parseAjax(String donnees) throws IOException
	{
		List<AjaxE> items = new ArrayList<AjaxE>();
		while(!donnees.isEmpty())
		{
			int i1 = donnees.indexOf("|");
			if (i1 >=0 )
			{
				String taille = donnees.substring(0,i1);
				int i2 = donnees.indexOf("|", i1+1);
				String type = donnees.substring(i1+1,i2);
				int i3 = donnees.indexOf("|", i2+1);
				String nom = donnees.substring(i2+1,i3);
				String donnees2 = donnees.substring(i3+1,i3+1+Integer.parseInt(taille));
				donnees = donnees.substring(i3+Integer.parseInt(taille)+2);
				
				items.add(new AjaxE(Integer.parseInt(taille), type, nom, donnees2));
			}
		}
		return items;
	}
	
	public String extract(int structure, int saison, int period, boolean forfaitaire)
			throws ClientProtocolException, IOException, JDOMException {
		HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet()
				+ "/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
		httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.addHeader("Accept-Language", "fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
		CloseableHttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();
		String obj = EntityUtils.toString(entity);
		if (logger_.isDebugEnabled())
			logger_.debug(obj);
		Document doc = Jsoup.parse(obj);
		viewstate = doc.select("#__VIEWSTATE").first().val();
		String viewstategenerator = doc.select("#__VIEWSTATEGENERATOR").first().val();
		String eventvalidation = doc.select("#__EVENTVALIDATION").first().val();
		response.close();

		Map<Integer, Integer> structureMap = new TreeMap<Integer, Integer>();

		Integer ddStructure = null;
		Integer tbStructure = null;
		String tbAutoCompleteCode = null;

		// Extraction des codes structure "dd" internes (visible avec un profile
		// "Groupe")
		Element ddCodes = doc.selectFirst("select[id=ctl00_MainContent__EditeurRegistrePresence__navigateur__ddStructure]");
		if (ddCodes != null) {
			Elements ddCodes2 = ddCodes.select("option");
			for (int i = 0; i < ddCodes2.size(); i++) {
				Element code = ddCodes2.get(i);
				if (code != null) {
					String va = code.text();
					if (va.compareTo("Toutes") != 0) {
						va = va.substring(0, va.indexOf(" - "));
						String value = code.attributes().get("value");
						structureMap.put(Integer.valueOf(va), Integer.valueOf(value));
					}
				}
			}
			ddStructure = (structure != ExtractionIntranet.STRUCTURE_TOUT) ? structureMap.get(structure) : null;
		}

		if (ddCodes == null) {
			HttpPost httppostStructures = new HttpPost(ExtractionIntranet.getIntranet()
					+ "/Specialisation/Sgdf/WebServices/AutoComplete.asmx/GetStructures");
			httppostStructures.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httppostStructures.addHeader("Content-Type", "application/json; charset=UTF-8");
			httppostStructures.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");

			String query = "{q: \"" + structure + "\", id_token: \"undefined\"}";
			StringEntity JsonEntity = new StringEntity(query);
			httppostStructures.setEntity(JsonEntity);
			response = httpclient.execute(httppostStructures);

			entity = response.getEntity();
			obj = EntityUtils.toString(entity);
			if (logger_.isDebugEnabled())
				logger_.debug(obj);
			response.close();

			Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(obj);
			if (jsonDocument != null) {
				tbAutoCompleteCode = JsonPath.read(jsonDocument, "$.d");
				jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(tbAutoCompleteCode.toString());
				try
       			{
					Object nodeId = JsonPath.read(jsonDocument, "$.[0].id");
					structureMap.put(structure, Integer.valueOf(nodeId.toString()));
       			}
       			catch(PathNotFoundException ex)
       			{
       				throw ex;
       			}
			}
			tbStructure = structure != ExtractionIntranet.STRUCTURE_TOUT ? structureMap.get(structure) : 0;
		}
		
		if (true) {
			HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet() + "/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
			httppost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httppost.addHeader("Accept","*/*");
			httppost.addHeader("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
			httppost.addHeader("Cache-Control", "no-cache");
			httppost.addHeader("Connection","keep-alive");
			httppost.addHeader("Pragma", "no-cache");
			httppost.addHeader("DNT", "1");
			httppost.addHeader("Origin", "https://intranet.sgdf.fr");
			httppost.addHeader("Referer", "https://intranet.sgdf.fr/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
			httppost.addHeader("X-MicrosoftAjax","Delta=true");
			httppost.addHeader("X-Requested-With","XMLHttpRequest");

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_autocompleteStructures"));
			
			if (tbAutoCompleteCode != null)
			{
				String tbAutoCompleteCode2 = new String(tbAutoCompleteCode);
				tbAutoCompleteCode2 = tbAutoCompleteCode2.replaceAll("\\{", "");
				tbAutoCompleteCode2 = tbAutoCompleteCode2.replaceAll("\\}", "");
				tbAutoCompleteCode2 = tbAutoCompleteCode2.replaceAll("\\[", "");
				tbAutoCompleteCode2 = tbAutoCompleteCode2.replaceAll("\\]", "");
				formparams.add(new BasicNameValuePair("__EVENTARGUMENT", "{\"eventName\":\"Add\","+tbAutoCompleteCode2+"}"));
			}
			formparams.add(new BasicNameValuePair("__LASTFOCUS", ""));
			formparams.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
			formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", viewstategenerator));
			formparams.add(new BasicNameValuePair("__EVENTVALIDATION", eventvalidation));
			formparams.add(new BasicNameValuePair("__ASYNCPOST","true"));

			formparams.add(new BasicNameValuePair("ctl00$ScriptManager1","ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_upSelecteurStructureAutocomplete|ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_autocompleteStructures"));
			formparams.add(new BasicNameValuePair("ctl00$_ddRechercheType", "0"));
			
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalId", "ctl00__btnAvisUtilisateur__modal"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalFrameId","ctl00__btnAvisUtilisateur__modalFrame"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurUrl", "https://sgdf.limequery.net/725453?lang=fr"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateur", ""));
			formparams.add(new BasicNameValuePair("ctl00$_hfAlertesId", "165,164,163,162,161,159,158,155,152,137,119"));
			
			formparams.add(new BasicNameValuePair("ctl00$_navbar$_ddRechercheTypeXS", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_hfRetirerId", ""));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddSaison", "" + saison));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_hidCodeStructure", "" + structure));
			if (ddStructure != null) {
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_ddStructure","" + ddStructure));
			}
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddPeriodes", "" + 6));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_cbAdherentsUniquement", "on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$ModeVolumeHoraire",forfaitaire ? "_rdbModeVolumeHoraireForfaitaire" : "_rdbModeVolumeHoraireReel"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_tbIdsCheckBox", ""));
			formparams.add(new BasicNameValuePair("eo_version", "11.0.20.2"));
			formparams.add(new BasicNameValuePair(" eo_style_keys", "/wFk"));
			formparams.add(new BasicNameValuePair("ctl00$_hidReferenceStatistiqueUtilisation", "-1"));
			formparams.add(new BasicNameValuePair("ctl00$_hfSuggestionAvisUtilisateur", "hidden"));
			formparams.add(new BasicNameValuePair("ctl00$_ddDelegations", "0"));

			entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			httppost.setEntity(entity);
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			obj = EntityUtils.toString(entity);
			response.close();
			if (logger_.isDebugEnabled())
				logger_.debug(obj);
			List<AjaxE> l = parseAjax(obj);
			AjaxE aj = rechercheAjax(l, "__VIEWSTATE");
			if (aj != null)
			{
				viewstate = aj.donnees_;
			}
			aj = rechercheAjax(l, "__EVENTVALIDATION");
			if (aj != null)
			{
				eventvalidation = aj.donnees_;
			}
		}

		{
			HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet() + "/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
			httppost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httppost.addHeader("Accept","*/*");
			httppost.addHeader("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
			httppost.addHeader("Cache-Control", "no-cache");
			httppost.addHeader("Connection","keep-alive");
			httppost.addHeader("Pragma", "no-cache");
			httppost.addHeader("DNT", "1");
			httppost.addHeader("Origin", "https://intranet.sgdf.fr");
			httppost.addHeader("Referer", "https://intranet.sgdf.fr/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
			httppost.addHeader("X-MicrosoftAjax","Delta=true");
			httppost.addHeader("X-Requested-With","XMLHttpRequest");

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$MainContent$_EditeurRegistrePresence$_ddSaison"));
			formparams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
			formparams.add(new BasicNameValuePair("__LASTFOCUS", ""));
			formparams.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
			formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", viewstategenerator));
			formparams.add(new BasicNameValuePair("__EVENTVALIDATION", eventvalidation));
			formparams.add(new BasicNameValuePair("__ASYNCPOST","true"));

			formparams.add(new BasicNameValuePair("ctl00$ScriptManager1","ctl00$_upMainContent|ctl00$MainContent$_EditeurRegistrePresence$_ddSaison"));
			formparams.add(new BasicNameValuePair("ctl00$_ddRechercheType", "0"));
			
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalId", "ctl00__btnAvisUtilisateur__modal"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalFrameId","ctl00__btnAvisUtilisateur__modalFrame"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurUrl", "https://sgdf.limequery.net/725453?lang=fr"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateur", ""));
			formparams.add(new BasicNameValuePair("ctl00$_hfAlertesId", "165,164,163,162,161,159,158,155,152,137,119"));
			
			formparams.add(new BasicNameValuePair("ctl00$_navbar$_ddRechercheTypeXS", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_hfRetirerId", ""));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddSaison", "" + saison));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_hidCodeStructure", "" + structure));
			if (ddStructure != null) {
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_ddStructure","" + ddStructure));
			}
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddPeriodes", "" + 6));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_cbAdherentsUniquement", "on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$ModeVolumeHoraire",forfaitaire ? "_rdbModeVolumeHoraireForfaitaire" : "_rdbModeVolumeHoraireReel"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_tbIdsCheckBox", ""));
			formparams.add(new BasicNameValuePair("eo_version", "11.0.20.2"));
			formparams.add(new BasicNameValuePair(" eo_style_keys", "/wFk"));
			formparams.add(new BasicNameValuePair("ctl00$_hidReferenceStatistiqueUtilisation", "-1"));
			formparams.add(new BasicNameValuePair("ctl00$_hfSuggestionAvisUtilisateur", "hidden"));
			formparams.add(new BasicNameValuePair("ctl00$_ddDelegations", "0"));

			entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			httppost.setEntity(entity);
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			obj = EntityUtils.toString(entity);
			response.close();
			if (logger_.isDebugEnabled())
				logger_.debug(obj);
			List<AjaxE> l = parseAjax(obj);
			AjaxE aj = rechercheAjax(l, "__VIEWSTATE");
			if (aj != null)
			{
				viewstate = aj.donnees_;
			}
			aj = rechercheAjax(l, "__EVENTVALIDATION");
			if (aj != null)
			{
				eventvalidation = aj.donnees_;
			}
		}
		
		{
			HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet() + "/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
			httppost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httppost.addHeader("Accept","*/*");
			httppost.addHeader("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
			httppost.addHeader("Cache-Control", "no-cache");
			httppost.addHeader("Connection","keep-alive");
			httppost.addHeader("Pragma", "no-cache");
			httppost.addHeader("DNT", "1");
			httppost.addHeader("Origin", "https://intranet.sgdf.fr");
			httppost.addHeader("Referer", "https://intranet.sgdf.fr/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
			httppost.addHeader("X-MicrosoftAjax","Delta=true");
			httppost.addHeader("X-Requested-With","XMLHttpRequest");

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$MainContent$_EditeurRegistrePresence$_ddPeriodes"));
			formparams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
			formparams.add(new BasicNameValuePair("__LASTFOCUS", ""));
			formparams.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
			formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", viewstategenerator));
			formparams.add(new BasicNameValuePair("__EVENTVALIDATION", eventvalidation));
			formparams.add(new BasicNameValuePair("__ASYNCPOST","true"));

			formparams.add(new BasicNameValuePair("ctl00$ScriptManager1","ctl00$_upMainContent|ctl00$MainContent$_EditeurRegistrePresence$_ddPeriodes"));
			formparams.add(new BasicNameValuePair("ctl00$_ddRechercheType", "0"));
			
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalId", "ctl00__btnAvisUtilisateur__modal"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalFrameId","ctl00__btnAvisUtilisateur__modalFrame"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurUrl", "https://sgdf.limequery.net/725453?lang=fr"));
			formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateur", ""));
			formparams.add(new BasicNameValuePair("ctl00$_hfAlertesId", "165,164,163,162,161,159,158,155,152,137,119"));
			
			formparams.add(new BasicNameValuePair("ctl00$_navbar$_ddRechercheTypeXS", "0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_hfRetirerId", ""));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddSaison", "" + saison));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_hidCodeStructure", "" + structure));
			if (ddStructure != null) {
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_ddStructure","" + ddStructure));
			}
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddPeriodes", "" + 0));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_cbAdherentsUniquement", "on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$ModeVolumeHoraire",forfaitaire ? "_rdbModeVolumeHoraireForfaitaire" : "_rdbModeVolumeHoraireReel"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_tbIdsCheckBox", ""));
			formparams.add(new BasicNameValuePair("eo_version", "11.0.20.2"));
			formparams.add(new BasicNameValuePair(" eo_style_keys", "/wFk"));
			formparams.add(new BasicNameValuePair("ctl00$_hidReferenceStatistiqueUtilisation", "-1"));
			formparams.add(new BasicNameValuePair("ctl00$_hfSuggestionAvisUtilisateur", "hidden"));
			formparams.add(new BasicNameValuePair("ctl00$_ddDelegations", "0"));

			entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			httppost.setEntity(entity);
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			obj = EntityUtils.toString(entity);
			response.close();
			if (logger_.isDebugEnabled())
				logger_.debug(obj);
			List<AjaxE> l = parseAjax(obj);
			AjaxE aj = rechercheAjax(l, "__VIEWSTATE");
			if (aj != null)
			{
				viewstate = aj.donnees_;
			}
			aj = rechercheAjax(l, "__EVENTVALIDATION");
			if (aj != null)
			{
				eventvalidation = aj.donnees_;
			}
		}

		HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet()
				+ "/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
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
		formparams.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$MainContent$ChercheurCamps1$_btnExporter"));
		formparams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		formparams.add(new BasicNameValuePair("__LASTFOCUS", ""));
		formparams.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
		formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", viewstategenerator));
		formparams.add(new BasicNameValuePair("__EVENTVALIDATION", eventvalidation));

		formparams.add(new BasicNameValuePair("ctl00$_ddRechercheType", "0"));
		formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalId", "ctl00__btnAvisUtilisateur__modal"));
		formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalFrameId","ctl00__btnAvisUtilisateur__modalFrame"));
		formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurUrl", "https://sgdf.limequery.net/725453?lang=fr"));
		formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateur", ""));
		formparams.add(new BasicNameValuePair("ctl00$_hfAlertesId", "159,158,155,152,150,137,135,133,119"));
		formparams.add(new BasicNameValuePair("ctl00$_navbar$_ddRechercheTypeXS", "0"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_hfRetirerId", ""));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddSaison", "" + saison));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_hidCodeStructure", "" + structure));
		if (ddStructure != null) {
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_ddStructure","" + ddStructure));
		}
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddPeriodes", "" + period));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_cbAdherentsUniquement", "on"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_cbExporterSousStructure", "on"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$ModeVolumeHoraire",forfaitaire ? "_rdbModeVolumeHoraireForfaitaire" : "_rdbModeVolumeHoraireReel"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_btnExporterExcel","Exporter au format Excel (CSV)"));
		formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_tbIdsCheckBox", ""));
		formparams.add(new BasicNameValuePair("eo_version", "11.0.20.2"));
		formparams.add(new BasicNameValuePair(" eo_style_keys", "/wFk"));
		formparams.add(new BasicNameValuePair("ctl00$_hidReferenceStatistiqueUtilisation", "-1"));
		formparams.add(new BasicNameValuePair("ctl00$_hfSuggestionAvisUtilisateur", "hidden"));
		formparams.add(new BasicNameValuePair("ctl00$_ddDelegations", "0"));

		entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		httppost.setEntity(entity);
		response = httpclient.execute(httppost);
		entity = response.getEntity();
		obj = EntityUtils.toString(entity);
		response.close();
		if (logger_.isDebugEnabled())
			logger_.debug(obj);
		return obj;
	}

	private AjaxE rechercheAjax(List<AjaxE> l, String key) {
		for (AjaxE v : l)
		{
			if (key.compareTo(v.nom_) == 0)
			{
				return v;
			}
		};
		return null;
	}
}
