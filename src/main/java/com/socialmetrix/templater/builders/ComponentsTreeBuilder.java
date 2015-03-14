package com.socialmetrix.templater.builders;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.builders.FreeOneByOneRegionIterable.FreeOneByOneRegionIterableProvider;
import com.socialmetrix.templater.components.*;
import com.socialmetrix.templater.geometry.*;
import com.socialmetrix.templater.utils.DAG;

/**
 * Builds a components tree from a list of named regions.
 */
public class ComponentsTreeBuilder {

	private FreeOneByOneRegionIterableProvider freeOneByOneRegionIterableProvider;
	private ComponentsFactory componentsFactory;
	private Sheet sheet;
	private List<NamedRegion> regions;
	private DAG regionsTreeDag;

	public ComponentsTreeBuilder(
			ComponentsFactory componentsFactory,
			FreeOneByOneRegionIterableProvider freeOneByOneRegionIterableProvider) {
		this.componentsFactory = componentsFactory;
		this.freeOneByOneRegionIterableProvider = freeOneByOneRegionIterableProvider;
	}

	public ComponentsTreeBuilder sheet(Sheet sheet) {
		this.sheet = sheet;
		return this;
	}

	public ComponentsTreeBuilder regions(List<NamedRegion> regions) {
		this.regions = regions;
		this.regionsTreeDag = new DAG(calculateAdjacencyMatrix(regions)).pseudoTransitiveReduction();
		return this;
	}

	protected ComponentsTreeBuilder regions(List<NamedRegion> regions, DAG regionsTreeDag) {
		// to test it without a real dag
		this.regions = regions;
		this.regionsTreeDag = regionsTreeDag;
		return this;
	}

	public Component build() {
		CompositeComponent rootComponent = componentsFactory.createRootComponent();

		for (Integer childIndex : regionsTreeDag.getRoots()) {
			Component child = namedRegionToComponentsTree(rootComponent.getRelativeOrigin(), childIndex);
			rootComponent.add(child);
		}

		return rootComponent;
	}

	private Component namedRegionToComponentsTree(Coordinates parentOrigin, Integer treeRootDagIndex) {
		NamedRegion treeRootRegion = regions.get(treeRootDagIndex);
		List<Integer> childrenDagIndex = regionsTreeDag.getChildren(treeRootDagIndex);

		if (childrenDagIndex.isEmpty()) {
			return componentsFactory.createVariableComponent(treeRootRegion, parentOrigin);
		} else {
			return buildComposite(parentOrigin, treeRootRegion, childrenDagIndex);
		}
	}

	private Component buildComposite(Coordinates parentOrigin, NamedRegion treeRootRegion, List<Integer> childrenDagIndex) {
		CompositeComponent parent = componentsFactory.createCompositeComponent(treeRootRegion, parentOrigin);
		Coordinates currentParentOrigin = treeRootRegion.getRegion().getOrigin();
		FreeOneByOneRegionIterable freeRegions = freeOneByOneRegionIterableProvider.get(treeRootRegion.getRegion());

		// add child components to parent
		for (Integer childDagIndex : childrenDagIndex) {
			Component child = namedRegionToComponentsTree(currentParentOrigin, childDagIndex);
			parent.add(child);

			Region childRegion = regions.get(childDagIndex).getRegion();
			freeRegions.addNonEmptyRegion(childRegion);
		}

		// add constant components for empty cells
		for (Region freeRegion : freeRegions) {
			Component component = componentsFactory.createConstantComponent(sheet, freeRegion, currentParentOrigin);
			parent.add(component);
		}

		return parent;
	}

	protected static boolean[][] calculateAdjacencyMatrix(List<NamedRegion> regions) {
		boolean[][] adjacencyMatrix = new boolean[regions.size()][regions.size()];

		for (int i = 0; i < regions.size(); i++) {
			NamedRegion orig = regions.get(i);

			for (int j = 0; j < regions.size(); j++) {
				NamedRegion dest = regions.get(j);
				if (i == j) {
					// ignore self
					continue;
				}
				if (orig.invalidOverlapping(dest)) {
					throw new InvalidOverlapping(orig, dest);
				}
				if (orig.includes(dest)) {
					adjacencyMatrix[i][j] = true;
				}
			}
		}

		return adjacencyMatrix;
	}

	public static class InvalidOverlapping extends TemplaterException {
		private static final long serialVersionUID = 3063252237407345495L;

		public InvalidOverlapping(NamedRegion region1, NamedRegion region2) {
			super("Invalid overlapping with " + region1.toString() + " and " + region2 + ".");
		}
	}

	public static class ComponentsTreeBuilderProvider {
		private final ComponentsFactory componentsFactory;
		private final FreeOneByOneRegionIterableProvider freeOneByOneRegionIterableProvider;

		public ComponentsTreeBuilderProvider(
				ComponentsFactory componentsFactory,
				FreeOneByOneRegionIterableProvider freeOneByOneRegionIterableProvider) {
			this.componentsFactory = componentsFactory;
			this.freeOneByOneRegionIterableProvider = freeOneByOneRegionIterableProvider;
		}

		public ComponentsTreeBuilder get() {
			return new ComponentsTreeBuilder(componentsFactory, freeOneByOneRegionIterableProvider);
		}
	}

}
