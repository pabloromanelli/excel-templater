package com.socialmetrix.templater.geometry;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class RegionTest {

	@Test
	public void canCreateARegionFromTwoCoordinates() {
		Region result = new Region(new Coordinates(1, 2), new Coordinates(7, 5));
		assertEquals(new Region(new Coordinates(1, 2), 7, 4), result);
	}

	@Test
	public void canGetLastRow() {
		Region region = new Region(new Coordinates(1, 2), 7, 4);
		assertEquals(7, region.getLastRow());
	}

	@Test
	public void canGetLastColumn() {
		Region region = new Region(new Coordinates(1, 2), 7, 4);
		assertEquals(5, region.getLastColumn());
	}

	@Test
	public void canGetBottomRight() {
		Region region = new Region(new Coordinates(1, 2), 7, 4);
		assertEquals(new Coordinates(7, 5), region.getBottomRight());
	}

	@Test
	public void canMove() {
		Region region = new Region(new Coordinates(5, 0), 7, 4);
		assertEquals(new Region(new Coordinates(8, 4), 7, 4), region.moveTo(new Coordinates(8, 4)));
	}

	@Test
	public void willNotExpandIfTheOtherRegionIsContained() {
		Region region = new Region(new Coordinates(1, 2), new Coordinates(7, 5));
		Region otherRegion = new Region(new Coordinates(4, 3), new Coordinates(5, 4));

		Region combination = region.combine(otherRegion);

		assertEquals(new Region(new Coordinates(1, 2), new Coordinates(7, 5)), combination);
	}

	@Test
	public void canCombineWithOtherNonIntersectingRegion() {
		Region region = new Region(new Coordinates(1, 2), new Coordinates(7, 5));
		Region otherRegion = new Region(new Coordinates(4, 7), new Coordinates(5, 9));

		Region combination = region.combine(otherRegion);

		assertEquals(new Region(new Coordinates(1, 2), new Coordinates(7, 9)), combination);
	}

	@Test
	public void canCombineWithOtherIntersectingRegion() {
		Region region = new Region(new Coordinates(3, 1), new Coordinates(4, 7));
		Region otherRegion = new Region(new Coordinates(1, 4), new Coordinates(6, 5));

		Region combination = region.combine(otherRegion);

		assertEquals(new Region(new Coordinates(1, 1), new Coordinates(6, 7)), combination);
	}

	@Test
	public void canCheckIfTwoRegionsIntersects() {
		Region r1 = region(2, 2, 5, 7);
		Region r2 = region(5, 7, 7, 9);
		Region r3 = region(1, 1, 2, 2);
		Region r4 = region(1, 7, 2, 9);
		Region r5 = region(5, 1, 7, 2);
		Region r6 = region(3, 4, 4, 5);
		Region r7 = region(3, 7, 4, 8);
		Region r8 = region(6, 4, 7, 5);
		Region r9 = region(3, 0, 4, 1);
		Region r10 = region(0, 4, 1, 5);
		Region r11 = region(5, 7, 5, 7);
		Region r12 = region(2, 7, 2, 7);
		Region r13 = region(2, 2, 5, 7);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
		assertTrue(r1.intersects(r3));
		assertTrue(r1.intersects(r4));
		assertTrue(r1.intersects(r5));
		assertTrue(r1.intersects(r6));
		assertTrue(r1.intersects(r7));
		assertFalse(r1.intersects(r8));
		assertFalse(r1.intersects(r9));
		assertFalse(r1.intersects(r10));
		assertFalse(r10.intersects(r1));
		assertTrue(r1.intersects(r11));
		assertTrue(r1.intersects(r12));
		assertTrue(r1.intersects(r13));
	}

	@Test
	public void canCheckIfARegionIsIncludedInAnother() {
		Region r1 = region(2, 2, 5, 7);
		Region r2 = region(5, 7, 7, 9);
		Region r3 = region(1, 1, 2, 2);
		Region r4 = region(1, 7, 2, 9);
		Region r5 = region(5, 1, 7, 2);
		Region r6 = region(3, 4, 4, 5);
		Region r7 = region(3, 7, 4, 8);
		Region r8 = region(6, 4, 7, 5);
		Region r9 = region(3, 0, 4, 1);
		Region r10 = region(0, 4, 1, 5);
		Region r11 = region(5, 7, 5, 7);
		Region r12 = region(2, 7, 2, 7);
		Region r13 = region(2, 2, 5, 7);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
		assertFalse(r1.contains(r3));
		assertFalse(r1.contains(r4));
		assertFalse(r1.contains(r5));
		assertTrue(r1.contains(r6));
		assertFalse(r1.contains(r7));
		assertFalse(r1.contains(r8));
		assertFalse(r1.contains(r9));
		assertFalse(r1.contains(r10));
		assertFalse(r10.contains(r1));
		assertTrue(r1.contains(r11));
		assertTrue(r1.contains(r12));
		assertTrue(r1.contains(r13));
	}

	@Test
	public void canIterateARegion() {
		ArrayList<Coordinates> actualCoordinates = new ArrayList<Coordinates>();
		for (Coordinates coordinates : region(1, 2, 3, 4)) {
			actualCoordinates.add(coordinates);
		}

		List<Coordinates> expectedCoordinates =
			Arrays.asList(coords(1, 2), coords(1, 3), coords(1, 4), coords(2, 2), coords(2, 3), coords(2, 4), coords(3, 2), coords(3, 3), coords(3, 4));

		assertEquals(expectedCoordinates, actualCoordinates);
	}

	@Test
	public void canIterateWithForEach() {
		ArrayList<Coordinates> actualCoordinates = new ArrayList<Coordinates>();
		for (Coordinates coordinates : region(1, 2, 1, 2)) {
			actualCoordinates.add(coordinates);
		}

		List<Coordinates> expectedCoordinates = Arrays.asList(coords(1, 2));

		assertEquals(expectedCoordinates, actualCoordinates);
	}

	private static Region region(int x1, int y1, int x2, int y2) {
		return new Region(coords(x1, y1), coords(x2, y2));
	}

	private static Coordinates coords(int x, int y) {
		return new Coordinates(x, y);
	}

}
