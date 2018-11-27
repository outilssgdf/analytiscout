package org.leplan73.outilssgdf.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.util.ContextInitializer;

public class Logging {

	public static Logger logger_;

	public static void initLogger(Class<?> classn, boolean debug)
	{
		System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback-gui.xml");
		if (debug)
		{
			System.setProperty("org.slf4j.simpleLogger.log.org.apache.http", "debug");
			System.setProperty("org.slf4j.simpleLogger.log.org.apache.http.wire", "debug");
		}
		logger_ = LoggerFactory.getLogger(classn);
	}
}
