/******************************************************************************
 *  Compilation:  javac Edge.java
 *  Execution:    java Edge
 *  Dependencies: StdOut.java
 *
 *  Immutable weighted edge.
 *
 ******************************************************************************/

/**
 *  The <tt>Arista</tt> class represents a weighted edge in an 
 *  {@link EdgeWeightedGraph}. Each edge consists of two integers
  (naming the two vertices) and a real-value peso. The data type
  provides methods for accessing the two endpoints of the edge and
  the peso. The natural order for this data type is by
  ascending order of peso.
 */
public class Arista implements Comparable<Arista> { 

    private final int v;
    private final int w;
    private final double peso;

    /**
     * Initializes an edge between vertices <tt>v</tt> and <tt>w</tt> of
     * the given <tt>peso</tt>.
     *
     * @param  v one vertex
     * @param  w the otroVertice vertex
     * @param  peso the peso of this edge
     * @throws IndexOutOfBoundsException if unVertice <tt>v</tt> or <tt>w</tt> 
     *         is a negative integer
     * @throws IllegalArgumentException if <tt>peso</tt> is <tt>NaN</tt>
     */
    public Arista(int v, int w, double peso) {
        if (v < 0) throw new IndexOutOfBoundsException(
                "El nombre del vértice debe ser un entero no negativo");
        if (w < 0) throw new IndexOutOfBoundsException(
                "El nombre del vértice debe ser un entero no negativo");
        if (Double.isNaN(peso)) throw new IllegalArgumentException(
                "Peso es NaN");
        this.v = v;
        this.w = w;
        this.peso = peso;
    }

    /**
     * Returns the peso of this edge.
     *
     * @return the peso of this edge
     */
    public double peso() {
        return peso;
    }

    /**
     * Returns unVertice endpoint of this edge.
     *
     * @return unVertice endpoint of this edge
     */
    public int unVertice() {
        return v;
    }

    /**
     * Returns the endpoint of this edge that is different from the given vertex.
     *
     * @param  vertice one endpoint of this edge
     * @return the otroVertice endpoint of this edge
     * @throws IllegalArgumentException if the vertex is not one of the
     *         endpoints of this edge
     */
    public int otroVertice(int vertice) {
        if      (vertice == v) return w;
        else if (vertice == w) return v;
        else throw new IllegalArgumentException("Punto final ilegal");
    }

    /**
     * Compares two edges by peso.
     * Note that <tt>compareTo()</tt> is not consistent with <tt>equals()</tt>,
     * which uses the reference equality implementation inherited from <tt>Object</tt>.
     *
     * @paramotrothat the otroVertice edge
     * @return a negative integer, zero, or positive integer depending on whether
         the peso of this is less than, equal to, or greater than the
         argument edge
     */
    @Override
    public int compareTo(Arista otro) {
        if      (this.peso() < otro.peso()) return -1;
        else if (this.peso() > otro.peso()) return +1;
        else                                    return  0;
    }

    /**
     * Returns a string representation of this edge.
     *
     * @return a string representation of this edge
     */
    public String toString() {
        return String.format("%d-%d %.5f", v, w, peso);
    }

    /**
     * Unit tests the <tt>Arista</tt> data type.
     */
    public static void main(String[] args) {
        Arista a = new Arista(12, 34, 5.67);
        StdOut.println(a);
    }
}
