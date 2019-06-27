package bicliques.graphs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class MuhBiclique<V extends Comparable<? super V>, E extends Comparable<? super E>>
        implements Comparable<MuhBiclique<V, E>> {

    private Graph<V, E> graph;

    private Set<Set<Graph.Vertex<V>>> elems;

    /**
     * Basic integrity check (not having more sets of vertices than we need.
     *
     */
    @Deprecated
    private void tegridyFarms() throws Exception {
        if (this.getElems().size()!=2) {
            throw new Exception("Something has died in the forest (multiclique detected");
        }
    }

    /**
     * The main intialization method.
     * @param graph my graph
     * @param vs1 the first set of bicliques
     * @param vs2 the second set of bicliques
     */
    public MuhBiclique(Graph<V, E> graph, Set<Graph.Vertex<V>> vs1, Set<Graph.Vertex<V>> vs2) {
        assert vs1.size()!=0;
        assert vs2.size()!=0;

        this.graph = graph;
        this.elems = new HashSet<>(2);
        this.elems.add(vs1);
        this.elems.add(vs2);
    }

    /**
     * A version of the initializer that takes just one vertex for v1 and v2.
     * @param graph
     * @param v1
     * @param v2
     */
    public MuhBiclique(Graph<V, E> graph, Graph.Vertex<V> v1, Graph.Vertex<V> v2) {
        this(graph, Collections.singleton(v1), Collections.singleton(v2));
    }


    @Override
    public int compareTo(MuhBiclique<V, E> veBiclique) {
        for (Set<Graph.Vertex<V>> side_here : this.getElems())
        {
            for (Set<Graph.Vertex<V>> side_there : veBiclique.getElems())
            {
                // compare the two sides, if not equal return the difference in size
                if(!side_here.containsAll(side_there)&&!side_there.containsAll(side_here))
                {
                    // just use the pointers because that is about as unique as this thing gets
                    return side_here.hashCode() - side_there.hashCode();
                } else { return 0; }
            }
        }
        // if we are here, it's an empty biclique, which cannot be because of the initialization checks
        return 0;
    }

    public Graph<V, E> getGraph() {
        return graph;
    }

    public void setGraph(Graph<V, E> graph) {
        this.graph = graph;
    }

    /**
     * Contains no more than 2 elems.
     */
    public Set<Set<Graph.Vertex<V>>> getElems() {
        return elems;
    }

    public void setElems(Set<Set<Graph.Vertex<V>>> elems) {
        this.elems = elems;
    }


    /**
     * Checks whether the biclique contains the specified biclique.
     * @param bq the biclique to be tested
     * @return true if it's contained within
     */
    public boolean absorbs(MuhBiclique<V, E> bq)
    {
        boolean output = true;
        for (Set<Graph.Vertex<V>> side_here : this.getElems())
        {
            boolean accum = false;
            for (Set<Graph.Vertex<V>> side_there : bq.getElems())
            {
                // check if the side contain side_there
                if(side_here.containsAll(side_there))
                {
                    // can we have the entire biclique inside one of the sides of the other bicliques?
                    // I think not, it would mess with the definition
                    // so if we see one true value here, we set the accumulator to true
                    accum = true;
                }
            }

            // now do a boolean and with the output accumulator
            // if one of the sides does not contain anything from either of the other sides,
            // then it's turned false
            output = output && accum;
        }
        return output;
    }

    /**
     * Returns a set of unique bicliques that are a result of consensus adjunction operation.
     * @param bq
     * @return set of bicliques
     */
    public Set<Biclique<V,E>> consensusAdjunction(MuhBiclique<V, E> bq) {
        Set<Biclique<V, E>> accum = new TreeSet<>();

        for (Set<Graph.Vertex<V>> side_here : this.getElems())
        {
            for (Set<Graph.Vertex<V>> side_there : bq.getElems())
            {
                // get the other sides
                //Set<Graph.Vertex<V>> oside_there = bq.getElems();
                // combine the two sides

            }
        }
        return null;
    }

}