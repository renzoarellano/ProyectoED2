/******************************************************************************
 *  Compilation:  javac Queue.java
 *  Execution:    java Queue < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/13stacks/tobe.txt  
 *
 *  A generic queue, implemented using a linked list.
 *
 *  % java Queue < tobe.txt 
 *  to be or not to be (2 left on queue)
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The <tt>Cola</tt> class represents a first-in-first-out (FIFO)
 *  queue of generic items.
 *  It supports the usual <em>entrarACola</em> and <em>salirDeCola</em>
 *  operations, along with methods for peeking at the first item,
 *  testing if the queue is empty, and iterating through
 *  the items in FIFO order.
 *  <p>
 *  This implementation uses a singly-linked list with a static nested class for
 *  linked-list nodes. See {@link LinkedQueue} for the version from the
 *  textbook that uses a non-static nested class.
 *  The <em>entrarACola</em>, <em>salirDeCola</em>, <em>enFrente</em>, <em>tamanno</em>, and <em>is-empty</em>
 *  operations all take constant time in the worst case.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/13stacks">Section 1.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Tipo> the generic type of an item in this bag
 */
public class Cola<Tipo> implements Iterable<Tipo> {
    private int N;               // number of elements on queue
    private Nodo<Tipo> primero;    // beginning of queue
    private Nodo<Tipo> ultimo;     // end of queue

    // helper linked list class
    private static class Nodo<Item> {
        private Item item;
        private Nodo<Item> prox;
    }

    /**
     * Initializes an empty queue.
     */
    public Cola() {
        primero = null;
        ultimo  = null;
        N = 0;
    }

    /**
     * Returns true if this queue is empty.
     *
     * @return <tt>true</tt> if this queue is empty; <tt>false</tt> otherwise
     */
    public boolean estaVacia() {
        return primero == null;
    }

    /**
     * Returns the number of items in this queue.
     *
     * @return the number of items in this queue
     */
    public int tamanno() {
        return N;     
    }

    /**
     * Returns the item least recently added to this queue.
     *
     * @return the item least recently added to this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public Tipo enFrente() {
        if (estaVacia()) throw new NoSuchElementException("Cola vacía");
        return primero.item;
    }

    /**
     * Adds the item to this queue.
     *
     * @param  item the item to add
     */
    public void entrarACola(Tipo item) {
        Nodo<Tipo> antiguoUltimo = ultimo;
        ultimo = new Nodo<Tipo>();
        ultimo.item = item;
        ultimo.prox = null;
        if (estaVacia()) primero = ultimo;
        else           antiguoUltimo.prox = ultimo;
        N++;
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     *
     * @return the item on this queue that was least recently added
     * @throws NoSuchElementException if this queue is empty
     */
    public Tipo salirDeCola() {
        if (estaVacia()) throw new NoSuchElementException("Cola vacía");
        Tipo item = primero.item;
        primero = primero.prox;
        N--;
        if (estaVacia()) ultimo = null;   // to avoid loitering
        return item;
    }

    /**
     * Returns a string representation of this queue.
     *
     * @return the sequence of items in FIFO order, separated by spaces
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Tipo item : this)
            s.append(item + " ");
        return s.toString();
    } 

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    public Iterator<Tipo> iterator()  {
        return new IteradorDeLista<Tipo>(primero);  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class IteradorDeLista<Item> implements Iterator<Item> {
        private Nodo<Item> actual;

        public IteradorDeLista(Nodo<Item> primero) {
            actual = primero;
        }

        public boolean hasNext()  { return actual != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = actual.item;
            actual = actual.prox; 
            return item;
        }
    }


    /**
     * Unit tests the <tt>Cola</tt> data type.
     */
    public static void main(String[] args) {
        Cola<String> q = new Cola<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.entrarACola(item);
            else if (!q.estaVacia()) StdOut.print(q.salirDeCola() + " ");
        }
        StdOut.println("(" + q.tamanno() + " left on queue)");
    }
}
/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
