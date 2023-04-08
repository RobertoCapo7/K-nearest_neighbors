package src.data;

import java.io.Serializable;

/** Classe astratta che modella l'attributo */
public abstract class Attribute implements Serializable {
    /** Variabile di istanza di tipo String che conterrà il nome dell'attributo */
 private final String name;
   /** Variabile di istanza di tipo intero che conterrà l'indice dell'attributo */
 private int index;

    /**
     * Costruttore della classe Attribute.
     * Inizializza le variabili di istanza della classe con quelle passate in input
     * @param name Nome dell'attributo.
     * @param index Indice dell'attributo.
     */
    protected Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Metodo di istanza getName che ritorna il nome dell'attributo
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Metodo di istanza getIndex che ritorna l'indice dell'attributo
     * @return int indice
     */
    public int getIndex() {
        return index;
    }

}
