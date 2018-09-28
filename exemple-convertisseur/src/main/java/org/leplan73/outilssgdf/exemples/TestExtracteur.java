package org.leplan73.outilssgdf.exemples;
import java.io.IOException;

import org.leplan73.outilssgdf.ExtractionException;

public class TestExtracteur {
	
	public void go(final String path)
	{
		SebExtracteur ex = new SebExtracteur();
			try {
				ex.charge(path);
				ex.go();
			} catch (ExtractionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static void main(String[] args)
	{
		TestExtracteur app = new TestExtracteur();
		app.go("S:\\SGDF\\adh�rents 2018\\extraction-20180926.xlsx");
	}
}
