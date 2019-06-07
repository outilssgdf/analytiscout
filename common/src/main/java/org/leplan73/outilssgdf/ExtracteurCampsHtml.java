package org.leplan73.outilssgdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.camp.Camp;
import org.leplan73.outilssgdf.camp.Camps;
import org.leplan73.outilssgdf.camp.Chef;
import org.leplan73.outilssgdf.extraction.ColonnesCamps;

public class ExtracteurCampsHtml {
	
	protected ColonnesCamps colonnes_;
	protected Camps camps_;
	
	public ExtracteurCampsHtml() {
	}
	
	public ExtracteurCampsHtml(InputStream input) throws ExtractionException, IOException, JDOMException {
		charge(input);
	}
	
	public ExtracteurCampsHtml(File fichier) throws ExtractionException, IOException, JDOMException {
		charge(fichier);
	}
	
	public void charge(final String donnnes) throws ExtractionException, IOException, JDOMException
	{
   		ByteArrayInputStream excelFile = new ByteArrayInputStream(donnnes.getBytes());
   		charge(excelFile);
		excelFile.close();
	}
	
	public void charge(final File fichier) throws ExtractionException, IOException, JDOMException
	{
   		FileInputStream excelFile = new FileInputStream(fichier);
   		charge(excelFile);
		excelFile.close();
	}
	
	private int construitColonnes(final org.jdom2.Document docx) throws ExtractionException, JDOMException, IOException
	{
		XPathFactory xpfac = XPathFactory.instance();
		
        // Scan des colonnes
        colonnes_ = new ColonnesCamps();
		XPathExpression<?> xpac = xpfac.compile("tbody/tr[1]/td/text()");
		 List<?> resultsc = xpac.evaluate(docx);
		 Iterator<?> firstCellIterator = resultsc.iterator();
		 int nbColumns = 0;
		 while (firstCellIterator.hasNext()) {
			 Object result = firstCellIterator.next();
				if(result instanceof Text)
				{
					Text resultElement = (Text) result;
					colonnes_.add(nbColumns++, resultElement.getText());
				}
		 }
        
        return nbColumns;
	}
	
	private void complete()
	{
	}
	
	public void charge(List<InputStream> streams) throws ExtractionException, IOException, JDOMException
	{
		complete();
	}
	
	public void charge(final InputStream stream) throws ExtractionException, IOException, JDOMException
	{
		chargeStream(stream);
		complete();
	}
	
	private void chargeStream(final InputStream stream) throws JDOMException, IOException, ExtractionException
	{
		XPathFactory xpfac = XPathFactory.instance();
		SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document docx = builder.build(stream);
        
        int nbColumns = construitColonnes(docx);

        // Chargement des lignes d'adherents
        camps_ = new Camps();
        
        XPathExpression<?> xpa = xpfac.compile("tbody/tr[position() > 1]/td");
        
        List<?> results = xpa.evaluate(docx);
        
        int index = 0;
        Camp camp = null;
        Chef maitrise = null;
		Iterator<?> iter = results.iterator();
		while (iter.hasNext())
		{
			Object result = iter.next();
			Element resultElement = (Element) result;
			
			// Création du camp 
			if (index % nbColumns == 0)
			{
				camp = camps_.get(resultElement.getText());
				if (camp == null) {
					camp = new Camp(resultElement.getText());
		        	camp.init();
					camps_.put(camp.getStructuresOrganistratices(), camp);
				}
				maitrise = new Chef();
				camp.add(maitrise);
			}
			
			// Maitrise
			maitrise.add(colonnes_.get(index % nbColumns), resultElement.getText());
            index++;
		}
	}

	public Collection<Camp> camps() {
		return camps_.values();
	}
}
