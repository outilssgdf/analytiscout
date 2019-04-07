package org.leplan73.outilssgdf.gui;

import javax.swing.JDialog;

import com.jcabi.manifests.Manifests;

public class Dialogue extends JDialog {
	
	protected String version_;
	
	public void go()
	{
		try {
			version_ = Manifests.read("version");
		} catch (java.lang.IllegalArgumentException e) {
		}
	}

}
