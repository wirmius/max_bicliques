package bicliques.graphs;

import java.util.Map;
import java.util.Set;

public interface Graph<V extends Comparable<? super V>, E extends Comparable<? super E>> {

    interface Vertex<V> {
        V getElem();
        void setElem(V elem);
        Set<? extends Edge> getOutgoingEdges();
        Set<? extends Edge> getIncomingEdges();
    }

    interface Edge<E> {
        E getElem();
        void setElem(E elem);
        Vertex getStart();
        Vertex getEnd();
    }

    Map<V, ? extends Vertex<V>> getVertices();
    int getVertexCount();
    Map<E, ? extends Edge<E>> getEdges();
    int getEdgeCount();

    void addVertex(V elem);
    void addEdge(E edge, V v1, V v2);
    void addEdge(E edge, Vertex<V> v1, Vertex<V> v2);

}