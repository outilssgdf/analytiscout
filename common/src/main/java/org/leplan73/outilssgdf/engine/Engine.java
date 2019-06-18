package org.leplan73.outilssgdf.engine;

import org.leplan73.outilssgdf.Progress;
import org.slf4j.Logger;

public class Engine {
	protected Progress progress_;
	protected Logger logger_;
	
	public Engine(Progress progress, Logger logger)
	{
		progress_ = progress;
		logger_ = logger;
	}
}
