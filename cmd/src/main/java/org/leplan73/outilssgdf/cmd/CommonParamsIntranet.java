package org.leplan73.outilssgdf.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.leplan73.outilssgdf.intranet.ExtractionMain;

import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

public class CommonParamsIntranet extends CommonParamsG {
	@Option(names = "-params", description = "Ficher de paramètres contenant l'identifiant et mot de passe intranet")
	protected File params;

	@Option(names = "-identifiant", description = "identifiant intranet")
	protected String identifiant;

	@Option(names = "-motdepasse", description = "mot de passe intranet")
	protected String motdepasse;
	
	protected ExtractionMain connection_;
	
	protected void charge() throws PicocliException, FileNotFoundException, IOException
	{
		if (params != null)
		{
			Logging.logger_.info("Chargement du fichier de propriétés");
			
			Properties pfile = new Properties();
			pfile.load(new FileInputStream(params));
				
			identifiant = pfile.getProperty("identifiant");
			motdepasse = pfile.getProperty("motdepasse");
			if (identifiant == null)
			{
				Logging.logger_.error("Pas d'identifiant");
				throw new PicocliException("Pas d'identifiant");
			}
			if (motdepasse == null)
			{
				Logging.logger_.error("Pas de mot de passe");
				throw new PicocliException("Pas de mot de passe");
			}
		}
	}

	protected void login(ExtractionMain connection) throws ClientProtocolException, IOException, CmdLineException
	{
		connection_ = connection;
		Logging.logger_.info("Connexion");
		
		connection_.init();
		if (connection_.login(identifiant,motdepasse) == false)
		{
			throw new CmdLineException("erreur de connexion", true);
		}
	}
	
	protected void logout() throws IOException
	{
		connection_.close();
	}
	
	protected void checkParams() throws CmdLineException
	{
		if (params == null && (identifiant == null || motdepasse == null))
		{
			throw new CmdLineException("Identifiants intranet manquants", false);
		}
	}
}
