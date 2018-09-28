package org.leplan73.outilssgdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.leplan73.outilssgdf.extraction.Adherent;
import org.leplan73.outilssgdf.extraction.Adherents;
import org.leplan73.outilssgdf.extraction.Colonnes;
import org.leplan73.outilssgdf.extraction.Parents;

public class ExtracteurXlsx {
	
	protected Adherents adherents_;
	protected Parents parents_;
	protected Colonnes colonnes_;
	
	public Adherents getAdherents()
	{
		return adherents_;
	}
	
	public Parents getParents()
	{
		return parents_;
	}
	
	public Colonnes getColonnes()
	{
		return colonnes_;
	}
	
	public void charge(final String path) throws ExtractionException, IOException
	{
   		FileInputStream excelFile = new FileInputStream(new File(path));
   		charge(excelFile);
		excelFile.close();
	}
	
	public void charge(final InputStream stream) throws ExtractionException, IOException
	{
        Workbook workbook = new XSSFWorkbook(stream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        
        // Scan des colonnes
        int nbColumns = 0;
        colonnes_ = new Colonnes();
        Row firstRow = datatypeSheet.getRow(0);
        Iterator<Cell> firstCellIterator = firstRow.iterator();
        while (firstCellIterator.hasNext()) {
            Cell currentCell = firstCellIterator.next();
            String cell = currentCell.getStringCellValue();
            colonnes_.put(nbColumns++, cell);
        }
        
        // Calcul des codes
        colonnes_.calculCodes();
        
        // Chargement des lignes d'adherents
        adherents_ = new Adherents();
        Iterator<Row> iterator = datatypeSheet.iterator();
        int nbLignes = 0;
		while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Adherent adherent = new Adherent();
        	int index = 0;
        	for (int i=0;i<colonnes_.size();i++)
        	{
        		Cell currentCell = currentRow.getCell(i);
        		if (currentCell != null)
        		{
        			 if (currentCell.getCellType() == CellType.STRING) {
                     	adherent.put(index, currentCell.getStringCellValue());
                     } else if (currentCell.getCellType() == CellType.NUMERIC) {
                     	adherent.put(index, String.valueOf(currentCell.getNumericCellValue()));
                     }
        		}
                index++;
        	}
        	if (nbLignes != 0)
        	{
            	adherent.init(colonnes_);
        		adherents_.add(adherent);
        	}
            nbLignes++;
        }
        workbook.close();
        
		parents_ = adherents_.parents(colonnes_);
		parents_.complete();
	}
}
