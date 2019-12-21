package org.leplan73.analytiscout.gui.extracteur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtracteurBatchJeunes extends ExtracteurBatch {

	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatchJeunes.class);

	public ExtracteurBatchJeunes() {
		super("ExtracteurBatch (Jeunes)",logger_,new File("conf/batch_jeunes.txt"));
	}
}
