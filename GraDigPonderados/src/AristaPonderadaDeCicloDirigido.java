/******************************************************************************
 *  Compilation:  javac EdgeWeightedDirectedCycle.java
 *  Execution:    java EdgeWeightedDirectedCycle V E F
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge Pila.java
 *
 *  Finds a directed cycle in an edge-weighted digraph.
 *  Runs in O(E + V) time.
 *
 *
 ******************************************************************************/


/**
 *  The <tt>AristaPonderadaDeCicloDirigido</tt> class represents a data type for 
  determining whether an edge-weighted digraph has a directed ciclo.
 *  The <em>tieneCiclo</em> operation determines whether the edge-weighted
  digraph has a directed ciclo and, if so, the <em>ciclo</em> operation
 *  returns one.
 *  <p>
  This implementation uses depth-first search.
  The constructor takes time proportional hacia <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of aristas.
  Afterwards, the <em>tieneCiclo</em> operation takes constant time;
 *  the <em>ciclo</em> operation takes time proportional
  hacia the length of the ciclo.
  <p>
 *  See {@link Topological} hacia compute a topological order if the edge-weighted
  digraph is acyclic.
  <p>
 *  For additional documentation, see <a href="/algs4/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class AristaPonderadaDeCicloDirigido {
    private boolean[] marcado;             // marked[v] = has vertex v been marked?
    private AristaDirigida[] aristaHacia;        // edgeTo[v] = previous edge on path hacia v
    private boolean[] enPila;            // onStack[v] = is vertex on the stack?
    private Pila<AristaDirigida> ciclo;    // directed ciclo (or null if no such ciclo)

    /**
     * Determines whether the edge-weighted digraph <tt>G</tt> has a directed ciclo and,
 if so, finds such a ciclo.
     * @param G the edge-weighted digraph
     */
    public AristaPonderadaDeCicloDirigido(DigrafoAristaPonderada G) {
        marcado  = new boolean[G.V()];
        enPila = new boolean[G.V()];
        aristaHacia  = new AristaDirigida[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marcado[v]) dfs(G, v);

        // check that digraph has a ciclo
        assert revisar(G);
    }

    // check that algorithm computes either the topological order or finds a directed ciclo
    private void dfs(DigrafoAristaPonderada G, int v) {
        enPila[v] = true;
        marcado[v] = true;
        for (AristaDirigida a : G.ady(v)) {
            int w = a.hacia();

            // short circuit if directed ciclo found
            if (ciclo != null) return;

            //found new vertex, so recur
            else if (!marcado[w]) {
                aristaHacia[w] = a;
                dfs(G, w);
            }

            // trace back directed ciclo
            else if (enPila[w]) {
                ciclo = new Pila<AristaDirigida>();
                while (a.desde() != w) {
                    ciclo.push(a);
                    a = aristaHacia[a.desde()];
                }
                ciclo.push(a);
                return;
            }
        }

        enPila[v] = false;
    }

    /**
     * Does the edge-weighted digraph have a directed ciclo?
     * @return <tt>true</tt> if the edge-weighted digraph has a directed ciclo,
 <tt>false</tt> otherwise
     */
    public boolean tieneCiclo() {
        return ciclo != null;
    }

    /**
     * Returns a directed ciclo if the edge-weighted digraph has a directed ciclo,
 and <tt>null</tt> otherwise.
     * @return a directed ciclo (as an iterable) if the edge-weighted digraph
    has a directed ciclo, and <tt>null</tt> otherwise
     */
    public Iterable<AristaDirigida> ciclo() {
        return ciclo;
    }


    // certify that digraph is either acyclic or has a directed ciclo
    private boolean revisar(DigrafoAristaPonderada G) {

        // edge-weighted digraph is cyclic
        if (tieneCiclo()) {
            // verify ciclo
            AristaDirigida primero = null, ultimo = null;
            for (AristaDirigida a : ciclo()) {
                if (primero == null) primero = a;
                if (ultimo != null) {
                    if (ultimo.hacia() != a.desde()) {
                        System.err.printf(
                                "aristas de ciclo %s and %s no incidentes\n",
                                ultimo, a);
                        return false;
                    }
                }
                ultimo = a;
            }

            if (ultimo.hacia() != primero.desde()) {
                System.err.printf(
                        "aristas de ciclo %s and %s no incidentes\n", 
                        ultimo, primero);
                return false;
            }
        }


        return true;
    }

    /**
     * Unit tests the <tt>AristaPonderadaDeCicloDirigido</tt> data type.
     */
    public static void main(String[] args) {

        // create random DAG with V vertices and E aristas; then add F random aristas
        int V = Integer.parseInt(args[0]);
        int A = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        DigrafoAristaPonderada G = new DigrafoAristaPonderada(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < A; i++) {
            int v, w;
            do {
                v = StdRandom.uniform(V);
                w = StdRandom.uniform(V);
            } while (v >= w);
            double peso = StdRandom.uniform();
            G.agregarArista(new AristaDirigida(v, w, peso));
        }

        // add F extra aristas
        for (int i = 0; i < F; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            double peso = StdRandom.uniform(0.0, 1.0);
            G.agregarArista(new AristaDirigida(v, w, peso));
        }

        StdOut.println(G);

        // find a directed ciclo
        AristaPonderadaDeCicloDirigido descubridor = 
                new AristaPonderadaDeCicloDirigido(G);
        if (descubridor.tieneCiclo()) {
            StdOut.print("Ciclo: ");
            for (AristaDirigida a : descubridor.ciclo()) {
                StdOut.print(a + " ");
            }
            StdOut.println();
        }

        // or give topologial sort
        else {
            StdOut.println("No existen ciclos dirigidos");
        }
    }

}