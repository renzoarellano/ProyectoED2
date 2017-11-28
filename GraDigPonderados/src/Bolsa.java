/******************************************************************************
 *  Compilation:  javac Bag.java
 *  Execution:    java Bag < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  A generic bag or multiset, implemented using a singly-linked list.
 *
 *  % more tobe.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java Bag < tobe.txt
 *  size of bag = 14
 *  is
 *  -
 *  -
 *  -
 *  that
 *  -
 *  -
 *  be
 *  -
 *  to
 *  not
 *  or
 *  be
 *  to
 *
 ******************************************************************************/


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The <tt>Bolsa</tt> class represents a bag (or multiset) of 
 *  generic items. It supports insertion and iterating over the 
 *  items in arbitrary order.
 *  <p>
 *  This implementation uses a singly-linked list with a static nested class Node.
 *  See {@link LinkedBag} for the version from the
 *  textbook that uses a non-static nested class.
 *  The <em>agregar</em>, <em>estaVacio</em>, and <em>tamanno</em> operations
 *  take constant time. Iteration takes time proportional to the number of items.
 *  <p>
 *
 *  @param <Item> the generic type of an item in this bag
 */
public class Bolsa<Item> implements Iterable<Item> {
    private int N;               // number of elements in bag
    private Nodo<Item> primero;    // beginning of bag

    // helper linked list class
    private static class Nodo<Item> {
        private Item item;
        private Nodo<Item> prox;
    }

    /**
     * Initializes an empty bag.
     */
    public Bolsa() {
        primero = null;
        N = 0;
    }

    /**
     * Returns true if this bag is empty.
     *
     * @return <tt>true</tt> if this bag is empty;
     *         <tt>false</tt> otherwise
     */
    public boolean estaVacio() {
        return primero == null;
    }

    /**
     * Returns the number of items in this bag.
     *
     * @return the number of items in this bag
     */
    public int tamanno() {
        return N;
    }

    /**
     * Adds the item to this bag.
     *
     * @param  item the item to agregar to this bag
     */
    public void agregar(Item item) {
        Nodo<Item> anteriorPrimero = primero;
        primero = new Nodo<Item>();
        primero.item = item;
        primero.prox = anteriorPrimero;
        N++;
    }


    /**
     * Returns an iterator that iterates over the items in this bag in arbitrary order.
     *
     * @return an iterator that iterates over the items in this bag in arbitrary order
     */
    public Iterator<Item> iterator()  {
        return new IteradorDeLista<Item>(primero);  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class IteradorDeLista<Item> implements Iterator<Item> {
        private Nodo<Item> actual;

        public IteradorDeLista(Nodo<Item> primero) {
            actual = primero;
        }

        public boolean hasNext()  { return actual != null;                    }
        public void remove()      { throw new UnsupportedOperationException();}

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = actual.item;
            actual = actual.prox; 
            return item;
        }
    }

    /**
     * Unit tests the <tt>Bolsa</tt> data type.
     */
    public static void main(String[] args) {
        Bolsa<String> bolsa = new Bolsa<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            bolsa.agregar(item);
        }

        StdOut.println("tamaño de bolsa = " + bolsa.tamanno());
        for (String s : bolsa) {
            StdOut.println(s);
        }
    }


}
