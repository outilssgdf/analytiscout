package org.leplan73.outilssgdf.cmd;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.cmd.utils.CmdLineException;
import org.leplan73.outilssgdf.cmd.utils.CmdParams;
import org.leplan73.outilssgdf.cmd.utils.CommonParamsG;
import org.leplan73.outilssgdf.cmd.utils.Logging;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.PicocliException;

@Command(name = "outilsgdf", mixinStandardHelpOptions = true, versionProvider = CommonParamsG.class,
	subcommands = {Analyseur.class, AnalyseurAdherents.class, Extracteur.class, ExtracteurBatch.class, Generateur.class, AnalyseurEnLigne.class, AnalyseurCompasEnLigne.class, AnalyseurAdherents.class})
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
		catch(CmdLineException e)
		{
			System.out.println(Logging.dumpStack(null,e));
			if (e.estLance() == false) {
				CommandLine.usage(e.getCmd(), System.out);
			}
		}
		catch(Exception e)
		{
			System.out.println(Logging.dumpStack(null,e));
		}
	}
	
	@Override
	public void run(CommandLine commandLine) throws CmdLineException, IOException, ExtractionException, JDOMException, InvalidFormatException
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
		
		try
		{
			cmd.run(commandLine);
		}
		catch (CmdLineException e)
		{
			e.setCommand(cmd);
			throw e;
		}
	}

	public static void main(String[] args)
	{
		CmdLine command = new CmdLine();
		command.go(args, CmdLine.class);
	}
}
