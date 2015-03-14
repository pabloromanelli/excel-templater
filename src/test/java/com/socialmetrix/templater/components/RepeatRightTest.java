package com.socialmetrix.templater.components;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;

@RunWith(MockitoJUnitRunner.class)
public class RepeatRightTest {

	@Mock
	private Component component1;
	@Mock
	private Component component2;
	@Mock
	private DataReader dataReader;

	@Test
	public void chagesOriginOnEachIteration() {
		when(component1.render(eq(new Integer(1)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 1, 2, 1));
		when(component2.render(eq(new Integer(1)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 2, 1, 2));

		when(component1.render(eq(new Integer(2)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 4, 1, 1));
		when(component2.render(eq(new Integer(2)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 5, 1, 1));

		when(component1.render(eq(new Integer(3)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 6, 3, 1));
		when(component2.render(eq(new Integer(3)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 7, 1, 1));

		Object data = mock(Object.class);
		when(dataReader.read(data, "prop")).thenReturn(Arrays.asList(1, 2, 3));
		RepeatRight repeatRight = new RepeatRight(dataReader, "prop", new Coordinates(2, 1));
		repeatRight.add(component1);
		repeatRight.add(component2);

		Region region = repeatRight.render(data, new Coordinates(0, 0), null);

		verify(component1).render(new Integer(1), new Coordinates(2, 1), null);
		verify(component2).render(new Integer(1), new Coordinates(2, 1), null);
		verify(component1).render(new Integer(2), new Coordinates(2, 4), null);
		verify(component2).render(new Integer(2), new Coordinates(2, 4), null);
		verify(component1).render(new Integer(3), new Coordinates(2, 6), null);
		verify(component2).render(new Integer(3), new Coordinates(2, 6), null);
		assertEquals(new Region(new Coordinates(2, 1), new Coordinates(4, 7)), region);
	}

	@Test
	public void canRenderNestedRepeatDown() {
		when(component1.render(eq(new Integer(1)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 1, 1, 2));
		when(component2.render(eq(new Integer(1)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 2, 1, 1));

		when(component1.render(eq(new Integer(2)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 3, 1, 1));
		when(component2.render(eq(new Integer(2)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(3, 3, 1, 2));

		when(component1.render(eq(new Integer(3)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 4, 1, 1));
		when(component2.render(eq(new Integer(3)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(3, 5, 1, 1));

		RepeatRight repeatRight2 = new RepeatRight(dataReader, "this", new Coordinates(0, 0));
		repeatRight2.add(component1);

		RepeatRight repeatRight3 = new RepeatRight(dataReader, "this", new Coordinates(1, 0));
		repeatRight3.add(component2);

		RepeatRight repeatRight1 = new RepeatRight(dataReader, "rep1", new Coordinates(2, 1));
		repeatRight1.add(repeatRight2);
		repeatRight1.add(repeatRight3);

		// rep1 -> rep2, rep3
		// rep2 -> comp1
		// rep3 -> comp2
		Object data = mock(Object.class);
		@SuppressWarnings("unchecked")
		List<List<Integer>> list = asList(asList(1), asList(2, 3));
		when(dataReader.read(data, "rep1")).thenReturn(list);
		when(dataReader.read(list, "this")).thenReturn(list);
		when(dataReader.read(asList(1), "this")).thenReturn(asList(1));
		when(dataReader.read(asList(2, 3), "this")).thenReturn(asList(2, 3));

		Region region = repeatRight1.render(data, new Coordinates(0, 0), null);

		verify(component1).render(new Integer(1), new Coordinates(2, 1), null);
		verify(component2).render(new Integer(1), new Coordinates(3, 1), null);
		verify(component1).render(new Integer(2), new Coordinates(2, 3), null);
		verify(component2).render(new Integer(2), new Coordinates(3, 3), null);
		verify(component1).render(new Integer(3), new Coordinates(2, 4), null);
		verify(component2).render(new Integer(3), new Coordinates(3, 5), null);
		assertEquals(new Region(new Coordinates(2, 1), new Coordinates(3, 5)), region);
	}

}
