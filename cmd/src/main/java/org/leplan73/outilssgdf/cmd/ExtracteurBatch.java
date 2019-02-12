package org.leplan73.outilssgdf.cmd;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "extracteurbatch", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class ExtracteurBatch extends CommonParamsIntranet {

	@Option(names = "-batch", description = "Fichier de batch contenant les extractions à effectuer (Valeur par défaut: ${DEFAULT-VALUE})")
	private File batch = new File("conf/batch_responsables.txt");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;

	@Option(names = "-recursif", description = "Extraction récursive (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean recursif = true;

	@Override
	public void run(CommandLine commandLine) throws CmdLineException {
		Instant now = Instant.now();
		checkParams();
		
		Logging.logger_.info("Lancement");
	    
	    chargeParametres();
	    
		try {
			charge();

			Logging.logger_.info("Chargement du fichier de traitement");
			
			Properties pbatch = new Properties();
			pbatch.load(new FileInputStream(batch));
			
			ExtractionAdherents app = new ExtractionAdherents();
			login(app);
			
			for (int structure : structures)
			{
				Logging.logger_.info("Traitement de la structure "+structure);
				
				int index=1;
				for(;;)
				{
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
					
					File dossierStructure = new File(sortie,""+structure);
					dossierStructure.mkdirs();
					
					File fichier = new File(dossierStructure, nom+"."+generateur);
					
					if (generateur.compareTo(ExtractionIntranet.GENERATEUR_XLS) == 0)
					{
						Logging.logger_.info("Extraction du fichier "+index+" dans "+fichier);
						
						Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichier), Consts.ENCODING_WINDOWS));
						String donnees = app.extract(structure,recursif,type,adherents,fonction,specialite,categorie, diplome,qualif,formation,format, true);
						out.write(donnees);
						out.flush();
						out.close();
						Logging.logger_.info("Extraction du fichier "+index+" fait");
					}
					else
					if (generateur.compareTo(ExtractionIntranet.GENERATEUR_XML) == 0)
					{
						Logging.logger_.info("Extraction du fichier "+index+" dans "+fichier);
						
						Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichier), Consts.ENCODING_UTF8));
						String donnees = app.extract(structure,recursif,type,adherents,fonction,specialite,categorie, diplome,qualif,formation,format, false);
						out.write(donnees);
						out.flush();
						out.close();
						Logging.logger_.info("Extraction du fichier "+index+" fait");
					}
					else
					if (generateur.compareTo(ExtractionIntranet.GENERATEUR_CSV) == 0)
					{
						Logging.logger_.info("Extraction du fichier "+index+" dans "+fichier);
						
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
						
						Logging.logger_.info("Extraction du fichier "+index+" fait");
					}
					index++;
				}
			}
			logout();
		} catch (IOException|JDOMException e) {
			Logging.logError(e);
		}

		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
}
