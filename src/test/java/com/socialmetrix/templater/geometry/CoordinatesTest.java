package com.socialmetrix.templater.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.socialmetrix.templater.geometry.Coordinates;

public class CoordinatesTest {

	@Test
	public void canAddAnotherCoordinate() {
		Coordinates result = new Coordinates(2, 3).plus(new Coordinates(1, 2));
		assertEquals(new Coordinates(3, 5), result);
	}

	@Test
	public void canAddOnlyRows() {
		Coordinates result = new Coordinates(2, 3).plusRows(4);
		assertEquals(new Coordinates(6, 3), result);
	}

	@Test
	public void canSubstractCoordinates() {
		Coordinates result = new Coordinates(3, 4).minus(new Coordinates(7, 1));
		assertEquals(new Coordinates(-4, 3), result);
	}

}
