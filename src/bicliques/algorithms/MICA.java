/**
 * 
 */
package bicliques.algorithms;

import java.util.Map.Entry;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;
import bicliques.graphs.Graph.Edge;
import bicliques.graphs.Graph.Vertex;

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
public class MICA<V extends Comparable<? super V>, E extends Comparable<? super E>> implements MaximalBicliquesAlgorithm<V, E> {
	
	@Override
	public Set<Biclique<V, E>> findMaxBicliques(Graph<V, E> graph) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
