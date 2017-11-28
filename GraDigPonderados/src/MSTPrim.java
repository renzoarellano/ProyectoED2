/******************************************************************************
 *  Compilation:  javac PrimMST.java
 *  Execution:    java PrimMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                IndexMinPQ.java UF.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using Prim's algorithm.
 *
 *  %  java PrimMST tinyEWG.txt 
 *  1-7 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  4-5 0.35000
 *  5-7 0.28000
 *  6-2 0.40000
 *  0-7 0.16000
 *  1.81000
 *
 *  % java PrimMST mediumEWG.txt
 *  1-72   0.06506
 *  2-86   0.05980
 *  3-67   0.09725
 *  4-55   0.06425
 *  5-102  0.03834
 *  6-129  0.05363
 *  7-157  0.00516
 *  ...
 *  10.46351
 *
 *  % java PrimMST largeEWG.txt
 *  ...
 *  647.66307
 *
 ******************************************************************************/


/**
 *  The <tt>MSTPrim</tt> class represents a data type for computing a
 *  <em>minimum spanning tree</em> in an edge-weighted graph.
 *  The edge weights can be positive, zero, or negative and need not
  be distinct. If the graph is not estanConectados, it computes a <em>minimum
 *  spanning forest</em>, which is the union of minimum spanning trees
  in each estanConectados component. The <tt>peso()</tt> method returns the 
  peso of a minimum spanning tree and the <tt>aristas()</tt> method
  returns its aristas.
  <p>
 *  This implementation uses <em>Prim's algorithm</em> with an indexed
 *  binary heap.
 *  The constructor takes time proportional to <em>E</em> log <em>V</em>
 *  and extra space (not including the graph) proportional to <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of aristas.
  Afterwards, the <tt>peso()</tt> method takes constant time
 *  and the <tt>aristas()</tt> method takes time proportional to <em>V</em>.
 *  <p>
 */
public class MSTPrim {
    private static final double EPSILON_PUNTO_FLOTANTE = 1E-12;

    private Arista[] aristaHacia;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distanciaHacia;      // distTo[v] = peso of shortest such edge
    private boolean[] marcado;     // marked[v] = true if v on tree, false otherwise
    private CPMinIndexada<Double> cp;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public MSTPrim(GrafoAristaPonderada G) {
        aristaHacia = new Arista[G.V()];
        distanciaHacia = new double[G.V()];
        marcado = new boolean[G.V()];
        cp = new CPMinIndexada<Double>(G.V());
        for (int v = 0; v < G.V(); v++)
            distanciaHacia[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.V(); v++)      // run from each vertex to encontrar
            if (!marcado[v]) prim(G, v);      // minimum spanning forest

        // check optimality conditions
        assert revisar(G);
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(GrafoAristaPonderada G, int s) {
        distanciaHacia[s] = 0.0;
        cp.insertar(s, distanciaHacia[s]);
        while (!cp.estaVacia()) {
            int v = cp.delMin();
            escanear(G, v);
        }
    }

    // scan vertex v
    private void escanear(GrafoAristaPonderada G, int v) {
        marcado[v] = true;
        for (Arista e : G.adj(v)) {
            int w = e.otroVertice(v);
            if (marcado[w]) continue;         // v-w is obsolete edge
            if (e.peso() < distanciaHacia[w]) {
                distanciaHacia[w] = e.peso();
                aristaHacia[w] = e;
                if (cp.contiene(w)) cp.decrementarClave(w, distanciaHacia[w]);
                else                cp.insertar(w, distanciaHacia[w]);
            }
        }
    }

    /**
     * Returns the aristas in a minimum spanning tree (or forest).
     * @return the aristas in a minimum spanning tree (or forest) as
     * an iterable of aristas
     */
    public Iterable<Arista> aristas() {
        Cola<Arista> mst = new Cola<Arista>();
        for (int v = 0; v < aristaHacia.length; v++) {
            Arista a = aristaHacia[v];
            if (a != null) {
                mst.entrarACola(a);
            }
        }
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double peso() {
        double peso = 0.0;
        for (Arista a : aristas())
            peso += a.peso();
        return peso;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean revisar(GrafoAristaPonderada G) {

        // check peso
        double pesoTotal = 0.0;
        for (Arista a : aristas()) {
            pesoTotal += a.peso();
        }
        if (Math.abs(pesoTotal - peso()) > EPSILON_PUNTO_FLOTANTE) {
            System.err.printf(
                    "Peso de la aristas no es igual a peso(): %f vs. %f\n",
                    pesoTotal, peso());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.V());
        for (Arista e : aristas()) {
            int v = e.unVertice(), w = e.otroVertice(v);
            if (uf.estanConectados(v, w)) {
                System.err.println("No es un bosque");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Arista a : G.aristas()) {
            int v = a.unVertice(), w = a.otroVertice(v);
            if (!uf.estanConectados(v, w)) {
                System.err.println("No es un bosque de expansion");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Arista a : aristas()) {

            // all aristas in MST except a
            uf = new UF(G.V());
            for (Arista o : aristas()) {
                int x = o.unVertice(), y = o.otroVertice(x);
                if (o != a) uf.union(x, y);
            }

            // check that e is min peso edge in crossing cut
            for (Arista o : G.aristas()) {
                int x = o.unVertice(), y = o.otroVertice(x);
                if (!uf.estanConectados(x, y)) {
                    if (o.peso() < a.peso()) {
                        System.err.println("Arista " + o + 
                                " viola las condiciones de optimalidad"
                                + "del corte");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    /**
     * Unit tests the <tt>MSTPrim</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In(args[0]);
        GrafoAristaPonderada G = new GrafoAristaPonderada(entrada);
        MSTPrim mst = new MSTPrim(G);
        for (Arista a : mst.aristas()) {
            StdOut.println(a);
        }
        StdOut.printf("%.5f\n", mst.peso());
    }


}

