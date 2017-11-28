/******************************************************************************
 *  Compilation:  javac DirectedCycle.java
 *  Execution:    java DirectedCycle input.txt
 *  Dependencies: Digraph.java Pila.java StdOut.java In.java
 *  Data files:   tinyDG.txt
 *                tinyDAG.txt
 *
 *  Finds a directed cycle in a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java DirectedCycle tinyDG.txt 
 *  Directed cycle: 3 5 4 3 
 *
 *  %  java DirectedCycle tinyDAG.txt 
 *  No directed cycle
 *
 ******************************************************************************/


/**
 *  The <tt>CicloDirigido</tt> class represents a data type for 
  determining whether a digraph has a directed ciclo.
 *  The <em>tieneCiclo</em> operation determines whether the digraph has
  a directed ciclo and, and of so, the <em>ciclo</em> operation
 *  returns one.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>A</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>A</em> is the number of edges.
 *  Afterwards, the <em>tieneCiclo</em> operation takes constant time;
 *  the <em>ciclo</em> operation takes time proportional
  to the length of the ciclo.
  <p>
 *  See {@link Topological} to compute a topological order if the
 *  digraph is acyclic.
 *  <p>
 */
public class CicloDirigido {
    private boolean[] marcado;        // marked[v] = has vertex v been marked?
    private int[] aristaHacia;            // edgeTo[v] = previous vertex on path to v
    private boolean[] enPila;       // onStack[v] = is vertex on the stack?
    private Pila<Integer> ciclo;    // directed ciclo (or null if no such ciclo)

    /**
     * Determines whether the digraph <tt>G</tt> has a directed ciclo and, if so,
 finds such a ciclo.
     * @param G the digraph
     */
    public CicloDirigido(Digrafo G) {
        marcado  = new boolean[G.V()];
        enPila = new boolean[G.V()];
        aristaHacia  = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marcado[v] && ciclo == null) dfs(G, v);
    }

    // check that algorithm computes either the topological order or finds a directed ciclo
    private void dfs(Digrafo G, int v) {
        enPila[v] = true;
        marcado[v] = true;
        for (int w : G.ady(v)) {

            // short circuit if directed ciclo found
            if (ciclo != null) return;

            //found new vertex, so recur
            else if (!marcado[w]) {
                aristaHacia[w] = v;
                dfs(G, w);
            }

            // trace back directed ciclo
            else if (enPila[w]) {
                ciclo = new Pila<Integer>();
                for (int x = v; x != w; x = aristaHacia[x]) {
                    ciclo.push(x);
                }
                ciclo.push(w);
                ciclo.push(v);
                assert revisar();
            }
        }
        enPila[v] = false;
    }

    /**
     * Does the digraph have a directed ciclo?
     * @return <tt>true</tt> if the digraph has a directed ciclo, <tt>false</tt> otherwise
     */
    public boolean tieneCiclo() {
        return ciclo != null;
    }

    /**
     * Returns a directed ciclo if the digraph has a directed ciclo, and <tt>null</tt> otherwise.
     * @return a directed ciclo (as an iterable) if the digraph has a directed ciclo,
    and <tt>null</tt> otherwise
     */
    public Iterable<Integer> ciclo() {
        return ciclo;
    }


    // certify that digraph has a directed ciclo if it reports one
    private boolean revisar() {

        if (tieneCiclo()) {
            // verify ciclo
            int primero = -1, ultimo = -1;
            for (int v : ciclo()) {
                if (primero == -1) primero = v;
                ultimo = v;
            }
            if (primero != ultimo) {
                System.err.printf("el ciclo comienza con %d y termina con %d\n",
                        primero, ultimo);
                return false;
            }
        }


        return true;
    }

    /**
     * Unit tests the <tt>CicloDirigido</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In(args[0]);
        Digrafo G = new Digrafo(entrada);

        CicloDirigido descubridor = new CicloDirigido(G);
        if (descubridor.tieneCiclo()) {
            StdOut.print("Ciclo Dirigido: ");
            for (int v : descubridor.ciclo()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }

        else {
            StdOut.println("No hay ciclos dirigidos");
        }
        StdOut.println();
    }

}
