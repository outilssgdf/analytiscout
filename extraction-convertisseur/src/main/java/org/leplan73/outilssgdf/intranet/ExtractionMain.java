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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ExtractionMain {

	public final static String GENERATEUR_CSV = "csv";
	public final static String GENERATEUR_XML = "xml";
	public final static String GENERATEUR_XLS = "xls";
	
	public final static int STRUCTURE_TOUT = 0;
	
	public final static int DIPLOME_TOUT = -1;
	public final static int DIPLOME_AFPS = 619;
	public final static int DIPLOME_BAFA = 73;
	public final static int DIPLOME_BAFD = 140;
	public final static int DIPLOME_PSC1 = 603;
	
	public final static int QUALIFICATION_TOUT = -1;
	public final static int QUALIFICATION_ANIMSF_CAFASF = 2;
	public final static int QUALIFICATION_DIRSF_CAFDSF = 1;
	public final static int QUALIFICATION_RESPONSABLE_UNITE_CAFRUSF = 3;
	
	public final static int FORMATION_TOUT = -1;
	public final static int FORMATION_AFPS = 18;
	public final static int FORMATION_APF = 219;
	public final static int FORMATION_APF_CHEFS = 248;
	public final static int FORMATION_APF_RG = 250;
	public final static int FORMATION_APPRO = 53;
	public final static int FORMATION_APPRO_ACCUEIL_SCOUTISME = 243;
	public final static int FORMATION_APPRO_ANIMATION = 244;
	public final static int FORMATION_CHAM = 77;
	public final static int FORMATION_PSC1 = 228;
	public final static int FORMATION_STAF = 42;
	public final static int FORMATION_STIF = 253;
	public final static int FORMATION_TECH = 52;
	
	public final static int FORMAT_INDIVIDU = 1 << 0;
	public final static int FORMAT_PARENTS = 1 << 1;
	public final static int FORMAT_INSCRIPTION = 1 << 2;
	public final static int FORMAT_ADHESION = 1 << 3;
	public final static int FORMAT_JS = 1 << 4;
	public final static int FORMAT_SANS_QF = 1 << 5;
	
	public final static int CATEGORIE_TOUT = -1;
	public final static int CATEGORIE_JEUNE = 0;
	public final static int CATEGORIE_RESPONSABLE = 1;
	
	public final static int SPECIALITE_SANS_IMPORTANCE = -1;
	public final static int SPECIALITE_MARINE = 622;
	public final static int SPECIALITE_SANS = 624;
	public final static int SPECIALITE_VENT_DU_LARGE = 623;
	
	public final static int TYPE_TOUT = -1;
	public final static int TYPE_INSCRIT = 0;
	public final static int TYPE_INVITE = 1;
	public final static int TYPE_PREINSCRIT = 2;
	
	protected CloseableHttpClient httpclient;
	protected String eventvalidation;
	protected String viewstate;
	protected static Logger logger_;
    
	static
	{
		logger_ = LogManager.getLogger(ExtractionMain.class);
	}
	
	public void init()
	{
		httpclient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
	}

	public void close() throws IOException {
		httpclient.close();
	}
	
	public boolean login(String identifiant, String motdepasse) throws ClientProtocolException, IOException
	{
        HttpGet httpget = new HttpGet("https://intranet.sgdf.fr");
        httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
        httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpget.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
        
        CloseableHttpResponse response = httpclient.execute(httpget);
        
        String viewstate = null;
        String eventvalidation = null;
        HttpEntity entity = response.getEntity();
        String obj = EntityUtils.toString(entity);
        if (logger_.isDebugEnabled())
        	logger_.debug(obj);
    	Document doc = Jsoup.parse(obj);
    	viewstate = doc.select("#__VIEWSTATE").first().val();
    	eventvalidation = doc.select("#__EVENTVALIDATION").first().val();
        response.close();
        	
        HttpPost httppost = new HttpPost("https://intranet.sgdf.fr");
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
        httppost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httppost.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("__EVENTVALIDATION",eventvalidation));
        formparams.add(new BasicNameValuePair("__EVENTTARGET",""));
        formparams.add(new BasicNameValuePair("__EVENTARGUMENT",""));
        formparams.add(new BasicNameValuePair("__VIEWSTATE",viewstate));
        formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","F4403698"));
        formparams.add(new BasicNameValuePair("ctl00$MainContent$login",identifiant));
        formparams.add(new BasicNameValuePair("ctl00$MainContent$password",motdepasse));
        formparams.add(new BasicNameValuePair("ctl00$MainContent$_btnValider","Se connecter"));
        formparams.add(new BasicNameValuePair("eo_version","11.0.20.2"));
        formparams.add(new BasicNameValuePair("eo_style_keys","/wFk"));
		if (logger_.isDebugEnabled())
		{
			formparams.forEach(k ->
			{
				logger_.debug("Param : " + k.getName() + " -> " + k.getValue());
			});
		}
		
        entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        httppost.setEntity(entity);
        response = httpclient.execute(httppost);
       
       entity = response.getEntity();
       obj = EntityUtils.toString(entity);
       if (logger_.isDebugEnabled())
       	logger_.debug(obj);
       if (obj.contains("J'ai oublié mon mot de passe"))
       {
    	   return false;
       }
       response.close();
       return true;
	}
}