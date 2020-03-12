package org.leplan73.analytiscout.cmd.utils;

import org.leplan73.analytiscout.engine.EngineException;

import picocli.CommandLine.Option;

public class CommonParamsG extends CmdParams {

	@Option(names = "-structures", description = "Structures à extraire", arity = "1..*", split = ",")
	protected int[] structures;
	
	protected void check() throws EngineException
	{
		if (structures == null)
		{
			throw new EngineException("-structures ou -structure doit être utilisé", false);
		}
	}
}
