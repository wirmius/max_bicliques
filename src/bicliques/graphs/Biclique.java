/**
 * 
 */
package bicliques.graphs;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import bicliques.graphs.Graph.Vertex;

/**
 * Class implements a complete bipartite graph (biclique).
 * @author Roland Koppenberger
 * @version 1.0, June 26th 2019.
 */
public class Biclique<V extends Comparable<? super V>, E extends Comparable<? super E>>
implements Comparable<Biclique<V, E>> {
	
	@Override
	public int compareTo(Biclique<V, E> other) {
		int cmp = compare(this.getLeft(), other.getLeft());
		if (cmp != 0)
			return cmp;
		return compare(this.getRight(), other.getRight());
	}

	/**
	 * The graph related to the biclique.
	 */
	private Graph<V, E> graph;
	
	/**
	 * First partition of vertices.<br>
	 * It always holds: {@code left <= right}
	 */	
	private Set<Vertex<V>> left;
	
	/**
	 * Second partition of vertices,
	 * computed on construction from first partition of vertices.<br>
	 * It always holds: {@code left <= right}
	 */
	private Set<Vertex<V>> right;
	
	/**
	 * Constructs a biclique on two disjoint sets of vertices.
	 * Tests if the two given sets are disjoint, otherwise an exception is thrown.
	 * @param graph Related graph.
	 * @param vertices One partition of vertices of the two.
	 */
	public Biclique(Graph<V, E> graph, Set<Vertex<V>> left, Set<Vertex<V>> right) {
		Set<Vertex<V>> intersection = new TreeSet<>(left);
		intersection.retainAll(right);
		if (!intersection.isEmpty())
			throw new IllegalArgumentException("Set of vertices are not disjoint.");
		this.left = new TreeSet<>(left);
		this.right = new TreeSet<>(right);
	}
	
	/**
	 * Constructs a maximal biclique on a generating set of vertices.<p>
	 * <b>Note:</b> {@code (gamma(gamma(X)), gamma(X))} with some subset {@code X}
	 * of vertices is a maximal biclique.
	 * @param graph Related graph.
	 * @param vertices Generating set of vertices.
	 */
	public Biclique(Graph<V, E> graph, Set<Vertex<V>> vertices) {
		Set<Vertex<V>> first = new TreeSet<>(gamma(vertices));
		Set<Vertex<V>> second = new TreeSet<>(gamma(first));
		if (compare(first, second) <= 0) {
			left = first;
			right = second;
		} else {
			left = second;
			right = first;
		}
	}
	
	/**
	 * Constructs a maximal biclique on a generating set of vertices
	 * containing only one vertex (maximal biclique generated from a star biclique).
	 * @param graph Related graph.
	 * @param vertex Only vertex in one partition of vertices. 
	 */
	public Biclique(Graph<V, E> graph, Graph.Vertex<V> vertex) {
		this(graph, Collections.singleton(vertex));
	}
	
	/**
	 * Gets graph.
	 * @return Related graph.
	 */
	public Graph<V, E> getGraph() {
		return graph;
	}
	
	/**
	 * Gets first (left) partition of vertices.
	 * @return First (left) partition of vertices.
	 */
	public Set<Vertex<V>> getLeft() {
		return left;
	}
	
	/**
	 * Gets second (right) partition of vertices.<br>
	 * @return Second (right) partition of vertices.
	 */
	public Set<Vertex<V>> getRight() {
		return right;
	}
	
	/**
	 * Computes the set of common neighbours some vertices.<br>
	 * <b>Note:</b> {@code (gamma(gamma(X)), gamma(X))} with some subset {@code X}
	 * of vertices is a maximal biclique. 
	 * @param vertices Set of vertices.
	 * @return Set of common neighbours of vertices.
	 */
	public Set<Vertex<V>> gamma(Set<Vertex<V>> vertices) {
		if (vertices.isEmpty())
			return Collections.emptySet();
		Iterator<Vertex<V>> iter = vertices.iterator();
		Vertex<V> vertex = iter.next();
		Set<Vertex<V>> result = vertex.getNeighbours();
		while (iter.hasNext())
			result.retainAll(iter.next().getNeighbours());
		return result;
	}
	
	/**
	 * Tests if biclique is absorbed by any biclique of a set of bicliques.<br>
	 * A biclique is absorbed by another byclique if both partitions of vertices
	 * are subsets of the partitions (swapped if applicable) of vertices of the other one. 
	 * @param setOfBicliques Set of bicliques to test absorption.
	 * @return True if biclique is absorbed, false otherwise.
	 */
	public boolean isAbsorbedOf(Set<Biclique<V, E>> setOfBicliques) {
		for (Biclique<V, E> bic : setOfBicliques) {
			if (bic.getLeft().containsAll(left) && bic.getRight().containsAll(right))
				return true;
			if (bic.getLeft().containsAll(right) && bic.getRight().containsAll(left))
				return true;
		}
		return false;
	}
	
	/**
	 * Computes the consensus of two bicliques.<p>
	 * From two bicliques {@code (X1, Y1)} and {@code (X2, Y2)} the consensus consists of
	 * the following bicliques (non empty intersection provided):
	 * <ul>
	 * <li>{@code (X1 intersection X2, Y1 union Y2)},
	 * <li>{@code (X1 intersection Y2, Y1 union X2)},
	 * <li>{@code (Y1 intersection X2, X1 union Y2)},
	 * <li>{@code (Y1 intersection Y2, X1 union X2)}.
	 * </ul>
	 * @param other The other biclique.
	 * @return Consensus set of (0 to 4) maximal bicliques.
	 */
 	public Set<Biclique<V, E>> consensus(Biclique<V, E> other) {
 
		Set<Biclique<V, E>> cons = new TreeSet<>();
		Set<Vertex<V>> x;
		
		// X1 cap X2
		x = new TreeSet<>(this.getLeft());
		x.retainAll(other.getLeft());
		if (!x.isEmpty())
			cons.add(new Biclique<V, E>(graph, x));
		
		// X1 cap Y2
		x = new TreeSet<>(this.getLeft());
		x.retainAll(other.getRight());
		if (!x.isEmpty())
			cons.add(new Biclique<V, E>(graph, x));
		
		// Y1 cap X2
		x = new TreeSet<>(this.getRight());
		x.retainAll(other.getLeft());
		if (!x.isEmpty())
			cons.add(new Biclique<V, E>(graph, x));
		
		// Y1 cap Y2
		x = new TreeSet<>(this.getRight());
		x.retainAll(other.getRight());
		if (!x.isEmpty())
			cons.add(new Biclique<V, E>(graph, x));
		
		return cons;
	}
	
	/**
	 * Extends biclique to a maximal biclique.
	 * (Could be done directly by creating a maximal biclique with a generation set.)
	 * @return Maximal biclique.
	 */
	public Biclique<V, E> extendToMaximal() {
		return new Biclique<>(graph, gamma(left));
	}
	
	@Override
	public String toString() {
		// TODO
		return "TODO";
	}
	
	/**
	 * Compares two sets X, Y of vertices.<p>
	 * It holds: {@code X < Y <=>}
	 * <ul>
	 * <li>{@code size(X) < size(Y)},
	 * <li>if size is equal, compare elementwise in ascending order,
	 * first difference is returned.
	 * </ul>
	 * @param vset0 First set of vertices.
	 * @param vset1 Second set of vertices.
	 * @return
	 */
	private int compare(Set<Vertex<V>> vset0, Set<Vertex<V>> vset1) {
		int cmp = vset0.size() - vset1.size();
		if (cmp != 0)
			return cmp;
		Iterator<Vertex<V>> iter1 = vset1.iterator();
		for (Vertex<V> v0 : vset0) {
			Vertex<V> v1 = iter1.next();
			cmp = v0.getElem().compareTo(v1.getElem());
			if (cmp != 0)
				return cmp;
		}
		return 0;
	}
	
}
