package bicliques.graphs;

/**
 * MBEA Algorithm
 * @author Hadi Sanaei
 * @version 1.2
 * @date 15/06/2019
 */
 

import java.util.ArrayList;
import java.util.HashSet;
import javafx.util.Pair;



public class Mbea <V extends Comparable<? super V>, E extends Comparable<? super E>>  {
    private boolean found = false;
    private ArrayList<Biclique> maximalBiclique;
    private HashSet<Pair<Integer,Integer>> vertexSet = new HashSet<>();
	private boolean isMaximal;


    private void bicliqueFindimP(VertexSet mL, VertexSet mR, VertexSet mP, VertexSet mQ)
    {
        VertexSet L = new VertexSet(mL.getSetV());
        VertexSet R = new VertexSet(mR.getSetV());
        VertexSet P = new VertexSet(mP.getSetV());
        VertexSet Q = new VertexSet(mQ.getSetV());

        while (!P.isSetEmpty())
        {
            Vertex x = P.getVertex(0);
            VertexSet Rprime = new VertexSet(R.getSetV());
            Rprime.addVertex(x);

            VertexSet Lprime = new VertexSet();
            VertexSet twiceLprime = new VertexSet(L.getSetV());
            VertexSet C = new VertexSet();

            for(int j=0;j<L.getSize();j++)
            {
                Vertex u = L.getVertex(j);
                if(u.isNeighbour(x))
                {
                    Lprime.addVertex(u);
                    twiceLprime.removeVertex(u);
                }
            }

            C.addVertex(x);

            VertexSet Pprime = new VertexSet();
            VertexSet Qprime = new VertexSet();

            isMaximal = true;

            for(int j=0;j<Q.getSize();j++)
            {
                Vertex v = Q.getVertex(j);
                int numLprimeNeighbours = v.numberOfNeighboursOfVInSet(Lprime.getSetV());

                if(numLprimeNeighbours == Lprime.getSize())
                {
                    isMaximal = false;
                    break;
                }
                else if (numLprimeNeighbours > 0)
                {
                    Qprime.addVertex(v);
                }
            }

            if(isMaximal)
            {
                for(int j=0;j<P.getSize();j++)
                {
                    Vertex v = P.getVertex(j);
                    if(v.isEqual(x)) // doubt equals
                        continue;

                    int numLprimeNeighbours = v.numberOfNeighboursOfVInSet(Lprime.getSetV());
                    if(numLprimeNeighbours == Lprime.getSize()) {
                        Rprime.addVertex(v);
                        int numoverlineLprimeneighbours = v.numberOfNeighboursOfVInSet(twiceLprime.getSetV());
                        if(numoverlineLprimeneighbours == 0)
                            C.addVertex(v);
                    }
                    else if(numLprimeNeighbours > 0)
                        Pprime.addVertex(v);
                }

                int isPresent = 0;
                Biclique bcq = new Biclique(Lprime.getSetV(), Rprime.getSetV());
                bcq.isMaximal = true;
                for(Vertex v1:bcq.getLeftNodes())
                {
                    for(Vertex v2:bcq.getRightNodes())
                    {
                        Pair<Integer,Integer> pr = new Pair<>(v1.getLabel(),v2.getLabel());
                        if(vertexSet.contains(pr)){
                            isPresent++;
                        }

                        vertexSet.add(pr);
                    }

                }

                if(isPresent!=(bcq.getLeftNodes().size()*bcq.getRightNodes().size()))
                    maximalBiclique.add(bcq);

                if(!Pprime.isSetEmpty()){
                    bicliqueFindimP(Lprime,Rprime,Pprime,Qprime);
                    }
            }

            for(int j=0;j<C.getSize();j++)
            {
                Vertex v = C.getVertex(j);
                Q.addVertex(v);
                P.removeVertex(v);
            }
        }
    }

    
    int getNumBicliques()
    {
        if(found)
            return maximalBiclique.size();
        return 0;
    }

    String toStringMbea()
    {


        if(found)
        {
            String result = "";
            for(int i=0;i<maximalBiclique.size();i++)
            {
                Biclique b = maximalBiclique.get(i);
                result += b.toStringBiclique();
                result += "\n";
            }
            return result;
        }
        else
            return null;

    }
}
