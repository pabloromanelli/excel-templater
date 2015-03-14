package com.socialmetrix.templater.geometry;

public class Coordinates {

	private final int rowIndex;
	private final int columnIndex;

	public Coordinates(int rowIndex, int columnIndex) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public Coordinates plus(Coordinates other) {
		return new Coordinates(this.rowIndex + other.rowIndex, this.columnIndex + other.columnIndex);
	}

	public Coordinates plusRows(int rowCount) {
		return new Coordinates(this.rowIndex + rowCount, columnIndex);
	}
	
	public Coordinates plusColumns(int columnCount) {
		return new Coordinates(this.rowIndex, columnIndex + columnCount);
	}

	public Coordinates withRow(int rowIndex) {
		return new Coordinates(rowIndex, this.columnIndex);
	}

	public Coordinates withColumn(int columnIndex) {
		return new Coordinates(this.rowIndex, columnIndex);
	}

	public Coordinates minus(Coordinates other) {
		return new Coordinates(this.rowIndex - other.rowIndex, this.columnIndex - other.columnIndex);
	}

	/**
	 * Returns true if both coordinates are grater or equal than those in the other object.
	 */
	public boolean graterOrEqualThan(Coordinates other) {
		return (this.rowIndex >= other.rowIndex) && //
				(this.columnIndex >= other.columnIndex);
	}

	/**
	 * Returns true if both coordinates are less or equal than those in the other object.
	 */
	public boolean lessOrEqualThan(Coordinates other) {
		return (this.rowIndex <= other.rowIndex) && //
				(this.columnIndex <= other.columnIndex);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnIndex;
		result = prime * result + rowIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		Coordinates other = (Coordinates) obj;
		return (columnIndex == other.columnIndex) && (rowIndex == other.rowIndex);
	}

	@Override
	public String toString() {
		return rowIndex + ":" + columnIndex;
	}

}
