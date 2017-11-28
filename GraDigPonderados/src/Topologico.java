/******************************************************************************
 *  Compilation:  javac Topoological.java
 *  Execution:    java  Topological filename.txt separator
 *  Dependencies: Digraph.java DepthFirstOrder.java DirectedCycle.java
 *                EdgeWeightedDigraph.java EdgeWeightedDirectedCycle.java
 *                SymbolDigraph.java
 *  Data files:   jobs.txt
 *
 *  Compute topological ordering of a DAG or edge-weighted DAG.
 *  Runs in O(E + V) time.
 *
 *  % java Topological jobs.txt "/"
 *  Calculus
 *  Linear Algebra
 *  Introduction to CS
 *  Programming Systems
 *  Algorithms
 *  Theoretical CS
 *  Artificial Intelligence
 *  Machine Learning
 *  Neural Networks
 *  Robotics
 *  Scientific Computing
 *  Computational Biology
 *  Databases
 *
 *
 ******************************************************************************/

/**
 *  The <tt>Topologico</tt> class represents a data type for 
  determining a topological orden of a directed acyclic graph (DAG).
 *  Recall, a digraph has a topological orden if and only if it is a DAG.
  The <em>tieneOrden</em> operation determines whether the digraph has
  a topological orden, and if so, the <em>orden</em> operation
 *  returns one.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>A</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>A</em> is the number of edges.
 *  Afterwards, the <em>tieneOrden</em> and <em>rango</em> operations takes constant time;
 *  the <em>orden</em> operation takes time proportional to <em>V</em>.
 *  <p>
 *  See {@link CicloDirigido}, {@link DirectedCycleX}, and
 *  {@link AristaPonderadaDeCicloDirigido} to compute a
  directed ciclo if the digraph is not a DAG.
  See {@link TopologicalX} for a nonrecursive queue-based algorithm
  to compute a topological orden of a DAG.
  <p>
 */
public class Topologico {
    private Iterable<Integer> orden;  // topological orden
    private int[] rango;      // rango[v] = position of vertex v in topological orden

    /**
     * Determines whether the digraph <tt>G</tt> has a topological orden and, if so,
 finds such a topological orden.
     * @param G the digraph
     */
    public Topologico(Digrafo G) {
        CicloDirigido descubridor = new CicloDirigido(G);
        if (!descubridor.tieneCiclo()) {
            OrdenPrimeraProfundidad dfs = new OrdenPrimeraProfundidad(G);
            orden = dfs.postReverso();
            rango = new int[G.V()];
            int i = 0;
            for (int v : orden)
                rango[v] = i++;
        }
    }

    /**
     * Determines whether the edge-weighted digraph <tt>G</tt> has a topological
 orden and, if so, finds such an orden.
     * @param G the edge-weighted digraph
     */
    public Topologico(DigrafoAristaPonderada G) {
        AristaPonderadaDeCicloDirigido descubridor = 
                new AristaPonderadaDeCicloDirigido(G);
        if (!descubridor.tieneCiclo()) {
            OrdenPrimeraProfundidad dfs = new OrdenPrimeraProfundidad(G);
            orden = dfs.postReverso();
        }
    }

    /**
     * Returns a topological orden if the digraph has a topologial orden,
 and <tt>null</tt> otherwise.
     * @return a topological orden of the vertices (as an interable) if the
    digraph has a topological orden (or equivalently, if the digraph is a DAG),
    and <tt>null</tt> otherwise
     */
    public Iterable<Integer> orden() {
        return orden;
    }

    /**
     * Does the digraph have a topological orden?
     * @return <tt>true</tt> if the digraph has a topological orden (or equivalently,
    if the digraph is a DAG), and <tt>false</tt> otherwise
     */
    public boolean tieneOrden() {
        return orden != null;
    }

    /**
     * The the rango of vertex <tt>v</tt> in the topological orden;
 -1 if the digraph is not a DAG
     * @return the position of vertex <tt>v</tt> in a topological orden
    of the digraph; -1 if the digraph is not a DAG
     * @throws IndexOutOfBoundsException unless <tt>v</tt> is between 0 and
     *    <em>V</em> &minus; 1
     */
    public int rango(int v) {
        validarVertice(v);
        if (tieneOrden()) return rango[v];
        else              return -1;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validarVertice(int v) {
        int V = rango.length;
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException(
                    "vértice " + v + " no está entre 0 y " + (V-1));
    }

    /**
     * Unit tests the <tt>Topologico</tt> data type.
     */
    public static void main(String[] args) {
        String nombreArchivo  = args[0];
        String delimitador = args[1];
        DigrafoDeSimbolos gs = new DigrafoDeSimbolos(nombreArchivo, delimitador);
        Topologico topologico = new Topologico(gs.G());
        for (int v : topologico.orden()) {
            StdOut.println(gs.nombre(v));
        }
    }

}
