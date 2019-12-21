package org.leplan73.analytiscout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import net.sf.jett.transform.ExcelTransformer;

public class Transformeur {
	
	public static void go(InputStream modele, Map<String, Object> beans, OutputStream sortie) throws TransformeurException
	{
		try
		{
			ExcelTransformer trans = new ExcelTransformer();
			Workbook workbook = trans.transform(modele, beans);
			workbook.write(sortie);
			sortie.flush();
		}
		catch(IOException | InvalidFormatException e)
		{
			throw new TransformeurException(e);
		}
	}
}
