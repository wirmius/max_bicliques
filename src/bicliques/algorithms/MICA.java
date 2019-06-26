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
	
	public class BiCompare implements Comparator<Set<V>> {

		@Override
		public int compare(Set<V> set0, Set<V> set1) {
			int cmp = set0.size() - set1.size();
			if (cmp != 0)
				return cmp;
			Iterator<V> iter1 = set1.iterator();
			for (V elem0 : set0) {
				V elem1 = iter1.next();
				cmp = elem0.compareTo(elem1);
				if (cmp != 0)
					return cmp;
			}
			return 0;
		}
		
	}

	@Override
	public Set<Biclique> findMaxBicliques(Graph<V, E> graph) {
		
		// vertices -> star bicliques
//		for (Entry<V, ? extends Vertex<V>> v : graph.getVertices().entrySet()) {
//			Set<V>= v.getValue().getIncomingEdges();
//		}
		return null;
	}
	
	public Set<V> getNeighbours(V v) {
		Set<V> n = new TreeSet<>();

		if (v.equals("1")) {
			n.add((V) "4");
			return n;
		}
		
		if (v.equals("2")) {
			n.add((V) "4");
			n.add((V) "5");
			n.add((V) "6");
			return n;
		}
		
		if (v.equals("3")) {
			n.add((V) "4");
			n.add((V) "6");
			return n;
		}
		
		if (v.equals("4")) {
			n.add((V) "1");
			n.add((V) "2");
			n.add((V) "3");
			return n;
		}
		
		if (v.equals("5")) {
			n.add((V) "2");
			return n;
		}
		
		if (v.equals("6")) {
			n.add((V) "2");
			n.add((V) "3");
			return n;
		}
		
		
/*		if (v.equals("0")) {
			n.add((V) "5");
			n.add((V) "9");
			return n;
		}
		
		if (v.equals("1")) {
			n.add((V) "2");
			n.add((V) "4");
			n.add((V) "8");
			return n;
		}
		
		if (v.equals("2")) {
			n.add((V) "1");
			n.add((V) "3");
			n.add((V) "4");
			return n;
		}
		
		if (v.equals("3")) {
			n.add((V) "2");
			n.add((V) "4");
			n.add((V) "5");
			n.add((V) "6");
			n.add((V) "7");
			return n;
		}
		
		if (v.equals("4")) {
			n.add((V) "1");
			n.add((V) "2");
			n.add((V) "3");
			n.add((V) "5");
			n.add((V) "7");
			n.add((V) "9");
			return n;
		}
		
		if (v.equals("5")) {
			n.add((V) "0");
			n.add((V) "3");
			n.add((V) "4");
			n.add((V) "6");
			return n;
		}
		
		if (v.equals("6")) {
			n.add((V) "3");
			n.add((V) "5");
			n.add((V) "7");
			return n;
		}
		
		if (v.equals("7")) {
			n.add((V) "3");
			n.add((V) "4");
			n.add((V) "6");
			return n;
		}
		
		if (v.equals("8")) {
			n.add((V) "1");
			n.add((V) "9");
			return n;
		}
		
		if (v.equals("9")) {
			n.add((V) "0");
			n.add((V) "4");
			n.add((V) "8");
			return n;
		}
*/		
		return null;
	}
	
	public Set<V> gamma(Set<V> set) {
		if (set.isEmpty())
			return new TreeSet<V>();
		Iterator<V> iter = set.iterator();
		V elem = iter.next();
		Set<V> result = getNeighbours(elem);
		while (iter.hasNext())
			result.retainAll(getNeighbours(iter.next()));
		return result;
	}
	
	public boolean absorbs(Set<Set<V>> setOfBi, Set<V> bx) {
		Set<V> by = gamma(bx);
		for (Set<V> x : setOfBi) {
			Set<V> y = gamma(x);
			if (x.containsAll(bx) && y.containsAll(by))
				return true;
			if (x.containsAll(by) && y.containsAll(bx))
				return true;
		}
		return false;
	}
	
	public Set<Set<V>> consensus(Set<V> x1, Set<V> x2) {
		
		BiCompare comp = new BiCompare();
		Set<Set<V>> cons = new TreeSet<>(comp);
		
		Set<V> y1 = gamma(x1);
		Set<V> y2 = gamma(x2);
		
		Set<V> x;
		Set<V> y;
		
		// X1 cap X2
		x = new TreeSet<>(x1);
		x.retainAll(x2);
		if (!x.isEmpty()) {
			// Y1 cup Y2
			y = new TreeSet<>(y1);
			y.addAll(y2);
			cons.add(comp.compare(x, y) <= 0 ? x : y);
		}
		
		// X1 cap Y2
		x = new TreeSet<>(x1);
		x.retainAll(y2);
		if (!x.isEmpty()) {
			// Y1 cup X2
			y = new TreeSet<>(y1);
			y.addAll(x2);
			cons.add(comp.compare(x, y) <= 0 ? x : y);
		}
		
		// Y1 cap X2
		x = new TreeSet<>(y1);
		x.retainAll(x2);
		if (!x.isEmpty()) {
			// X1 cup Y2
			y = new TreeSet<>(x1);
			y.addAll(y2);
			cons.add(comp.compare(x, y) <= 0 ? x : y);
		}
		
		// Y1 cap Y2
		x = new TreeSet<>(y1);
		x.retainAll(y2);
		if (!x.isEmpty()) {
			// X1 cup X2
			y = new TreeSet<>(x1);
			y.addAll(x2);
			cons.add(comp.compare(x, y) <= 0 ? x : y);
		}
		
		return cons;
	}
	
}
