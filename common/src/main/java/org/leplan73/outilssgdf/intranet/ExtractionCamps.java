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
		HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/camps/camps/TableauDeBordCamp.aspx");
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
	       
	       HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet()+"/Specialisation/Sgdf/Formations/ExtraireFormations.aspx");
	       httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
	       httppost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	       httppost.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
	       List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	       
	       formparams.add(new BasicNameValuePair("ctl00_MainContent_ChercheurCamps1__pnlFormulaire_CurrentState","false"));
		   formparams.add(new BasicNameValuePair("__EVENTTARGET","ctl00$MainContent$ChercheurCamps1$_btnExporter"));
		   formparams.add(new BasicNameValuePair("__EVENTARGUMENT",""));
		   formparams.add(new BasicNameValuePair("tl00$_ddRechercheType","0"));
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
