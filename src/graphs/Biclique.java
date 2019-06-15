package graphs;

import java.util.Set;

/**
 * Bicliques
 *
 */
public class Biclique {
    boolean isMaximal;
	private List<Vertex> leftNodes;
	private List<Vertex> rightNodes;
    Biclique(List<Vertex> leftV, List<Vertex> rightV)
    {
        leftNodes = leftV;
        rightNodes = rightV;
      
        for(int i=0;i<leftNodes.size();i++)
        {

            for(int j=0;j<rightNodes.size();j++)
            {
                Vertex left = leftNodes.get(i);
                Vertex right = rightNodes.get(j);
                try
                {
                    Vertex.addEdge(left,right);
                }
                catch (RuntimeException e) {}
            }
        }

      }
    
    public List<Vertex> getLeftNodes() {
        return leftNodes;
    }

    public List<Vertex> getRightNodes() {
        return rightNodes;
    }

    public boolean isMaximal() {
        return isMaximal;
    }

    
}

