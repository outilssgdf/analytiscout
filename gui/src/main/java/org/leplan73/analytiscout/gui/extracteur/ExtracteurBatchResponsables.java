package org.leplan73.analytiscout.gui.extracteur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ExtracteurBatchResponsables extends ExtracteurBatch {

	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatchResponsables.class);

	public ExtracteurBatchResponsables() {
		super("ExtracteurBatch (Responsables)",logger_,new File("conf/batch_responsables.txt"));
	}
}
