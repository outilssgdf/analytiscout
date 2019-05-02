package org.leplan73.outilssgdf.cmd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import com.jcabi.manifests.Manifests;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "analyseurenligne", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class AnalyseurEnLigne extends CommonParamsIntranet {

	@Option(names = "-batch", description = "Fichier de batch contenant les extractions à effectuer (Valeur par défaut: ${DEFAULT-VALUE})")
	private File batch = new File("conf/batch_responsables.txt");

	@Option(names = "-modele", description = "Fichier de modèle facilitant la présentation de l'analyse (Valeur par défaut: ${DEFAULT-VALUE})")
	private File modele = new File("conf/modele_responsables.xlsx");

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
					int diplome = pbatch.getProperty("diplome."+index,"").isEmpty() ? ExtractionIntranet.DIPLOME_TOUT : Integer.parseInt(pbatch.getProperty("diplome."+index));
					int qualif = pbatch.getProperty("qualif."+index,"").isEmpty() ? ExtractionIntranet.QUALIFICATION_TOUT : Integer.parseInt(pbatch.getProperty("qualif."+index));
					int formation = pbatch.getProperty("formation."+index,"").isEmpty() ? ExtractionIntranet.FORMATION_TOUT : Integer.parseInt(pbatch.getProperty("formation."+index));
					int format = pbatch.getProperty("format."+index,"").isEmpty() ? ExtractionIntranet.FORMAT_INDIVIDU : Integer.parseInt(pbatch.getProperty("format."+index));
					int categorie = pbatch.getProperty("categorie."+index,"").isEmpty() ? ExtractionIntranet.CATEGORIE_TOUT : Integer.parseInt(pbatch.getProperty("categorie."+index));
					int type = pbatch.getProperty("type."+index,"").isEmpty() ? ExtractionIntranet.TYPE_TOUT : Integer.parseInt(pbatch.getProperty("type."+index));
					int specialite = pbatch.getProperty("specialite."+index,"").isEmpty() ? ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE : Integer.parseInt(pbatch.getProperty("specialite."+index));
					boolean adherentsseul = pbatch.getProperty("adherents."+index,"").isEmpty() ? false : Boolean.parseBoolean(pbatch.getProperty("adherents."+index));
					String nom = pbatch.getProperty("nom." + index, "");
					String fonction = pbatch.getProperty("fonction."+index);
					
					ExtraKey extra = new ExtraKey(pbatch.getProperty("fichier." + index, nom), nom, pbatch.getProperty("batchtype." + index, "tout_responsables"));
					
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
				ExtracteurIndividusHtml adherents = new ExtracteurIndividusHtml(in, extraMap,age);
		 
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
		
			    Logging.logger_.info("Génération du fichier \""+sortie.getName()+"\" à partir du modèle \""+modele.getName()+"\"");
				
				Map<String, Object> beans = new HashMap<String, Object>();
				beans.put("adherents", adherents.getAdherentsList());
				beans.put("chefs", adherents.getChefsList());
				beans.put("compas", adherents.getCompasList());
				beans.put("unites", adherents.getUnitesList());
				beans.put("general", general);
				beans.put("global", global);
				Transformeur.go(modele, beans, sortie);
			}

			logout();
		} catch (IOException|JDOMException | InvalidFormatException | ExtractionException e) {
			Logging.logError(e);
		}
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
}
