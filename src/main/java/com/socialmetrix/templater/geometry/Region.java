package com.socialmetrix.templater.geometry;

import static java.lang.Math.*;

import java.util.*;

import com.socialmetrix.templater.TemplaterException;

/**
 * Immutable type to represent a non-empty set of coordinates.
 */
public class Region implements Iterable<Coordinates> {

	private final Coordinates origin;
	private final Rectangle dimensions;

	public Region(Coordinates origin, Rectangle dimensions) {
		if (dimensions.isEmpty()) {
			throw new EmptyRegionException(origin, dimensions);
		}
		this.origin = origin;
		this.dimensions = dimensions;
	}

	public Region(Coordinates origin, int rowCount, int columnCount) {
		this(origin, new Rectangle(rowCount, columnCount));
	}

	public Region(int rowIndex, int columnIndex, int rowCount, int columnCount) {
		this(new Coordinates(rowIndex, columnIndex), rowCount, columnCount);
	}

	public Region(Coordinates topLeft, Coordinates bottomRight) {
		this(topLeft, //
				bottomRight.getRowIndex() - topLeft.getRowIndex() + 1,
				bottomRight.getColumnIndex() - topLeft.getColumnIndex() + 1);
	}

	public Coordinates getOrigin() {
		return origin;
	}

	public Coordinates getTopLeft() {
		return this.origin;
	}

	public int getColumnCount() {
		return dimensions.getColumnCount();
	}

	public int getRowCount() {
		return dimensions.getRowCount();
	}

	public int getFirstRow() {
		return origin.getRowIndex();
	}

	public int getFirstColumn() {
		return origin.getColumnIndex();
	}

	public int getLastRow() {
		return origin.getRowIndex() + dimensions.getRowCount() - 1;
	}

	public int getLastColumn() {
		return origin.getColumnIndex() + dimensions.getColumnCount() - 1;
	}

	public Coordinates getBottomRight() {
		return new Coordinates(this.getLastRow(), this.getLastColumn());
	}

	public Region moveTo(Coordinates coordinates) {
		return new Region(coordinates, dimensions.getRowCount(), dimensions.getColumnCount());
	}

	/**
	 * Generates a new, minimal region, including both.
	 */
	public Region combine(Region other) {
		Coordinates thisBottomRight = this.getBottomRight();
		Coordinates otherBottomRight = other.getBottomRight();
		return new Region( //
				new Coordinates( //
						min(this.origin.getRowIndex(), other.getOrigin().getRowIndex()), //
						min(this.origin.getColumnIndex(), other.getOrigin().getColumnIndex())), //
				new Coordinates( //
						max(thisBottomRight.getRowIndex(), otherBottomRight.getRowIndex()), //
						max(thisBottomRight.getColumnIndex(), otherBottomRight.getColumnIndex())));
	}

	/**
	 * Returns true if the other region intersects (at least by one cell) with this one.
	 */
	public boolean intersects(Region other) {
		return other.getBottomRight().graterOrEqualThan(this.getOrigin()) &&
				this.getBottomRight().graterOrEqualThan(other.getOrigin());
	}

	/**
	 * Returns true if the other region is completely included in this one (including equal regions).
	 */
	public boolean contains(Region other) {
		return other.getTopLeft().graterOrEqualThan(this.getTopLeft()) && //
				other.getBottomRight().lessOrEqualThan(this.getBottomRight());
	}

	/**
	 * Iterates over each region's coordinates, row by row from left to right.
	 */
	@Override
	public Iterator<Coordinates> iterator() {
		return new CoordinatesIterator(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + origin.hashCode();
		result = prime * result + dimensions.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		Region other = (Region) obj;
		return origin.equals(other.origin) && //
				dimensions.equals(other.dimensions);
	}

	@Override
	public String toString() {
		return origin.toString() + "|" + dimensions.toString();
	}

	public static class EmptyRegionException extends TemplaterException {
		private static final long serialVersionUID = -7178981681402370866L;

		public EmptyRegionException(Coordinates origin, Rectangle dimensions) {
			super("Can't create an empty region [" + origin + "|" + dimensions + "].");
		}
	}

	/**
	 * Iterates over each region's coordinates, row by row from left to right.
	 */
	protected static class CoordinatesIterator implements Iterator<Coordinates> {

		private Region region;
		private Coordinates currentPosition;

		public CoordinatesIterator(Region region) {
			this.region = region;
			this.currentPosition = region.origin;
		}

		@Override
		public boolean hasNext() {
			return currentPosition.getRowIndex() <= region.getLastRow();
		}

		@Override
		public Coordinates next() {
			if (currentPosition.getRowIndex() > region.getLastRow()) {
				throw new NoSuchElementException("There are no more coordinates for '" + region + "'");
			}

			Coordinates result = currentPosition;

			if (currentPosition.getColumnIndex() < region.getLastColumn()) {
				currentPosition = currentPosition.plusColumns(1);
			} else {
				currentPosition = new Coordinates(currentPosition.getRowIndex() + 1, region.getFirstColumn());
			}

			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
