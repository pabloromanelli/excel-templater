package com.socialmetrix.templater.utils;

import java.util.*;

/**
 * Simplified Directed acyclic graph to find relationship trees.
 */
public class DAG {

	private final boolean[][] adjacencyMatrix;

	public DAG(boolean[][] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	/**
	 * Recursively removes relationships of nodes which, transitively can relate to other.<br>
	 * The resulting state is a DAG with many trees.
	 */
	public DAG pseudoTransitiveReduction() {
		// reduce adjMatrix to get multiple trees
		List<Integer> roots = getRoots();
		deleteTransitiveRelationshipsForEach(roots);
		return this;
	}

	private void deleteTransitiveRelationshipsForEach(List<Integer> parents) {
		for (Integer parent : parents) {
			List<Integer> children = killNonExclusiveRelationshipsAndRetrieveExclusiveOnes(parent);
			deleteTransitiveRelationshipsForEach(children);
		}
	}

	private List<Integer> killNonExclusiveRelationshipsAndRetrieveExclusiveOnes(int parent) {
		List<Integer> result = new ArrayList<Integer>();

		for (Integer child : getChildren(parent)) {
			if (countEdgesTo(child) == 1) {
				result.add(child);
			} else {
				adjacencyMatrix[parent][child] = false;
			}
		}

		return result;
	}

	public List<Integer> getChildren(int parent) {
		List<Integer> result = new ArrayList<Integer>();

		for (int child = 0; child < adjacencyMatrix.length; child++) {
			if (adjacencyMatrix[parent][child]) {
				result.add(child);
			}
		}

		return result;
	}

	public List<Integer> getRoots() {
		List<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < adjacencyMatrix.length; i++) {
			if (countEdgesTo(i) == 0) {
				result.add(i);
			}
		}

		return result;
	}

	private int countEdgesTo(int destIndex) {
		int sum = 0;

		for (int i = 0; i < adjacencyMatrix.length; i++) {
			if (adjacencyMatrix[i][destIndex]) {
				sum++;
			}
		}

		return sum;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(adjacencyMatrix);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;

		DAG other = (DAG) obj;
		return Arrays.deepEquals(adjacencyMatrix, other.adjacencyMatrix);
	}

}
