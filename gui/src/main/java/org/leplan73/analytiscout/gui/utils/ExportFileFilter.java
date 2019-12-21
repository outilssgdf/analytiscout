package org.leplan73.analytiscout.gui.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExportFileFilter extends FileFilter {

	private String extension_;
	
	public ExportFileFilter(String extension)
	{
		extension_ = extension;
	}
	
	@Override
	public boolean accept(File f) {
	    if (f.isDirectory()) {  
	        return true;  
	    }  
	    return f.getName().toLowerCase().endsWith("."+extension_);  
	}

	@Override
	public String getDescription() {
		 return "Fichier "+extension_;  
	}
}
