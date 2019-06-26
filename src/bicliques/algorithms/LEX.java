package bicliques.algorithms;

import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LEX<V extends Comparable<? super V>, E extends Comparable<? super E>> implements MaximalBicliquesAlgorithm<V, E> {
    @Override
    public Set<Biclique<V, E>> findMaxBicliques(Graph<V, E> graph) {

        return null;
    }

    public Set<Biclique<V, E>> createInitialSet(Graph<V, E> graph) {
        // get the edge set of the graph
        Map<E, Graph.Edge<E>> edges = (Map<E, Graph.Edge<E>>) graph.getEdges();

        // construct a biclique around every edge
        Set<Biclique<V, E>> retset = new TreeSet<>();
        for(Map.Entry<E, Graph.Edge<E>> entry : edges.entrySet()) {
            Graph.Edge<E> edge = entry.getValue();
            // get the two sides and initialize the biiclique
            Biclique<V, E> bc = new Biclique<>(graph, edge.getStart(), edge.getEnd());
            retset.add(bc);
        }

        // return the result
        return retset;
    }
}

