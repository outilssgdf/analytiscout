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

public class ExtractionRegistrePresence extends ExtractionIntranet {
	
	public String extract(int structure, int saison, int period, boolean forfaitaire) throws ClientProtocolException, IOException, JDOMException
	{
		HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
	       httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
	       httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	       httpget.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
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
        	
           	// Extraction des codes structure "dd" internes (visible avec un profile "Groupe")
           	Element ddCodes = doc.selectFirst("select[id=ctl00_MainContent__EditeurRegistrePresence__navigateur__ddStructure]");
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
           	
           	if (ddCodes == null)
           	{
           		HttpPost httppostStructures = new HttpPost(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/WebServices/AutoComplete.asmx/GetStructures");
           		httppostStructures.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
           		httppostStructures.addHeader("Content-Type","application/json; charset=UTF-8");
           		httppostStructures.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
           		
           		String query = "{q: \""+structure+"\", id_token: \"undefined\"}";
           		StringEntity JsonEntity = new StringEntity(query);
        	    httppostStructures.setEntity(JsonEntity);
        	    response = httpclient.execute(httppostStructures);
        	    
        	    entity = response.getEntity();
               	obj = EntityUtils.toString(entity);
        	    if (logger_.isDebugEnabled())
        	    	logger_.debug(obj);
               	response.close();
           		
           		Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(obj);
           		if (jsonDocument != null)
           		{
           			tbAutoCompleteCode = JsonPath.read(jsonDocument,"$.d");
           			jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(tbAutoCompleteCode.toString());
           			Object nodeId = JsonPath.read(jsonDocument,"$.[0].id");
           			structureMap.put(structure, Integer.valueOf(nodeId.toString()));
           		}
           		tbStructure = structure != ExtractionIntranet.STRUCTURE_TOUT ? structureMap.get(structure) : 0;
           	}
	       
	       HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
	       httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
	       httppost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
	       httppost.addHeader("Accept-Language","fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
	       httppost.addHeader("Cache-Control","max-age=0");
	       httppost.addHeader("DNT","1");
	       httppost.addHeader("Origin","https://intranet.sgdf.fr");
	       httppost.addHeader("Referer","https://intranet.sgdf.fr/Specialisation/Sgdf/camps/TableauDeBordCamp.aspx");
	       httppost.addHeader("Upgrade-Insecure-Requests","1");
	       
	       List<NameValuePair> formparams = new ArrayList<NameValuePair>();       
		   formparams.add(new BasicNameValuePair("__EVENTTARGET","ctl00$MainContent$ChercheurCamps1$_btnExporter"));
		   formparams.add(new BasicNameValuePair("__EVENTARGUMENT",""));
		   formparams.add(new BasicNameValuePair("__LASTFOCUS","")); 
		   formparams.add(new BasicNameValuePair("__VIEWSTATE",viewstate));
		   formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR",viewstategenerator));
		   formparams.add(new BasicNameValuePair("__EVENTVALIDATION",eventvalidation));
		   
		   formparams.add(new BasicNameValuePair("ctl00$_ddRechercheType","0"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalId","ctl00__btnAvisUtilisateur__modal"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalFrameId","ctl00__btnAvisUtilisateur__modalFrame"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurUrl","https://sgdf.limequery.net/725453?lang=fr"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateur",""));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAlertesId","159,158,155,152,150,137,135,133,119"));
		   formparams.add(new BasicNameValuePair("ctl00$_navbar$_ddRechercheTypeXS","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_hfRetirerId","")); 
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddSaison",""+saison));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_hidCodeStructure",""+structure));
		   if (ddStructure != null)
			{
			   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_ddStructure",""+ddStructure));
			}
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_ddPeriodes,",""+period));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_cbAdherentsUniquement","on"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_cbExporterSousStructure","on"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$ModeVolumeHoraire",forfaitaire ? "_rdbModeVolumeHoraireForfaitaire" : "_rdbModeVolumeHoraireReel"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_btnExporterExcel","Exporter au format Excel (CSV)"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$_EditeurRegistrePresence$_tbIdsCheckBox",""));
		   formparams.add(new BasicNameValuePair("eo_version","11.0.20.2"));
		   formparams.add(new BasicNameValuePair(" eo_style_keys","/wFk"));
		   formparams.add(new BasicNameValuePair("ctl00$_hidReferenceStatistiqueUtilisation","-1"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfSuggestionAvisUtilisateur","hidden"));
		   formparams.add(new BasicNameValuePair("ctl00$_ddDelegations","0"));
			  
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
}
