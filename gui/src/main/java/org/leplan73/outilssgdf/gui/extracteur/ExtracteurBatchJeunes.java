package org.leplan73.outilssgdf.gui.extracteur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtracteurBatchJeunes extends ExtracteurBatch {

	private static File fBatch = new File("/conf/batch_jeunes.txt");
	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatchJeunes.class);

	public ExtracteurBatchJeunes() {
		super("ExtracteurBatch (Jeunes)",logger_,fBatch);
	}
}
