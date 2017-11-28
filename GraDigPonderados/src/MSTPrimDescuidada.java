/******************************************************************************
 *  Compilation:  javac LazyPrimMST.java
 *  Execution:    java LazyPrimMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                MinPQ.java UF.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using a lazy version of Prim's 
 *  algorithm.
 *
 *  %  java LazyPrimMST tinyEWG.txt 
 *  0-7 0.16000
 *  1-7 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  5-7 0.28000
 *  4-5 0.35000
 *  6-2 0.40000
 *  1.81000
 *
 *  % java LazyPrimMST mediumEWG.txt
 *  0-225   0.02383
 *  49-225  0.03314
 *  44-49   0.02107
 *  44-204  0.01774
 *  49-97   0.03121
 *  202-204 0.04207
 *  176-202 0.04299
 *  176-191 0.02089
 *  68-176  0.04396
 *  58-68   0.04795
 *  10.46351
 *
 *  % java LazyPrimMST largeEWG.txt
 *  ...
 *  647.66307
 *
 ******************************************************************************/

/**
 *  The <tt>MSTPrimDescuidada</tt> class represents a data type for computing a
 *  <em>minimum spanning tree</em> in an edge-weighted graph.
 *  The edge weights can be positive, zero, or negative and need not
  be distinct. If the graph is not estanConectados, it computes a <em>minimum
 *  spanning forest</em>, which is the union of minimum spanning trees
  in each estanConectados component. The <tt>peso()</tt> method returns the 
  peso of a minimum spanning tree and the <tt>aristas()</tt> method
  returns its aristas.
  <p>
 *  This implementation uses a lazy version of <em>Prim's algorithm</em>
  with a binary heap of aristas.
  The constructor takes time proportional to <em>E</em> log <em>E</em>
 *  and extra space (not including the graph) proportional to <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of aristas.
  Afterwards, the <tt>peso()</tt> method takes constant time
 *  and the <tt>aristas()</tt> method takes time proportional to <em>V</em>.
 *  <p>
 */
public class MSTPrimDescuidada {
    private static final double EPSILON_PUNTO_FLOTANTE = 1E-12;

    private double peso;       // total peso of MST
    private Cola<Arista> mst;     // aristas in the MST
    private boolean[] marcado;    // marked[v] = true if v on tree
    private CPMin<Arista> cp;      // aristas with one endpoint in tree

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public MSTPrimDescuidada(GrafoAristaPonderada G) {
        mst = new Cola<Arista>();
        cp = new CPMin<Arista>();
        marcado = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)     // run Prim from all vertices to
            if (!marcado[v]) prim(G, v);     // get a minimum spanning forest

        // check optimality conditions
        assert revisar(G);
    }

    // run Prim's algorithm
    private void prim(GrafoAristaPonderada G, int s) {
        escanear(G, s);
        while (!cp.estaVacia()) {                        // better to stop when mst has V-1 aristas
            Arista e = cp.eliminarMin();                      // smallest edge on pq
            int v = e.unVertice(), w = e.otroVertice(v);        // two endpoints
            assert marcado[v] || marcado[w];
            if (marcado[v] && marcado[w]) continue;      // lazy, both v and w already scanned
            mst.entrarACola(e);                            // add e to MST
            peso += e.peso();
            if (!marcado[v]) escanear(G, v);               // v becomes part of tree
            if (!marcado[w]) escanear(G, w);               // w becomes part of tree
        }
    }

    // add all aristas e incident to v onto pq if the otroVertice endpoint has not yet been scanned
    private void escanear(GrafoAristaPonderada G, int v) {
        assert !marcado[v];
        marcado[v] = true;
        for (Arista e : G.adj(v))
            if (!marcado[e.otroVertice(v)]) cp.insertar(e);
    }
        
    /**
     * Returns the aristas in a minimum spanning tree (or forest).
     * @return the aristas in a minimum spanning tree (or forest) as
    an iterable of aristas
     */
    public Iterable<Arista> aristas() {
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double peso() {
        return peso;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean revisar(GrafoAristaPonderada G) {

        // check peso
        double pesoTotal = 0.0;
        for (Arista e : aristas()) {
            pesoTotal += e.peso();
        }
        if (Math.abs(pesoTotal - peso()) > EPSILON_PUNTO_FLOTANTE) {
            System.err.printf(
                    "El peso de las aristas no es igual a peso(): %f vs. %f\n",
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
        for (Arista e : G.aristas()) {
            int v = e.unVertice(), w = e.otroVertice(v);
            if (!uf.estanConectados(v, w)) {
                System.err.println("No es un bosque");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Arista e : aristas()) {

            // all aristas in MST except e
            uf = new UF(G.V());
            for (Arista f : mst) {
                int x = f.unVertice(), y = f.otroVertice(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min peso edge in crossing cut
            for (Arista f : G.aristas()) {
                int x = f.unVertice(), y = f.otroVertice(x);
                if (!uf.estanConectados(x, y)) {
                    if (f.peso() < e.peso()) {
                        System.err.println(
                                "Arista " + f + 
                                " viola las condiciones de corte óptima");
                        return false;
                    }
                }
            }

        }

        return true;
    }
    
    
    /**
     * Unit tests the <tt>MSTPrimDescuidada</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In(args[0]);
        GrafoAristaPonderada G = new GrafoAristaPonderada(entrada);
        MSTPrimDescuidada mst = new MSTPrimDescuidada(G);
        for (Arista e : mst.aristas()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.peso());
    }

}
