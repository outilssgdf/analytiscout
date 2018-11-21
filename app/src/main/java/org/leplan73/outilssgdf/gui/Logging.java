package org.leplan73.outilssgdf.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {

	public static Logger logger_;

	public static void initLogger(Class<?> classn, boolean debug)
	{
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "off");
		System.setProperty("org.slf4j.simpleLogger.log.org.leplan73.outilssgdf",debug ? "debug" : "info");
		if (debug)
		{
			System.setProperty("org.slf4j.simpleLogger.log.org.apache.http", "debug");
			System.setProperty("org.slf4j.simpleLogger.log.org.apache.http.wire", "debug");
		}
		logger_ = LoggerFactory.getLogger(classn);
	}
}
