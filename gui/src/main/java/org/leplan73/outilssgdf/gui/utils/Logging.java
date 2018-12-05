package org.leplan73.outilssgdf.gui.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

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
}
