package org.leplan73.outilssgdf.gui.extracteur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtracteurBatchAdherents extends ExtracteurBatch {

	private static File fBatch = new File("./conf/batch_jeunes.txt");
	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatchAdherents.class);

	public ExtracteurBatchAdherents() {
		super("ExtracteurBatch (Adherents)",logger_,fBatch);
	}
}
