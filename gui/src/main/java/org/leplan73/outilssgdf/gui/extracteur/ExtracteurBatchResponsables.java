package org.leplan73.outilssgdf.gui.extracteur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtracteurBatchResponsables extends ExtracteurBatch {

	private static File fBatch = new File("conf/batch_responsables.txt");
	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatchResponsables.class);

	public ExtracteurBatchResponsables() {
		super("ExtracteurBatch (Responsables)",logger_,fBatch);
	}
}
