package org.leplan73.outilssgdf.gui;

import org.jxls.common.AreaListener;
import org.jxls.common.CellRef;
import org.jxls.common.Context;

public class QualifDirSfListener implements AreaListener {

	@Override
	public void afterTransformCell(CellRef srcCell, CellRef targetCell, Context context) {
/*		Block block = event.getBlock();
		Sheet sheet = event.getSheet();
		Workbook workbook = sheet.getWorkbook();
		Row row = sheet.getRow(block.getTopRowNum());
		Cell cell = row.getCell(block.getLeftColNum());
		if (cell != null)
		{
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			{
				Date fin_validite = cell.getDateCellValue();
				Date debutCamp = Params.getDateDebutCamp();
				if (fin_validite.before(debutCamp))
				{
					CellStyle backgroundStyle = workbook.createCellStyle();
					backgroundStyle.cloneStyleFrom(cell.getCellStyle());
					backgroundStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
					backgroundStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
					backgroundStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(backgroundStyle);
				}
				
			}
		}*/
	}

	@Override
	public void beforeApplyAtCell(CellRef cellRef, Context context) {
	}

	@Override
	public void afterApplyAtCell(CellRef cellRef, Context context) {
	}

	@Override
	public void beforeTransformCell(CellRef srcCell, CellRef targetCell, Context context) {
	}

}
