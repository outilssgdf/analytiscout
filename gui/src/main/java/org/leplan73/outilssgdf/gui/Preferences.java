package org.leplan73.outilssgdf.gui;

import java.io.File;

public class Preferences {
	
	static private File file_;
	
	static public void init()
	{
		file_ = new File(System.getProperty("java.home")+"outilssgdf.property");
	}
}
