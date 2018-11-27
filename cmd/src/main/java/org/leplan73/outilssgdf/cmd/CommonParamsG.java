package org.leplan73.outilssgdf.cmd;

import picocli.CommandLine.Option;

public class CommonParamsG extends CmdParams {

	@Option(names = "-structure", required=true, description = "Structure à extraire", arity = "1..*", split = ",")
	protected int[] structures;
}
