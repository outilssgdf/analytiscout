package org.leplan73.outilssgdf.server;

import java.io.InputStream;

import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class TomcatEmbedded {

	public static void main(String[] args) {
		new TomcatEmbedded().startServer();
	}
	
	public TomcatEmbedded()
	{
	}
	
	private static Connector getConnector(int port) {
	    Connector connector = new Connector();
	    connector.setPort(port);
	    connector.setScheme("http");
	    return connector;
	 }
	
	public void startServer()
	{
		// Debug trace if needed
		InputStream in = ClassLoader.getSystemResourceAsStream("log4j2.xml");
		if (in == null) {
			
			// Standard logging configuration file
		in = TomcatEmbedded.class.getResourceAsStream("log4j2.xml");
		}
//		ConfigurationSource source;
//		try {
//			source = new ConfigurationSource(in);
//			Configurator.initialize(null, source);
//		} catch (FileNotFoundException e1) {
//		} catch (IOException e1) {
//		}
        
        try {
			Tomcat tomcat = new Tomcat();
			Service service = tomcat.getService();
		    service.addConnector(getConnector(8080));
			
		    String c = System.getProperty("user.dir");
		    String root = c+"/../extraction-servlet/src/main/webapp";
			Context rootCtx = tomcat.addWebapp("/extractionsgdf", root);
			rootCtx.getServletContext().setAttribute(Globals.ALT_DD_ATTR, root+"/WEB-INF/web.xml");

			tomcat.start();
			tomcat.getServer().await();
			
			} catch (LifecycleException e) {
				e.printStackTrace();
			}
	}
}
