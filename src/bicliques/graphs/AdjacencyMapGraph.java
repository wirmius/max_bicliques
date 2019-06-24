package bicliques.graphs;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Adapted from the Adjacency Graph by Alexander Baumgartner.
 * Adjacency Graph from the slides. Essentially everything is copypasted from the slides with minor modifications.
 *
 * @param <V> payload for the vertices
 * @param <E> payload for the edges
 */
public class AdjacencyMapGraph<V extends Comparable<? super V>, E extends Comparable<? super E>> implements Graph<V, E> {
    private boolean directed;
    private Map<V, AdjacencyMapVertex<V>> vertices;
    private Map<E, AdjacencyMapEdge<E>> edges;

    /**
     * Initializer.
     * @param directed whether the graph is directed
     */
    protected AdjacencyMapGraph(boolean directed) {
        this.directed = directed;
        this.vertices = new TreeMap<>();
        this.edges = new TreeMap<>();

    }

    public class AdjacencyMapEdge<E> implements Graph.Edge<E> {
        private E elem;  // Weight, name, ...
        private AdjacencyMapVertex<V> u, v;

        protected AdjacencyMapEdge(AdjacencyMapVertex u, AdjacencyMapVertex v, E elem) {
            this.u = u;
            this.v = v;
            this.elem = elem;
        }

        public AdjacencyMapVertex opposite(AdjacencyMapVertex v) {
            assert this.v == v || this.u == v;
            if (this.v == v) {
                return this.u;
            } else if (this.u == v) {
                return this.v;
            } else return null;
        }

        @Override
        public E getElem() {
            return elem;
        }

        @Override
        public void setElem(E elem) {
            this.elem = elem;
        }

        @Override
        public Vertex getStart() {
            return u;
        }

        @Override
        public Vertex getEnd() {
            return v;
        }

        @Override
        public String toString() {
            return "{EDGE:" + elem.toString() + ";" + u.toString() + ":" + v.toString() + "}\n";
        }
    }

    public class AdjacencyMapVertex<V> implements Graph.Vertex<V> {
        private V elem;
        private Set<AdjacencyMapEdge<E>> outgoing, incoming;

        protected AdjacencyMapVertex(V elem) {
            outgoing = new TreeSet<>();
            if (AdjacencyMapGraph.this.directed) incoming = new TreeSet<>();
            else incoming = outgoing;
            this.elem = elem;
        }

        @Override
        public Set<AdjacencyMapEdge<E>> getOutgoingEdges() {
            return outgoing;
        }

        @Override
        public Set<AdjacencyMapEdge<E>> getIncomingEdges() {
            return incoming;
        }

        @Override
        public V getElem() {
            return elem;
        }

        @Override
        public void setElem(V elem) {
            this.elem = elem;
        }

        @Override
        public String toString() {
            return "{VER:" + elem.toString() + ";" + ((Integer) incoming.size()).toString() + ":" + ((Integer) outgoing.size()).toString() + "}\n";
        }
    }

    @Override
    public Map<E, AdjacencyMapEdge<E>> getEdges() {
        return this.edges;
    }

    @Override
    public Map<V, AdjacencyMapVertex<V>> getVertices() {
        return this.vertices;
    }


    @Override
    public int getVertexCount() {
        return this.vertices.size();
    }

    @Override
    public int getEdgeCount() {
        return this.edges.size();
    }

    @Override
    public void addVertex(V elem) {
        assert this.vertices.get(elem) == null;
        AdjacencyMapVertex<V> v = new AdjacencyMapVertex(elem);
        this.vertices.put(elem, v);
    }

    @Override
    public void addEdge(E edge, V v1, V v2) {
        assert this.edges.get(edge) == null;
        assert this.vertices.get(v1) == null;
        assert this.vertices.get(v2) == null;

        this.addVertex(v1);
        this.addVertex(v2);

        AdjacencyMapEdge<E> e = new AdjacencyMapEdge(this.vertices.get(v1), this.vertices.get(v2), edge);
        this.edges.put(edge, e);
    }

    @Override
    public void addEdge(E edge, Vertex<V> v1, Vertex<V> v2) {
        assert this.vertices.get(v1.getElem()) != null;
        assert this.vertices.get(v2.getElem()) != null;

        AdjacencyMapEdge<E> e = new AdjacencyMapEdge(this.vertices.get(v1), this.vertices.get(v2), edge);
        this.edges.put(edge, e);
    }
}