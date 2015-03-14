package com.socialmetrix.templater.builders;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.socialmetrix.templater.builders.FreeOneByOneRegionIterable;
import com.socialmetrix.templater.geometry.*;

public class FreeOneByOneRegionIterableTest {

	@Test
	public void onlyOneFreeRegion() {
		FreeOneByOneRegionIterable freeRegions = new FreeOneByOneRegionIterable(region(1, 2, 1, 2));

		ArrayList<Region> actualFreeRegions = new ArrayList<Region>();
		for (Region freeRegion : freeRegions) {
			actualFreeRegions.add(freeRegion);
		}

		assertEquals(Arrays.asList(region(1, 2, 1, 2)), actualFreeRegions);
	}

	@Test
	@SuppressWarnings("unused")
	public void noFreeRegions() {
		FreeOneByOneRegionIterable freeRegions = new FreeOneByOneRegionIterable(region(1, 2, 4, 3));
		freeRegions.addNonEmptyRegion(region(1, 2, 4, 3));

		for (Region freeRegion : freeRegions) {
			fail();
		}

		assertFalse(freeRegions.iterator().hasNext());
	}

	@Test
	public void multipleFreeRegions() {
		FreeOneByOneRegionIterable freeRegions = new FreeOneByOneRegionIterable(region(1, 2, 4, 5));
		freeRegions.addNonEmptyRegion(region(1, 2, 1, 2));
		freeRegions.addNonEmptyRegion(region(1, 3, 3, 3));
		freeRegions.addNonEmptyRegion(region(4, 2, 4, 2));
		freeRegions.addNonEmptyRegion(region(2, 4, 2, 4));
		freeRegions.addNonEmptyRegion(region(3, 5, 3, 5));

		ArrayList<Region> actualFreeRegions = new ArrayList<Region>();
		for (Region freeRegion : freeRegions) {
			actualFreeRegions.add(freeRegion);
		}

		assertEquals(Arrays.asList(
				region(1, 4, 1, 4),
				region(1, 5, 1, 5),
				region(2, 2, 2, 2),
				region(2, 5, 2, 5),
				region(3, 2, 3, 2),
				region(3, 4, 3, 4),
				region(4, 3, 4, 3),
				region(4, 4, 4, 4),
				region(4, 5, 4, 5)), actualFreeRegions);
	}

	private static Region region(int x1, int y1, int x2, int y2) {
		return new Region(coords(x1, y1), coords(x2, y2));
	}

	private static Coordinates coords(int x, int y) {
		return new Coordinates(x, y);
	}

}
