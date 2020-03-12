package org.leplan73.analytiscout.engine;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.http.client.ClientProtocolException;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.ExtracteurIndividusHtml;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.extraction.ColonnesAdherents;
import org.leplan73.analytiscout.formatage.CsvMySqlFormateur;
import org.leplan73.analytiscout.intranet.ExtractionAdherents;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;
import org.leplan73.analytiscout.intranet.LoginEngineException;
import org.leplan73.analytiscout.outils.FichierSortie;
import org.leplan73.analytiscout.outils.Structure;
import org.slf4j.Logger;

public class EngineExtracteurBatch extends EngineConnecte {
	public EngineExtracteurBatch(Progress progress, Logger logger) {
		super(progress, logger);
	}

	private boolean gopriv(ExtractionAdherents app, Properties pbatch, String identifiant, String motdepasse, File batch, File sortie, int structure, boolean recursif, boolean sous_dossier) throws ClientProtocolException, IOException, JDOMException
	{
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
			
			File fichier = new FichierSortie(dossierStructure, nfichier + "." + generateur);
			
			if (generateur.compareTo(ExtractionIntranet.GENERATEUR_XLS) == 0)
			{
				logger_.info("Extraction du fichier \""+nom+"\" dans "+fichier);
				
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichier), Consts.ENCODING_WINDOWS));
				String donnees = app.extract(structure,recursif,type,adherents,fonction,specialite,categorie,diplome,qualif,formation,format, true);
				out.write(donnees);
				out.flush();
				out.close();
				logger_.info("Extraction du fichier \""+nom+"\" fait");
			}
			else
			if (generateur.compareTo(ExtractionIntranet.GENERATEUR_XML) == 0)
			{
				logger_.info("Extraction du fichier \""+nom+"\" dans "+fichier);
				
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichier), Consts.ENCODING_UTF8));
				String donnees = app.extract(structure,recursif,type,adherents,fonction,specialite,categorie,diplome,qualif,formation,format, false);
				out.write(donnees);
				out.flush();
				out.close();
				logger_.info("Extraction du fichier \""+nom+"\" fait");
			}
			else
			if (generateur.compareTo(ExtractionIntranet.GENERATEUR_CSVMYSQL) == 0)
			{
				logger_.info("Extraction du fichier \""+nom+"\" dans "+fichier);

				nfichier = pbatch.getProperty("batchtype." + index, "script");
				
				fichier = new FichierSortie(dossierStructure, nfichier + "." + "csv");
				
				String donnees = app.extract(structure,recursif,type,adherents,fonction,specialite,categorie,diplome,qualif,formation,format, false);
				
				logger_.info("Conversion");
				try {
					OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(fichier, true), Charset.forName(Consts.ENCODING_WINDOWS));
					final CSVPrinter out = CSVFormat.DEFAULT.withAllowMissingColumnNames().print(w);
					
					ExtracteurIndividusHtml x = new ExtracteurIndividusHtml();
					x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true,false);
					
					Map<String,ExtracteurIndividusHtml> groupes = x.genereGroupes();
					
					if (nfichier.compareTo("tout") == 0)
					{
						// Creation des scripts sql
						File sqlFichier = new File(dossierStructure, "creation.sql");
						PrintStream wSql = new PrintStream(new FileOutputStream(sqlFichier, true));
						
						ColonnesAdherents colonnes = x.getColonnes();
						Set<Integer> noms = colonnes.ids();
						
						wSql.print("CREATE TABLE adherents(");
						for (Integer id : noms)
						{
							String nomSql2 = colonnes.getNom(id);
							String nomSql = colonnes.getNom(id).replace(".", "_");
							String typeSql = "text(1024) NULL";
							if (nomSql2.compareTo("Individu_CodeAdherent") == 0 || nomSql2.compareTo("Structure_CodeStructure") == 0)
								typeSql = "int NULL";
							wSql.print("`"+nomSql+"` "+typeSql);
							wSql.print(",");
						}
						wSql.print(" groupe_code INT NULL, groupe_nom text(1024) NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
						wSql.println();
						
						wSql.println("CREATE TABLE qualifs (code INT NULL, nom text(1024) NULL, `Individu_CodeAdherent` INT NULL, `QualificationsQualificationJeunesseSports.Libelle` text(1024) NULL, `Qualifications_EstTitulaire` text(1024) NULL,`Qualifications_DateFinValidite` DATE NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
						wSql.println("CREATE TABLE diplomes (code INT NULL, nom text(1024) NULL, `Individu_CodeAdherent` INT NULL,`DiplomesType_Libelle` text(1024) NULL,`Diplomes_Numero` text(1024) NULL,`Diplomes_DateObtention` DATE NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
						wSql.println("CREATE TABLE formations (code INT NULL, nom text(1024) NULL, `Individu_CodeAdherent` INT NULL,`FormationsType_Libelle` text(1024) NULL,`Formations_Role` text(1024) NULL,`Formations_DateFin` DATE NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
						
						wSql.flush();
						wSql.close();
						
						for (Integer id : noms)
						{
							String nomSql = colonnes.getNom(id).replace(".", "_");
							out.print(nomSql);
						}
						out.print("groupe_code");
						out.print("groupe_nom");
						out.println();
					}
					
					Set<String> codeGroupes = groupes.keySet();
					for (String codeGroupe : codeGroupes)
					{
						ExtracteurIndividusHtml groupe = groupes.get(codeGroupe);
						
						CsvMySqlFormateur f = new CsvMySqlFormateur();
						
						int id = diplome;
						if (id == ExtractionIntranet.DIPLOME_TOUT)
							id = formation;
						if (id == ExtractionIntranet.FORMATION_TOUT)
							id = qualif;
						f.genereEmail(id,nom, groupe.getUnites(), groupe.getAdherents(), groupe.getColonnes(), out ,!fichier.exists(), groupe.getGroupe());
					}
					out.flush();
					out.close();
				} catch (ExtractionException e) {
					e.printStackTrace();
				}
				
			}
			if (generateur.compareTo(ExtractionIntranet.GENERATEUR_CSV) == 0)
			{
				logger_.info("Extraction du fichier \""+nom+"\" dans "+fichier);
				
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
				
				logger_.info("Extraction du fichier \""+nom+"\" fait");
			}
			index++;
		}
		return true;
	}
	
	public void go(String identifiant, String motdepasse, File batch, File sortie, int[] structures, boolean recursif) throws EngineException, LoginEngineException {
		start();
		try {
			Properties pbatch = new Properties();
			pbatch.load(new FileInputStream(batch));

			progress_.setProgress(20, "Connexion");
			ExtractionAdherents app = new ExtractionAdherents();
			login(app, identifiant, motdepasse);
			progress_.setProgress(40, "Extraction");

			for (int structure : structures)
			{
				logger_.info("Traitement de la structure "+Structure.formatStructure(structure));
				boolean ret = gopriv(app, pbatch, identifiant, motdepasse, batch, sortie, structure, recursif, (structures.length > 1));
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
