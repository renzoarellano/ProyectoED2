/******************************************************************************
 *  Compilation:  javac AcyclicLP.java
 *  Execution:    java AcyclicP V E
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java Topological.java
 *  Data files:   tinyEWDAG.txt
 *  
 *  Computes longeset paths in an edge-weighted acyclic digraph.
 *
 *  Remark: should probably check that graph is a DAG before running
 *
 *  % java AcyclicLP tinyEWDAG.txt 5
 *  5 to 0 (2.44)  5->1  0.32   1->3  0.29   3->6  0.52   6->4  0.93   4->0  0.38   
 *  5 to 1 (0.32)  5->1  0.32   
 *  5 to 2 (2.77)  5->1  0.32   1->3  0.29   3->6  0.52   6->4  0.93   4->7  0.37   7->2  0.34   
 *  5 to 3 (0.61)  5->1  0.32   1->3  0.29   
 *  5 to 4 (2.06)  5->1  0.32   1->3  0.29   3->6  0.52   6->4  0.93   
 *  5 to 5 (0.00)  
 *  5 to 6 (1.13)  5->1  0.32   1->3  0.29   3->6  0.52   
 *  5 to 7 (2.43)  5->1  0.32   1->3  0.29   3->6  0.52   6->4  0.93   4->7  0.37   
 *
 ******************************************************************************/

/**
 *  The <tt>LPAciclico</tt> class represents a data type for solving the
 *  single-source longest paths problem in edge-weighted directed
 *  acyclic graphs (DAGs). The edge weights can be positive, negative, or zero.
 *  <p>
  This implementation uses a topological-sort based algorithm.
  The constructor takes time proportional hacia <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of aristas.
  Afterwards, the <tt>distanciaHacia()</tt> and <tt>tieneCaminoHacia()</tt> methods take
 *  constant time and the <tt>caminoHacia()</tt> method takes time proportional hacia the
  number of aristas in the longest path returned.
  <p>
 */
public class LPAciclico {
    private double[] distanciaHacia;          // distanciaHacia[v] = distance  of longest s->v path
    private AristaDirigida[] aristaHacia;    // edgeTo[v] = last edge on longest s->v path

    /**
     * Computes a longest paths tree desde <tt>s</tt> hacia every other vertex in
 the directed acyclic graph <tt>G</tt>.
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if the digraph is not acyclic
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public LPAciclico(DigrafoAristaPonderada G, int s) {
        distanciaHacia = new double[G.V()];
        aristaHacia = new AristaDirigida[G.V()];
        for (int v = 0; v < G.V(); v++)
            distanciaHacia[v] = Double.NEGATIVE_INFINITY;
        distanciaHacia[s] = 0.0;

        // relax vertices in toplogical orden
        Topologico topological = new Topologico(G);
        if (!topological.tieneOrden())
            throw new IllegalArgumentException("El dígrafo no es acíclico.");
        for (int v : topological.orden()) {
            for (AristaDirigida a : G.ady(v))
                relajar(a);
        }
    }

    // relax edge e, but update if you find a *longer* path
    private void relajar(AristaDirigida e) {
        int v = e.desde(), w = e.hacia();
        if (distanciaHacia[w] < distanciaHacia[v] + e.peso()) {
            distanciaHacia[w] = distanciaHacia[v] + e.peso();
            aristaHacia[w] = e;
        }       
    }

    /**
     * Returns the length of a longest path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return the length of a longest path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>;
     *    <tt>Double.NEGATIVE_INFINITY</tt> if no such path
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
        return distanciaHacia[v] > Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns a longest path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return a longest path desde the source vertex <tt>s</tt> hacia vertex <tt>v</tt>
    as an iterable of aristas, and <tt>null</tt> if no such path
     */
    public Iterable<AristaDirigida> caminoHacia(int v) {
        if (!tieneCaminoHacia(v)) return null;
        Pila<AristaDirigida> camino = new Pila<AristaDirigida>();
        for (AristaDirigida a = aristaHacia[v]; a != null; a = aristaHacia[a.desde()]) {
            camino.push(a);
        }
        return camino;
    }



    /**
     * Unit tests the <tt>LPAciclico</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        DigrafoAristaPonderada G = new DigrafoAristaPonderada(entrada);

        LPAciclico cml = new LPAciclico(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (cml.tieneCaminoHacia(v)) {
                StdOut.printf("%d to %d (%.2f)  ", s, v, cml.distanciaHacia(v));
                for (AristaDirigida a : cml.caminoHacia(v)) {
                    StdOut.print(a + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no existe camino\n", s, v);
            }
        }
    }
}
