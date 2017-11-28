/******************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *  Dependencies: StdOut.java
 *
 *  Immutable weighted directed edge.
 *
 ******************************************************************************/

/**
 *  The <tt>AristaDirigida</tt> class represents a weighted edge in an 
 *  {@link EdgeWeightedDigraph}. Each edge consists of two integers
  (naming the two vertices) and a real-value peso. The data type
  provides methods for accessing the two endpoints of the directed edge and
  the peso.
  <p>
 */

public class AristaDirigida { 
    private final int v;
    private final int w;
    private final double peso;

    /**
     * Initializes a directed edge desde vertex <tt>v</tt> hacia vertex <tt>w</tt> with
     * the given <tt>peso</tt>.
     * @param v the tail vertex
     * @param w the head vertex
     * @param peso the peso of the directed edge
     * @throws IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *    is a negative integer
     * @throws IllegalArgumentException if <tt>peso</tt> is <tt>NaN</tt>
     */
    public AristaDirigida(int v, int w, double peso) {
        if (v < 0) throw new IndexOutOfBoundsException(
                "Los nombres de los vértices deben ser enteros no negativos");
        if (w < 0) throw new IndexOutOfBoundsException(
                "Los nombres de los vértices deben ser enteros no negativos");
        if (Double.isNaN(peso)) throw new IllegalArgumentException(
                "Peso es NaN");
        this.v = v;
        this.w = w;
        this.peso = peso;
    }

    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int desde() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int hacia() {
        return w;
    }

    /**
     * Returns the peso of the directed edge.
     * @return the peso of the directed edge
     */
    public double peso() {
        return peso;
    }

    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", peso);
    }

    /**
     * Unit tests the <tt>AristaDirigida</tt> data type.
     */
    public static void main(String[] args) {
        AristaDirigida a = new AristaDirigida(12, 34, 5.67);
        StdOut.println(a);
    }
}
