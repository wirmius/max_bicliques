package graphs;

import java.util.*;


/**
 * Adapted from the Adjacency Graph by Alexander Baumgartner.
 * Adjacency Graph from the slides. Essentially everything is copypasted from the slides with minor modifications.
 *
 * @param <V>
 * @param <E>
 */
public class AdjacencyMapGraph<V, E> {
    private boolean directed;
    private List<Vertex> vertices = new LinkedList<>();
    private List<Edge> edges = new LinkedList<>();

    AdjacencyMapGraph(boolean directed) {
        this.directed = directed;
    }

    public class Edge {
        private E elem;  // Weight, name, ...
        private Vertex u, v;

        public Edge(Vertex u, Vertex v, E elem) {
            this.u = u;
            this.v = v;
            this.elem = elem;
        }

        public Vertex opposite(Vertex v) {
            assert this.v == v || this.u == v;
            if (this.v == v) {
                return this.u;
            } else if (this.u == v) {
                return this.v;
            } else return null;
        }

        public E getElem() {
            return elem;
        }

        public Vertex getStart() {
            return u;
        }

        public Vertex getEnd() {
            return v;
        }

        @Override
        public String toString() {
            return "{EDGE:" + elem.toString() + ";" + u.toString() + ":" + v.toString() + "}\n";
        }
    }

    public class Vertex {
        private V elem;
        private Map<Vertex, Edge> outgoing, incoming;

        public Vertex(V elem) {
            outgoing = new HashMap<>();
            if (AdjacencyMapGraph.this.directed) incoming = new HashMap<>();
            else incoming = outgoing;
            this.elem = elem;
        }

        public Map<Vertex, Edge> getOutgoing() {
            return outgoing;
        }

        public Map<Vertex, Edge> getIncoming() {
            return incoming;
        }

        public V getElem() {
            return elem;
        }

        public void setElem(V elem) {
            this.elem = elem;
        }

        @Override
        public String toString() {
            return "{VER:" + elem.toString() + ";" + ((Integer) incoming.size()).toString() + ":" + ((Integer) outgoing.size()).toString() + "}\n";
        }
    }

    public Edge getEdge(Vertex u, Vertex v) {
        return u.getOutgoing().get(v);
    }

    public Vertex insertVertex(V elem) {
        Vertex v = new Vertex(elem);
        vertices.add(v);
        return v;
    }

    public List<Vertex> vertices() {
        return vertices;
    }

    public List<Edge> edges() {
        return edges;
    }

    public Edge insertEdge(Vertex u, Vertex v, E elem) {
        assert getEdge(u, v) == null;//Already exists
        Edge e = new Edge(u, v, elem);
        edges.add(e);
        u.getOutgoing().put(v, e);
        v.getIncoming().put(u, e);
        return e;
    }

    public void depthFirst(Vertex v, Set<Vertex> known, Map<Vertex, Edge> forest) {
        known.add(v);
        for (Edge e : v.getOutgoing().values()) {
            Vertex u = e.opposite(v);
            if (!known.contains(u)) {
                forest.put(u, e);
                depthFirst(u, known, forest);
            }
        }
    }

    public void breadthFirst(Vertex v, Set<Vertex> known, Map<Vertex, Edge> forest) {
        Queue<Vertex> q = new LinkedList<>();
        known.add(v);
        q.add(v);
        while (!q.isEmpty()) {
            v = q.poll();
            for (Edge e : v.getOutgoing().values()) {
                Vertex u = e.opposite(v);
                if (!known.contains(u)) {
                    forest.put(u, e);
                    known.add(u);
                    q.add(u);
                }
            }
        }
    }

    public List<Edge> constructPath(Vertex u, Vertex v, Map<Vertex, Edge> forest) {
        LinkedList<Edge> path = new LinkedList<>();
        if (forest.get(v) != null) {
            while (v != u) {
                Edge edge = forest.get(v);
                path.addFirst(edge);
                v = edge.opposite(v);
            }
        }
        return path;
    }
}