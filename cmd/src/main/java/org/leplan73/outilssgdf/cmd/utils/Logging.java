package org.leplan73.outilssgdf.cmd.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {

	public static Logger logger_;

	public static void initLogger(Class<?> classn, boolean debug)
	{
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "off");
		System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_DATE_TIME_KEY,"true");
		System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_THREAD_NAME_KEY,"true");
		System.setProperty("org.slf4j.simpleLogger.log.org.leplan73.outilssgdf",debug ? "debug" : "info");
		if (debug)
		{
			System.setProperty("org.slf4j.simpleLogger.log.org.apache.http", "debug");
			System.setProperty("org.slf4j.simpleLogger.log.org.apache.http.wire", "debug");
		}
		logger_ = LoggerFactory.getLogger(classn);
	}
	
	static public String dumpStack(String message, Throwable ex)
	{
		final StringBuilder messageBuilder = new StringBuilder();
		if (message != null)
		{
			messageBuilder.append(message).append(":\n");
		}
		OutputStream stream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new BufferedOutputStream(stream));
		ex.printStackTrace(writer);
		writer.flush();
		messageBuilder.append(stream.toString());
		return messageBuilder.toString();
	}
	
	static public void logError(String message, Throwable ex)
	{
		logger_.error(dumpStack(message,ex));
	}
	
	static public void logError(Throwable ex)
	{
		logger_.error(dumpStack(null,ex));
	}
}
