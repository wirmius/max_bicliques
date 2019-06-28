package bicliques.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import bicliques.graphs.*;

public class AdjacencyMapGraphTest {

    Graph<String, Integer> graph;

    @Before
    public void setUp() throws Exception {
        graph = GraphVendingMachine.lemmeHaveAnEmptyGraph(false);
        graph.addEdge(1, "1", "2");
        graph.addEdge(2, "1", "3");
        graph.addEdge(3, "2", "3");
        graph.addEdge(4, "3", "4");
        graph.addEdge(5, "4", "5");
        graph.addEdge(6, "5", "3");

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getEdges() {
    }

    @Test
    public void getVertices() {
    }

    @Test
    public void getVertexCount() {
    }

    @Test
    public void getEdgeCount() {
    }

    @Test
    public void addVertex() {
    }

    @Test
    public void addEdge() {
    }

    @Test
    public void addEdge1() {
    }

    @Test
    public void getNeighbors() {
        System.out.println(graph.getVertices().get("3").getNeighbours().toString());
        assertEquals(4 ,graph.getVertices().get("3").getNeighbours().size());
        System.out.println(graph.getVertices().get("1").getNeighbours().toString());
        assertEquals(2 ,graph.getVertices().get("1").getNeighbours().size());
        System.out.println(graph.getVertices().get("5").getNeighbours().toString());
        assertEquals(2 ,graph.getVertices().get("5").getNeighbours().size());
    }
}