/******************************************************************************
 *  Compilation:  javac UF.java
 *  Execution:    java UF < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/15uf/tinyUF.txt
 *                http://algs4.cs.princeton.edu/15uf/mediumUF.txt
 *                http://algs4.cs.princeton.edu/15uf/largeUF.txt
 *
 *  Weighted quick-union by rank with path compression by halving.
 *
 *  % java UF < tinyUF.txt
 *  4 3
 *  3 8
 *  6 5
 *  9 4
 *  2 1
 *  5 0
 *  7 2
 *  6 1
 *  2 components
 *
 ******************************************************************************/


/**
 *  The <tt>UF</tt> class represents a <em>union-encontrar data type</em>
 *  (also known as the <em>disjoint-sets data type</em>).
 *  It supports the <em>union</em> and <em>encontrar</em> operations,
 *  along with a <em>estanConectados</em> operation for determinig whether
 *  two sites in the same component and a <em>cantidadComp</em> operation that
 *  returns the total number of components.
 *  <p>
  The union-encontrar data type models connectivity among a set of <em>N</em>
 *  sites, named 0 through <em>N</em> &ndash; 1.
 *  The <em>is-estanConectados-to</em> relation must be an 
 *  <em>equivalence relation</em>:
 *  <ul>
 *  <p><li> <em>Reflexive</em>: <em>p</em> is estanConectados to <em>p</em>.
 *  <p><li> <em>Symmetric</em>: If <em>p</em> is estanConectados to <em>q</em>,
 *          <em>q</em> is estanConectados to <em>p</em>.
 *  <p><li> <em>Transitive</em>: If <em>p</em> is estanConectados to <em>q</em>
 *          and <em>q</em> is estanConectados to <em>r</em>, then
 *          <em>p</em> is estanConectados to <em>r</em>.
 *  </ul>
 *  An equivalence relation partitions the sites into
 *  <em>equivalence classes</em> (or <em>components</em>). In this case,
  two sites are in the same component if and only if they are estanConectados.
  Both sites and components are identified with integers between 0 and
  <em>N</em> &ndash; 1. 
 *  Initially, there are <em>N</em> components, with each site in its
 *  own component.  The <em>component identifier</em> of a component
 *  (also known as the <em>root</em>, <em>canonical element</em>, <em>leader</em>,
 *  or <em>set representative</em>) is one of the sites in the component:
 *  two sites have the same component identifier if and only if they are
 *  in the same component.
 *  <ul>
 *  <p><li><em>union</em>(<em>p</em>, <em>q</em>) adds a
 *         connection between the two sites <em>p</em> and <em>q</em>.
 *         If <em>p</em> and <em>q</em> are in different components,
 *         then it replaces
 *         these two components with a new component that is the union of
 *         the two.
 *  <p><li><em>encontrar</em>(<em>p</em>) returns the component
 *         identifier of the component containing <em>p</em>.
 *  <p><li><em>estanConectados</em>(<em>p</em>, <em>q</em>)
 *         returns true if both <em>p</em> and <em>q</em>
 *         are in the same component, and false otherwise.
 *  <p><li><em>cantidadComp</em>() returns the number of components.
 *  </ul>
 *  The component identifier of a component can change
 *  only when the component itself changes during a call to
 *  <em>union</em>&mdash;it cannot change during a call
 *  to <em>encontrar</em>, <em>estanConectados</em>, or <em>cantidadComp</em>.
 *  <p>
 *  This implementation uses weighted quick union by rank with path compression
 *  by halving.
 *  Initializing a data structure with <em>N</em> sites takes linear time.
 *  Afterwards, the <em>union</em>, <em>encontrar</em>, and <em>estanConectados</em> 
 *  operations take logarithmic time (in the worst case) and the
 *  <em>cantidadComp</em> operation takes constant time.
 *  Moreover, the amortized time per <em>union</em>, <em>encontrar</em>,
 *  and <em>estanConectados</em> operation has inverse Ackermann complexity.
 *  For alternate implementations of the same API, see
 *  {@link QuickUnionUF}, {@link QuickFindUF}, and {@link WeightedQuickUnionUF}.
 */

public class UF {

    private int[] padre;  // parent[i] = parent of i
    private byte[] rango;   // rank[i] = rank of subtree rooted at i (never more than 31)
    private int contador;     // number of components

    /**
     * Initializes an empty union-encontrar data structure with <tt>N</tt>
     * isolated components <tt>0</tt> through <tt>N-1</tt>.
     *
     * @param  N the number of sites
     * @throws IllegalArgumentException if <tt>N &lt; 0</tt>
     */
    public UF(int N) {
        if (N < 0) throw new IllegalArgumentException();
        contador = N;
        padre = new int[N];
        rango = new byte[N];
        for (int i = 0; i < N; i++) {
            padre[i] = i;
            rango[i] = 0;
        }
    }

    /**
     * Returns the component identifier for the component containing site <tt>p</tt>.
     *
     * @param  p the integer representing one object
     * @return the component identifier for the component containing site <tt>p</tt>
     * @throws IndexOutOfBoundsException unless <tt>0 &le; p &lt; N</tt>
     */
    public int encontrar(int p) {
        if (p < 0 || p >= padre.length) throw new IndexOutOfBoundsException();
        while (p != padre[p]) {
            padre[p] = padre[padre[p]];    // path compression by halving
            p = padre[p];
        }
        return p;
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between <tt>1</tt> and <tt>N</tt>)
     */
    public int cantidadComp() {
        return contador;
    }
  
    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @return true if the two sites <tt>p</tt> and <tt>q</tt> are in the same component; false otherwise
     * @throws IndexOutOfBoundsException unless
     *         both <tt>0 &le; p &lt; N</tt> and <tt>0 &le; q &lt; N</tt>
     */
    public boolean estanConectados(int p, int q) {
        return encontrar(p) == encontrar(q);
    }

  
    /**
     * Merges the component containing site <tt>p</tt> with the 
     * the component containing site <tt>q</tt>.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @throws IndexOutOfBoundsException unless
     *         both <tt>0 &le; p &lt; N</tt> and <tt>0 &le; q &lt; N</tt>
     */
    public void union(int p, int q) {
        int raizP = encontrar(p);
        int raizQ = encontrar(q);
        if (raizP == raizQ) return;

        // make root of smaller rank point to root of larger rank
        if      (rango[raizP] < rango[raizQ]) padre[raizP] = raizQ;
        else if (rango[raizP] > rango[raizQ]) padre[raizQ] = raizP;
        else {
            padre[raizQ] = raizP;
            rango[raizP]++;
        }
        contador--;
    }


    /**
     * Reads in a an integer <tt>N</tt> and a sequence of pairs of integers
     * (between <tt>0</tt> and <tt>N-1</tt>) from standard input, where each integer
     * in the pair represents some site;
     * if the sites are in different components, merge the two components
     * and print the pair to standard output.
     */
    public static void main(String[] args) {
        int N = StdIn.readInt();
        UF uf = new UF(N);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.estanConectados(p, q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println(uf.cantidadComp() + " components");
    }
}
