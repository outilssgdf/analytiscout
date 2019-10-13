package org.leplan73.outilssgdf.servlet.common;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.leplan73.outilssgdf.Params;
import org.leplan73.outilssgdf.servlet.war.Logger;
import org.leplan73.outilssgdf.servlet.war.Server;

import io.swagger.jaxrs.config.BeanConfig;

public class Manager implements ServletContextListener {

	static private File fConf_;
	static private Database db_;
	
	static public File getConf()
	{
		return fConf_;
	}
	
	static public Database getDb()
	{
		return db_;
	}
	
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
        beanConfig.setBasePath("/outilssgdf/api");
        beanConfig.setResourcePackage(Server.class.getPackage().getName());
        beanConfig.setDescription("Outils d'analyse SGDF");
        beanConfig.setScan( true );
 	}
    
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Logger.init();
		Logger.get().info("Démarrage du serveur");
		
		Properties props = System.getProperties();
		fConf_ = new File(props.getProperty("OUTILSSGDF_ROOT",""));
		if (fConf_.exists() == false)
		{
			Logger.get().error("OUTILSSGDF_ROOT non disponible");
		}
		Params.init();

		Logger.get().info("Ouverture de la base de données");
		db_ = new Database();
		db_.init(fConf_);
		Logger.get().info("Base de données ouverte");

		initializeSwagger(8080);
		Logger.get().info("Serveur démarré");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		db_.uninit();
		Logger.get().info("Serveur stoppé");
	}
}
