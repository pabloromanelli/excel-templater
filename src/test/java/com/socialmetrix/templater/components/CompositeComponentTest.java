package com.socialmetrix.templater.components;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.datareaders.DataReader;
import com.socialmetrix.templater.geometry.*;

@RunWith(MockitoJUnitRunner.class)
public class CompositeComponentTest {

	@Mock
	private Component component1;
	@Mock
	private Component component2;
	@Mock
	private DataReader dataReader;
	private CompositeComponent composite;

	@Before
	public void setUp() {
		composite = new CompositeComponent(dataReader, "prop1", new Coordinates(2, 1));
		composite.add(component1);
		composite.add(component2);
	}

	@Test
	public void toRenderIsUsingDataReader() {
		when(dataReader.read(new Integer(1), "prop1")).thenReturn("1");

		composite.render(new Integer(1), new Coordinates(0, 0), null);

		verify(component1).render(eq("1"), any(Coordinates.class), any(Sheet.class));
		verify(component1).render(eq("1"), any(Coordinates.class), any(Sheet.class));
	}

	@Test
	public void canRenderNonOverlappingComponents() {
		when(component1.render(any(), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 1, 1, 1));
		when(component2.render(any(), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 2, 1, 1));

		composite.render(null, new Coordinates(0, 0), null);

		verify(component1).render(null, new Coordinates(2, 1), null);
		verify(component2).render(null, new Coordinates(2, 1), null);
	}

	@Test
	public void willNOTMoveComponentsOrigin() {
		when(component1.render(any(), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 2, 1, 2));
		when(component2.render(any(), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 1, 1, 1));

		composite.render(null, new Coordinates(0, 0), null);

		verify(component1).render(null, new Coordinates(2, 1), null);
		verify(component2).render(null, new Coordinates(2, 1), null);
	}

	@Test
	public void canRenderExpandingBottom() {
		when(component1.render(any(), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 1, 2, 1));
		when(component2.render(any(), any(Coordinates.class), any(Sheet.class))) //
			.thenReturn(new Region(2, 2, 1, 1));

		Region result = composite.render(null, new Coordinates(0, 0), null);

		assertEquals(new Region(2, 1, 2, 2), result);
	}

	@Test
	public void canRenderNestedComposite() {
		// obj1 -> obj2 -> obj3
		Object obj1 = mock(Object.class);
		Object obj2 = mock(Object.class);
		Object obj3 = mock(Object.class);
		when(dataReader.read(obj1, "obj2")).thenReturn(obj2);
		when(dataReader.read(obj2, "obj3")).thenReturn(obj3);

		// composite1("obj2") -> composite2("obj3") -> component1
		when(component1.render(any(), any(Coordinates.class), any(Sheet.class))).thenReturn(new Region(3, 2, 1, 2));
		CompositeComponent composite2 = new CompositeComponent(dataReader, "obj3", new Coordinates(1, 1));
		composite2.add(component1);
		CompositeComponent composite1 = new CompositeComponent(dataReader, "obj2", new Coordinates(2, 1));
		composite1.add(composite2);

		composite1.render(obj1, new Coordinates(0, 0), null);

		verify(component1).render(obj3, new Coordinates(3, 2), null);
	}

}
