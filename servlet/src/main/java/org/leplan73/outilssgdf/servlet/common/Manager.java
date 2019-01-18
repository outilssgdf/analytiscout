package org.leplan73.outilssgdf.servlet.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.leplan73.outilssgdf.servlet.war.Logger;
import org.leplan73.outilssgdf.servlet.war.Server;

import io.swagger.jaxrs.config.BeanConfig;

public class Manager implements ServletContextListener {

    public static void init(final String host, int port)
    {
    }
    
    public static void initializeSwagger(int port)
	{
    	BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        try {
			beanConfig.setHost(InetAddress.getLocalHost().getHostName()+":"+port);
		} catch (UnknownHostException e) {
			beanConfig.setHost("localhost:"+port);
		}
        beanConfig.setBasePath("/outilsgdf/api");
        beanConfig.setResourcePackage(Server.class.getPackage().getName());
        beanConfig.setDescription("Outils de convertion SGDF");
        beanConfig.setScan( true );
 	}
    
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		initializeSwagger(8080);
		
		Logger.init();
		Logger.get().info("Manager initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Logger.get().info("Manager destroyed");
	}
}
