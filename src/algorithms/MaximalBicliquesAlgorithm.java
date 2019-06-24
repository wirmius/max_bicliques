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

	Set<Biclique> findMaxBicliques(Graph graph);
}
