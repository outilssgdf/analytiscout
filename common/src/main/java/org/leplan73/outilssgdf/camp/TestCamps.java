package org.leplan73.outilssgdf.camp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.leplan73.outilssgdf.ExtracteurCampsHtml;
import org.leplan73.outilssgdf.ExtractionException;

import net.sf.jett.transform.ExcelTransformer;

public class TestCamps {

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
	
	public static void main(String[] args) throws ExtractionException, IOException, JDOMException, InvalidFormatException {

		System.setProperty("line.separator","\n");
		Document doc = Jsoup.parse(new File("C:\\dev\\outilssgdf_data\\camps_sjbs.xls"),"UTF-8");
		Elements tables = doc.select("table");
		
		ExtracteurCampsHtml c = new ExtracteurCampsHtml();
		c.charge(tables.html());
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("camps", c.camps());
		
		go(new File("C:\\dev\\outilssgdf\\fichiers\\conf\\modele_camps.xlsx"), beans, new File("C:\\dev\\outilssgdf_data\\analyse_camps.xlsx"));
	}
}
