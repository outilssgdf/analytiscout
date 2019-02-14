package org.leplan73.outilssgdf.gui;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtracteurBatchCompas extends ExtracteurBatch {

	private static File fBatch = new File("./conf/batch_compas.txt");
	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatchCompas.class);
	
	public ExtracteurBatchCompas() {
		super("ExtracteurBatch (Compas)", logger_, fBatch);
	}
}
