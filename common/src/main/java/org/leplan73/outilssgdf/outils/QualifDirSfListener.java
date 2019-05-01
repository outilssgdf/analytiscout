package org.leplan73.outilssgdf.outils;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.leplan73.outilssgdf.Params;

import net.sf.jett.event.TagEvent;
import net.sf.jett.event.TagListener;
import net.sf.jett.model.Block;

public class QualifDirSfListener implements TagListener {

	@Override
	public boolean beforeTagProcessed(TagEvent event) {
		return true;
	}

	@Override
	public void onTagProcessed(TagEvent event) {
		Block block = event.getBlock();
		Sheet sheet = event.getSheet();
		Workbook workbook = sheet.getWorkbook();
		Row row = sheet.getRow(block.getTopRowNum());
		Cell cell = row.getCell(block.getLeftColNum());
		if (cell != null)
		{
			if (cell.getCellType() == CellType.NUMERIC)
			{
				Date fin_validite = cell.getDateCellValue();
				Date debutCamp = Params.getDateDebutCamp();
				if (fin_validite.before(debutCamp))
				{
					CellStyle backgroundStyle = workbook.createCellStyle();
					backgroundStyle.cloneStyleFrom(cell.getCellStyle());
					backgroundStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
					backgroundStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
					backgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					cell.setCellStyle(backgroundStyle);
				}
				
			}
		}
	}

}
