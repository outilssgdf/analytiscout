package org.leplan73.analytiscout.intranet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class ExtractionIntranet {

	private static final String HTTPS_INTRANET_SGDF_FR = "https://intranet.sgdf.fr";
	private static final String HTTPS_TESTINTRANET_SGDF_FR = "https://intranet-qualification.sgdf.fr";
	
	public final static String GENERATEUR_CSV = "csv";
	public final static String GENERATEUR_XML = "xml";
	public final static String GENERATEUR_XLS = "xls";
	public final static String GENERATEUR_CSVMYSQL = "csvmysql";
	
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
	protected WebClient webClient;
	protected HtmlPage page;
	
	public static String getIntranet()
	{
		return testmode_ ? HTTPS_TESTINTRANET_SGDF_FR : HTTPS_INTRANET_SGDF_FR;
	}
	
	private static boolean testmode_ = false;
	private boolean nouvelIntranet_ = true;
    
	static
	{
		logger_ = LoggerFactory.getLogger(ExtractionIntranet.class);
	}
	
	public static void setQualifications(boolean testmode)
	{
		testmode_ = testmode;
	}
	
	public static String getIntranetServeur()
	{
		if (testmode_)
			return " (serveur de qualification)";
		return "";
	}
	
	public void init(boolean complet)
	{
		httpclient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
		if (complet)
		{
			webClient = new WebClient();
			webClient.getOptions().setTimeout(300*1000*10);
			webClient.getOptions().setDownloadImages(false);
			webClient.getOptions().setAppletEnabled(false);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setPopupBlockerEnabled(false);
            webClient.getOptions().setRedirectEnabled(true);
		}
	}

	public void close() throws IOException {
		httpclient.close();
		if (webClient != null) webClient.close();
	}
	
	public boolean login(String identifiant, String motdepasse) throws ClientProtocolException, IOException, LoginEngineException
	{
		boolean ret = loginHttp(identifiant, motdepasse);
		boolean ret2 = loginWebClient(identifiant, motdepasse);
		
		return ret && ret2;
	}
	
	public class RpCode
	{
		public String id;
		public String name;
		public String jcode() {
			return "[{\"id\":\""+id+"\",\"name\":\""+name+"\"}]";
		}
	}
	
	public RpCode code(int structure) throws ParseException, IOException
	{
		for (;;)
		{
			HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet()
					+ "/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
			httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.addHeader("Accept-Language", "fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
			if (logger_.isDebugEnabled())
			{
				Header[] headers = httpget.getAllHeaders();
				for (Header header : headers)
				{
					logger_.debug("header: "+header.getName()+":"+header.getValue());
				}
			}
			CloseableHttpResponse response = httpclient.execute(httpget);
	
			HttpEntity entity = response.getEntity();
			String obj = EntityUtils.toString(entity);
			if (logger_.isDebugEnabled())
				logger_.debug(obj);
			Document doc = Jsoup.parse(obj);
			if (doc == null)
				continue;
			viewstate = doc.select("#__VIEWSTATE").first().val();
			response.close();
	
			// Extraction des codes structure "dd" internes (visible avec un profile
			// "Groupe")
			Element ddCodes = doc.selectFirst("select[id=ctl00_MainContent__EditeurRegistrePresence__navigateur__ddStructure]");
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
					String tbAutoCompleteCode = JsonPath.read(jsonDocument, "$.d");
					jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(tbAutoCompleteCode.toString());
					try
	       			{
						Object nodeId = JsonPath.read(jsonDocument, "$.[0].id");
						Object name = JsonPath.read(jsonDocument, "$.[0].name");
						RpCode rp = new RpCode();
						rp.id = (String)nodeId;
						rp.name = (String)name;
						return rp;
	       			}
	       			catch(PathNotFoundException ex)
	       			{
	       				throw ex;
	       			}
				}
			}
		}
	}
	
	private boolean loginWebClient(String identifiant, String motdepasse) throws FailingHttpStatusCodeException, IOException
	{
		if (webClient != null)
		{
			final URL url = new URL(ExtractionIntranet.getIntranet());
	        page = (HtmlPage)webClient.getPage(url);
	        
	        HtmlForm form = page.getFormByName("ctl01");
	        HtmlTextInput login = form.getInputByName("login");
	        login.setValueAttribute(identifiant);
	        HtmlPasswordInput mdp = form.getInputByName("password");
	        mdp.setValueAttribute(motdepasse);
	        HtmlSubmitInput btn = form.getInputByName("_btnValider");
	        page = btn.click();
	        
	        String contenu = page.asText();
	        if (contenu.contains("J'ai oublié mon mot de passe"))
	        {
	     	   return false;
	        }
		}
       return true;
	}
	
	private boolean loginHttp(String identifiant, String motdepasse) throws ClientProtocolException, IOException
	{
        HttpGet httpget = new HttpGet(ExtractionIntranet.getIntranet());
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
    	viewstate = doc.selectFirst("#__VIEWSTATE").val();
    	eventvalidation = doc.selectFirst("#__EVENTVALIDATION").val();
    	
    	Element login = doc.select("#_divFormulaire").first();
    	if (login != null)
    	{
    		nouvelIntranet_ = false;
    	}
    	
        response.close();
        	
        HttpPost httppost = new HttpPost(ExtractionIntranet.getIntranet());
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
        httppost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httppost.addHeader("Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("__EVENTVALIDATION",eventvalidation));
        formparams.add(new BasicNameValuePair("__EVENTTARGET",""));
        formparams.add(new BasicNameValuePair("__EVENTARGUMENT",""));
        formparams.add(new BasicNameValuePair("__VIEWSTATE",viewstate));
        formparams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR","F4403698"));
        if (nouvelIntranet_)
        {
			formparams.add(new BasicNameValuePair("login",identifiant));
			formparams.add(new BasicNameValuePair("password",motdepasse));
			formparams.add(new BasicNameValuePair("_btnValider","Se connecter"));
        }
        else
        {
            formparams.add(new BasicNameValuePair("ctl00$MainContent$login",identifiant));
            formparams.add(new BasicNameValuePair("ctl00$MainContent$password",motdepasse));
            formparams.add(new BasicNameValuePair("ctl00$MainContent$_btnValider","Se connecter"));
        }
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

	protected boolean checkContentType(CloseableHttpResponse response, String type) {
		Header[] headers = response.getHeaders(HttpHeaders.CONTENT_TYPE);
		boolean found = false;
		for (Header header : headers) {
			if (header.getValue().contains(type) == true) {
				found = true;
				break;
			}
		}
		return found;
	}
}
