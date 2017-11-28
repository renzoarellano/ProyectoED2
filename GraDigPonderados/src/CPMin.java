/******************************************************************************
 *  Compilation:  javac MinPQ.java
 *  Execution:    java MinPQ < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  
 *  Generic min priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order.
 *
 *  % java MinPQ < tinyPQ.txt
 *  E A E (6 left on pq)
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 *
 ******************************************************************************/

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The <tt>CPMin</tt> class represents a priority queue of generic keys.
 *  It supports the usual <em>insertar</em> and <em>delete-the-minimum</em>
 *  operations, along with methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a binary heap.
 *  The <em>insertar</em> and <em>delete-the-minimum</em> operations take
 *  logarithmic amortized time.
 *  The <em>min</em>, <em>tamanio</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes time proportional to the specified capacity or the number of
 *  items used to initialize the data structure.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Clave> the generic type of key on this priority queue
 */
public class CPMin<Clave> implements Iterable<Clave> {
    private Clave[] cp;                    // store items at indices 1 to N
    private int N;                       // number of items on priority queue
    private Comparator<Clave> comparador;  // optional comparator

    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param  capacidadInicial the initial capacity of this priority queue
     */
    public CPMin(int capacidadInicial) {
        cp = (Clave[]) new Object[capacidadInicial + 1];
        N = 0;
    }

    /**
     * Initializes an empty priority queue.
     */
    public CPMin() {
        this(1);
    }

    /**
     * Initializes an empty priority queue with the given initial capacity,
     * using the given comparator.
     *
     * @param  capacidadInicial the initial capacity of this priority queue
     * @param  comparador the order to use when comparing keys
     */
    public CPMin(int capacidadInicial, Comparator<Clave> comparador) {
        this.comparador = comparador;
        cp = (Clave[]) new Object[capacidadInicial + 1];
        N = 0;
    }

    /**
     * Initializes an empty priority queue using the given comparator.
     *
     * @param  comparador the order to use when comparing keys
     */
    public CPMin(Comparator<Clave> comparador) {
        this(1, comparador);
    }

    /**
     * Initializes a priority queue from the array of keys.
     * <p>
     * Takes time proportional to the number of keys, using sink-based heap construction.
     *
     * @param  claves the array of keys
     */
    public CPMin(Clave[] claves) {
        N = claves.length;
        cp = (Clave[]) new Object[claves.length + 1];
        for (int i = 0; i < N; i++)
            cp[i+1] = claves[i];
        for (int k = N/2; k >= 1; k--)
            hundir(k);
        assert esMonticuloMin();
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return <tt>true</tt> if this priority queue is empty;
     *         <tt>false</tt> otherwise
     */
    public boolean estaVacia() {
        return N == 0;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int tamanio() {
        return N;
    }

    /**
     * Returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Clave min() {
        if (estaVacia()) throw new NoSuchElementException("Cola de Prioridad vacía");
        return cp[1];
    }

    // helper function to double the tamanio of the heap array
    private void redimensionar(int capacidad) {
        assert capacidad > N;
        Clave[] temp = (Clave[]) new Object[capacidad];
        for (int i = 1; i <= N; i++) {
            temp[i] = cp[i];
        }
        cp = temp;
    }

    /**
     * Adds a new key to this priority queue.
     *
     * @param  x the key to add to this priority queue
     */
    public void insertar(Clave x) {
        // double tamanio of array if necessary
        if (N == cp.length - 1) redimensionar(2 * cp.length);

        // add x, and percolate it up to maintain heap invariant
        cp[++N] = x;
        emerger(N);
        assert esMonticuloMin();
    }

    /**
     * Removes and returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Clave eliminarMin() {
        if (estaVacia()) throw new NoSuchElementException("Cola de Prioridad vacía");
        intercambiar(1, N);
        Clave min = cp[N--];
        hundir(1);
        cp[N+1] = null;         // avoid loitering and help with garbage collection
        if ((N > 0) && (N == (cp.length - 1) / 4)) redimensionar(cp.length  / 2);
        assert esMonticuloMin();
        return min;
    }


   /***************************************************************************
    * Helper functions to restore the heap invariant.
    ***************************************************************************/

    private void emerger(int k) {
        while (k > 1 && mayorQue(k/2, k)) {
            intercambiar(k, k/2);
            k = k/2;
        }
    }

    private void hundir(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && mayorQue(j, j+1)) j++;
            if (!mayorQue(k, j)) break;
            intercambiar(k, j);
            k = j;
        }
    }

   /***************************************************************************
    * Helper functions for compares and swaps.
    ***************************************************************************/
    private boolean mayorQue(int i, int j) {
        if (comparador == null) {
            return ((Comparable<Clave>) cp[i]).compareTo(cp[j]) > 0;
        }
        else {
            return comparador.compare(cp[i], cp[j]) > 0;
        }
    }

    private void intercambiar(int i, int j) {
        Clave aux = cp[i];
        cp[i] = cp[j];
        cp[j] = aux;
    }

    // is pq[1..N] a min heap?
    private boolean esMonticuloMin() {
        return esMonticuloMin(1);
    }

    // is subtree of pq[1..N] rooted at k a min heap?
    private boolean esMonticuloMin(int k) {
        if (k > N) return true;
        int izq = 2*k, der = 2*k + 1;
        if (izq <= N && mayorQue(k, izq)) return false;
        if (der <= N && mayorQue(k, der)) return false;
        return esMonticuloMin(izq) && esMonticuloMin(der);
    }


    /**
     * Returns an iterator that iterates over the keys on this priority queue
     * in ascending order.
     * <p>
     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    public Iterator<Clave> iterator() { return new IteradorMonticulo(); }

    private class IteradorMonticulo implements Iterator<Clave> {
        // create a new pq
        private CPMin<Clave> copia;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public IteradorMonticulo() {
            if (comparador == null) copia = new CPMin<Clave>(tamanio());
            else                    copia = new CPMin<Clave>(tamanio(), comparador);
            for (int i = 1; i <= N; i++)
                copia.insertar(cp[i]);
        }

        public boolean hasNext()  { return !copia.estaVacia();                }
        public void remove()      { throw new UnsupportedOperationException();}

        public Clave next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copia.eliminarMin();
        }
    }

    /**
     * Unit tests the <tt>CPMin</tt> data type.
     */
    public static void main(String[] args) {
        In entrada = new In("tinyPQ.txt");
        CPMin<String> cp = new CPMin<String>();
        while (!entrada.isEmpty()) {
            String item = entrada.readString();
            if (!item.equals("-")) cp.insertar(item);
            else if (!cp.estaVacia()) StdOut.print(cp.eliminarMin() + " ");
        }
        StdOut.println("(" + cp.tamanio() + " izquierda en pq)");
    }

}
