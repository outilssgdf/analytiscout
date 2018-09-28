package org.leplan73.outilssgdf.exemples;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import org.leplan73.outilssgdf.ExtracteurXlsx;
import org.leplan73.outilssgdf.extraction.Unite;
import org.leplan73.outilssgdf.formatage.GmailCsvFormatteur;

public class SebExtracteur extends ExtracteurXlsx {
	
	public void go()
	{
//		Set<Unite> unites = adherents_.unites(colonnes_);
//		for (Unite unite : unites)
//		{
//			adherents_.check(colonnes_, unite);
//		}
		GmailCsvFormatteur f = new GmailCsvFormatteur();
		try {
//			ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(new File("C:\\dev\\sgdfExtractor\\tmp", "test.zip")));
//			f.genereEmail(parents_, adherents_, colonnes_, "C:\\dev\\sgdfExtractor\\tmp", zout);
//			zout.close();
			
			f.genereEmail(parents_, adherents_, colonnes_, "C:\\dev\\sgdfExtractor\\tmp", null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
