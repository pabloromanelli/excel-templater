package com.socialmetrix.templater.utils;

import org.apache.poi.ss.usermodel.*;

import com.socialmetrix.templater.geometry.Coordinates;

public class CellUtils {

	public static Row getRow(Sheet sheet, int rowIndex) {
		Row row = sheet.getRow(rowIndex);
		return row != null ? row : sheet.createRow(rowIndex);
	}

	public static Cell getCell(Sheet sheet, Coordinates coordinates) {
		Row row = getRow(sheet, coordinates.getRowIndex());
		Cell cell = row.getCell(coordinates.getColumnIndex());
		return cell != null ? cell : row.createCell(coordinates.getColumnIndex());
	}

}
