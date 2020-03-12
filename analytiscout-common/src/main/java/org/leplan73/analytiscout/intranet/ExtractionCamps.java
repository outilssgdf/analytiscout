package org.leplan73.analytiscout.intranet;

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

public class ExtractionCamps extends ExtractionIntranet {

	public String extract(int structure) throws ClientProtocolException, IOException, JDOMException {
		for (;;) {
			HttpGet httpget = new HttpGet(
					ExtractionIntranet.getIntranet() + "/Specialisation/Sgdf/camps/TableauDeBordCamp.aspx");
			httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.addHeader("Accept-Language", "fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3");
			CloseableHttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();
			String obj = EntityUtils.toString(entity);
			if (logger_.isDebugEnabled())
				logger_.debug(obj);
			Document doc = Jsoup.parse(obj);
			if (doc == null)
				continue;
			viewstate = doc.select("#__VIEWSTATE").first().val();
			String viewstategenerator = doc.select("#__VIEWSTATEGENERATOR").first().val();
			String eventvalidation = doc.select("#__EVENTVALIDATION").first().val();
			response.close();

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
			formparams.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$TypeSuivi",
					"_rbCampsPreparesParmisParticipantes"));
			formparams.add(new BasicNameValuePair(
					"ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_hidCodeStructure",
					"" + structure));
			formparams.add(new BasicNameValuePair(
					"ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_txtAutoComplete",
					"" + 1059985));
			formparams.add(new BasicNameValuePair(
					"ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_autocompleteStructures$_hiddenAutoComplete",
					"[{\"id\":\"1059985\",\"name\":\"117540000 - TERRITOIRE PARIS SUD - PORTE DU SOLEIL\"}]"));
			formparams.add(new BasicNameValuePair(
					"ctl00$MainContent$ChercheurCamps1$_selecteurStructuresParticipantes$_hdSelectionChanged", ""));
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
					.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_dpbDateMin$_tbDate", "03/09/2018"));
			formparams
					.add(new BasicNameValuePair("ctl00$MainContent$ChercheurCamps1$_dpbDateMax$_tbDate", "31/08/2019"));
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

			entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			httppost.setEntity(entity);
			response = httpclient.execute(httppost);
			boolean ret = checkContentType(response, "application/vnd.ms-excel");
			if (!ret)
				throw new IOException("Données invalide");
			entity = response.getEntity();
			obj = EntityUtils.toString(entity);
			response.close();
			if (logger_.isDebugEnabled())
				logger_.debug(obj);
			doc = Jsoup.parse(obj);
			Elements tables = doc.select("table");
			String v = tables.html();
			if (v.indexOf("<td align=\"left\">") != -1) {
				continue;
			}
			return v;
		}
	}
}
