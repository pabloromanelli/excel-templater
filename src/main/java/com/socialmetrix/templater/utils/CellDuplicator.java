package com.socialmetrix.templater.utils;

import org.apache.poi.ss.usermodel.*;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.geometry.Coordinates;

public abstract class CellDuplicator extends CellStyleDuplicator {

	public CellDuplicator(Cell cell) {
		super(cell);
	}

	@Override
	public Cell duplicateIn(Sheet sheet, Coordinates position) {
		Cell newCell = super.duplicateIn(sheet, position);
		setValue(newCell);
		return newCell;
	}

	protected abstract void setValue(Cell newCell);

	public static CellDuplicator duplicatorFor(Cell cell) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				return new BlackDuplicator(cell);
			case Cell.CELL_TYPE_BOOLEAN:
				return new BooleanDuplicator(cell);
			case Cell.CELL_TYPE_ERROR:
				return new ErrorDuplicator(cell);
			case Cell.CELL_TYPE_FORMULA:
				return new FormulaDuplicator(cell);
			case Cell.CELL_TYPE_NUMERIC:
				return new NumericDuplicator(cell);
			case Cell.CELL_TYPE_STRING:
				return new StringDuplicator(cell);
			default:
				throw new UnknownCellTypeException("Unknown cell type '" + cell.getCellType() + "'.");
		}
	}

	public static class UnknownCellTypeException extends TemplaterException {
		private static final long serialVersionUID = 6269163164201515495L;

		public UnknownCellTypeException(String message) {
			super(message);
		}
	}

	private static class BlackDuplicator extends CellDuplicator {
		public BlackDuplicator(Cell cell) {
			super(cell);
		}

		@Override
		protected void setValue(Cell newCell) {
			// do nothing
		}
	}

	private static class BooleanDuplicator extends CellDuplicator {
		private final boolean value;

		public BooleanDuplicator(Cell cell) {
			super(cell);
			this.value = cell.getBooleanCellValue();
		}

		@Override
		protected void setValue(Cell newCell) {
			newCell.setCellValue(value);
		}
	}

	private static class ErrorDuplicator extends CellDuplicator {
		private final byte value;

		public ErrorDuplicator(Cell cell) {
			super(cell);
			this.value = cell.getErrorCellValue();
		}

		@Override
		protected void setValue(Cell newCell) {
			newCell.setCellErrorValue(value);
		}
	}

	private static class FormulaDuplicator extends CellDuplicator {
		private final String value;

		public FormulaDuplicator(Cell cell) {
			super(cell);
			this.value = cell.getCellFormula();
		}

		@Override
		protected void setValue(Cell newCell) {
			newCell.setCellFormula(value);
		}
	}

	private static class NumericDuplicator extends CellDuplicator {
		private final double value;

		public NumericDuplicator(Cell cell) {
			super(cell);
			this.value = cell.getNumericCellValue();
		}

		@Override
		protected void setValue(Cell newCell) {
			newCell.setCellValue(value);
		}
	}

	private static class StringDuplicator extends CellDuplicator {
		private final RichTextString value;

		public StringDuplicator(Cell cell) {
			super(cell);
			this.value = cell.getRichStringCellValue();
		}

		@Override
		protected void setValue(Cell newCell) {
			newCell.setCellValue(value);
		}
	}
}
