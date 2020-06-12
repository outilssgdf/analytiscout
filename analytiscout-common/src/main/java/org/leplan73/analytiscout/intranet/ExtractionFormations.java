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

public class ExtractionFormations extends ExtractionIntranet {

	public String extract(int structure, String fonction, Date debut, Date fin, boolean brut) throws ClientProtocolException, IOException, JDOMException
	{
		DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
		String fdebut = DATE_TIME_FORMATTER.format(debut.toInstant());
		String ffin = DATE_TIME_FORMATTER.format(fin.toInstant());
		
		for (;;) {
			Document docViewstate = null;
			HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/Formations/ExtraireFormations.aspx");
			httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
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

	    	Integer tbStructure = null;
	    	String tbAutoCompleteCode = null;
	    	String tbAutoCompleteCodeName = null;
	    	
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
	       				tbAutoCompleteCodeName = JsonPath.read(jsonDocument,"$.[0].name");
	       				structureMap.put(structure, Integer.valueOf(nodeId.toString()));
	       			}
	       			catch(PathNotFoundException ex)
	       			{
	       				throw ex;
	       			}
	       		}
	       		tbStructure = structureMap.get(structure);
	       	}
	       
			HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/Formations/ExtraireFormations.aspx");
			httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httppost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httppost.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				formparams.add(new BasicNameValuePair("__EVENTTARGET",""));
			formparams.add(new BasicNameValuePair("__EVENTARGUMENT",""));
			formparams.add(new BasicNameValuePair("__eo_obj_states","")); 
			formparams.add(new BasicNameValuePair("__eo_sc",""));
			formparams.add(new BasicNameValuePair("__LASTFOCUS","")); 
			formparams.add(new BasicNameValuePair("__VIEWSTATE",viewstate));
			formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR",viewstategenerator));
			formparams.add(new BasicNameValuePair("__EVENTVALIDATION",eventvalidation));
			formparams.add(new BasicNameValuePair("ctl00$_ddRechercheType","0"));
			formparams.add(new BasicNameValuePair("eo_version","11.0.20.2"));
			formparams.add(new BasicNameValuePair("eo_style_keys","/wFk"));
			formparams.add(new BasicNameValuePair("ctl00$_ddDelegations","0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddlRequetesExistantes","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_tbNomNouvelleRequete",""));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_btnPopupStructure$_tbResult",tbAutoCompleteCodeName));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbCumulees","on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbAdherents","on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbNonAdherents","on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbSatisfaisantes","on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbNonSatisfaisantes","on"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$fonctions",""));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$CodesFonctionsSignifications","_rbCFS_FonctionsPrincipales"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_national","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_typeAction","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$rbTypeStage","_rbTypeStageTous"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddPeriode","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_btnPopupLieu$_tbResult","")); 
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_dpbDateDebut$_tbDate",fdebut));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_dpbDateFin$_tbDate",ffin));
			  
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
