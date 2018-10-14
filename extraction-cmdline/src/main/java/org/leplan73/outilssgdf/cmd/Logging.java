package org.leplan73.outilssgdf.cmd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Logging {

	public static Logger logger_;

	public static void initLogger(Class<?> classn)
	{
		ConfigurationSource source;
		try {
			InputStream in = Logging.class.getResourceAsStream("log4j2.xml");
			source = new ConfigurationSource(in);
			Configurator.initialize(null, source);
			logger_ = LogManager.getLogger(classn);
		} catch (FileNotFoundException e1) {
		} catch (IOException e1) {
		}
	}
	
	public static void enableDebug()
	{
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig("org.leplan73.outilssgdf"); 
		loggerConfig.setLevel(Level.DEBUG);
		
		loggerConfig = config.getLoggerConfig("org.apache.http"); 
		loggerConfig.setLevel(Level.DEBUG);
		
		config.getLoggerConfig("org.apache.http.wire"); 
		loggerConfig.setLevel(Level.DEBUG);
	}
}
