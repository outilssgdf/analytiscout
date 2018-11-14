package org.leplan73.outilssgdf.cmd;

import picocli.CommandLine.Option;

public class CommonParamsG {

	@Option(names = "-debug", description = "debug (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean debug = false;
	
	@Option(names = "-log", description = "log (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean log = false;

	@Option(names = "-structure", required=true, description = "Structure à extraire", arity = "1..*", split = ",")
	protected int[] structures;

}
