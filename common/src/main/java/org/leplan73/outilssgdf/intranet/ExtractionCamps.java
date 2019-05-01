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
import org.jsoup.select.Elements;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class ExtractionCamps extends ExtractionIntranet {
	
	public String extract(int structure) throws ClientProtocolException, IOException, JDOMException
	{
		HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/camps/TableauDeBordCamp.aspx");
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
            response.close();

        	Map<Integer, Integer> structureMap = new TreeMap<Integer, Integer>();
        	
            Integer tbStructure = null;
        	String tbAutoCompleteCode = null;
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
	       
	       HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/camps/TableauDeBordCamp.aspx");
	       httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
	       httppost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
	       httppost.addHeader("Accept-Language","fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
	       httppost.addHeader("Accept-Encoding,","gzip, deflate, br");
	       
	       List<NameValuePair> formparams = new ArrayList<NameValuePair>();       
	       formparams.add(new BasicNameValuePair("ctl00_MainContent_ChercheurCamps1__pnlFormulaire_CurrentState","false"));
		   formparams.add(new BasicNameValuePair("__EVENTTARGET","ctl00$MainContent$ChercheurCamps1$_btnExporter"));
		   formparams.add(new BasicNameValuePair("__EVENTARGUMENT",""));
		   formparams.add(new BasicNameValuePair("__LASTFOCUS","")); 
		   formparams.add(new BasicNameValuePair("__VIEWSTATE",viewstate));
		   formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","83318356"));
		   formparams.add(new BasicNameValuePair("__EVENTVALIDATION","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"));
		   formparams.add(new BasicNameValuePair("ctl00$_ddRechercheType","0"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalId","ctl00__btnAvisUtilisateur__modal"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurModalFrameId","ctl00__btnAvisUtilisateur__modalFrame"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateurUrl","https://sgdf.limequery.net/725453?lang=fr"));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAvisUtilisateur",""));
		   formparams.add(new BasicNameValuePair("ctl00$_hfAlertesId","150,149,137,135,133,119"));
		   formparams.add(new BasicNameValuePair("ctl00$_navbar$_ddRechercheTypeXS","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$TypeSuivi","_rbCampsPreparesParmisParticipantes"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_hidCodeStructure",""+structure));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_txtAutoComplete",""+ tbStructure));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_hiddenAutoComplete",tbAutoCompleteCode));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_hdSelectionChanged",""));
		   formparams.add(new BasicNameValuePair("eo_version","11.0.20.2"));
		   formparams.add(new BasicNameValuePair("eo_style_keys","/wFk"));
		   formparams.add(new BasicNameValuePair("_eo_js_modules",""));
		   formparams.add(new BasicNameValuePair("_eo_obj_inst",""));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_cbCumule","on"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddTypeStructure","-1"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddSpecialite","-1"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_tbCodePostal",""));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_tbPays",""));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddDepartementAdministratif","-1"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_dpbDateMin$_tbDate","03/09/2018"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_dpbDateMax$_tbDate","31/08/2019"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlDossierTransmisAuRGLetACCOPED","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlValidationDuRGLetACCOPED","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlValidationDuTerritoire","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlValiditeDesReglesJS","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlFicheJSTransmise","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlEtatGeneralDuDossier","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlVisasMarins","0"));
		   formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_ddlVisasPourLEtranger","0"));
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
           doc = Jsoup.parse(obj);
           Elements tables = doc.select("table");
           return tables.html();
	}
}
