import java.util.Iterator;
import java.util.NoSuchElementException;

public class Pila<Item> implements Iterable<Item>
{
    private int N; //tamaÃ±o de la pila
    private Nodo primero = null;//parte superior de la pila
    
    private class Nodo
    {
        private Item item;
        private Nodo prox;
    }
    
    public Pila(){
        primero = null;
        N = 0;
        assert revisar();
    }
    
    public boolean estaVacio()
    {
        return primero == null; 
    }
    
    public void push(Item item)
    {
        Nodo anteriorPrimero = primero;
        primero = new Nodo();
        primero.item = item;
        primero.prox = anteriorPrimero;
        N++;
        assert revisar();
    }
    public Item pop()
    {
        if (estaVacio()) throw new NoSuchElementException("Pila Vacia");
        Item item = primero.item;
        primero = primero.prox;
        N--;
        assert revisar();
        return item;
    }
    public Item cima()
    {
        if (estaVacio()) throw new NoSuchElementException("Pila Vacia");
        return primero.item;
    }
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (Item item:this)
            s.append(item+" ");
        return s.toString();
    }
    private boolean revisar()
    {
        if (N==0){
            if (primero != null) return false;
            if (primero.prox != null) return false;
        }
        else if (N == 1){
            if (primero == null) return false;
            if (primero.prox != null) return false;
        }
        else {
            if (primero.prox == null) return false;
        }
        
        //revisiÃ³n de la consistencia interna de la variable
        //de instancia N
        int numeroDeNodos = 0;
        for (Nodo x=primero; x != null; x = x.prox){
            numeroDeNodos++;
        }
        if (numeroDeNodos != N) return false;
        
        return true;
    }
    public Iterator<Item> iterator()
    {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item>
    {   
        private Nodo actual = primero;
        public boolean hasNext(){
            return actual != null;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public Item next(){
            if (!hasNext()) throw new NoSuchElementException();
            Item item = actual.item;
            actual = actual.prox;
            return item;
        }
    }
}

