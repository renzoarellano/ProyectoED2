/******************************************************************************
 *  Compilation:  javac Digraph.java
 *  Execution:    java Digraph filename.txt
 *  Dependencies: Bag.java In.java StdOut.java
 *  Data files:   tinyDG.txt
 *
 *  A graph, implemented using an array of lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java Digraph tinyDG.txt
 *  13 vertices, 22 edges
 *  0: 5 1 
 *  1: 
 *  2: 0 3 
 *  3: 5 2 
 *  4: 3 2 
 *  5: 4 
 *  6: 9 4 8 0 
 *  7: 6 9
 *  8: 6 
 *  9: 11 10 
 *  10: 12 
 *  11: 4 12 
 *  12: 9 
 *  
 ******************************************************************************/


import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 *  The <tt>Digrafo</tt> class represents a directed graph of vertices
 *  named 0 through <em>V</em> - 1.
 *  It supports the following two primary operations: agregar an edge to the digraph,
  iterate over all of the vertices adjacent from a given vertex.
  Parallel edges and self-loops are permitted.
  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bolsa} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the vertices adjacent from a given vertex, which takes
 *  time proportional to the number of such vertices.
 *  <p>
 */

public class Digrafo {
    private static final String NUEVALINEA = 
            System.getProperty("line.separator");

    private final int V;
    private int A;
    private Bolsa<Integer>[] ady;
    
    /**
     * Initializes an empty digraph with <em>V</em> vertices.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if V < 0
     */
    public Digrafo(int V) {
        if (V < 0) throw new IllegalArgumentException(
                "El número de vértices en un Dígrafo no debe ser negativo");
        this.V = V;
        this.A = 0;
        ady = (Bolsa<Integer>[]) new Bolsa[V];
        for (int v = 0; v < V; v++) {
            ady[v] = new Bolsa<Integer>();
        }
    }

    /**  
     * Initializes a digraph from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>A</em>,
     * followed by <em>A</em> pairs of vertices, with each entry separated by whitespace.
     *
     * @param  entrada the input stream
     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public Digrafo(In entrada) {
        try {
            this.V = entrada.readInt();
            if (V < 0) throw new IllegalArgumentException(
                    "El número de vértices en un Dígrafo no debe ser negativo");
            ady = (Bolsa<Integer>[]) new Bolsa[V];
            for (int v = 0; v < V; v++) {
                ady[v] = new Bolsa<Integer>();
            }
            int E = entrada.readInt();
            if (E < 0) throw new IllegalArgumentException(
                    "El número de vértices en un Dígrafo no debe ser negativo");
            for (int i = 0; i < E; i++) {
                int v = entrada.readInt();
                int w = entrada.readInt();
                agregarArista(v, w); 
            }
        }
        catch (NoSuchElementException e) {
            throw new InputMismatchException(
                    "Formato de entrada inválido en el constructor de Digrafo");
        }
    }

    /**
     * Initializes a new digraph that is a deep copy of <tt>G</tt>.
     *
     * @param  G the digraph to copy
     */
    public Digrafo(Digrafo G) {
        this(G.V());
        this.A = G.A();
        for (int v = 0; v < G.V(); v++) {
            // reverso so that adjacency list is in same order as original
            Pila<Integer> reverso = new Pila<Integer>();
            for (int w : G.ady[v]) {
                reverso.push(w);
            }
            for (int w : reverso) {
                ady[v].agregar(w);
            }
        }
    }
        
    /**
     * Returns the number of vertices in this digraph.
     *
     * @return the number of vertices in this digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this digraph.
     *
     * @return the number of edges in this digraph
     */
    public int A() {
        return A;
    }


    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validarVertice(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException(
                    "vértice " + v + " no está entre 0 y " + (V-1));
    }

    /**
     * Adds the directed edge v->w to this digraph.
     *
     * @param  v the tail vertex
     * @param  w the head vertex
     * @throws IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V
     */
    public void agregarArista(int v, int w) {
        validarVertice(v);
        validarVertice(w);
        ady[v].agregar(w);
        A++;
    }
    


    /**
     * Returns the vertices adjacent from vertex <tt>v</tt> in this digraph.
     *
     * @param  v the vertex
     * @return the vertices adjacent from vertex <tt>v</tt> in this digraph, as an iterable
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Integer> ady(int v) {
        validarVertice(v);
        return ady[v];
    }

    /**
     * Returns the number of directed edges incident from vertex <tt>v</tt>.
     * This is known as the <em>gradoSalida</em> of vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the gradoSalida of vertex <tt>v</tt>               
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int gradoSalida(int v) {
        validarVertice(v);
        return ady[v].tamanno();
    }

    /**
     * Returns the reverso of the digraph.
     *
     * @return the reverso of the digraph
     */
    public Digrafo reverso() {
        Digrafo R = new Digrafo(V);
        for (int v = 0; v < V; v++) {
            for (int w : ady(v)) {
                R.agregarArista(w, v);
            }
        }
        return R;
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>A</em>,  
     *         followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vértices, " + A + " aristas " + NUEVALINEA);
        for (int v = 0; v < V; v++) {
            s.append(String.format("%d: ", v));
            for (int w : ady[v]) {
                s.append(String.format("%d ", w));
            }
            s.append(NUEVALINEA);
        }
        return s.toString();
    }

    /**
     * Unit tests the <tt>Digrafo</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digrafo G = new Digrafo(in);
        StdOut.println(G);
    }

}