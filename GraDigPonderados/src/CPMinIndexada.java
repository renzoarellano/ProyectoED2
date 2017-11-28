/******************************************************************************
 *  Compilation:  javac IndexMinPQ.java
 *  Execution:    java IndexMinPQ
 *  Dependencies: StdOut.java
 *
 *  Minimum-oriented indexed PQ implementation using a binary heap.
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The <tt>CPMinIndexada</tt> class represents an indexed priority queue of generic keys.
 *  It supports the usual <em>insertar</em> and <em>eliminar-the-minimum</em>
 *  operations, along with <em>eliminar</em> and <em>cambiar-the-key</em> 
 *  methods. In order to let the client refer to keys on the priority queue,
 *  an integer between 0 and maxN-1 is associated with each key&mdash;the client
  uses this integer to specify which key to eliminar or cambiar.
  It also supports methods for peeking at the minimum key,
  testing if the priority queue is empty, and iterating through
  the keys.
  <p>
 *  This implementation uses a binary heap along with an array to associate
 *  keys with integers in the given range.
 *  The <em>insertar</em>, <em>eliminar-the-minimum</em>, <em>eliminar</em>,
 *  <em>cambiar-key</em>, <em>decrease-key</em>, and <em>increase-key</em>
 *  operations take logarithmic time.
 *  The <em>is-empty</em>, <em>tamanno</em>, <em>min-index</em>, <em>min-key</em>, and <em>key-of</em>
 *  operations take constant time.
 *  Construction takes time proportional to the specified capacity.
 *  <p>
 *
 *  @param <Clave> the generic type of key on this priority queue
 */
public class CPMinIndexada<Clave extends Comparable<Clave>> implements Iterable<Integer> {
    private int maxN;        // maximum number of elements on PQ
    private int N;           // number of elements on PQ
    private int[] cp;        // binary heap using 1-based indexing
    private int[] cpi;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Clave[] claves;      // keys[i] = priority of i

    /**
     * Initializes an empty indexed priority queue with indices between <tt>0</tt>
     * and <tt>maxN - 1</tt>.
     * @param  maxN the keys on this priority queue are index from <tt>0</tt>
     *         <tt>maxN - 1</tt>
     * @throws IllegalArgumentException if <tt>maxN</tt> &lt; <tt>0</tt>
     */
    public CPMinIndexada(int maxN) {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        claves = (Clave[]) new Comparable[maxN + 1];    // make this of length maxN??
        cp   = new int[maxN + 1];
        cpi   = new int[maxN + 1];                   // make this of length maxN??
        for (int i = 0; i <= maxN; i++)
            cpi[i] = -1;
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
     * Is <tt>i</tt> an index on this priority queue?
     *
     * @param  i an index
     * @return <tt>true</tt> if <tt>i</tt> is an index on this priority queue;
     *         <tt>false</tt> otherwise
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     */
    public boolean contiene(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        return cpi[i] != -1;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int tamanno() {
        return N;
    }

    /**
     * Associates key with index <tt>i</tt>.
     *
     * @param  i an index
     * @param  clave the key to associate with index <tt>i</tt>
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws IllegalArgumentException if there already is an item associated
     *         with index <tt>i</tt>
     */
    public void insertar(int i, Clave clave) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (contiene(i)) throw new IllegalArgumentException(
                "índice ya se encuentra en la cola de prioridad");
        N++;
        cpi[i] = N;
        cp[N] = i;
        claves[i] = clave;
        emerger(N);
    }

    /**
     * Returns an index associated with a minimum key.
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int indiceMin() { 
        if (N == 0) throw new NoSuchElementException(
                "Desbordamiento de cola de prioridad");
        return cp[1];        
    }

    /**
     * Returns a minimum key.
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Clave claveMin() { 
        if (N == 0) throw new NoSuchElementException(
                "Desbordamiento de cola de prioridad");
        return claves[cp[1]];        
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMin() { 
        if (N == 0) throw new NoSuchElementException(
                "Desbordamiento de cola de prioridad");
        int min = cp[1];        
        intercambiar(1, N--); 
        hundir(1);
        cpi[min] = -1;            // eliminar
        claves[cp[N+1]] = null;    // to help with garbage collection
        cp[N+1] = -1;            // not needed
        return min; 
    }

    /**
     * Returns the key associated with index <tt>i</tt>.
     *
     * @param  i the index of the key to return
     * @return the key associated with index <tt>i</tt>
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
     */
    public Clave claveDe(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contiene(i)) throw new NoSuchElementException(
                "el índice no se encuentra en la cola de prioridad");
        else return claves[i];
    }

    /**
     * Change the key associated with index <tt>i</tt> to the specified value.
     *
     * @param  i the index of the key to cambiar
     * @param key cambiar the key assocated with index <tt>i</tt> to this key
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @deprecated Replaced by changeKey()
     */
    @Deprecated public void cambiar(int i, Clave key) {
        cambiarClave(i, key);
    }

