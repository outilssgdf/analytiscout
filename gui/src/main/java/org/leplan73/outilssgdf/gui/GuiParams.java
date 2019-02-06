package org.leplan73.outilssgdf.gui;

import org.leplan73.outilssgdf.gui.utils.Version;

import com.jcabi.manifests.Manifests;

import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;

public class GuiParams implements IVersionProvider {
	@Option(names = "-qualifications", hidden=true)
	protected boolean qualifications = false;
	
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
