/******************************************************************************
 *  Compilation:  javac KruskalMST.java
 *  Execution:    java  KruskalMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                UF.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using Kruskal's algorithm.
 *
 *  %  java KruskalMST tinyEWG.txt 
 *  0-7 0.16000
 *  2-3 0.17000
 *  1-7 0.19000
 *  0-2 0.26000
 *  5-7 0.28000
 *  4-5 0.35000
 *  6-2 0.40000
 *  1.81000
 *
 *  % java KruskalMST mediumEWG.txt
 *  168-231 0.00268
 *  151-208 0.00391
 *  7-157   0.00516
 *  122-205 0.00647
 *  8-152   0.00702
 *  156-219 0.00745
 *  28-198  0.00775
 *  38-126  0.00845
 *  10-123  0.00886
 *  ...
 *  10.46351
 *
 ******************************************************************************/


/**
 *  The <tt>MSTKruskal</tt> class represents a data type for computing a
 *  <em>minimum spanning tree</em> in an edge-weighted graph.
 *  The edge weights can be positive, zero, or negative and need not
  be distinct. If the graph is not estanConectados, it computes a <em>minimum
 *  spanning forest</em>, which is the union of minimum spanning trees
  in each estanConectados component. The <tt>peso()</tt> method returns the 
  peso of a minimum spanning tree and the <tt>aristas()</tt> method
  returns its aristas.
  <p>
 *  This implementation uses <em>Krusal's algorithm</em> and the
  union-encontrar data type.
  The constructor takes time proportional to <em>E</em> log <em>V</em>
 *  and extra space (not including the graph) proportional to <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of aristas.
  Afterwards, the <tt>peso()</tt> method takes constant time
 *  and the <tt>aristas()</tt> method takes time proportional to <em>V</em>.
 *  <p>
 */
public class MSTKruskal {
    private static final double EPSILON_PUNTO_FLOTANTE = 1E-12;

    private double peso;                        // peso of MST
    private Cola<Arista> mst = new Cola<Arista>();  // aristas in MST

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public MSTKruskal(GrafoAristaPonderada G) {
        // more efficient to build heap by passing array of aristas
        CPMin<Arista> cp = new CPMin<Arista>();
        for (Arista a : G.aristas()) {
            cp.insertar(a);
        }

        // run greedy algorithm
        UF uf = new UF(G.V());
        while (!cp.estaVacia() && mst.tamanno() < G.V() - 1) {
            Arista e = cp.eliminarMin();
            int v = e.unVertice();
            int w = e.otroVertice(v);
            if (!uf.estanConectados(v, w)) { // v-w does not create a cycle
                uf.union(v, w);  // merge v and w components
                mst.entrarACola(e);  // add edge e to mst
                peso += e.peso();
            }
        }

        // check optimality conditions
        assert revisar(G);
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

        // check total peso
        double total = 0.0;
        for (Arista e : aristas()) {
            total += e.peso();
        }
        if (Math.abs(total - peso()) > EPSILON_PUNTO_FLOTANTE) {
            System.err.printf(
                    "Peso de las aristas no es igual a peso(): %f vs. %f\n",
                    total, peso());
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
                        System.err.println("Arista " + f + 
                                " viola las condiciones de corte óptimo");
                        return false;
                    }
                }
            }

        }

        return true;
    }


    /**
     * Unit tests the <tt>MSTKruskal</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In("tinyEWG.txt");
        GrafoAristaPonderada G = new GrafoAristaPonderada(entrada);
        MSTKruskal mst = new MSTKruskal(G);
        for (Arista a : mst.aristas()) {
            StdOut.println(a);
        }
        StdOut.printf("%.5f\n", mst.peso());
    }

}
