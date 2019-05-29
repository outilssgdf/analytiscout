package org.leplan73.outilssgdf.registrepresence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import net.sf.jett.transform.ExcelTransformer;

public class TestRegistrePresence {

	public static void go(File modele, Map<String, Object> beans, File sortie) throws InvalidFormatException, IOException
	{
		FileOutputStream outputStream = new FileOutputStream(sortie);
		InputStream inModele = new FileInputStream(modele);
		ExcelTransformer trans = new ExcelTransformer();
		Workbook workbook = trans.transform(inModele, beans);
		workbook.write(outputStream);
		inModele.close();
		outputStream.flush();
		outputStream.close();
	}
	
	public static void main(String[] args) throws InvalidFormatException, IOException {
		System.setProperty("line.separator","\n");
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		ex.charge(new FileInputStream(new File("C:\\dev\\outilssgdf_data\\RegistrePresence_2018_territoire.csv")));
		ex.exportInfluxDb();
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("activites", ex.getActivites());
		
		go(new File("C:\\dev\\outilssgdf\\fichiers\\conf\\modele_registrepresence.xlsx"), beans, new File("C:\\dev\\outilssgdf_data\\RegistrePresence_2018_territoire.xlsx"));
	}
}
