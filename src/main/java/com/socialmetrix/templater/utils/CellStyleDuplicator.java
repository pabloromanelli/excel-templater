package com.socialmetrix.templater.utils;

import static com.socialmetrix.templater.utils.CellUtils.getRow;

import org.apache.poi.ss.usermodel.*;

import com.socialmetrix.templater.geometry.Coordinates;

public class CellStyleDuplicator {

	protected final CellStyle cellStyle;
	protected final int cellType;
	protected final CellStyle rowStyle;
	protected final short rowHeight;
	protected final int columnWidth;

	public CellStyleDuplicator(Cell cell) {
		this.columnWidth = cell.getSheet().getColumnWidth(cell.getColumnIndex());
		this.cellType = cell.getCellType();
		this.cellStyle = cell.getCellStyle();
		this.rowStyle = cell.getRow().getRowStyle();
		this.rowHeight = cell.getRow().getHeight();
	}

	public Cell duplicateIn(Sheet sheet, Coordinates position) {
		Row row = getRow(sheet, position.getRowIndex());
		row.setRowStyle(rowStyle);
		row.setHeight(rowHeight);
		sheet.setColumnWidth(position.getColumnIndex(), columnWidth);
		Cell newCell = row.createCell(position.getColumnIndex(), cellType);
		newCell.setCellStyle(cellStyle);
		return newCell;
	}

	public int getCellType() {
		return cellType;
	}

}
