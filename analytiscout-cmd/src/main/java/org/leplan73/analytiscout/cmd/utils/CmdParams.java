package org.leplan73.analytiscout.cmd.utils;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.ExtractionException;
import org.leplan73.analytiscout.Params;

import com.jcabi.manifests.Manifests;

import picocli.CommandLine;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;
import picocli.CommandLine.PicocliException;

public class CmdParams implements IVersionProvider {

	@Option(names = "-debugintranet", description = "debug (Valeur par défaut: ${DEFAULT-VALUE})")
	public boolean debugintranet = false;
	
	@Option(names = "-nologfile", description = "pas de log dans un fichier (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
	public boolean nologfile = false;
	
	@Option(names = "-anonymiser", description = "anonymiser les nom, prénoms et code (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
	protected boolean anonymiser = false;
	
	public void chargeParametres()
	{
		Logging.logger_.info("Chargement du fichier de paramètres");
		Params.init();
		if (anonymiser)
		{
			Params.set(Consts.PARAMS_ANONYMISER, "true");
		}
	}

	@Override
	public String[] getVersion() throws Exception {
		return new String[] {"Version: "+Manifests.read("version"),"Date du build: "+Manifests.read("Build-Time")};
	}
	
	public void run(CommandLine commandLine) throws CmdLineException, IOException, ExtractionException, JDOMException, InvalidFormatException
	{
	}
	
	protected void go(String[] args, Class<?> classn)
	{
		Logging.initLogger(classn, debugintranet, nologfile);
		try
		{
			CommandLine commandLine = new CommandLine(this);
			List<CommandLine> parsed = commandLine.parse(args);
			if (commandLine.isVersionHelpRequested())
			{
			    commandLine.printVersionHelp(System.out);
			    return;
			}
			if (commandLine.isUsageHelpRequested())
			{
			    commandLine.usage(System.out);
			    return;
			}
	        run(parsed.get(0));
		}
		catch(PicocliException e)
		{
			System.out.println("Erreur : " + e.getMessage());
			CommandLine.usage(this, System.out);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
