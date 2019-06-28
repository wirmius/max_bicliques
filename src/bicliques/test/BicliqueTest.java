/**
 * 
 */
package bicliques.test;

import static org.junit.Assert.*;

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
 * Test class for class Biclique.
 * @author Roland Koppenberger
 * @version 1.0, June 28th 2019.
 */
public class BicliqueTest {

	private static Graph<String, Integer> graph;
	private MaximalBicliquesAlgorithm<String, Integer> mba;
	private static Set<Biclique<String, Integer>> bicSet;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// set up graph
		graph = GraphVendingMachine.lemmeHaveAnEmptyGraph(false);
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
		bicSet = new TreeSet<>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#compareTo(bicliques.graphs.Biclique)}.
	 */
	@Test
	public final void testCompareTo() {
		// biclique ([1, 3], [2, 4]) generated from vertex 1
		Biclique<String, Integer> bic13_24 = new Biclique<>(graph, graph.getVertices().get("1"));
				
		// biclique ([4], [5]) created from vertices 4 and 5
		Biclique<String, Integer> bic4_5 =
				new Biclique<>(graph, graph.getVertices().get("4"), graph.getVertices().get("5"));
		
		// biclique ([4], [6]) created from vertices 4 and 6
		Biclique<String, Integer> bic4_6 =
				new Biclique<>(graph, graph.getVertices().get("4"), graph.getVertices().get("6"));
		
		// biclique ([3], [2, 4, 5, 7]) generated from vertex 3
		Biclique<String, Integer> bic3_2457 = new Biclique<>(graph, graph.getVertices().get("3"));
		
		// tests
		assertTrue("([4],[5]) < ([4],[6])", bic4_5.compareTo(bic4_6) < 0);
		assertTrue("([3],[2,4,5,7]) < ([4],[5])", bic3_2457.compareTo(bic4_5) < 0);
		assertTrue("([3],[2,4,5,7]) < ([1,3],[2,4])", bic3_2457.compareTo(bic13_24) < 0);
}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#Biclique(bicliques.graphs.Graph, java.util.Set, java.util.Set)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testBicliqueGraphSetOfVertexSetOfVertexNotDisjoint() {
		// vertex set [4, 5]
		Set<Vertex<String>> vset1 = new TreeSet<>();
		vset1.add(graph.getVertices().get("4"));
		vset1.add(graph.getVertices().get("5"));
		
		// vertex set [4, 6]
		Set<Vertex<String>> vset2 = new TreeSet<>();
		vset2.add(graph.getVertices().get("4"));
		vset2.add(graph.getVertices().get("6"));

