package graphs;

public class GraphVendingMachine {
    static <V extends Comparable<? super V>, E extends Comparable<? super E>> Graph<V, E> lemmeHaveAnEmptyGraph(boolean directed) {
        return new AdjacencyMapGraph<V, E>(directed);
    }
}
