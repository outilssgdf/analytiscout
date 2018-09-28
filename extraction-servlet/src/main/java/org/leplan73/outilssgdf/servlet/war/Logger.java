package org.leplan73.outilssgdf.servlet.war;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class Logger {

	static private Log logger_;
	
	static public void init()
	{
		logger_ = LogFactory.getLog("org.leplan73.outilssgdf.war");
	}
	
	static public Log get() {
		return logger_;
	}
}
