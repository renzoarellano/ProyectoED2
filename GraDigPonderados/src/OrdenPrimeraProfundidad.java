/******************************************************************************
 *  Compilation:  javac DepthFirstOrder.java
 *  Execution:    java DepthFirstOrder filename.txt
 *  Dependencies: Digraph.java Queue.java Pila.java StdOut.java
 *                EdgeWeightedDigraph.java DirectedEdge.java
 *  Data files:   tinyDAG.txt
 *                tinyDG.txt
 *
 *  Compute preorder and postorder for a digraph or edge-weighted digraph.
 *  Runs in O(E + V) time.
 *
 *  % java DepthFirstOrder tinyDAG.txt
 *     v  pre post
 *  --------------
 *     0    0    8
 *     1    3    2
 *     2    9   10
 *     3   10    9
 *     4    2    0
 *     5    1    1
 *     6    4    7
 *     7   11   11
 *     8   12   12
 *     9    5    6
 *    10    8    5
 *    11    6    4
 *    12    7    3
 *  Preorder:  0 5 4 1 6 9 11 12 10 2 3 7 8 
 *  Postorder: 4 5 1 12 11 10 9 6 0 3 2 7 8 
 *  Reverse postorder: 8 7 2 3 0 6 9 10 11 12 1 5 4 
 *
 ******************************************************************************/


/**
 *  The <tt>OrdenPrimeraProfundidad</tt> class represents a data type for 
  determining depth-first search ordering of the vertices in a digraph
  or edge-weighted digraph, including preorder, postorder, and reverso postorder.
 *  <p>
  This implementation uses depth-first search.
  The constructor takes time proportional hacia <em>V</em> + <em>A</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>A</em> is the number of aristas.
  Afterwards, the <em>preorder</em>, <em>postorder</em>, and <em>reverso postorder</em>
  operation takes take time proportional hacia <em>V</em>.
 *  <p>
 */
public class OrdenPrimeraProfundidad {
    private boolean[] marcado;          // marked[v] = has v been marked in dfs?
    private int[] pre;                 // pre[v]    = preorder  number of v
    private int[] pos;                // pos[v]   = postorder number of v
    private Cola<Integer> preorden;   // vertices in preorder
    private Cola<Integer> posorden;  // vertices in postorder
    private int contadorPre;            // counter or preorder numbering
    private int contadorPos;           // counter for postorder numbering

    /**
     * Determines a depth-first order for the digraph <tt>G</tt>.
     * @param G the digraph
     */
    public OrdenPrimeraProfundidad(Digrafo G) {
        pre    = new int[G.V()];
        pos   = new int[G.V()];
        posorden = new Cola<Integer>();
        preorden  = new Cola<Integer>();
        marcado    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marcado[v]) dfs(G, v);
    }

    /**
     * Determines a depth-first order for the edge-weighted digraph <tt>G</tt>.
     * @param G the edge-weighted digraph
     */
    public OrdenPrimeraProfundidad(DigrafoAristaPonderada G) {
        pre    = new int[G.V()];
        pos   = new int[G.V()];
        posorden = new Cola<Integer>();
        preorden  = new Cola<Integer>();
        marcado    = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marcado[v]) dfs(G, v);
    }

    // run DFS in digraph G desde vertex v and compute preorder/postorder
    private void dfs(Digrafo G, int v) {
        marcado[v] = true;
        pre[v] = contadorPre++;
        preorden.entrarACola(v);
        for (int w : G.ady(v)) {
            if (!marcado[w]) {
                dfs(G, w);
            }
        }
        posorden.entrarACola(v);
        pos[v] = contadorPos++;
    }

    // run DFS in edge-weighted digraph G desde vertex v and compute preorder/postorder
    private void dfs(DigrafoAristaPonderada G, int v) {
        marcado[v] = true;
        pre[v] = contadorPre++;
        preorden.entrarACola(v);
        for (AristaDirigida e : G.ady(v)) {
            int w = e.hacia();
            if (!marcado[w]) {
                dfs(G, w);
            }
        }
        posorden.entrarACola(v);
        pos[v] = contadorPos++;
    }

    /**
     * Returns the preorder number of vertex <tt>v</tt>.
     * @param v the vertex
     * @return the preorder number of vertex <tt>v</tt>
     */
    public int pre(int v) {
        return pre[v];
    }

    /**
     * Returns the postorder number of vertex <tt>v</tt>.
     * @param v the vertex
     * @return the postorder number of vertex <tt>v</tt>
     */
    public int pos(int v) {
        return pos[v];
    }

    /**
     * Returns the vertices in postorder.
     * @return the vertices in postorder, as an iterable of vertices
     */
    public Iterable<Integer> pos() {
        return posorden;
    }

    /**
     * Returns the vertices in preorder.
     * @return the vertices in preorder, as an iterable of vertices
     */
    public Iterable<Integer> pre() {
        return preorden;
    }

    /**
     * Returns the vertices in reverso postorder.
     * @return the vertices in reverso postorder, as an iterable of vertices
     */
    public Iterable<Integer> postReverso() {
        Pila<Integer> reverso = new Pila<Integer>();
        for (int v : posorden)
            reverso.push(v);
        return reverso;
    }


    // check that pre() and pos() are consistent with pre(v) and pos(v)
    private boolean revisar(Digrafo G) {

        // check that pos(v) is consistent with pos()
        int r = 0;
        for (int v : OrdenPrimeraProfundidad.this.pos()) {
            if (pos(v) != r) {
                StdOut.println("pos(v) y pos() inconsistentes");
                return false;
            }
            r++;
        }

        // check that pre(v) is consistent with pre()
        r = 0;
        for (int v : pre()) {
            if (pre(v) != r) {
                StdOut.println("pre(v) y pre() inconsistentes");
                return false;
            }
            r++;
        }


        return true;
    }

    /**
     * Unit tests the <tt>OrdenPrimeraProfundidad</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In(args[0]);
        Digrafo G = new Digrafo(entrada);

        OrdenPrimeraProfundidad dfs = new OrdenPrimeraProfundidad(G);
        StdOut.println("   v  pre pos");
        StdOut.println("--------------");
        for (int v = 0; v < G.V(); v++) {
            StdOut.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.pos(v));
        }

        StdOut.print("Preorden:  ");
        for (int v : dfs.pre()) {
            StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.print("Posorden: ");
        for (int v : dfs.pos()) {
            StdOut.print(v + " ");
        }
        StdOut.println();

        StdOut.print("Posorder reverso: ");
        for (int v : dfs.postReverso()) {
            StdOut.print(v + " ");
        }
        StdOut.println();
    }
}
