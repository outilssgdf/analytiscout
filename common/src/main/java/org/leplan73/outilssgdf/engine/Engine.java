package org.leplan73.outilssgdf.engine;

import java.time.Instant;

import org.leplan73.outilssgdf.Params;
import org.leplan73.outilssgdf.Progress;
import org.slf4j.Logger;

public class Engine {
	protected Progress progress_;
	protected Logger logger_;
	
	private Instant start_;
	
	public Engine(Progress progress, Logger logger)
	{
		progress_ = progress;
		logger_ = logger;
	}
	
	protected void chargeParametres()
	{
		logger_.info("Chargement du fichier de paramètres");
		Params.init();
	}
	
	protected void start()
	{
		start_ = Instant.now();
		if (progress_ != null) progress_.setProgress(0, "Lancement");
	}
	
	protected void stop()
	{
		long d = Instant.now().getEpochSecond() - start_.getEpochSecond();
		if (logger_ != null) logger_.info("Terminé en "+d+" secondes");
		if (progress_ != null) progress_.setProgress(100, "Fini");
	}
}
