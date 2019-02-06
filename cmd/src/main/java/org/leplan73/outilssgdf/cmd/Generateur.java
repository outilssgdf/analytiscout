package org.leplan73.outilssgdf.cmd;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.zip.ZipOutputStream;

import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsIntranet;
import org.leplan73.outilssgdf.cmd.utils.Logging;
import org.leplan73.outilssgdf.formatage.GmailCsvFormatteur;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generateur", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class)
public class Generateur extends CommonParamsIntranet {

	@Option(names = "-sortie", required=true, description = "Fichier de sortie")
	private File sortie;

	@Override
	public void run(CommandLine commandLine) throws CmdLineException
	{
		Instant now = Instant.now();
		
		checkParams();
		
		Logging.logger_.info("Lancement");
	    
	    chargeParametres();
	    
		try {
			charge();
			
			// Connexion
			ExtractionAdherents app = new ExtractionAdherents();
			login(app);

			// Extraction des données
			Logging.logger_.info("Extraction (structure="+structures[0]+")");
			String donnees = app.extract(structures[0], true, ExtractionIntranet.TYPE_INSCRIT, false, null, ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE, ExtractionIntranet.CATEGORIE_TOUT, ExtractionIntranet.DIPLOME_TOUT,ExtractionIntranet.QUALIFICATION_TOUT,ExtractionIntranet.FORMATION_TOUT, ExtractionIntranet.FORMAT_INDIVIDU|ExtractionIntranet.FORMAT_PARENTS,false);
			logout();
			
			// Conversion des données
			Logging.logger_.info("Conversion");
			ExtracteurIndividusHtml x = new ExtracteurIndividusHtml();
			x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true);

			// Génération de l'archive zip
			Logging.logger_.info("Génération de l'archive "+sortie.getName());
			ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(sortie)));
			GmailCsvFormatteur f = new GmailCsvFormatteur();
		    f.genereEmail(x.getUnites(), x.getParents(), x.getAdherents(), x.getColonnes(), null, zipOut);
		    zipOut.flush();
		    zipOut.close();
			
		} catch (IOException|JDOMException|ExtractionException e) {
			Logging.logError(e);
		}
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
}
