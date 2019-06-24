/**
 * 
 */
package algorithms;

import java.util.Set;

import graphs.Biclique;
import graphs.Graph;

/**
 * Interface for all algorithms computing the set of maximal bicliques of a graph.
 * @author Roland Koppenberger
 * @version 1.0, June 24th 2019.
 */
public interface MaximalBicliquesAlgorithm {

	/**
	 * Computes all maximal bicliques of the given graph.
	 * @param graph Graph to examine.
	 * @return Set of maximal bicliques of graph.
	 */
	Set<Biclique> findMaxBicliques(Graph graph);
}
