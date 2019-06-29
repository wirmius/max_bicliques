/**
 * 
 */
package bicliques.algorithms;

import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Map.Entry;

import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;
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
		
		// no graph
		if (graph == null)
			throw new NullPointerException("No graph provided.");
		
		// empty graph
		if (graph.getVertices().size() == 0)
			return Collections.emptySet();
		
		// c0:
		// vertices -> set of star bicliques covering edges of graph
		// star bicliques are extended to maximal bicliques
		Set<Biclique<V, E>> c0 = new TreeSet<>();
		for (Entry<V, ? extends Vertex<V>> entry : graph.getVertices().entrySet()) {
			Biclique<V, E> starExtended = new Biclique<>(graph, entry.getValue());
			if (!starExtended.isAbsorbedOf(c0))
				c0.add(starExtended);
		}
		
		// c collects max. bicliques
		Set<Biclique<V, E>> c = new TreeSet<>();
		c.addAll(c0);
		
		// w0 is actual working set (= new ones in last stage)
		Set<Biclique<V, E>> w0 = new TreeSet<>();
		w0.addAll(c0);
		
		// n0 collects new max. bicliques in actual stage
		Set<Biclique<V, E>> n0;
		
		do {
			// clear newly found max. bicliques
			n0 = new TreeSet<>();
			// for every two bicliques,
			// one from working set w0, and one from extended stars set c0
			for (Biclique<V, E> bic : w0)
				for (Biclique<V, E> starExtended : c0)
					if (!bic.equals(starExtended)) {
						// compute set of extended consensuses and add them to n0
						// if they are not absorbed by bicliques in c or n0
						Set<Biclique<V, E>> cons = bic.extendedConsensus(starExtended);
						for (Biclique<V, E> con : cons)
							if (!con.isAbsorbedOf(c) && !con.isAbsorbedOf(n0))
								n0.add(con);
					}
			
			// next working set consists of newly found maximal bicliques
			w0 = new TreeSet<>();
			w0.addAll(n0);
			
			// add newly found max. bicliques
			c.addAll(n0);
			
			// repeat while new bicliques have been found
		} while (!n0.isEmpty());
		
		return c;
	}
	
}
