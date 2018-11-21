package org.leplan73.outilssgdf.servlet.war;

import org.apache.logging.log4j.LogManager;

public class Logger {

	static private org.apache.logging.log4j.Logger logger_;
	
	static public void init()
	{
		logger_ = LogManager.getLogger("org.leplan73.outilssgdf.war");
	}
	
	static public org.apache.logging.log4j.Logger get() {
		return logger_;
	}
}
