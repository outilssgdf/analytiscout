package org.leplan73.analytiscout.gui;

import org.leplan73.analytiscout.gui.utils.Version;

import com.jcabi.manifests.Manifests;

import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;

public class GuiParams implements IVersionProvider {
	@Option(names = "-qualifications", hidden=true)
	protected boolean qualifications = false;
	
	@Option(names = "-debugintranet", description = "debugintranet (Valeur par défaut: ${DEFAULT-VALUE})")
	protected boolean debugintranet = false;
	
	@Option(names = "-anonymiser", description = "anonymiser les nom, prénoms et code (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
	protected boolean anonymiser = false;
	
	@Option(names = "-nologfile", description = "pas de log dans un fichier (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
	protected boolean nologfile = false;
	
	@Override
	public String[] getVersion() throws Exception {
		try {
			Version v = Version.parse(Manifests.read("version"));
			return new String[] {"Version: "+v.toString()};
		} catch (java.lang.IllegalArgumentException e) {
			return new String[] {"Version: "+"dev"};
		}
	}
}
