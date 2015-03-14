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
public class RepeatDownTest {

	@Mock
	private Component component1;
	@Mock
	private Component component2;
	@Mock
	private DataReader dataReader;

	@Test
	public void chagesOriginOnEachIteration() {
		when(component1.render(eq(new Integer(1)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 2, 1, 1));
		when(component2.render(eq(new Integer(1)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(3, 1, 2, 1));

		when(component1.render(eq(new Integer(2)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(5, 2, 1, 2));
		when(component2.render(eq(new Integer(2)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(6, 1, 1, 1));

		when(component1.render(eq(new Integer(3)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(7, 2, 1, 1));
		when(component2.render(eq(new Integer(3)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(8, 1, 1, 1));

		RepeatDown repeatDown = new RepeatDown(dataReader, "prop", new Coordinates(2, 1));
		repeatDown.add(component1);
		repeatDown.add(component2);

		Object data = mock(Object.class);
		when(dataReader.read(data, "prop")).thenReturn(Arrays.asList(1, 2, 3));
		Region region = repeatDown.render(data, new Coordinates(0, 0), null);

		verify(component1).render(new Integer(1), new Coordinates(2, 1), null);
		verify(component2).render(new Integer(1), new Coordinates(2, 1), null);
		verify(component1).render(new Integer(2), new Coordinates(5, 1), null);
		verify(component2).render(new Integer(2), new Coordinates(5, 1), null);
		verify(component1).render(new Integer(3), new Coordinates(7, 1), null);
		verify(component2).render(new Integer(3), new Coordinates(7, 1), null);
		assertEquals(new Region(new Coordinates(2, 1), new Coordinates(8, 3)), region);
	}

	@Test
	public void canRenderNestedRepeatDown() {
		when(component1.render(eq(new Integer(1)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 1, 2, 1));
		when(component2.render(eq(new Integer(1)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 2, 1, 1));

		when(component1.render(eq(new Integer(2)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(4, 1, 1, 1));
		when(component2.render(eq(new Integer(2)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(4, 2, 2, 1));

		when(component1.render(eq(new Integer(3)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(5, 1, 1, 1));
		when(component2.render(eq(new Integer(3)), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(6, 2, 1, 1));

		RepeatDown repeatDown2 = new RepeatDown(dataReader, "this", new Coordinates(0, 0));
		repeatDown2.add(component1);

		RepeatDown repeatDown3 = new RepeatDown(dataReader, "this", new Coordinates(0, 1));
		repeatDown3.add(component2);

		RepeatDown repeatDown1 = new RepeatDown(dataReader, "rep1", new Coordinates(2, 1));
		repeatDown1.add(repeatDown2);
		repeatDown1.add(repeatDown3);

		// rep1("rep1") -> rep2, rep3
		// rep2 -> comp1
		// rep3 -> comp2
		Object data = mock(Object.class);
		@SuppressWarnings("unchecked")
		List<List<Integer>> list = asList(asList(1), asList(2, 3));
		when(dataReader.read(data, "rep1")).thenReturn(list);
		when(dataReader.read(list, "this")).thenReturn(list);
		when(dataReader.read(asList(1), "this")).thenReturn(asList(1));
		when(dataReader.read(asList(2, 3), "this")).thenReturn(asList(2, 3));
		Region region = repeatDown1.render(data, new Coordinates(0, 0), null);

		verify(component1).render(new Integer(1), new Coordinates(2, 1), null);
		verify(component2).render(new Integer(1), new Coordinates(2, 2), null);
		verify(component1).render(new Integer(2), new Coordinates(4, 1), null);
		verify(component2).render(new Integer(2), new Coordinates(4, 2), null);
		verify(component1).render(new Integer(3), new Coordinates(5, 1), null);
		verify(component2).render(new Integer(3), new Coordinates(6, 2), null);
		assertEquals(new Region(new Coordinates(2, 1), new Coordinates(6, 2)), region);
	}

}