		// no biclique is created from sets [4, 5] and [4, 6] but
		// IllegalArgumentException is thrown because of non disjoint sets
		Biclique<String, Integer> bic = new Biclique<>(graph, vset1, vset2);
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#Biclique(bicliques.graphs.Graph, java.util.Set, java.util.Set)}.
	 */
	@Test
	public final void testBicliqueGraphSetOfVertexSetOfVertex() {
		// vertex set [1, 3]
		Set<Vertex<String>> vset1 = new TreeSet<>();
		vset1.add(graph.getVertices().get("1"));
		vset1.add(graph.getVertices().get("3"));
		
		// vertex set [2, 4]
		Set<Vertex<String>> vset2 = new TreeSet<>();
		vset2.add(graph.getVertices().get("2"));
		vset2.add(graph.getVertices().get("4"));

		// biclique ([1, 3], [2, 4]) created from sets [1, 3] and [2, 4]
		Biclique<String, Integer> bic1 = new Biclique<>(graph, vset1, vset2);
		
		// biclique ([1, 3], [2, 4]) created from sets [2, 4] and [1, 3]
		Biclique<String, Integer> bic2 = new Biclique<>(graph, vset2, vset1);
		
		// test
		assertEquals("swapped vertex sets", bic1, bic2);
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#Biclique(bicliques.graphs.Graph, java.util.Set)}.
	 */
	@Test
	public final void testBicliqueGraphSetOfVertex() {		
		// biclique ([1, 3], [2, 4]) generated from vertex set [1, 3]
		Set<Vertex<String>> vset = new TreeSet<>();
		vset.add(graph.getVertices().get("1"));
		vset.add(graph.getVertices().get("3"));
		Biclique<String, Integer> bic1 = new Biclique<>(graph, vset);
		
		// biclique ([1, 3], [2, 4]) generated from vertex set [2, 4]
		vset.clear();
		vset.add(graph.getVertices().get("2"));
		vset.add(graph.getVertices().get("4"));
		Biclique<String, Integer> bic2 = new Biclique<>(graph, vset);
		
		// test
		assertEquals("swapped vertex sets", bic1, bic2);
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#Biclique(bicliques.graphs.Graph, bicliques.graphs.Graph.Vertex)}.
	 */
	@Test
	public final void testBicliqueGraphVertex() {
		// biclique ([1, 3], [2, 4]) generated from vertex set [1, 3]
		Set<Vertex<String>> vset = new TreeSet<>();
		vset.add(graph.getVertices().get("1"));
		vset.add(graph.getVertices().get("3"));
		Biclique<String, Integer> bic1 = new Biclique<>(graph, vset);
		
		// biclique ([1, 3], [2, 4]) generated from vertex 1
		Biclique<String, Integer> bic2 = new Biclique<>(graph, graph.getVertices().get("1"));
		
		// test
		assertEquals(bic1, bic2);
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#Biclique(bicliques.graphs.Graph, bicliques.graphs.Graph.Vertex, bicliques.graphs.Graph.Vertex)}.
	 */
	@Test
	public final void testBicliqueGraphVertexVertex() {
		// biclique ([1], [2]) generated from vertices 1 and 2
		Biclique<String, Integer> bic1 =
			new Biclique<>(graph, graph.getVertices().get("1"), graph.getVertices().get("2"));
		
		// biclique ([1], [2]) generated from vertices 2 and 1
		Biclique<String, Integer> bic2 =
			new Biclique<>(graph, graph.getVertices().get("2"), graph.getVertices().get("1"));
		
		// test
		assertEquals("swapped vertex sets", bic1, bic2);		
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#getGraph()}.
	 */
	@Test
	public final void testGetGraph() {
		Biclique<String, Integer> bic = new Biclique<>(graph, graph.getVertices().get("1"));
		
		// test would fail with assertEquals since equals is not implemented in graph
		assertTrue(graph == bic.getGraph());
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#getLeft()}.
	 */
	@Test
	public final void testGetLeft() {
		// vertex set [1, 3]
		Set<Vertex<String>> left = new TreeSet<>();
		left.add(graph.getVertices().get("1"));
		left.add(graph.getVertices().get("3"));
		
		// vertex set [2, 4]
		Set<Vertex<String>> right = new TreeSet<>();
		right.add(graph.getVertices().get("2"));
		right.add(graph.getVertices().get("4"));

		// two different constructors have to be tested because sets are constructed in both
		// biclique ([1, 3], [2, 4]) created from sets [1, 3] and [2, 4]
		Biclique<String, Integer> bic1 = new Biclique<>(graph, left, right);
		
		// biclique ([1, 3], [2, 4]) generated from set [1, 3]
		Biclique<String, Integer> bic2 = new Biclique<>(graph, left);
		
		// tests
		assertEquals("test with constructing constructor", left, bic1.getLeft());
		assertEquals("test with generating constructor", left, bic2.getLeft());
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#getRight()}.
	 */
	@Test
	public final void testGetRight() {
		// vertex set [1, 3]
		Set<Vertex<String>> left = new TreeSet<>();
		left.add(graph.getVertices().get("1"));
		left.add(graph.getVertices().get("3"));
		
		// vertex set [2, 4]
		Set<Vertex<String>> right = new TreeSet<>();
		right.add(graph.getVertices().get("2"));
		right.add(graph.getVertices().get("4"));

		// two different constructors have to be tested because sets are constructed in both
		
		// biclique ([1, 3], [2, 4]) created from sets [1, 3] and [2, 4]
		Biclique<String, Integer> bic1 = new Biclique<>(graph, left, right);
		
		// biclique ([1, 3], [2, 4]) generated from set [1, 3]
		Biclique<String, Integer> bic2 = new Biclique<>(graph, left);
		
		// tests
		assertEquals("test with constructing constructor", right, bic1.getRight());
		assertEquals("test with generating constructor", right, bic2.getRight());
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#gamma(java.util.Set)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testGammaVertex() {
		// vertex set [2, 6]
		Set<Vertex<String>> vset = new TreeSet<>();
		vset.add(graph.getVertices().get("2"));
		vset.add(graph.getVertices().get("6"));
		
		// just for access to gamma
		Biclique<String, Integer> bic = new Biclique<>(graph, vset);
		
		// test
		fail("Not yet implemented"); // TODO		
		//assertEquals("", vset, bic.gamma((Set<Vertex<String>>) graph.getVertices().get("1")));		
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#gamma(java.util.Set)}.
	 */
	@Test
	public final void testGammaVertices() {
		// vertex set [2, 6]
		Set<Vertex<String>> vset1 = new TreeSet<>();
		vset1.add(graph.getVertices().get("2"));
		vset1.add(graph.getVertices().get("6"));
		
		// vertex set [1, 3, 4, 5]
		Set<Vertex<String>> vset2 = new TreeSet<>();
		vset2.add(graph.getVertices().get("1"));
		vset2.add(graph.getVertices().get("3"));
		vset2.add(graph.getVertices().get("4"));
		vset2.add(graph.getVertices().get("5"));
		
		// just for access to gamma
		Biclique<String, Integer> bic = new Biclique<>(graph, vset1);
		
		// test
		fail("Not yet implemented"); // TODO
		//assertEquals("", vset2, bic.gamma(vset1));
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#isAbsorbedOf(java.util.Set)}.
	 */
	@Test
	public final void testIsAbsorbedOfTrue() {
		// biclique ([1, 3], [2, 4]) generated from vertex 1
		Biclique<String, Integer> bic13_24 = new Biclique<>(graph, graph.getVertices().get("1"));
				
		// biclique ([1], [2]) created from vertices 1 and 2
		Biclique<String, Integer> bic1_2 =
				new Biclique<>(graph, graph.getVertices().get("1"), graph.getVertices().get("2"));
		
		// set of bicliques
		Set<Biclique<String, Integer>> setOfBicliques = new TreeSet<>();
		setOfBicliques.add(bic13_24);
		
		assertTrue(bic1_2.isAbsorbedOf(setOfBicliques));
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#isAbsorbedOf(java.util.Set)}.
	 */
	@Test
	public final void testIsAbsorbedOfTrueSwapped() {
		// biclique ([1, 3], [2, 4]) generated from vertex 1
		Biclique<String, Integer> bic13_24 = new Biclique<>(graph, graph.getVertices().get("1"));
				
		// biclique ([2], [3]) created from vertices 2 and 3
		Biclique<String, Integer> bic2_3 =
				new Biclique<>(graph, graph.getVertices().get("2"), graph.getVertices().get("3"));
		
		// set of bicliques
		Set<Biclique<String, Integer>> setOfBicliques = new TreeSet<>();
		setOfBicliques.add(bic13_24);
		
		assertTrue(bic2_3.isAbsorbedOf(setOfBicliques));
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#isAbsorbedOf(java.util.Set)}.
	 */
	@Test
	public final void testIsAbsorbedOfFalse() {
		// biclique ([3], [2, 4, 5, 7]) generated from vertex 3
		Biclique<String, Integer> bic3_2457 = new Biclique<>(graph, graph.getVertices().get("3"));
		
		// biclique ([5, 7], [3, 4, 6]) generated from vertex 5
		Biclique<String, Integer> bic57_346 = new Biclique<>(graph, graph.getVertices().get("5"));
		
		// set of bicliques
		Set<Biclique<String, Integer>> setOfBicliques = new TreeSet<>();
		setOfBicliques.add(bic3_2457);
		
		assertFalse(bic57_346.isAbsorbedOf(setOfBicliques));
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#consensus(bicliques.graphs.Biclique)}.
	 */
	@Test
	public final void testConsensus() {
		// vertex set [3, 4]
		Set<Vertex<String>> left = new TreeSet<>();
		left.add(graph.getVertices().get("3"));
		left.add(graph.getVertices().get("4"));
		
		// vertex set [2, 5, 7]
		Set<Vertex<String>> right = new TreeSet<>();
		right.add(graph.getVertices().get("2"));
		right.add(graph.getVertices().get("5"));
		right.add(graph.getVertices().get("7"));
		
		// biclique ([3, 4], [2, 5, 7]) created from sets [3, 4] and [2, 5, 7]
		Biclique<String, Integer> bic34_257 = new Biclique<>(graph, left, right);
		
		// biclique ([2], [1, 3, 4]) generated from vertex 2
		Biclique<String, Integer> bic2_134 = new Biclique<>(graph, graph.getVertices().get("2"));
		
		// biclique ([5, 7], [3, 4, 6]) generated from vertex 5
		Biclique<String, Integer> bic57_346 = new Biclique<>(graph, graph.getVertices().get("5"));
		
		// consensus
		Set<Biclique<String, Integer>> cons = new TreeSet<>();
		cons.add(bic34_257);
		
		assertEquals(cons, bic2_134.consensus(bic57_346));
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#extendToMaximalFromLeft()}.
	 */
	@Test
	public final void testExtendToMaximalFromLeft() {
		// biclique ([2], [1, 3, 4]) generated from vertex 2
		Biclique<String, Integer> bic2_134 = new Biclique<>(graph, graph.getVertices().get("2"));
		
		// biclique ([2], [3]) created from vertices 2 and 3
		Biclique<String, Integer> bic2_3 =
				new Biclique<>(graph, graph.getVertices().get("2"), graph.getVertices().get("3"));
		
		assertEquals(bic2_134, bic2_3.extendToMaximalFromLeft());
	}

	/**
	 * Test method for {@link bicliques.graphs.Biclique#extendToMaximalFromRight()}.
	 */
	@Test
	public final void testExtendToMaximalFromRight() {
		// biclique ([3], [2, 4, 5, 7]) generated from vertex 3
		Biclique<String, Integer> bic3_2457 = new Biclique<>(graph, graph.getVertices().get("3"));
		
		// biclique ([2], [3]) created from vertices 2 and 3
		Biclique<String, Integer> bic2_3 =
				new Biclique<>(graph, graph.getVertices().get("2"), graph.getVertices().get("3"));
		
		assertEquals(bic3_2457, bic2_3.extendToMaximalFromRight());
	}

}
