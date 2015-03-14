package com.socialmetrix.templater.utils;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.*;

import com.socialmetrix.templater.utils.DAG;

public class DAGTest {

	private DAG actualDag;

	@Before
	public void setUp() {
		boolean[][] adjMatrixs = new boolean[][] { //
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
		actualDag = new DAG(adjMatrixs);
	}

	@Test
	public void canReduceADag() {
		boolean[][] expectedMatrix = new boolean[][] { //
						{ false, false, false, false, false, false, false, false, false, false, false },
						{ false, false, true, false, false, false, false, false, false, false, false },
						{ false, false, false, true, true, false, false, false, false, false, false },
						{ false, false, false, false, false, false, false, true, false, false, false },
						{ false, false, false, false, false, true, false, false, false, false, false },
						{ false, false, false, false, false, false, true, false, false, false, false },
						{ false, false, false, false, false, false, false, false, false, false, false },
						{ false, false, false, false, false, false, false, false, false, false, false },
						{ false, false, false, false, false, false, false, false, false, true, false },
						{ false, false, false, false, false, false, false, false, false, false, true },
						{ false, false, false, false, false, false, false, false, false, false, false } //
				};

		actualDag.pseudoTransitiveReduction();

		assertEquals(new DAG(expectedMatrix), actualDag);
	}

	@Test
	public void canGetRootNodes() {
		assertEquals(Arrays.asList(0, 1, 8), actualDag.getRoots());
	}

	@Test
	public void canGetChildren() {
		assertEquals(Arrays.asList(2, 3, 4, 5, 6, 7), actualDag.getChildren(1));
		assertEquals(Arrays.asList(7), actualDag.getChildren(3));
		assertEquals(new ArrayList<Integer>(), actualDag.getChildren(7));
	}

}
