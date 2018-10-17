package org.leplan73.outilssgdf.cmd;

import picocli.CommandLine.Option;

public class CommonParamsG {

	@Option(names = "-debug", description = "debug")
	protected boolean debug = false;

	@Option(names = "-structure", required=true, description = "structure")
	protected Integer structure;

}
