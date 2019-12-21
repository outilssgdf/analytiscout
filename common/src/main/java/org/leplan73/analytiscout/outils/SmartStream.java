package org.leplan73.analytiscout.outils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SmartStream {
	
	private PrintStream os_;
	private ZipOutputStream zout_;
	
	public SmartStream(File dir, String fichier, ZipOutputStream zout) throws IOException
	{
		if (zout != null)
		{
			ZipEntry ze = new ZipEntry(fichier);
		    zout.putNextEntry(ze);
		}
	    
		os_ = new PrintStream(zout != null ? zout : new FileOutputStream(new File(dir,fichier)));
		zout_ = zout;
	}
	
	public void close() throws IOException
	{
		os_.flush();
		if (zout_ != null)
		{
			zout_.closeEntry();
		}
		else
		{
			os_.close();
		}
	}
	
	public PrintStream getStream()
	{
		return os_;
	}

	public void write(ByteArrayOutputStream bos) {
		PrintStream fos = this.getStream();
		fos.print(bos.toString());
		fos.flush();
	}
}
