/**
 * 
 */
package bicliques.algorithms;

import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;
import bicliques.graphs.GraphVendingMachine;
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
/*		
		do {
			n0 = new TreeSet<>();
			for (<Biclique<V, E> elem : w0)
				for (<Biclique<V, E> star : c0)
					if (!elem.equals(star)) {
						Set<Set<String>> cons = mba.consensus(elem, star);
						Set<<Biclique<V, E>> cons = 
						for (Set<String> con : cons) 
							if (!mba.absorbs(c, con) && !mba.absorbs(n0, con)) {
								Set<String> x = mba.gamma(con);
								Set<String> y = mba.gamma(x);
								n0.add(comp.compare(x, y) <= 0 ? x : y);
							}
					}
			w0 = new TreeSet<>();
			w0.addAll(n0);
			c.addAll(n0);
			
		} while (!n0.isEmpty());
*/		
		return c;
	}
	
}
