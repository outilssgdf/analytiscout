package org.leplan73.outilssgdf.intranet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ExtractionFormations extends ExtractionMain {

	public String extract(int structure, String fonction, int categorie, int format) throws ClientProtocolException, IOException, JDOMException
	{
		HttpGet httpget = new HttpGet("https://intranet.sgdf.fr/Specialisation/Sgdf/Formations/ExtraireFormations.aspx");
	       httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
	       httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	       httpget.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
	       CloseableHttpResponse response11 = httpclient.execute(httpget);
	       
           HttpEntity entity = response11.getEntity();
           String obj = EntityUtils.toString(entity);
            if (logger_.isDebugEnabled())
            	logger_.debug(obj);
            Document doc = Jsoup.parse(obj);
            viewstate = doc.select("#__VIEWSTATE").first().val();
            response11.close();
	       
	       HttpPost httppost = new HttpPost("https://intranet.sgdf.fr/Specialisation/Sgdf/Formations/ExtraireFormations.aspx");
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
			formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","D7E61A64"));
			formparams.add(new BasicNameValuePair("ctl00$_tbRechAdherent",""));
			formparams.add(new BasicNameValuePair("ctl00$_tbRechStructure","")); 
			formparams.add(new BasicNameValuePair("eo_version","11.0.20.2"));
			formparams.add(new BasicNameValuePair("eo_style_keys","/wFk"));
			formparams.add(new BasicNameValuePair("ctl00$_ddDelegations","0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddlRequetesExistantes","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_tbNomNouvelleRequete",""));
			if (structure != 0)
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_selecteur$_ddStructure",""+structure));
			else
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_selecteur$_ddStructure","0"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_tbCodesFonctions",fonction != null ? fonction : "")); 
			formparams.add(new BasicNameValuePair("ctl00$MainContent$CodesFonctionsSignifications","_rbCFS_FonctionsPrincipales"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddCategorieMembre",""+categorie));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddTypeInscription","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddSpecialite","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddTypeContact","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddDiplome","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddQualification","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddFormation","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddRevue","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddNpai","tous"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddMailInfoMouv","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_ddMailInfoExt","-1"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_btnExporter.x","53"));
			formparams.add(new BasicNameValuePair("ctl00$MainContent$_btnExporter.y","13"));
			formparams.add(new BasicNameValuePair("ctl00$_hidReferenceStatistiqueUtilisation","-1"));
			if (format == ExtractionMain.FORMAT_TOUT)
			{
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireIndividu","on"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireParents","on"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireInscription","on"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireAdhesion","on"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireJsInformations","on"));
			}
			if (format == ExtractionMain.FORMAT_INDIVIDU)
			{
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireIndividu","on"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireParents","off"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireInscription","off"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireAdhesion","off"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireJsInformations","off"));
			}
			if (format == ExtractionMain.FORMAT_INDIVIDU_PARENTS)
			{
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireIndividu","on"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireParents","on"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireInscription","off"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireAdhesion","off"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireJsInformations","off"));
			}
			else
			{
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireIndividu","on"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireParents","off"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireInscription","off"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireAdhesion","off"));
				formparams.add(new BasicNameValuePair("ctl00$MainContent$_cbExtraireJsInformations","on"));
			}
			  
	        entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
	        httppost.setEntity(entity);
	       CloseableHttpResponse response3 = httpclient.execute(httppost);
	       
           entity = response3.getEntity();
           obj = EntityUtils.toString(entity);
           if (logger_.isDebugEnabled())
           	logger_.debug(obj);
           doc = Jsoup.parse(obj);
           Elements tables = doc.select("table");
           return tables.html();
	}
}
