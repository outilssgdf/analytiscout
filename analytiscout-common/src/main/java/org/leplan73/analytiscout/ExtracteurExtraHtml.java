package org.leplan73.analytiscout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.analytiscout.extraction.Adherent;
import org.leplan73.analytiscout.extraction.AdherentForme;
import org.leplan73.analytiscout.extraction.ColonnesAdherents;

public class ExtracteurExtraHtml {
	
	protected List<AdherentForme> adherents_;
	protected ColonnesAdherents colonnes_;
	
	public ExtracteurExtraHtml() throws ExtractionException, IOException, JDOMException {
	}
	
	public ExtracteurExtraHtml(InputStream input, boolean age) throws ExtractionException, IOException, JDOMException {
		charge(input, age);
	}

	public ExtracteurExtraHtml(File fichier, boolean age) throws ExtractionException, IOException, JDOMException {
		charge(fichier, age);
	}

	public List<AdherentForme> getAdherents()
	{
		return adherents_;
	}
	
	public ColonnesAdherents getColonnes()
	{
		return colonnes_;
	}
	
	private void charge(final File fichier, boolean age) throws ExtractionException, IOException, JDOMException
	{
   		FileInputStream excelFile = new FileInputStream(fichier);
   		charge(excelFile, age);
		excelFile.close();
	}
	
	private int construitColonnes(final org.jdom2.Document docx) throws ExtractionException, JDOMException, IOException
	{
		XPathFactory xpfac = XPathFactory.instance();
		
        // Scan des colonnes
        colonnes_ = new ColonnesAdherents();
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
		        
        // Calcul des codes
        colonnes_.calculCodes();
        
        return nbColumns;
	}
	
	public void charge(final InputStream stream, boolean age) throws ExtractionException, IOException, JDOMException
	{
		chargeStream(stream, age);
	}
	
	private void chargeStream(final InputStream stream, boolean age) throws JDOMException, IOException, ExtractionException
	{
		XPathFactory xpfac = XPathFactory.instance();
		SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document docx = builder.build(stream);
        
        int nbColumns = construitColonnes(docx);

        // Chargement des lignes d'adherents
        adherents_ = new ArrayList<AdherentForme>();
        
        XPathExpression<?> xpa = xpfac.compile("tbody/tr[position() > 1]/td");
        
        List<?> results = xpa.evaluate(docx);
        
        int index = 0;
        Adherent adherent = null;
		Iterator<?> iter = results.iterator();
		while (iter.hasNext())
		{
			if (index % nbColumns == 0)
			{
				adherent = new Adherent(colonnes_);
			}
			
			Object result = iter.next();
			Element resultElement = (Element) result;
            adherent.add(index % nbColumns, resultElement.getText());
            index++;
        	if (index % nbColumns == 0)
        	{
            	adherent.init(age);
           		adherents_.add(new AdherentForme(adherent));
        	}
		}
	}
}
