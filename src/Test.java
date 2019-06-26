import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import bicliques.algorithms.MICA;
import bicliques.graphs.Graph;
import bicliques.graphs.Graph.Vertex;
import bicliques.graphs.GraphVendingMachine;

/**
 * Just for testing purpose.
 * @author Roland Koppenberger
 */
public class Test {

	static Comparator<Set<String>> comp = new Comparator<Set<String>>() {

		@Override
		public int compare(Set<String> set0, Set<String> set1) {
			int cmp = set0.size() - set1.size();
			if (cmp != 0)
				return cmp;
			Iterator<String> iter1 = set1.iterator();
			for (String elem0 : set0) {
				String elem1 = iter1.next();
				cmp = elem0.compareTo(elem1);
				if (cmp != 0)
					return cmp;
			}
			return 0;
		}
		
	};

	public static void main(String[] args) {

		Graph<String, Integer> graph = GraphVendingMachine.lemmeHaveAnEmptyGraph(false);
		for (Entry<String, ? extends Vertex<String>> v : graph.getVertices().entrySet()) {
			Set<? extends Vertex<String>> neighbours = v.getValue().getNeighbours();
			for (Vertex<String> u : neighbours)
				u.getElem();
		}
		
		MICA<String, Integer> mba = new MICA<>();

		Set<String> vertices = new TreeSet<>();
		vertices.add("1");
		vertices.add("2");
		vertices.add("3");
		vertices.add("4");
		vertices.add("5");
		vertices.add("6");
		
		System.out.println("v: gamma(v), gamma^2(v)");
		System.out.println("-----------------------");
		for (String v : vertices)
			System.out.println(v + ": " + mba.getNeighbours(v) + ", " + mba.gamma(mba.getNeighbours(v)));
		System.out.println();

		// c0:
		// vertices -> set of star bicliques covering edges of graph
		// star bicliques are extended to maximal bicliques
		Set<Set<String>> c0 = new TreeSet<>(comp);
		for (String v : vertices) {
			Set<String> n = mba.getNeighbours(v);
			Set<String> g2 = mba.gamma(n);
			if (!mba.absorbs(c0, n))
				c0.add(comp.compare(n, g2) <= 0 ? n : g2);
		}
		System.out.println("c0 = " + c0);
		System.out.println();
		
		// c collects max. bicliques
		Set<Set<String>> c = new TreeSet<>(comp);
		c.addAll(c0);
		
		// w0 is actual working set (= new ones in last stage)
		Set<Set<String>> w0 = new TreeSet<>(comp);
		w0.addAll(c0);
		
		// n0 collects new max. bicliques in actual stage
		Set<Set<String>> n0;
		
		do {
			n0 = new TreeSet<>(comp);
			for (Set<String> elem : w0)
				for (Set<String> star : c0)
					if (!elem.equals(star)) {
						Set<Set<String>> cons = mba.consensus(elem, star);
						for (Set<String> con : cons) 
							if (!mba.absorbs(c, con) && !mba.absorbs(n0, con)) {
								Set<String> x = mba.gamma(con);
								Set<String> y = mba.gamma(x);
								n0.add(comp.compare(x, y) <= 0 ? x : y);
							}
					}
			w0 = new TreeSet<>(comp);
			w0.addAll(n0);
			c.addAll(n0);
			
		} while (!n0.isEmpty());
		
		System.out.println("c  = " + c);
	}

}
