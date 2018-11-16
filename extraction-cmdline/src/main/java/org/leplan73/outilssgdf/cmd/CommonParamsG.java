package org.leplan73.outilssgdf.cmd;

import org.leplan73.outilssgdf.Params;

import com.jcabi.manifests.Manifests;

import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;

public class CommonParamsG implements IVersionProvider {

	@Option(names = "-debug", description = "debug (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean debug = false;
	
	@Option(names = "-log", description = "log (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean log = false;

	@Option(names = "-structure", required=true, description = "Structure à extraire", arity = "1..*", split = ",")
	protected int[] structures;
	
	public void chargeParametres()
	{
		Logging.logger_.info("Chargement du fichier de paramètres");
		Params.init("./conf/params.properties");
	}

	@Override
	public String[] getVersion() throws Exception {
		return new String[] {"Version: "+Manifests.read("version"),"Date du build: "+Manifests.read("Build-Time")};
	}
}
