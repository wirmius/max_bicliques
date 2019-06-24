/**
 * 
 */
package bicliques.algorithms;

import java.util.Set;

import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;

/**
 * Implements MICA algorithm for finding all
 * maximal complete bipartite subgraphs (bicliques) of a graph.<br>
 * 
 * @see <a href="https://doi.org/10.1016/j.dam.2003.09.004">
 * Gabriela Alexe, Sorin Alexe, Yves Crama, Stephan Foldes, Peter L. Hammer, Bruno Simeone,
 * Consensus algorithms for the generation of all maximal bicliques,
 * Discrete Applied Mathematics,
 * Volume 145,
 * Issue 1,
 * 2004,
 * Pages 11-21.</a>
 * 
 * @author Roland Koppenberger
 * @version 1.0, June 24th 2019.
 */
public class MICA implements MaximalBicliquesAlgorithm {

	/* (non-Javadoc)
	 * @see algorithms.MaximalBicliquesAlgorithm#findMaxBicliques(graphs.Graph)
	 */
	@Override
	public Set<Biclique> findMaxBicliques(Graph graph) {
		// TODO
		double x;
		for (long i = 0; i < 100000000; i++)
			x = Math.log(1 + Math.sin(Math.sqrt(i)));
		return null;
	}

}
