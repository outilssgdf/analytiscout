package org.leplan73.outilssgdf.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.registrepresence.ExtracteurRegistrePresence;
import org.slf4j.Logger;

public class EngineExportRegistreDePresence extends Engine {

	public EngineExportRegistreDePresence(Progress progress, Logger logger) {
		super(progress, logger);
	}

	public void go(File entree, String connexion, String utilisateur, String motdePasse, String database) throws EngineException {
		start();
		try
		{
			progress_.setProgress(20,"Chargement des fichiers");
			ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
			logger_.info("Chargement du fichier \"" + entree.getName() + "\"");
			ex.charge(new FileInputStream(entree));
			progress_.setProgress(40,"Export");
			logger_.info("Export");
			
			InfluxDB influxDB = InfluxDBFactory.connect(connexion, utilisateur, motdePasse);
			influxDB.enableBatch(100, 200, TimeUnit.MILLISECONDS);
			influxDB.setDatabase(database);
			ex.exportInfluxDb(influxDB);
			influxDB.flush();
			influxDB.disableBatch();
			influxDB.close();
			
		} catch (IOException e) {
			throw new EngineException("Exception dans "+this.getClass().getName(),e);
		}
		finally  {
			stop();
		}
	}
}
