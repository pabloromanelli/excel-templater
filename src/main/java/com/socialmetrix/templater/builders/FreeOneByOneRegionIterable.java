package com.socialmetrix.templater.builders;

import java.util.*;

import com.socialmetrix.templater.geometry.*;

/**
 * Iterable useful to find free cells (1 by 1 regions) over a region with child regions.
 */
public class FreeOneByOneRegionIterable implements Iterable<Region> {

	private Region containerRegion;
	private List<Region> nonEmptyRegions;

	public FreeOneByOneRegionIterable(Region containerRegion) {
		this.containerRegion = containerRegion;
		this.nonEmptyRegions = new ArrayList<Region>();
	}

	public void addNonEmptyRegion(Region nonEmptyRegion) {
		this.nonEmptyRegions.add(nonEmptyRegion);
	}

	@Override
	public Iterator<Region> iterator() {
		return new FreeOneByOneRegionIterator(containerRegion.iterator(), nonEmptyRegions);
	}

	protected static class FreeOneByOneRegionIterator implements Iterator<Region> {

		private static enum Status {
			Found, NoMore
		}

		private final Iterator<Coordinates> iterator;
		private final List<Region> nonEmptyRegions;
		private Status status;
		private Region current;

		public FreeOneByOneRegionIterator(Iterator<Coordinates> iterator, List<Region> nonEmptyRegions) {
			this.iterator = iterator;
			this.nonEmptyRegions = nonEmptyRegions;
			this.advanceToNextFree();
		}

		private void advanceToNextFree() {
			while (iterator.hasNext()) {
				current = new Region(iterator.next(), 1, 1);
				if (isFree(current)) {
					this.status = Status.Found;
					return;
				}
			}
			this.status = Status.NoMore;
		}

		/**
		 * Returns true if none of the "nonEmptyRegions" contains this coordinates.
		 */
		private boolean isFree(Region oneByOneRegion) {
			for (Region nonEmptyRegion : this.nonEmptyRegions) {
				if (nonEmptyRegion.contains(oneByOneRegion)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean hasNext() {
			return status.equals(Status.Found);
		}

		@Override
		public Region next() {
			Region result = current;
			this.advanceToNextFree();
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public static class FreeOneByOneRegionIterableProvider {
		public FreeOneByOneRegionIterable get(Region containerRegion) {
			return new FreeOneByOneRegionIterable(containerRegion);
		}
	}

}
