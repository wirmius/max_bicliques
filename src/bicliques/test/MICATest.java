/**
 * 
 */
package bicliques.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bicliques.algorithms.MICA;
import bicliques.algorithms.MaximalBicliquesAlgorithm;
import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;
import bicliques.graphs.Graph.Vertex;
import bicliques.graphs.GraphVendingMachine;

/**
 * Test class for MICA algorithm.
 * @author Roland Koppenberger
 * @version 1.1, September 15th 2019.
 */
public class MICATest {

	private class DummyThread extends Thread {
		public void run() {}
	}
	
	private Graph<String, Integer> graph;
	private MaximalBicliquesAlgorithm<String, Integer> mba;
	private Set<Biclique<String, Integer>> bicSet;
	
	private DummyThread dummy;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		graph = GraphVendingMachine.lemmeHaveAnEmptyGraph(false);
		mba = new MICA<String, Integer>();
		bicSet = new TreeSet<>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link bicliques.algorithms.MICA#findMaxBicliques(bicliques.graphs.Graph)}.
	 */
	@Test
	public final void testFindMaxBicliquesExample() {
		// set up graph
		graph.addEdge(1, "1", "2");
		graph.addEdge(2, "1", "4");
		graph.addEdge(3, "2", "3");
		graph.addEdge(4, "2", "4");
		graph.addEdge(5, "3", "4");
		graph.addEdge(6, "3", "5");
		graph.addEdge(7, "3", "7");
		graph.addEdge(8, "4", "5");
		graph.addEdge(9, "4", "7");
		graph.addEdge(10, "5", "6");
		graph.addEdge(11, "6", "7");
		
		// biclique ([2], [1, 3, 4])
		bicSet.add(new Biclique<>(graph, graph.getVertices().get("2")));

		// biclique ([3], [2, 4, 5, 7])
		bicSet.add(new Biclique<>(graph, graph.getVertices().get("3")));

		// biclique ([4], [1, 2, 3, 5, 7])
		bicSet.add(new Biclique<>(graph, graph.getVertices().get("4")));

		// biclique ([1, 3], [2, 4])
		Set<Vertex<String>> vset = new TreeSet<>();
		vset.add(graph.getVertices().get("1"));
		vset.add(graph.getVertices().get("3"));
		bicSet.add(new Biclique<>(graph, vset));

		// biclique ([3, 4], [2, 5, 7])
		vset.clear();
		vset.add(graph.getVertices().get("3"));
		vset.add(graph.getVertices().get("4"));
		bicSet.add(new Biclique<>(graph, vset));

		// biclique ([5, 7], [3, 4, 6])
		vset.clear();
		vset.add(graph.getVertices().get("5"));
		vset.add(graph.getVertices().get("7"));
		bicSet.add(new Biclique<>(graph, vset));
		
		dummy = new DummyThread();
		assertEquals("example failed", bicSet, mba.findMaxBicliques(graph, dummy));
	}

	/**
	 * Test method for {@link bicliques.algorithms.MICA#findMaxBicliques(bicliques.graphs.Graph)}.
	 */
	@Test
	public final void testFindMaxBicliquesEmptyGraph() {
		dummy = new DummyThread();
		assertEquals("empty graph provided", Collections.emptySet(), mba.findMaxBicliques(graph, dummy));
	}

	/**
	 * Test method for {@link bicliques.algorithms.MICA#findMaxBicliques(bicliques.graphs.Graph)}.
	 */
	@Test(expected=NullPointerException.class)
	public final void testFindMaxBicliquesNoGraph() {
		dummy = new DummyThread();
		bicSet = mba.findMaxBicliques(null, dummy);
	}

}
