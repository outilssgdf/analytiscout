package org.leplan73.outilssgdf.cmd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

import com.jcabi.manifests.Manifests;

import net.sf.jett.transform.ExcelTransformer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseurenligne", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurEnLigne extends CommonParamsIntranet {

	@Option(names = "-batch", description = "Fichier de batch contenant les extractions à effectuer (Valeur par défaut: ${DEFAULT-VALUE})")
	private File batch = new File("conf/batch.txt");

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele.xlsx");

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;
	
	@Option(names = "-age", description = "Gère l'âge des adhérents (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean age = false;

	@Option(names = "-recursif", description = "Extraction récursive (Valeur par défaut: ${DEFAULT-VALUE})")
	private boolean recursif = true;
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		Instant now = Instant.now();
		
		Logging.logger_.info("Lancement");
	    
	    chargeParametres();

		try {
		    Logging.logger_.info("Chargement du fichier de traitement");
			
			Properties pbatch = new Properties();
			pbatch.load(new FileInputStream(batch));
			
			ExtractionAdherents app = new ExtractionAdherents();
			login(app);
			
			for (int structure : structures)
			{
				Logging.logger_.info("Traitement de la structure "+structure);
				
				Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();

				String donneesAdherents=null;
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
					int diplome = pbatch.getProperty("diplome."+index,"").isEmpty() ? ExtractionMain.DIPLOME_TOUT : Integer.parseInt(pbatch.getProperty("diplome."+index));
					int qualif = pbatch.getProperty("qualif."+index,"").isEmpty() ? ExtractionMain.QUALIFICATION_TOUT : Integer.parseInt(pbatch.getProperty("qualif."+index));
					int formation = pbatch.getProperty("formation."+index,"").isEmpty() ? ExtractionMain.FORMATION_TOUT : Integer.parseInt(pbatch.getProperty("formation."+index));
					int format = pbatch.getProperty("format."+index,"").isEmpty() ? ExtractionMain.FORMAT_INDIVIDU : Integer.parseInt(pbatch.getProperty("format."+index));
					int categorie = pbatch.getProperty("categorie."+index,"").isEmpty() ? ExtractionMain.CATEGORIE_TOUT : Integer.parseInt(pbatch.getProperty("categorie."+index));
					int type = pbatch.getProperty("type."+index,"").isEmpty() ? ExtractionMain.TYPE_TOUT : Integer.parseInt(pbatch.getProperty("type."+index));
					int specialite = pbatch.getProperty("specialite."+index,"").isEmpty() ? ExtractionMain.SPECIALITE_SANS_IMPORTANCE : Integer.parseInt(pbatch.getProperty("specialite."+index));
					boolean adherentsseul = pbatch.getProperty("adherents."+index,"").isEmpty() ? false : Boolean.parseBoolean(pbatch.getProperty("adherents."+index));
					String nom = pbatch.getProperty("nom." + index, "");
					String fonction = pbatch.getProperty("fonction."+index);
					
					ExtraKey extra = new ExtraKey(pbatch.getProperty("nom." + index, ""),
							pbatch.getProperty("batchtype." + index, "tout"));
					
					Logging.logger_.info("Extraction de  "+nom);
					String donnees = app.extract(structure,recursif,type,adherentsseul,fonction,specialite,categorie, diplome,qualif,formation,format, false);
					Logging.logger_.info("Extraction de  "+nom+" fait");
					
					if (extra.ifTout()) {
						donneesAdherents = donnees;
					} else {
						InputStream in = new ByteArrayInputStream(donnees.getBytes(Consts.ENCODING_UTF8));
						extraMap.put(extra, new ExtracteurExtraHtml(in, age));
					}
					index++;
				}
				
				InputStream in = new ByteArrayInputStream(donneesAdherents.getBytes(Consts.ENCODING_UTF8));
				ExtracteurHtml adherents = new ExtracteurHtml(in, extraMap,age);
		 
				AdherentFormes compas = new AdherentFormes();
				compas.charge(adherents,extraMap);
				
				String version = "";
				try
				{
					version = Manifests.read("version");
				}
				catch(java.lang.IllegalArgumentException e) {
				}
				General general = new General(version);
				Global global = new Global(adherents.getGroupe(), adherents.getMarins());
				adherents.calculGlobal(global);
		
				FileOutputStream outputStream = new FileOutputStream(sortie);
		
			    Logging.logger_.info("Génération du fichier \""+sortie.getName()+"\" à partir du modèle \""+modele.getName()+"\"");
				ExcelTransformer trans = new ExcelTransformer();
				Map<String, Object> beans = new HashMap<String, Object>();
				beans.put("chefs", adherents.getChefsList());
				beans.put("compas", adherents.getCompasList());
				beans.put("unites", adherents.getUnitesList());
				beans.put("general", general);
				beans.put("global", global);
				Workbook workbook = trans.transform(new FileInputStream(modele), beans);
				workbook.write(outputStream);
				
				outputStream.flush();
				outputStream.close();
			}

			logout();
		} catch (IOException|JDOMException | InvalidFormatException | ExtractionException e) {
			Logging.logError(e);
		}
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
}
