package org.leplan73.outilssgdf.cmd;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.PicocliException;

@Command(name = "outilsgdf", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class,
	subcommands = {Analyseur.class, AnalyseurAdherents.class, Extracteur.class, ExtracteurBatch.class, Generateur.class})
public class CmdLine extends CmdParams {
	
	@Override
	protected void go(String[] args, Class<?> classn)
	{
		Logging.initLogger(classn, debug);
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
			if (parsed.size() > 1)
				run(parsed.get(1));
			else
				commandLine.usage(System.out);
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
	
	@Override
	public void run(CommandLine commandLine) throws IOException, ExtractionException, JDOMException, InvalidFormatException
	{
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
		
		CmdParams cmd = (CmdParams)commandLine.getCommand();
		Logging.initLogger(cmd.getClass(), debug);
		cmd.run(commandLine);
	}

	public static void main(String[] args) {
		
		CmdLine command = new CmdLine();
		command.go(args, CmdLine.class);
	}
}
