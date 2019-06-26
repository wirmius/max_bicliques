package bicliques.algorithms;


/**
 * MBEA Algorithm
 * @author Hadi Sanaei
 * @version 1.2
 * @date 15/06/2019
 */
 

import java.util.ArrayList;
import bicliques.graphs.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import bicliques.graphs.Biclique;
import bicliques.graphs.VertexSet;



public class Mbea <V extends Comparable<? super V>, E extends Comparable<? super E>>  {
    private boolean found = false;
    private ArrayList<Biclique> maxiBiclique;
    private HashSet<Map<Integer,Integer>> vertexSet = new HashSet<>();
	private boolean isMaximal;
	

    private void bicliqueFind(VertexSet mL, VertexSet mR, VertexSet mP, VertexSet mQ)
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
                int NumLeftPrimeNeigh = v.numberOfNeighboursOfVInSet(Lprime.getSetV());

                if(NumLeftPrimeNeigh == Lprime.getSize())
                {
                    isMaximal = false;
                    break;
                }
                else if (NumLeftPrimeNeigh > 0)
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

                    int NumLeftPrimeNeigh = v.numberOfNeighboursOfVInSet(Lprime.getSetV());
                    if(NumLeftPrimeNeigh == Lprime.getSize()) {
                        Rprime.addVertex(v);
                        int NumOverlineLeftprimeNeigh = v.numberOfNeighboursOfVInSet(twiceLprime.getSetV());
                        if(NumOverlineLeftprimeNeigh == 0)
                            C.addVertex(v);
                    }
                    else if(NumLeftPrimeNeigh > 0)
                        Pprime.addVertex(v);
                }

                int isPresent = 0;
                Biclique bcq = new Biclique(Lprime.getSetV(), Rprime.getSetV());
                bcq.isMaximal = true;
                for(Vertex v1:bcq.getLeftNodes())
                {
                    for(Vertex v2:bcq.getRightNodes())
                    {
                        Map<Integer,Integer> pr = new HashMap<>(v1.getLabel(),v2.getLabel());
                        if(vertexSet.contains(pr)){
                            isPresent++;
                        }

                        vertexSet.add(pr);
                    }

                }

                if(isPresent!=(bcq.getLeftNodes().size()*bcq.getRightNodes().size()))
                    maxiBiclique.add(bcq);

                if(!Pprime.isSetEmpty()){
                    bicliqueFind(Lprime,Rprime,Pprime,Qprime);
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
            return maxiBiclique.size();
        return 0;
    }

 }
