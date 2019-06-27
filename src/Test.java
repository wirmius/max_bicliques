import java.util.Map.Entry;

import bicliques.graphs.Graph;
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

		Vertex<String> v = null;

		for (Entry<String, ? extends Vertex<String>> entry : graph.getVertices().entrySet()) {			
			v = entry.getValue();
			System.out.println("v = " + v + ", neighbours: " + v.getNeighbours());
		}
		
	}

}