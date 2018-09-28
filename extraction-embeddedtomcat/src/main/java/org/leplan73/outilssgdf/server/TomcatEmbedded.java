package org.leplan73.outilssgdf.server;

import java.net.URL;
import java.net.URLClassLoader;

import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.PropertyConfigurator;

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
        URL configFileResource = URLClassLoader.getSystemResource("logging.properties");
        if (configFileResource == null) {
        	
        	// Standard logging configuration file
        	configFileResource = URLClassLoader.getSystemResource("org/leplan73/outilssgdf/servlet/resources/logging.properties");
        }
        if (configFileResource != null) {
        	PropertyConfigurator.configure(configFileResource);
        }
        
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
