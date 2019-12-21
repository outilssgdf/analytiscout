package org.leplan73.analytiscout.servlet.war;

import org.slf4j.LoggerFactory;

public class Logger {

	static private org.slf4j.Logger logger_;
	
	static public void init()
	{
		logger_ = LoggerFactory.getLogger("org.leplan73.analytiscout.war");
	}
	
	static public org.slf4j.Logger get() {
		return logger_;
	}
}
