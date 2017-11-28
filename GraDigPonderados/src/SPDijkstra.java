/******************************************************************************
 *  Compilation:  javac DijkstraSP.java
 *  Execution:    java DijkstraSP input.txt s
 *  Dependencies: EdgeWeightedDigraph.java IndexMinPQ.java Pila.java 
 *                DirectedEdge.java
 *  Data files:   tinyEWD.txt
 *                mediumEWD.txt
 *                largeEWD.txt
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all weights are nonnegative.
 *
 *  % java DijkstraSP tinyEWD.txt 0
 *  0 to 0 (0.00)  
 *  0 to 1 (1.05)  0->4  0.38   4->5  0.35   5->1  0.32   
 *  0 to 2 (0.26)  0->2  0.26   
 *  0 to 3 (0.99)  0->2  0.26   2->7  0.34   7->3  0.39   
 *  0 to 4 (0.38)  0->4  0.38   
 *  0 to 5 (0.73)  0->4  0.38   4->5  0.35   
 *  0 to 6 (1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   
 *  0 to 7 (0.60)  0->2  0.26   2->7  0.34   
 *
 *  % java DijkstraSP mediumEWD.txt 0
 *  0 to 0 (0.00)  
 *  0 to 1 (0.71)  0->44  0.06   44->93  0.07   ...  107->1  0.07   
 *  0 to 2 (0.65)  0->44  0.06   44->231  0.10  ...  42->2  0.11   
 *  0 to 3 (0.46)  0->97  0.08   97->248  0.09  ...  45->3  0.12   
 *  0 to 4 (0.42)  0->44  0.06   44->93  0.07   ...  77->4  0.11   
 *  ...
 *
 ******************************************************************************/


/**
 *  The <tt>SPDijkstra</tt> class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted digraphs
 *  where the edge weights are nonnegative.
 *  <p>
  This implementation uses Dijkstra's algorithm with a binary heap.
  The constructor takes time proportional hacia <em>E</em> log <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of aristas.
  Afterwards, the <tt>distanciaHacia()</tt> and <tt>tieneCaminoHacia()</tt> methods take
 *  constant time and the <tt>caminoHacia()</tt> method takes time proportional hacia the
  number of aristas in the shortest path returned.
  <p>
 */
public class SPDijkstra {
    private double[] distanciaHacia;          // distanciaHacia[v] = distance  of shortest s->v path
    private AristaDirigida[] aristaHacia;    // edgeTo[v] = last edge on shortest s->v path
    private CPMinIndexada<Double> cp;    // priority queue of vertices

    /**
     * Computes a shortest paths tree desde <tt>s</tt> hacia every other vertex in
 the edge-weighted digraph <tt>G</tt>.
     * @param G the edge-weighted digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if an edge peso is negative
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public SPDijkstra(DigrafoAristaPonderada G, int s) {
        for (AristaDirigida e : G.aristas()) {
            if (e.peso() < 0)
                throw new IllegalArgumentException(
                        "arista " + e + " tiene peso negativo");
        }

        distanciaHacia = new double[G.V()];
        aristaHacia = new AristaDirigida[G.V()];
        for (int v = 0; v < G.V(); v++)
            distanciaHacia[v] = Double.POSITIVE_INFINITY;
        distanciaHacia[s] = 0.0;

        // relax vertices in order of distance desde s
        cp = new CPMinIndexada<Double>(G.V());
        cp.insertar(s, distanciaHacia[s]);
        while (!cp.estaVacia()) {
            int v = cp.delMin();
            for (AristaDirigida e : G.ady(v))
                relajar(e);
        }

        // check optimality conditions
        assert revisar(G, s);
    }

    // relax edge e and update pq if changed
    private void relajar(AristaDirigida e) {
        int v = e.desde(), w = e.hacia();
        if (distanciaHacia[w] > distanciaHacia[v] + e.peso()) {
            distanciaHacia[w] = distanciaHacia[v] + e.peso();
            aristaHacia[w] = e;
            if (cp.contiene(w)) cp.decrementarClave(w, distanciaHacia[w]);
            else                cp.insertar(w, distanciaHacia[w]);
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
        for (AristaDirigida a = aristaHacia[v]; a != null;
                a = aristaHacia[a.desde()]) {
            camino.push(a);
        }
        return camino;
    }


    // check optimality conditions:
    // (i) for all aristas e:            distanciaHacia[e.hacia()] <= distanciaHacia[e.desde()] + e.peso()
    // (ii) for all edge e on the SPT: distanciaHacia[e.hacia()] == distanciaHacia[e.desde()] + e.peso()
    private boolean revisar(DigrafoAristaPonderada G, int s) {

        // check that edge weights are nonnegative
        for (AristaDirigida a : G.aristas()) {
            if (a.peso() < 0) {
                System.err.println("arista con peso negativo detectado");
                return false;
            }
        }

        // check that distanciaHacia[v] and edgeTo[v] are consistent
        if (distanciaHacia[s] != 0.0 || aristaHacia[s] != null) {
            System.err.println("distanciaHacia[s] y aristaHacia[s]"
                    + " inconsistentes");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (aristaHacia[v] == null && 
                    distanciaHacia[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distanciaHacia[] and aristaHacia[] "
                        + "inconsistentes");
                return false;
            }
        }

        // check that all aristas e = v->w satisfy distanciaHacia[w] <= distanciaHacia[v] + e.peso()
        for (int v = 0; v < G.V(); v++) {
            for (AristaDirigida e : G.ady(v)) {
                int w = e.hacia();
                if (distanciaHacia[v] + e.peso() < distanciaHacia[w]) {
                    System.err.println("arista " + e + " no relajada");
                    return false;
                }
            }
        }

        // check that all aristas e = v->w on SPT satisfy distanciaHacia[w] == distanciaHacia[v] + e.peso()
        for (int w = 0; w < G.V(); w++) {
            if (aristaHacia[w] == null) continue;
            AristaDirigida a = aristaHacia[w];
            int v = a.desde();
            if (w != a.hacia()) return false;
            if (distanciaHacia[v] + a.peso() != distanciaHacia[w]) {
                System.err.println("arista " + a + " en el camino más corto"
                        + " no ajustado");
                return false;
            }
        }
        return true;
    }


    /**
     * Unit tests the <tt>SPDijkstra</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In(args[0]);
        DigrafoAristaPonderada G = new DigrafoAristaPonderada(entrada);
        int s = Integer.parseInt(args[1]);

        // compute shortest paths
        SPDijkstra cmc = new SPDijkstra(G, s);


        // print shortest path
        for (int t = 0; t < G.V(); t++) {
            if (cmc.tieneCaminoHacia(t)) {
                StdOut.printf("%d to %d (%.2f)  ", s, t, cmc.distanciaHacia(t));
                for (AristaDirigida e : cmc.caminoHacia(t)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d hacia %d         no en el camino\n", s, t);
            }
        }
    }
}