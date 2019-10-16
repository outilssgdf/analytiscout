package org.leplan73.outilssgdf.gui;

import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.leplan73.outilssgdf.Params;
import org.leplan73.outilssgdf.gui.utils.Logging;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "outilsgdf", mixinStandardHelpOptions = true)
public class GuiCmd extends GuiParams
{
	public void go(String[] args)
	{
		Logging.initLogger(avance ? OutilsSGDFNormal.class: OutilsSGDFAdvanced.class);
		Params.init();
		
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
			Logging.debugintranet(debugintranet);
			if (cmd.avance == true)
			{
				avance = true;
			}
		}
		
		Preferences.init();
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
		
		if (avance)
			new OutilsSGDFAdvanced().go();
		else
			new OutilsSGDFNormal().go();
	}
	
	public static void main(String[] args) {
		new GuiCmd().go(args);
	}
}
