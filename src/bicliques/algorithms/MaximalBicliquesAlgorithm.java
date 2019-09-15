/**
 * 
 */
package bicliques.algorithms;

import java.util.Set;

import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;

/**
 * Interface for all algorithms computing the set of maximal bicliques of a graph.
 * @author Roland Koppenberger
 * @version 1.1, September 15th 2019.
 */
public interface MaximalBicliquesAlgorithm<V extends Comparable<? super V>, E extends Comparable<? super E>> {

	/**
	 * Computes all maximal bicliques of the given graph.
	 * @param graph Graph to examine.
	 * @return Set of maximal bicliques of graph.
	 */
	Set<Biclique<V, E>> findMaxBicliques(Graph<V, E> graph, Thread computation);
}
