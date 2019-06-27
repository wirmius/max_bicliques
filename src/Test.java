import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import bicliques.graphs.Graph;
import bicliques.graphs.Graph.Edge;
import bicliques.graphs.Graph.Vertex;
import bicliques.graphs.GraphVendingMachine;

/**
 * 
 */

/**
 * Just for temporarily testing purpose.
 * @author Roland Koppenberger
 */
public class Test {

	public static void main(String[] args) {
		
		Graph<String, Integer> graph = GraphVendingMachine.lemmeHaveAnEmptyGraph(false);
		graph.addEdge(1, "1", "2");
		graph.addEdge(2, "1", "3");
		graph.addEdge(3, "1", "4");
		graph.addEdge(4, "2", "3");

		Vertex<String> v = null;
		Set<Vertex<String>> set = null;
		
		for (Entry<Integer, ? extends Edge<Integer>> e : graph.getEdges().entrySet()) {
			System.out.println(e);
		}
		System.out.println();
		
		for (Entry<String, ? extends Vertex<String>> entry : graph.getVertices().entrySet()) {			
			set = new TreeSet<>();
			v = entry.getValue();
			System.out.print("Neighbours of vertex " + v + ": ");
			for (Entry<Integer, ? extends Edge<Integer>> e : graph.getEdges().entrySet()) {
				Vertex<String> a = e.getValue().getStart();
				Vertex<String> b = e.getValue().getEnd();
				if (v.getElem().equals(a.getElem()))
					set.add(b);
				else if (v.getElem().equals(b.getElem()))
					set.add(a);
			}
			System.out.println(set);
		}		

		for (Entry<String, ? extends Vertex<String>> entry : graph.getVertices().entrySet()) {			
			System.out.println(entry.getValue().getNeighbours());
		}

	}

}
