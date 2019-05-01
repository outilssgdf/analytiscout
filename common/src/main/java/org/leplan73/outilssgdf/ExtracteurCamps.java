package org.leplan73.outilssgdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.extraction.ColonnesAdherents;

public class ExtracteurCamps {
	protected ColonnesAdherents colonnes_;
	
	public ExtracteurCamps(InputStream input) throws ExtractionException, IOException, JDOMException {
		charge(input);
	}

	private void charge(final InputStream stream) throws ExtractionException, IOException, JDOMException {
		chargeStream(stream);
		complete();
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
	
	private void chargeStream(final InputStream stream) throws JDOMException, IOException, ExtractionException
	{
		XPathFactory xpfac = XPathFactory.instance();
		SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document docx = builder.build(stream);
        
        int nbColumns = construitColonnes(docx);
        
        XPathExpression<?> xpa = xpfac.compile("tbody/tr[position() > 1]/td");
        
        List<?> results = xpa.evaluate(docx);
        
        int index = 0;
		Iterator<?> iter = results.iterator();
		while (iter.hasNext())
		{
			if (index % nbColumns == 0)
			{
//				adherent = new Adherent(colonnes_);
			}
			
			Object result = iter.next();
			Element resultElement = (Element) result;
//           adherent.add(index % nbColumns, resultElement.getText());
            index++;
        	if (index % nbColumns == 0)
        	{
        		
        	}
		}
	}
	
	private void complete()
	{
		
	}
}
