package bicliques.graphs;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import bicliques.graphs.Graph.Edge;
import bicliques.graphs.Graph.Vertex;

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

    public class AdjacencyMapEdge<E extends Comparable<? super E>> implements Graph.Edge<E>, Comparable<AdjacencyMapEdge<E>> {
        private E elem;  // Weight, name, ...
        private AdjacencyMapVertex<V> u, v;

        protected AdjacencyMapEdge(AdjacencyMapVertex u, AdjacencyMapVertex v, E elem) {
            this.u = u;
            this.v = v;
            this.elem = elem;
        }

        public AdjacencyMapVertex<V> opposite(AdjacencyMapVertex v) {
            assert this.v == v || this.u == v;
            if (this.v == v) {
                return this.u;
            } else if (this.u == v) {
                return this.v;
            } else return null; // TODO: change to throw an error
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
//            return "{EDGE:" + elem.toString() + ";" + u.toString() + ":" + v.toString() + "}";
            return "(" + u.getElem().toString() + ", " + v.getElem().toString() + ")";
        }

        @Override
        public int compareTo(AdjacencyMapEdge<E> eAdjacencyMapEdge) {
            return this.getElem().compareTo(eAdjacencyMapEdge.getElem());
        }
    }

    public class AdjacencyMapVertex<V extends Comparable<? super V>> implements Graph.Vertex<V>, Comparable<AdjacencyMapVertex<V>> {
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
//            return "{VER:" + elem.toString() + ";" + ((Integer) incoming.size()).toString() + ":" + ((Integer) outgoing.size()).toString() + "}";
            return elem.toString();
        }

/*  	// original version
		@Override
		public Set<Vertex<V>> getNeighbours() {
			Set<AdjacencyMapEdge<E>> sin = this.getIncomingEdges();
			Set<AdjacencyMapEdge<E>> sout = this.getOutgoingEdges();

			Set<AdjacencyMapEdge<E>> retset = new TreeSet<>(sin);
			retset.addAll(sout);

			Set<Vertex<V>> vertret = new TreeSet<>();
			for (AdjacencyMapEdge<E> edge : retset) {
			    vertret.add((Vertex<V>)edge.opposite(this));
            }
			
			return vertret;
		}

*/
        
        // TODO - just a workaraound for not working getNeighbours() above        
        @Override
        public Set<Vertex<V>> getNeighbours() {
        	Set<Vertex<V>> set = new TreeSet<>();
    		for (Entry<E, AdjacencyMapEdge<E>> e : edges.entrySet()) {
    			Vertex<V> start = e.getValue().getStart();
    			Vertex<V> end = e.getValue().getEnd();
    			if (elem.equals(start.getElem()))
    				set.add(end);
    			else if (elem.equals(end.getElem()))
    				set.add(start);
    		}
        	return set;
        }

        @Override
        public int compareTo(AdjacencyMapVertex<V> vAdjacencyMapVertex) {
            return this.getElem().compareTo(vAdjacencyMapVertex.getElem());
        }

        protected void wireIncomingEdge(AdjacencyMapEdge<E> edge)
        {
        	this.incoming.add(edge);
        }

        protected void wireOutgoingEdge(AdjacencyMapEdge<E> edge)
        {
            this.outgoing.add(edge);
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
        assert this.vertices.get(v2) == null; // replace with throws

        this.addVertex(v1);
        this.addVertex(v2);

        AdjacencyMapEdge<E> e = new AdjacencyMapEdge(this.vertices.get(v1), this.vertices.get(v2), edge);

        // wire up the vertices
        this.vertices.get(v1).wireOutgoingEdge(e);
        this.vertices.get(v2).wireIncomingEdge(e);

        this.edges.put(edge, e);
    }

    @Override
    public void addEdge(E edge, Vertex<V> v1, Vertex<V> v2) {
        assert this.vertices.get(v1.getElem()) != null;
        assert this.vertices.get(v2.getElem()) != null;


        AdjacencyMapEdge<E> e = new AdjacencyMapEdge(this.vertices.get(v1), this.vertices.get(v2), edge);

        // wire up the vertices
        this.vertices.get(v1).wireOutgoingEdge(e);
        this.vertices.get(v2).wireIncomingEdge(e);

        this.edges.put(edge, e);
    }
    
}