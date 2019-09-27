package org.leplan73.outilssgdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import net.sf.jett.transform.ExcelTransformer;

public class Transformeur {
	
	public static void go(File modele, Map<String, Object> beans, File sortie) throws TransformeurException
	{
		FileOutputStream outputStream = null;
		InputStream inModele = null;
		try
		{
			outputStream = new FileOutputStream(sortie);
			inModele = new FileInputStream(modele);
			ExcelTransformer trans = new ExcelTransformer();
			Workbook workbook = trans.transform(inModele, beans);
			workbook.write(outputStream);
			inModele.close();
			outputStream.flush();
			outputStream.close();
		}
		catch(IOException | InvalidFormatException e)
		{
			throw new TransformeurException(e);
		}
		finally
		{
			try {
				if (outputStream != null) outputStream.close();
				if (inModele != null) inModele.close();
			} catch (IOException e) {
				throw new TransformeurException(e);
			}
		}
	}
	
	public static void go(File modele, Map<String, Object> beans, ByteArrayOutputStream sortie) throws TransformeurException
	{
		InputStream inModele = null;
		try
		{
			inModele = new FileInputStream(modele);
			ExcelTransformer trans = new ExcelTransformer();
			Workbook workbook = trans.transform(inModele, beans);
			workbook.write(sortie);
			inModele.close();
			sortie.flush();
			sortie.close();
		}
		catch(IOException | InvalidFormatException e)
		{
			throw new TransformeurException(e);
		}
		finally
		{
			try {
				inModele.close();
			} catch (IOException e) {
			}
		}
	}
}
