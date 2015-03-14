package com.socialmetrix.templater.components;

import java.util.Date;

import org.apache.poi.ss.usermodel.*;

import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;
import com.socialmetrix.templater.utils.CellStyleDuplicator;

/**
 * Tries to preserve original cell's type. Supported Types: String, Number (converted to Double), Boolean, Date. If the provided
 * value type is not the same as the original cell's type tries to parse value to match the cell type.
 */
public class VariableComponent extends PropertyReaderComponent {

	private final CellStyleDuplicator cellStyleDuplicator;
	private final Coordinates relativePosition;

	public VariableComponent(
			DataReader dataReader,
			String property,
			CellStyleDuplicator cellStyleDuplicator,
			Coordinates relativePosition) {
		super(dataReader, property);
		this.cellStyleDuplicator = cellStyleDuplicator;
		this.relativePosition = relativePosition;
	}

	@Override
	protected Region renderProperty(Object property, Coordinates parentOrigin, Sheet sheet) {
		Coordinates position = parentOrigin.plus(relativePosition);

		Cell newCell = cellStyleDuplicator.duplicateIn(sheet, position);
		setValue(newCell, cellStyleDuplicator.getCellType(), property);

		return new Region(position, 1, 1);
	}

	public Coordinates getRelativePosition() {
		return relativePosition;
	}

	private static void setValue(Cell cell, int cellType, Object value) {
		switch (cellType) {
			case Cell.CELL_TYPE_BOOLEAN:
				if (value instanceof Boolean) {
					cell.setCellValue((Boolean) value);
				} else { // try to parse from string
					boolean bool = Boolean.parseBoolean(value.toString());
					cell.setCellValue(bool);
				}
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (value instanceof Number) {
					Number number = (Number) value;
					cell.setCellValue(number.doubleValue());
				} else if (value instanceof Date) {
					cell.setCellValue((Date) value);
				} else { // try to parse from string
					double numericValue = Double.parseDouble(value.toString());
					cell.setCellValue(numericValue);
				}
				break;
			default: // default to Cell.CELL_TYPE_STRING
				cell.setCellValue(value.toString());
		}
	}

}
