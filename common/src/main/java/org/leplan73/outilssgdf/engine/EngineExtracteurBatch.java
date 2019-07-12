package org.leplan73.outilssgdf.engine;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.http.client.ClientProtocolException;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.slf4j.Logger;

public class EngineExtracteurBatch extends EngineConnecte {
	public EngineExtracteurBatch(Progress progress, Logger logger) {
		super(progress, logger);
	}

	private boolean gopriv(ExtractionAdherents app, Properties pbatch, String identifiant, String motdepasse, File batch, File sortie, int structure, boolean recursif, boolean sous_dossier) throws ClientProtocolException, IOException, JDOMException
	{
		logger_.info("Traitement de la structure "+structure);
		
		int index=1;
		for(;;)
		{
			if (progress_.isCanceled()) {
				logger_.info("Action annulée");
				return false;
			}
			
			// generateur.x
			// format.x
			// categorie.x
			// specialite.x
			// fonction.x
			// diplome.x
			// qualif.x
			// formation.x
			// nom.x
			// type.x
			String generateur = pbatch.getProperty("generateur."+index);
			if (generateur == null)
			{
				break;
			}
			int diplome = pbatch.getProperty("diplome."+index,"").isEmpty() ? ExtractionIntranet.DIPLOME_TOUT : Integer.parseInt(pbatch.getProperty("diplome."+index));
			int qualif = pbatch.getProperty("qualif."+index,"").isEmpty() ? ExtractionIntranet.QUALIFICATION_TOUT : Integer.parseInt(pbatch.getProperty("qualif."+index));
			int formation = pbatch.getProperty("formation."+index,"").isEmpty() ? ExtractionIntranet.FORMATION_TOUT : Integer.parseInt(pbatch.getProperty("formation."+index));
			int format = pbatch.getProperty("format."+index,"").isEmpty() ? ExtractionIntranet.FORMAT_INDIVIDU : Integer.parseInt(pbatch.getProperty("format."+index));
			int categorie = pbatch.getProperty("categorie."+index,"").isEmpty() ? ExtractionIntranet.CATEGORIE_TOUT : Integer.parseInt(pbatch.getProperty("categorie."+index));
			int type = pbatch.getProperty("type."+index,"").isEmpty() ? ExtractionIntranet.TYPE_TOUT : Integer.parseInt(pbatch.getProperty("type."+index));
			int specialite = pbatch.getProperty("specialite."+index,"").isEmpty() ? ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE : Integer.parseInt(pbatch.getProperty("specialite."+index));
			boolean adherents = pbatch.getProperty("adherents."+index,"").isEmpty() ? false : Boolean.parseBoolean(pbatch.getProperty("adherents."+index));
			String nom = pbatch.getProperty("nom."+index,"");
			String fonction = pbatch.getProperty("fonction."+index);
			String nfichier = pbatch.getProperty("fichier." + index, nom);
			
			File dossierStructure = sous_dossier ? new File(sortie,""+structure) : sortie;
			dossierStructure.mkdirs();
			
			File fichier = new File(dossierStructure, nfichier + "." + generateur);
			
			if (generateur.compareTo(ExtractionIntranet.GENERATEUR_XLS) == 0)
			{
				logger_.info("Extraction du fichier "+index+" dans "+fichier);
				
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichier), Consts.ENCODING_WINDOWS));
				String donnees = app.extract(structure,recursif,type,adherents,fonction,specialite,categorie,diplome,qualif,formation,format, true);
				out.write(donnees);
				out.flush();
				out.close();
				logger_.info("Extraction du fichier "+index+" fait");
			}
			else
			if (generateur.compareTo(ExtractionIntranet.GENERATEUR_XML) == 0)
			{
				logger_.info("Extraction du fichier "+index+" dans "+fichier);
				
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichier), Consts.ENCODING_UTF8));
				String donnees = app.extract(structure,recursif,type,adherents,fonction,specialite,categorie,diplome,qualif,formation,format, false);
				out.write(donnees);
				out.flush();
				out.close();
				logger_.info("Extraction du fichier "+index+" fait");
			}
			else
			if (generateur.compareTo(ExtractionIntranet.GENERATEUR_CSV) == 0)
			{
				logger_.info("Extraction du fichier "+index+" dans "+fichier);
				
				final CSVPrinter out = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(fichier, Charset.forName(Consts.ENCODING_WINDOWS));
				
				String donnees = app.extract(structure,recursif,type,adherents,fonction,specialite,categorie,diplome,qualif,formation,format, false);
				
				XPathFactory xpfac = XPathFactory.instance();
				SAXBuilder builder = new SAXBuilder();
		        org.jdom2.Document docx = builder.build(new ByteArrayInputStream(donnees.getBytes(Charset.forName(Consts.ENCODING_UTF8))));
		        
		        // Scan des colonnes
		     	XPathExpression<?> xpac = xpfac.compile("tbody/tr[1]/td/text()");
		     	List<?> resultsc = xpac.evaluate(docx);
		     	int nbColumns = resultsc.size();	 
		     			 
		        XPathExpression<?> xpa = xpfac.compile("tbody/tr/td");
		        
		        List<?> results = xpa.evaluate(docx);
		        
		        int indexCsv = 0;
				Iterator<?> iter = results.iterator();
				while (iter.hasNext())
				{
					Object result = iter.next();
					Element resultElement = (Element) result;
					out.print(resultElement.getText());
					indexCsv++;
		        	if (indexCsv % nbColumns == 0)
		        	{
		        		out.println();
		        	}
				}
				out.flush();
				out.close();
				
				logger_.info("Extraction du fichier "+index+" fait");
			}
			index++;
		}
		return true;
	}
	
	public void go(String identifiant, String motdepasse, File batch, File sortie, int[] structures, boolean recursif) throws EngineException {
		start();
		try {
			Properties pbatch = new Properties();
			pbatch.load(new FileInputStream(batch));

			progress_.setProgress(20, "Connexion");
			ExtractionAdherents app = new ExtractionAdherents();
			login(app, identifiant, motdepasse);
			progress_.setProgress(40, "Extraction");

			for (int istructure : structures)
			{
				logger_.info("Traitement de la structure "+istructure);
				boolean ret = gopriv(app, pbatch, identifiant, motdepasse, batch, sortie, istructure, recursif, (structures.length > 1));
				if (ret == false)
					break;
			}
			logout();
		} catch (IOException | JDOMException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
