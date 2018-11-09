package org.leplan73.outilssgdf.cmd;

import picocli.CommandLine.Option;

public class CommonParamsG {

	@Option(names = "-debug", description = "debug")
	protected boolean debug = false;
	
	@Option(names = "-log", description = "log")
	protected boolean log = false;

	@Option(names = "-structure", required=true, description = "Structure à extraire", arity = "1..*", split = ",")
	protected int[] structures;

}
