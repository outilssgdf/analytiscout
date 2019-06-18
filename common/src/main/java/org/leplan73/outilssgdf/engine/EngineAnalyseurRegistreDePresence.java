package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Progress;
import org.slf4j.Logger;

public class EngineAnalyseurRegistreDePresence extends Engine {

	public EngineAnalyseurRegistreDePresence(Progress progress, Logger logger) {
		super(progress, logger);
	}
	
	private boolean gopriv(File entree, File modele, File sortie) throws ClientProtocolException, IOException, JDOMException, InvalidFormatException, ExtractionException
	{
		return true;
	}

	public void go(File entree, File modele, File sortie) throws Exception
	{
		Instant now = Instant.now();
		try
		{
			gopriv(entree, modele, sortie);
			progress_.setProgress(80);
		} catch (IOException | JDOMException | InvalidFormatException | ExtractionException e) {
			throw e;
		}
		progress_.setProgress(100);

		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		logger_.info("Terminé en " + d + " secondes");
	}

}