    /**
     * Change the key associated with index <tt>i</tt> to the specified value.
     *
     * @param  i the index of the key to cambiar
     * @param  clave cambiar the key assocated with index <tt>i</tt> to this key
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
     */
    public void cambiarClave(int i, Clave clave) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contiene(i)) throw new NoSuchElementException(
                "el índice no se encuentra en la cola de prioridad");
        claves[i] = clave;
        emerger(cpi[i]);
        hundir(cpi[i]);
    }

    /**
     * Decrease the key associated with index <tt>i</tt> to the specified value.
     *
     * @param  i the index of theclavekey to decrease
     * @param  key decrease the key assocated with index <tt>i</tt> to this key
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws IllegalArgumentException if key &ge; key associated with index <tt>i</tt>
     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
     */
    public void decrementarClave(int i, Clave clave) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contiene(i)) throw new NoSuchElementException(
                "el índice no se encuentra en la cola de prioridad");
        if (claves[i].compareTo(clave) <= 0)
            throw new IllegalArgumentException(
                    "Llamar a decrementarClave() con un argumento dado"
                            + " podría no estrictamente decrementar la clave");
        claves[i] = clave;
        emerger(cpi[i]);
    }

    /**
     * Increase the key associated with index <tt>i</tt> to the specified value.
     *
     * @param  i the index of the key to increase
     * @param  clave increase the key assocated with index <tt>i</tt> to this key
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws IllegalArgumentException if key &le; key associated with index <tt>i</tt>
     * @throws NoSuchElementException no key is associated with index <tt>i</tt>
     */
    public void incrementarClave(int i, Clave clave) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contiene(i)) throw new NoSuchElementException(
                "el índice no está en la cola de prioridad");
        if (claves[i].compareTo(clave) >= 0)
            throw new IllegalArgumentException(
                    "Llamar a incrementarClave() con un argumento dado"
                            + " podría no estrictamente incrementar la clave");
        claves[i] = clave;
        hundir(cpi[i]);
    }

    /**
     * Remove the key associated with index <tt>i</tt>.
     *
     * @param  i the index of the key to remove
     * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
     * @throws NoSuchElementException no key is associated with index <t>i</tt>
     */
    public void eliminar(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contiene(i)) throw new NoSuchElementException(
                "el índice no está en la cola de prioridad");
        int indice = cpi[i];
        intercambiar(indice, N--);
        emerger(indice);
        hundir(indice);
        claves[i] = null;
        cpi[i] = -1;
    }


   /***************************************************************************
    * General helper functions.
    ***************************************************************************/
    private boolean mayorQue(int i, int j) {
        return claves[cp[i]].compareTo(claves[cp[j]]) > 0;
    }

    private void intercambiar(int i, int j) {
        int temp = cp[i];
        cp[i] = cp[j];
        cp[j] = temp;
        cpi[cp[i]] = i;
        cpi[cp[j]] = j;
    }


   /***************************************************************************
    * Heap helper functions.
    ***************************************************************************/
    private void emerger(int k)  {
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
    * Iterators.
    ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on the
     * priority queue in ascending order.
     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    public Iterator<Integer> iterator() { return new IteradorDeMonticulo(); }

    private class IteradorDeMonticulo implements Iterator<Integer> {
        // create a new pq
        private CPMinIndexada<Clave> copy;

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        public IteradorDeMonticulo() {
            copy = new CPMinIndexada<Clave>(cp.length - 1);
            for (int i = 1; i <= N; i++)
                copy.insertar(cp[i], claves[cp[i]]);
        }

        public boolean hasNext()  { 
            return !copy.estaVacia();                     }
        public void remove()      { 
            throw new UnsupportedOperationException();  }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }


    /**
     * Unit tests the <tt>CPMinIndexada</tt> data type.
     */
    public static void main(String[] args) {
        // insertar a bunch of strings
        String[] cadenas = 
            { "it", "was", "the", "best", "of", "times", "it",
                "was", "the", "worst" };

        CPMinIndexada<String> cp = new CPMinIndexada<String>(cadenas.length);
        for (int i = 0; i < cadenas.length; i++) {
            cp.insertar(i, cadenas[i]);
        }

        // eliminar and print each key
        while (!cp.estaVacia()) {
            int i = cp.delMin();
            StdOut.println(i + " " + cadenas[i]);
        }
        StdOut.println();

        // reinsert the same strings
        for (int i = 0; i < cadenas.length; i++) {
            cp.insertar(i, cadenas[i]);
        }

        // print each key using the iterator
        for (int i : cp) {
            StdOut.println(i + " " + cadenas[i]);
        }
        while (!cp.estaVacia()) {
            cp.delMin();
        }

    }
}

