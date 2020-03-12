package org.leplan73.analytiscout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sf.jett.transform.ExcelTransformer;

public class Transformeur {
	
	public static void retireOnglet(File fichier, String nom) throws TransformeurException
	{
		try
		{
			FileInputStream fileStream = new FileInputStream(fichier);
			XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
	
			int index = 0;
	
			XSSFSheet sheet = workbook.getSheet(nom);
			if(sheet != null)   {
			    index = workbook.getSheetIndex(sheet);
			    workbook.removeSheetAt(index);
			}
			fileStream.close();
			
			FileOutputStream output = new FileOutputStream(fichier);
			workbook.write(output);
			output.close();
			
			workbook.close();
		}
		catch(IOException e)
		{
			throw new TransformeurException(e);
		}
	}
	
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
