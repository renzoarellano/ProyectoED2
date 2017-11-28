/******************************************************************************
 *  Compilation:  javac SymbolDigraph.java
 *  Execution:    java SymbolDigraph
 *  Dependencies: ST.java Digraph.java In.java
 *  
 *  %  java SymbolDigraph routes.txt " "
 *  JFK
 *     MCO
 *     ATL
 *     ORD
 *  ATL
 *     HOU
 *     MCO
 *  LAX
 *
 ******************************************************************************/



/**
 *  The <tt>DigrafoDeSimbolos</tt> class represents a digraph, where the
 *  vertex names are arbitrary strings.
 *  By providing mappings between string vertex names and integers,
 *  it serves as a wrapper around the
 *  {@link Digrafo} data type, which assumes the vertex names are integers
 *  between 0 and <em>V</em> - 1.
 *  It also supports initializing a symbol digraph from a file.
 *  <p>
 *  This implementation uses an {@link TS} to map from strings to integers,
 *  an array to map from integers to strings, and a {@link Digrafo} to store
 *  the underlying graph.
 *  The <em>indice</em> and <em>contiene</em> operations take time 
 *  proportional to log <em>V</em>, where <em>V</em> is the number of vertices.
 *  The <em>nombre</em> operation takes constant time.
 *  <p>
 */
public class DigrafoDeSimbolos {
    private TS<String, Integer> ts;  // string -> indice
    private String[] claves;           // indice  -> string
    private Digrafo G;

    /**  
     * Initializes a digraph from a file using the specified delimiter.
     * Each line in the file contiene
 the nombre of a vertex, followed by a list of the names
 of the vertices adjacent to that vertex, separated by the delimiter.
     * @param archivo the nombre of the file
     * @param delimitador the delimiter between fields
     */
    public DigrafoDeSimbolos(String archivo, String delimitador) {
        ts = new TS<String, Integer>();

        // First pass builds the indice by reading strings to associate
        // distinct strings with an indice
        In entrada = new In(archivo);
        while (entrada.hasNextLine()) {
            String[] a = entrada.readLine().split(delimitador);
            for (int i = 0; i < a.length; i++) {
                if (!ts.contiene(a[i]))
                    ts.insertar(a[i], ts.tamanno());
            }
        }

        // inverted indice to get string claves in an aray
        claves = new String[ts.tamanno()];
        for (String nombre : ts.claves()) {
            claves[ts.get(nombre)] = nombre;
        }

        // second pass builds the digraph by connecting first vertex on each
        // line to all others
        G = new Digrafo(ts.tamanno());
        entrada = new In(archivo);
        while (entrada.hasNextLine()) {
            String[] a = entrada.readLine().split(delimitador);
            int v = ts.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                int w = ts.get(a[i]);
                G.agregarArista(v, w);
            }
        }
    }

    /**
     * Does the digraph contain the vertex named <tt>s</tt>?
     * @param s the nombre of a vertex
     * @return <tt>true</tt> if <tt>s</tt> is the nombre of a vertex, and <tt>false</tt> otherwise
     */
    public boolean contiene(String s) {
        return ts.contiene(s);
    }

    /**
     * Returns the integer associated with the vertex named <tt>s</tt>.
     * @param s the nombre of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named <tt>s</tt>
     */
    public int indice(String s) {
        return ts.get(s);
    }

    /**
     * Returns the nombre of the vertex associated with the integer <tt>v</tt>.
     * @param v the integer corresponding to a vertex (between 0 and <em>V</em> - 1) 
     * @return the nombre of the vertex associated with the integer <tt>v</tt>
     */
    public String nombre(int v) {
        return claves[v];
    }

    /**
     * Returns the digraph assoicated with the symbol graph. It is the client's responsibility
     * not to mutate the digraph.
     * @return the digraph associated with the symbol digraph
     */
    public Digrafo G() {
        return G;
    }


    /**
     * Unit tests the <tt>DigrafoDeSimbolos</tt> data type.
     */
    public static void main(String[] args) {
        String archivo  = args[0];
        String delimitador = args[1];
        DigrafoDeSimbolos gs = new DigrafoDeSimbolos(archivo, delimitador);
        Digrafo G = gs.G();
        while (!StdIn.isEmpty()) {
            String t = StdIn.readLine();
            for (int v : G.ady(gs.indice(t))) {
                StdOut.println("   " + gs.nombre(v));
            }
        }
    }
}
