package com.socialmetrix.templater.builders;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.*;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.socialmetrix.templater.builders.ComponentsTreeBuilder.InvalidOverlapping;
import com.socialmetrix.templater.builders.FreeOneByOneRegionIterable.FreeOneByOneRegionIterableProvider;
import com.socialmetrix.templater.builders.NamedRegion.Repeat;
import com.socialmetrix.templater.components.*;
import com.socialmetrix.templater.geometry.*;
import com.socialmetrix.templater.utils.DAG;

@RunWith(MockitoJUnitRunner.class)
public class ComponentsTreeBuilderTest {

	@Mock
	private Sheet sheet;
	@Mock
	private ComponentsFactory componentsFactory;
	@Mock
	private FreeOneByOneRegionIterableProvider freeOneByOneRegionIterableProvider;
	@Mock
	private DAG dag;
	private List<NamedRegion> regionsList;

	@Before
	public void setUp() {
		NamedRegion r1 = namedRegion(region(4, 1, 5, 2), 0);
		NamedRegion r2 = namedRegion(region(1, 4, 9, 11), 0);
		NamedRegion r3 = namedRegion(region(1, 4, 9, 11), 1);
		NamedRegion r4 = namedRegion(region(3, 6, 5, 7), 0);
		NamedRegion r5 = namedRegion(region(4, 9, 5, 10), 0);
		NamedRegion r6 = namedRegion(region(4, 9, 5, 10), 1);
		NamedRegion r7 = namedRegion(region(5, 10, 5, 10), 0);
		NamedRegion r8 = namedRegion(region(5, 7, 5, 7), 0);
		NamedRegion r9 = namedRegion(region(2, 13, 5, 15), 0);
		NamedRegion r10 = namedRegion(region(2, 13, 5, 15), 1);
		NamedRegion r11 = namedRegion(region(3, 14, 4, 14), 0);
		regionsList = Arrays.asList(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
	}

	@Test
	public void canCalculateAdjMatrix() {
		boolean[][] expectedMatrix = new boolean[][] { //
					{ false, false, false, false, false, false, false, false, false, false, false },
					{ false, false, true, true, true, true, true, true, false, false, false },
					{ false, false, false, true, true, true, true, true, false, false, false },
					{ false, false, false, false, false, false, false, true, false, false, false },
					{ false, false, false, false, false, true, true, false, false, false, false },
					{ false, false, false, false, false, false, true, false, false, false, false },
					{ false, false, false, false, false, false, false, false, false, false, false },
					{ false, false, false, false, false, false, false, false, false, false, false },
					{ false, false, false, false, false, false, false, false, false, true, true },
					{ false, false, false, false, false, false, false, false, false, false, true },
					{ false, false, false, false, false, false, false, false, false, false, false } //
			};

		boolean[][] adjMatrix = ComponentsTreeBuilder.calculateAdjacencyMatrix(regionsList);

		assertTrue(Arrays.deepEquals(expectedMatrix, adjMatrix));
	}

	@Test(expected = InvalidOverlapping.class)
	public void adjMatrixCalculationWillFailOnInvalidOverlap() {
		NamedRegion r1 = namedRegion(region(4, 1, 5, 2), 0);
		NamedRegion r2 = namedRegion(region(1, 4, 9, 11), 0);
		NamedRegion r3 = namedRegion(region(1, 4, 9, 11), 1);
		NamedRegion r4 = namedRegion(region(3, 6, 5, 7), 0);
		NamedRegion r5 = namedRegion(region(4, 9, 5, 10), 0);
		NamedRegion r6 = namedRegion(region(4, 9, 5, 10), 0);
		NamedRegion r7 = namedRegion(region(5, 10, 5, 10), 0);
		NamedRegion r8 = namedRegion(region(5, 7, 5, 7), 0);
		NamedRegion r9 = namedRegion(region(2, 13, 5, 15), 0);
		NamedRegion r10 = namedRegion(region(2, 13, 5, 15), 1);
		NamedRegion r11 = namedRegion(region(3, 14, 4, 14), 0);

		ComponentsTreeBuilder.calculateAdjacencyMatrix(Arrays.asList(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11));
	}

	@Test
	public void canReduceToMultipleTrees() {
		// regions
		List<NamedRegion> regions =
			Arrays.asList(
				namedRegion(region(3, 1, 3, 1), Repeat.DOWN, 0),
				namedRegion(region(2, 2, 3, 4), Repeat.RIGHT, 0),
				namedRegion(region(3, 1, 3, 1), Repeat.NONE, 1),
				namedRegion(region(2, 3, 2, 3), Repeat.NONE, 0),
				namedRegion(region(3, 2, 3, 3), Repeat.DOWN, 0),
				namedRegion(region(3, 2, 3, 2), Repeat.NONE, 0));

		// tree dag
		ArrayList<Integer> empty = new ArrayList<Integer>();
		when(dag.getRoots()).thenReturn(asList(0, 1));
		when(dag.getChildren(0)).thenReturn(asList(2));
		when(dag.getChildren(2)).thenReturn(empty);
		when(dag.getChildren(1)).thenReturn(asList(3, 4));
		when(dag.getChildren(3)).thenReturn(empty);
		when(dag.getChildren(4)).thenReturn(asList(5));
		when(dag.getChildren(5)).thenReturn(empty);

		// components
		CompositeComponent root = mock(CompositeComponent.class);
		when(root.getRelativeOrigin()).thenReturn(coords(0, 0));
		RepeatDown comp0 = mock(RepeatDown.class);
		RepeatRight comp1 = mock(RepeatRight.class);
		VariableComponent comp2 = mock(VariableComponent.class);
		VariableComponent comp3 = mock(VariableComponent.class);
		RepeatDown comp4 = mock(RepeatDown.class);
		VariableComponent comp5 = mock(VariableComponent.class);
		when(componentsFactory.createRootComponent()).thenReturn(root);
		when(componentsFactory.createCompositeComponent(regions.get(0), coords(0, 0))).thenReturn(comp0);
		when(componentsFactory.createCompositeComponent(regions.get(1), coords(0, 0))).thenReturn(comp1);
		when(componentsFactory.createVariableComponent(regions.get(2), coords(3, 1))).thenReturn(comp2);
		when(componentsFactory.createVariableComponent(regions.get(3), coords(2, 2))).thenReturn(comp3);
		when(componentsFactory.createCompositeComponent(regions.get(4), coords(2, 2))).thenReturn(comp4);
		when(componentsFactory.createVariableComponent(regions.get(5), coords(3, 2))).thenReturn(comp5);

		// constant components
		ConstantComponent c0 = mock(ConstantComponent.class);
		ConstantComponent c1 = mock(ConstantComponent.class);
		ConstantComponent c2 = mock(ConstantComponent.class);
		ConstantComponent c3 = mock(ConstantComponent.class);
		when(componentsFactory.createConstantComponent(sheet, region(2, 2, 2, 2), coords(2, 2))).thenReturn(c0);
		when(componentsFactory.createConstantComponent(sheet, region(2, 4, 2, 4), coords(2, 2))).thenReturn(c1);
		when(componentsFactory.createConstantComponent(sheet, region(3, 4, 3, 4), coords(2, 2))).thenReturn(c2);
		when(componentsFactory.createConstantComponent(sheet, region(3, 3, 3, 3), coords(3, 2))).thenReturn(c3);

		ArrayList<Region> emptyRegions = new ArrayList<Region>();
		FreeOneByOneRegionIterable free0 = mock(FreeOneByOneRegionIterable.class);
		when(free0.iterator()).thenReturn(emptyRegions.iterator());
		when(freeOneByOneRegionIterableProvider.get(region(3, 1, 3, 1))).thenReturn(free0);

		FreeOneByOneRegionIterable free1 = mock(FreeOneByOneRegionIterable.class);
		when(free1.iterator()).thenReturn(Arrays.asList(region(2, 2, 2, 2), region(2, 4, 2, 4), region(3, 4, 3, 4)).iterator());
		when(freeOneByOneRegionIterableProvider.get(region(2, 2, 3, 4))).thenReturn(free1);

		FreeOneByOneRegionIterable free4 = mock(FreeOneByOneRegionIterable.class);
		when(free4.iterator()).thenReturn(Arrays.asList(region(3, 3, 3, 3)).iterator());
		when(freeOneByOneRegionIterableProvider.get(region(3, 2, 3, 3))).thenReturn(free4);

		new ComponentsTreeBuilder(componentsFactory, freeOneByOneRegionIterableProvider).sheet(sheet).regions(regions, dag).build();

		verify(root).add(comp0);
		verify(root).add(comp1);
		verify(root, times(2)).getRelativeOrigin();
		verifyNoMoreInteractions(root);
		verify(comp0).add(comp2);
		verifyNoMoreInteractions(comp0);
		verify(comp1).add(comp3);
		verify(comp1).add(comp4);
		verify(comp1).add(c0);
		verify(comp1).add(c1);
		verify(comp1).add(c2);
		verifyNoMoreInteractions(comp1);
		verify(comp4).add(comp5);
		verify(comp4).add(c3);
		verifyNoMoreInteractions(comp4);
		verify(free0).addNonEmptyRegion(region(3, 1, 3, 1));
		verify(free0).iterator();
		verifyNoMoreInteractions(free0);
		verify(free1).addNonEmptyRegion(region(2, 3, 2, 3));
		verify(free1).addNonEmptyRegion(region(3, 2, 3, 3));
		verify(free1).iterator();
		verifyNoMoreInteractions(free1);
		verify(free4).addNonEmptyRegion(region(3, 2, 3, 2));
		verify(free4).iterator();
		verifyNoMoreInteractions(free4);
	}

	private NamedRegion namedRegion(Region region, Integer order) {
		return namedRegion(region, Repeat.NONE, order);
	}

	private NamedRegion namedRegion(Region region, Repeat repeat, Integer order) {
		return new NamedRegion(sheet, region, "prop", repeat, order);
	}

	private static Region region(int x1, int y1, int x2, int y2) {
		return new Region(coords(x1, y1), coords(x2, y2));
	}

	private static Coordinates coords(int x, int y) {
		return new Coordinates(x, y);
	}

}
