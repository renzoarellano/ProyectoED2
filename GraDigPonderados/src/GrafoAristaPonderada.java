/******************************************************************************
 *  Compilation:  javac EdgeWeightedGraph.java
 *  Execution:    java EdgeWeightedGraph filename.txt
 *  Dependencies: Bag.java Edge.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *
 *  An edge-weighted undirected graph, implemented using adjacency lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java EdgeWeightedGraph tinyEWG.txt 
 *  8 16
 *  0: 6-0 0.58000  0-2 0.26000  0-4 0.38000  0-7 0.16000  
 *  1: 1-3 0.29000  1-2 0.36000  1-7 0.19000  1-5 0.32000  
 *  2: 6-2 0.40000  2-7 0.34000  1-2 0.36000  0-2 0.26000  2-3 0.17000  
 *  3: 3-6 0.52000  1-3 0.29000  2-3 0.17000  
 *  4: 6-4 0.93000  0-4 0.38000  4-7 0.37000  4-5 0.35000  
 *  5: 1-5 0.32000  5-7 0.28000  4-5 0.35000  
 *  6: 6-4 0.93000  6-0 0.58000  3-6 0.52000  6-2 0.40000
 *  7: 2-7 0.34000  1-7 0.19000  0-7 0.16000  5-7 0.28000  4-7 0.37000
 *
 ******************************************************************************/


/**
 *  The <tt>GrafoAristaPonderada</tt> class represents an edge-weighted
 *  graph of vertices named 0 through <em>V</em> - 1, where each
 *  undirected edge is of type {@link Arista} and has a real-valued peso.
 *  It supports the following two primary operations: agregar an edge to the graph,
  iterate over all of the aristas incident to a vertex. It also provides
  methods for returning the number of vertices <em>V</em> and the number
  of aristas <em>E</em>. Parallel aristas and self-loops are permitted.
  <p>
  This implementation uses an adjacency-lists representation, which 
  is a vertex-indexed array of @link{Bolsa} objects.
  All operations take constant time (in the worst case) except
  iterating over the aristas incident to a given vertex, which takes
  time proportional to the number of such aristas.
  <p>
 */
public class GrafoAristaPonderada {
    private static final String NUEVALINEA = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bolsa<Arista>[] ady;
    
    /**
     * Initializes an empty edge-weighted graph with <tt>V</tt> vertices and 0 aristas.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     */
    public GrafoAristaPonderada(int V) {
        if (V < 0) throw new IllegalArgumentException(
                "El número de vértices no debe ser negativo");
        this.V = V;
        this.E = 0;
        ady = (Bolsa<Arista>[]) new Bolsa[V];
        for (int v = 0; v < V; v++) {
            ady[v] = new Bolsa<Arista>();
        }
    }

    /**
     * Initializes a random edge-weighted graph with <tt>V</tt> vertices and <em>E</em> aristas.
     *
     * @param  V the number of vertices
     * @param  E the number of aristas
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     * @throws IllegalArgumentException if <tt>E</tt> < 0
     */
    public GrafoAristaPonderada(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException(
                "El número de vértices no debe ser negativo");
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            double peso = Math.round(100 * StdRandom.uniform()) / 100.0;
            Arista a = new Arista(v, w, peso);
            agregarArista(a);
        }
    }

    /**  
     * Initializes an edge-weighted graph from an input stream.
     * The format is the number of vertices <em>V</em>,
 followed by the number of aristas <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * entrada  in the input stream
     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or aristas is negative
     */
    public GrafoAristaPonderada(In entrada) {
        this(entrada.readInt());
        int E = entrada.readInt();
        if (E < 0) throw new IllegalArgumentException(
                "El número de vértices no debe ser negativo");
        for (int i = 0; i < E; i++) {
            int v = entrada.readInt();
            int w = entrada.readInt();
            double peso = entrada.readDouble();
            Arista a = new Arista(v, w, peso);
            agregarArista(a);
        }
    }

    /**
     * Initializes a new edge-weighted graph that is a deep copy of <tt>G</tt>.
     *
     * @param  G the edge-weighted graph to copy
     */
    public GrafoAristaPonderada(GrafoAristaPonderada G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Pila<Arista> reverso = new Pila<Arista>();
            for (Arista a : G.ady[v]) {
                reverso.push(a);
            }
            for (Arista a : reverso) {
                ady[v].agregar(a);
            }
        }
    }


    /**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of aristas in this edge-weighted graph.
     *
     * @return the number of aristas in this edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validarVertice(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException(
                    "el vértice " + v + " no está entre 0 y " + (V-1));
    }

    /**
     * Adds the undirected edge <tt>e</tt> to this edge-weighted graph.
     *
     * @param  a the edge
     * @throws IndexOutOfBoundsException unless both endpoints are between 0 and V-1
     */
    public void agregarArista(Arista a) {
        int v = a.unVertice();
        int w = a.otroVertice(v);
        validarVertice(v);
        validarVertice(w);
        ady[v].agregar(a);
        ady[w].agregar(a);
        E++;
    }

    /**
     * Returns the aristas incident on vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the aristas incident on vertex <tt>v</tt> as an Iterable
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Arista> adj(int v) {
        validarVertice(v);
        return ady[v];
    }

    /**
     * Returns the grado of vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the grado of vertex <tt>v</tt>               
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int grado(int v) {
        validarVertice(v);
        return ady[v].tamanno();
    }

    /**
     * Returns all aristas in this edge-weighted graph.
     * To iterate over the aristas in this edge-weighted graph, use foreach notation:
 <tt>for (Arista e : G.aristas())</tt>.
     *
     * @return all aristas in this edge-weighted graph, as an iterable
     */
    public Iterable<Arista> aristas() {
        Bolsa<Arista> lista = new Bolsa<Arista>();
        for (int v = 0; v < V; v++) {
            int lazos = 0;
            for (Arista e : adj(v)) {
                if (e.otroVertice(v) > v) {
                    lista.agregar(e);
                }
                // only agregar one copy of each self loop (self loops will be consecutive)
                else if (e.otroVertice(v) == v) {
                    if (lazos % 2 == 0) lista.agregar(e);
                    lazos++;
                }
            }
        }
        return lista;
    }

    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of aristas <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of aristas
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NUEVALINEA);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Arista a : ady[v]) {
                s.append(a + "  ");
            }
            s.append(NUEVALINEA);
        }
        return s.toString();
    }

    /**
     * Unit tests the <tt>GrafoAristaPonderada</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In("tinyEWG.txt");
        GrafoAristaPonderada G = new GrafoAristaPonderada(entrada);
        StdOut.println(G);
    }

}

