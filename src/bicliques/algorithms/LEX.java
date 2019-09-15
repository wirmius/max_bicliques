package bicliques.algorithms;

import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LEX<V extends Comparable<? super V>, E extends Comparable<? super E>> implements MaximalBicliquesAlgorithm<V, E> {
    @Override
    public Set<Biclique<V, E>> findMaxBicliques(Graph<V, E> graph, Thread computation) {
        // get the initial set
        Set<Biclique<V, E>> initial_set = createInitialSet(graph);

		if (computation.isInterrupted())
			return null;
		
        // iterate over the set
        while (true) {
            // create a set for the bicliques in the next generation
            Set<Biclique<V, E>> intermediate1 = new TreeSet<>();
            Set<Biclique<V, E>> intermediate2 = new TreeSet<>();

    		if (computation.isInterrupted())
    			return null;
    		
            // go on to create all of consensus adjunctions
            for(Biclique<V, E> bq: initial_set) {
                // make a consensus adjunction with every other element of the set and dump
                // it all to the new set
                for(Biclique<V, E> target: initial_set) {
                    intermediate1.addAll(bq.extendedConsensus(target));
                    
            		if (computation.isInterrupted())
            			return null;            		
                }
            }

            // now go over the  set with the contains operation
            for (Biclique<V, E> bq : intermediate1) {
            	
        		if (computation.isInterrupted())
        			return null;
        		
                if(!bq.isAbsorbedOf(intermediate2)) {
                    // if it is not absorbed by anything in the new set,
                    // add it to the other set
                    intermediate2.add(bq);
                }
            }

            // now compare the set to the original one
            // if there is no difference, call it a day
            if(initial_set.size()==intermediate2.size()) { return intermediate2; }
            else { initial_set = intermediate2; }

        }
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

