package org.leplan73.outilssgdf.servlet.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.leplan73.outilssgdf.Params;
import org.leplan73.outilssgdf.outils.CryptoException;
import org.leplan73.outilssgdf.servlet.common.Database.Requete;
import org.leplan73.outilssgdf.servlet.war.Logger;
import org.leplan73.outilssgdf.servlet.war.Server;

import io.swagger.jaxrs.config.BeanConfig;

public class Manager implements ServletContextListener {

	private static final String OUTILSSGDF_ROOT = "OUTILSSGDF_ROOT";
	private static final String OUTILSSGDF_CONF = "OUTILSSGDF_CONF";

	static class Generateur implements Runnable
	{
		private boolean stop_ = false;
		
		private void go()
		{
			try {
				Requete rqResponsables = db_.lireRequeteResponsables();
				if (rqResponsables != null)
				{
					// Execution de la requête
					boolean ret = rqResponsables.go(true);
					if (ret)
					{
						// Suppression de la requête effectuéd
						db_.supprimerRequetesResponsables(rqResponsables.id);
					}
				}
				Requete rqJeunes = db_.lireRequeteJeunes();
				if (rqJeunes != null)
				{
					// Execution de la requête
					boolean ret = rqJeunes.go(false);
					if (ret)
					{
						// Suppression de la requête effectuéd
						db_.supprimerRequetesJeunes(rqJeunes.id);
					}
				}
			} catch (CryptoException e) {
			} catch (FileNotFoundException e) {
			}
		}
		
		@Override
		public void run() {
			while(!stop_)
			{
				try {
					go();
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}

		public void stop() {
			stop_ = true;
		}
	}
	
	static private File fConfDir_;
	static private File fConfigFile_;
	static private Database db_;
	
	static Thread thread_;
	static Generateur generateur_;
	
	static public File getConf()
	{
		return fConfDir_;
	}
	
	static public Database getDb()
	{
		return db_;
	}
	
    public static void init()
    {
    	generateur_ = new Generateur();
    	thread_ = new Thread(generateur_);
    	thread_.setDaemon(true);
    	thread_.setName("Thread Generateur");
    	thread_.start();
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
		fConfDir_ = new File(props.getProperty(OUTILSSGDF_ROOT));
		if (fConfDir_.isDirectory() == false || fConfDir_.exists() == false)
		{
			Logger.get().error("OUTILSSGDF_ROOT non disponible");
		}
		
		fConfigFile_ = new File(props.getProperty(OUTILSSGDF_CONF));
		if (fConfigFile_.exists() == false)
		{
			Logger.get().error("OUTILSSGDF_CONF non disponible");
		}
		
		Params.init();
		Preferences.init(fConfigFile_);

		Logger.get().info("Ouverture de la base de données");
		db_ = new Database();
		db_.init(fConfDir_);
		Logger.get().info("Base de données ouverte");

		initializeSwagger(8080);
		init();
		Logger.get().info("Serveur démarré");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (generateur_ != null) generateur_.stop();
		try {
			thread_.join();
		} catch (InterruptedException e) {
		}
		db_.uninit();
		Logger.get().info("Serveur stoppé");
	}
}
