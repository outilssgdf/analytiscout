package org.leplan73.analytiscout.gui.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExcelFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {  
	        return true;  
	    }  
	    return f.getName().toLowerCase().endsWith(".xlsx");
	}

	@Override
	public String getDescription() {
		return "Excel file(*.xlsx)";  
	}
}
