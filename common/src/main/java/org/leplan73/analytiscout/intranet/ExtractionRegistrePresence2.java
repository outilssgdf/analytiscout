package org.leplan73.analytiscout.intranet;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.jdom2.JDOMException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class ExtractionRegistrePresence2 extends ExtractionIntranet {
	public String extract(int structure, boolean recursif, int saison, int period, boolean forfaitaire)
			throws ClientProtocolException, IOException, JDOMException, InterruptedException {
		
		RpCode code = code(structure);
		
		HtmlAnchor a = page.getAnchorByHref("/Specialisation/Sgdf/ActivitesAnnee/ConsulterRegistrePresence.aspx");
        HtmlPage page3 = a.click(false, false, false, false, true, false);

        // Saison
        HtmlForm form2 = page3.getFormByName("aspnetForm");
        HtmlSubmitInput btn2 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_btnExporterExcel");
        
        HtmlSelect select1 = form2.getSelectByName("ctl00$MainContent$_EditeurRegistrePresence$_ddSaison");
        select1.setSelectedIndex(1);
        page3 = select1.click();
        Thread.sleep(5*1000);
        
        // Groupe
        form2 = page3.getFormByName("aspnetForm");
        btn2 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_btnExporterExcel");
        
       try
        {
    	   DomElement el = page3.getElementById("ctl00_MainContent__EditeurRegistrePresence__navigateur__btnHomeAll");
    	   page3 = el.click();
    	   form2 = page3.getFormByName("aspnetForm");
           btn2 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_btnExporterExcel");

        	HtmlTextInput input2 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_autocompleteStructures$_txtAutoComplete");
        	input2.setValueAttribute(""+structure);
	        page3 = input2.click(false, false, false, false, true, false);
	        Thread.sleep(5*1000);
        }
        catch(ElementNotFoundException e)
        {
        }
        
        // Période
        form2 = page3.getFormByName("aspnetForm");
        btn2 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_btnExporterExcel");
        
        HtmlSelect select2 = form2.getSelectByName("ctl00$MainContent$_EditeurRegistrePresence$_ddPeriodes");
        select2.setSelectedIndex(0);
        page3 = select2.click();
        Thread.sleep(5*1000);
        
        form2 = page.getFormByName("aspnetForm");
        try
        {
    	   HtmlHiddenInput input2 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_hidCodeStructure");
    	   input2.setValueAttribute(String.valueOf(structure));
	        
    	   HtmlTextInput input3 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_autocompleteStructures$_txtAutoComplete");
    	   input3.setValueAttribute(code.id);
	        
	        input2 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_navigateur$_autocompleteStructures$_hiddenAutoComplete");
        	input2.setValueAttribute(code.jcode());
        }
        catch(ElementNotFoundException e)
        {
        }
        
        btn2 = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_btnExporterExcel");
        
        HtmlCheckBoxInput ch = form2.getInputByName("ctl00$MainContent$_EditeurRegistrePresence$_cbExporterSousStructure");
        ch.setNodeValue("1"); 
        ch.click();
        
        TextPage csv = btn2.click();
        String content = csv.getContent();
        webClient.close();
        return content;
	}

}
