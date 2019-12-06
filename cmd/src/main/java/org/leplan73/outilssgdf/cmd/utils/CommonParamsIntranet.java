package org.leplan73.outilssgdf.cmd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.leplan73.outilssgdf.intranet.LoginEngineException;

import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

public class CommonParamsIntranet extends CommonParamsG {
	@Option(names = "-params", description = "Ficher de paramètres contenant l'identifiant et mot de passe intranet")
	protected File params;

	@Option(names = "-identifiant", description = "identifiant intranet")
	protected String identifiant;

	@Option(names = "-motdepasse", description = "mot de passe intranet")
	protected String motdepasse;
	
	@Option(names = "-qualifications", description = "", hidden = true)
	protected boolean qualifications = false;
	
	protected ExtractionIntranet connection_;
	
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

	protected void login(ExtractionIntranet connection) throws ClientProtocolException, IOException, CmdLineException, LoginEngineException
	{
		connection_ = connection;
		Logging.logger_.info("Connexion");
		
		connection_.init(false);
		connection_.login(identifiant,motdepasse);
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
