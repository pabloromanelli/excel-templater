package com.socialmetrix.templater.builders;

import static org.junit.Assert.*;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.builders.NamedRegion;
import com.socialmetrix.templater.builders.NamedRegion.Repeat;
import com.socialmetrix.templater.geometry.*;

@RunWith(MockitoJUnitRunner.class)
public class NamedRegionTest {

	@Mock
	private Sheet sheet;

	@Test
	public void overlappingOnTheSameRegionWithDifferentOrder() {
		NamedRegion region1 = namedRegion(region(1, 1, 2, 2), 0);
		NamedRegion region2 = namedRegion(region(1, 1, 2, 2), 1);

		assertFalse(region1.invalidOverlapping(region2));
		assertTrue(region1.includes(region2));
		assertFalse(region2.includes(region1));
	}
	
	@Test
	public void overlappingOnTheSameRegionWithSameOrder() {
		NamedRegion region1 = namedRegion(region(1, 1, 2, 2), 0);
		NamedRegion region2 = namedRegion(region(1, 1, 2, 2), 0);
		
		assertTrue(region1.invalidOverlapping(region2));
		assertFalse(region1.includes(region2));
		assertFalse(region2.includes(region1));
	}
	
	@Test
	public void nonOverlapping() {
		NamedRegion region1 = namedRegion(region(1, 1, 2, 2), 0);
		NamedRegion region2 = namedRegion(region(3, 3, 4, 4), 0);

		assertFalse(region1.invalidOverlapping(region2));
		assertFalse(region1.includes(region2));
		assertFalse(region2.includes(region1));
	}
	
	@Test
	public void including() {
		NamedRegion region1 = namedRegion(region(2, 2, 5, 7), 0);
		NamedRegion region2 = namedRegion(region(3, 4, 4, 5), 0);
		
		assertFalse(region1.invalidOverlapping(region2));
		assertTrue(region1.includes(region2));
		assertFalse(region2.includes(region1));
	}
	
	@Test
	public void partialInclusion() {
		NamedRegion region1 = namedRegion(region(2, 2, 5, 7), 0);
		NamedRegion region2 = namedRegion(region(3, 7, 4, 8), 1);
		
		assertTrue(region1.invalidOverlapping(region2));
		assertFalse(region1.includes(region2));
		assertFalse(region2.includes(region1));
	}

	private NamedRegion namedRegion(Region region, Integer order) {
		return new NamedRegion(sheet, region, "prop", Repeat.NONE, order);
	}

	private static Region region(int x1, int y1, int x2, int y2) {
		return new Region(new Coordinates(x1, y1), new Coordinates(x2, y2));
	}

}
