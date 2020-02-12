package org.leplan73.analytiscout.gui.extracteur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ExtracteurBatchMarins extends ExtracteurBatch {

	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatchMarins.class);

	public ExtracteurBatchMarins() {
		super("ExtracteurBatch (Marins)",logger_,new File("conf/batch_marins.txt"));
	}
}
