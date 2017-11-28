/******************************************************************************
 *  Compilation:  javac AcyclicSP.java
 *  Execution:    java AcyclicSP V E
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java Topological.java
 *  Data files:   tinyEWDAG.txt
 *
 *  Computes shortest paths in an edge-weighted acyclic digraph.
 *
 *  % java AcyclicSP tinyEWDAG.txt 5
 *  5 to 0 (0.73)  5->4  0.35   4->0  0.38   
 *  5 to 1 (0.32)  5->1  0.32   
 *  5 to 2 (0.62)  5->7  0.28   7->2  0.34   
 *  5 to 3 (0.61)  5->1  0.32   1->3  0.29   
 *  5 to 4 (0.35)  5->4  0.35   
 *  5 to 5 (0.00)  
 *  5 to 6 (1.13)  5->1  0.32   1->3  0.29   3->6  0.52   
 *  5 to 7 (0.28)  5->7  0.28   
 *
 ******************************************************************************/

/**
 *  The <tt>SPAciclico</tt> class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted directed acyclic
 *  graphs (DAGs). The edge weights can be positive, negative, or zero.
 *  <p>
  This implementation uses a topological-sort based algorithm.
  The constructor takes time proportional hacia <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of aristas.
  Afterwards, the <tt>distanciaHacia()</tt> and <tt>tieneCaminoHacia()</tt> methods take
 *  constant time and the <tt>caminoHacia()</tt> method takes time proportional hacia the
  number of aristas in the shortest path returned.
  <p>
 */
public class SPAciclico {
    private double[] distanciaHacia;         // distanciaHacia[v] = distance  of shortest s->v path
    private AristaDirigida[] aristaHacia;   // edgeTo[v] = last edge on shortest s->v path


    /**
     * Computes a shortest paths tree desde <tt>s</tt> hacia every other vertex in
 the directed acyclic graph <tt>G</tt>.
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if the digraph is not acyclic
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public SPAciclico(DigrafoAristaPonderada G, int s) {
        distanciaHacia = new double[G.V()];
        aristaHacia = new AristaDirigida[G.V()];
        for (int v = 0; v < G.V(); v++)
            distanciaHacia[v] = Double.POSITIVE_INFINITY;
        distanciaHacia[s] = 0.0;

        // visit vertices in toplogical orden
        Topologico topologico = new Topologico(G);
        if (!topologico.tieneOrden())
            throw new IllegalArgumentException("El digrafo no es acíclico.");
        for (int v : topologico.orden()) {
            for (AristaDirigida a : G.ady(v))
                relajar(a);
        }
    }

    // relax edge e
    private void relajar(AristaDirigida a) {
        int v = a.desde(), w = a.hacia();
        if (distanciaHacia[w] > distanciaHacia[v] + a.peso()) {
            distanciaHacia[w] = distanciaHacia[v] + a.peso();
            aristaHacia[w] = a;
        }       
    }

    /**
     * Returns the length of a shortest path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return the length of a shortest path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>;
     *    <tt>Double.POSITIVE_INFINITY</tt> if no such path
     */
    public double distanciaHacia(int v) {
        return distanciaHacia[v];
    }

    /**
     * Is there a path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>?
     * @param v the destination vertex
     * @return <tt>true</tt> if there is a path desde the source vertex
    <tt>s</tt> hacia vertex <tt>v</tt>, and <tt>false</tt> otherwise
     */
    public boolean tieneCaminoHacia(int v) {
        return distanciaHacia[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return a shortest path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>
    as an iterable of aristas, and <tt>null</tt> if no such path
     */
    public Iterable<AristaDirigida> caminoHacia(int v) {
        if (!tieneCaminoHacia(v)) return null;
        Pila<AristaDirigida> camino = new Pila<AristaDirigida>();
        for (AristaDirigida e = aristaHacia[v]; e != null; e = aristaHacia[e.desde()]) {
            camino.push(e);
        }
        return camino;
    }


    /**
     * Unit tests the <tt>SPAciclico</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        DigrafoAristaPonderada G = new DigrafoAristaPonderada(entrada);

        // find shortest path desde s hacia each other vertex in DAG
        SPAciclico cmc = new SPAciclico(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (cmc.tieneCaminoHacia(v)) {
                StdOut.printf("%d hacia %d (%.2f)  ", s, v, cmc.distanciaHacia(v));
                for (AristaDirigida a : cmc.caminoHacia(v)) {
                    StdOut.print(a + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", s, v);
            }
        }
    }
}
