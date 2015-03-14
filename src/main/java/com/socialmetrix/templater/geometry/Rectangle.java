package com.socialmetrix.templater.geometry;

import com.socialmetrix.templater.TemplaterException;

public class Rectangle {

	private final int rowCount;
	private final int columnCount;

	public Rectangle(int rowCount, int columnCount) {
		if (rowCount < 0 || columnCount < 0) {
			throw new NegativeRectangleException(rowCount, columnCount);
		}
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public boolean isEmpty() {
		return rowCount == 0 && columnCount == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnCount;
		result = prime * result + rowCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		Rectangle other = (Rectangle) obj;
		return (columnCount == other.columnCount) && //
				(rowCount == other.rowCount);
	}

	@Override
	public String toString() {
		return rowCount + "x" + columnCount;
	}

	public static class NegativeRectangleException extends TemplaterException {
		private static final long serialVersionUID = -3294474942659798953L;

		public NegativeRectangleException(int rowCount, int columnCount) {
			super("Can't create a negative rectangle [" + rowCount + ":" + columnCount + "].");
		}
	}

}
