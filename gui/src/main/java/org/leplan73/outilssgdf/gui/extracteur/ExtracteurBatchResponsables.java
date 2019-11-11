package org.leplan73.outilssgdf.gui.extracteur;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtracteurBatchResponsables extends ExtracteurBatch {

	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatchResponsables.class);

	public ExtracteurBatchResponsables(boolean anonymiser) {
		super("ExtracteurBatch (Responsables)",logger_,new File("conf/batch_responsables.txt"), anonymiser);
	}
}
