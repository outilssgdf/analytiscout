package org.leplan73.analytiscout.outils;

import java.time.Instant;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.leplan73.analytiscout.Params;

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
				Date date_aujourdhui = Date.from(Instant.now());
				Date fin_validite = cell.getDateCellValue();
				Date debutCamp = Params.getDateDebutCamp();
				
				if (fin_validite.before(date_aujourdhui))
				{
					// Qualif déjà expiré
					CellStyle backgroundStyle = workbook.createCellStyle();
					backgroundStyle.cloneStyleFrom(cell.getCellStyle());
					backgroundStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
					backgroundStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
					backgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					cell.setCellStyle(backgroundStyle);
					return;
				}
				if (fin_validite.before(debutCamp))
				{
					// Qualif qui expirera bientôt
					CellStyle backgroundStyle = workbook.createCellStyle();
					backgroundStyle.cloneStyleFrom(cell.getCellStyle());
					backgroundStyle.setFillBackgroundColor(IndexedColors.ORANGE.getIndex());
					backgroundStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
					backgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					cell.setCellStyle(backgroundStyle);
				}
				
			}
		}
	}

}
