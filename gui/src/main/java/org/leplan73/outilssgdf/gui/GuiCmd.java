package org.leplan73.outilssgdf.gui;

import java.util.List;

import org.leplan73.outilssgdf.Params;
import org.leplan73.outilssgdf.gui.utils.Logging;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "outilsgdf", mixinStandardHelpOptions = true)
public class GuiCmd extends GuiParams
{
	private void go(String[] args)
	{
		boolean avance = false;
		
		CommandLine commandLine = new CommandLine(this);
		List<CommandLine> parsed = commandLine.parse(args);
		if (parsed != null && parsed.size() > 0)
		{
			commandLine = parsed.get(0);
			GuiParams cmd = (GuiParams) commandLine.getCommand();
			if (cmd.qualifications == true)
			{
				ExtractionIntranet.setQualifications(true);
			}
			if (cmd.avance == true)
			{
				avance = true;
			}
		}
		
		if (avance)
			new OutilsSGDFAvance().go();
		else
		new OutilsSGDF().go();
	}
	
	public static void main(String[] args) {
		Logging.initLogger(OutilsSGDF.class, true);
		Params.init();
		new GuiCmd().go(args);
	}
}
