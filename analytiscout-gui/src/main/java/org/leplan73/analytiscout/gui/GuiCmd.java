package org.leplan73.analytiscout.gui;

import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.Params;
import org.leplan73.analytiscout.gui.utils.Logging;
import org.leplan73.analytiscout.gui.utils.Preferences;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "analytiscout", mixinStandardHelpOptions = true)
public class GuiCmd extends GuiParams
{
	public void go(String[] args)
	{
		boolean avance = false;
		
		CommandLine commandLine = new CommandLine(this);
		List<CommandLine> parsed = commandLine.parse(args);
		if (parsed != null && parsed.size() > 0)
		{
			commandLine = parsed.get(0);
			GuiParams cmd = (GuiParams) commandLine.getCommand();
			Logging.initLogger(cmd.avance ? AnalytiscoutAdvanced.class: AnalytiscoutNormal.class, cmd.nologfile);
			
			if (cmd.qualifications == true)
			{
				ExtractionIntranet.setQualifications(true);
			}
			Logging.debugintranet(debugintranet);
			if (cmd.avance == true)
			{
				avance = true;
			}
			if (anonymiser)
			{
				Params.set(Consts.PARAMS_ANONYMISER, "true");
			}
		}

		Params.init();
		
		Preferences.init();
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
		
		if (avance)
			new AnalytiscoutAdvanced().go();
		else
			new AnalytiscoutNormal().go();
	}
	
	public static void main(String[] args) {
		new GuiCmd().go(args);
	}
}
