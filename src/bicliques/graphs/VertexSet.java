package bicliques.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class VertexSet extends Vertex{
    private List<Vertex> setV ;

    public VertexSet()
    {
        setV =  new ArrayList<>();
    }

    public VertexSet(List<Vertex> nodesIn)
    {
        List<Vertex> newlist = new ArrayList<>();
        newlist.addAll(nodesIn);
        setV = newlist;
    }

    public List<Vertex> getSetV()
    {
        return setV;
    }
    public int getSize()
    {
        return setV.size();
    }
    public Vertex getVertex(int i)
    {
        return setV.get(i);
    }

    public void addVertex(Vertex v)
    {
        if(!setV.contains(v))
            setV.add(v);
    }

    public void removeVertex(Vertex v)
    {
            setV.remove(v);
    }

    List<Vertex> sortByNumOfNeighbours()
    {
        Collections.sort(setV, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) {
                return o1.getNeighboursSize() - o2.getNeighboursSize();
            }
        });

        return setV;
    }

    boolean isEqual(VertexSet other)
    {
        return setV.equals(other.setV);
    }

    public boolean isSetEmpty()
    {
        return setV.isEmpty();
    }

    String toStringVertexSet()
    {
        String res = "";
        for (Vertex v:setV)
            res += Integer.toString(v.getLabel())+ " ";
        return res;
    }
}
