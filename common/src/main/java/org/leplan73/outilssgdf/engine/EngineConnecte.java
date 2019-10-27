package org.leplan73.outilssgdf.engine;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.slf4j.Logger;

public class EngineConnecte extends Engine {
	protected ExtractionIntranet connection_;

	public EngineConnecte(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	protected void login(ExtractionIntranet connection, String identifiant, String motdepasse, boolean full) throws ClientProtocolException, IOException, LoginEngineException
	{
		connection_ = connection;
		logger_.info("Connexion");
		
		connection_.init(full);
		if (connection_.login(identifiant,motdepasse) == false)
		{
			throw new LoginEngineException("erreur de connexion");
		}
	}

	protected void login(ExtractionIntranet connection, String identifiant, String motdepasse) throws ClientProtocolException, IOException, EngineException, LoginEngineException
	{
		login(connection, identifiant, motdepasse, false);
	}
	
	protected void logout() throws IOException
	{
		connection_.close();
	}

}
