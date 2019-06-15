package graphs;

import java.util.ArrayList;
import java.util.List;

class Vertex{
    private int label;
    private List<Vertex> neighbours  = new ArrayList<>();
    
    Vertex(int label)
    {
        this.label = label;
    }

    List<Vertex> getNeighbours()
    {
        return neighbours;
    }

    int getLabel()
    {
        return label;
    }

    private void addNeighbour(Vertex v)
    {
        if(neighbours.contains(v))
            throw new RuntimeException("Vertex::add_neighbour - vertex is already a neighbour.");
        neighbours.add(v);
    }

    public void removeNeighbour(Vertex v)
    {
        if(neighbours.contains(v))
            neighbours.remove(v);

    }

    static void addEdge(Vertex v1, Vertex v2)
    {
    
    
       .......
       
       
    }

    void removeEdge(Vertex v1, Vertex v2)
    {
            v1.removeNeighbour(v2);
            v2.removeNeighbour(v1);
    }


    int getNeighboursSize()
    {
        return neighbours.size();
    }

   
    boolean isNeighbour(Vertex otherV)
    {
        return neighbours.contains(otherV);
    }

    boolean isEqual(Vertex otherV)
    {
        return this.label == otherV.label;
    }

    boolean isMember(List<Vertex> set)
    {
        boolean out = false;
        for(Vertex v:set)
        {
            if(this.equals(v))
                out = true;
        }
        return out;
    }

    public int compareTo(Vertex other)
    {
        return this.getNeighboursSize() - other.getNeighboursSize();
    }

}
