package src.example;

import java.io.Serializable;
import java.util.ArrayList;

/** Classe che modella l'Esempio */
public class Example implements Serializable {
    /** Variabile di istanza di tipo ArrayList che conterrà un gli esempi*/
    private final ArrayList<Object> example;

    /**
     * Costruttore della classe Example inizializza l'arrayList specificando in input la dimensione da riservare
     * @param size Dimensione del vettore example.
     */
    public Example(int size) {
        example = new ArrayList<>(size);
    }

    /**
     * Metodo di istanza get che permette di ritornare un elemento dell'array example data la sua posizione in input
     * @param index posizione dell'esempio che si vuole ottenere
     * @return Object array in posizione index
     */
    public Object get(int index) {
        return example.get(index);
    }

    /**
     * Metodo di istanza set che permette di settare un elemento nell'array in una determinata posizione
     * @param example esempio da settare
     * @param index posizione di scrittura dell'esempio nell'array
     */
    public void set(Object example, int index) {
        if (index < 0) {
            throw new ExampleSizeException("L'indice deve essere >= 0");
        }

        this.example.add(index,example);
    }

    /**
     * Metodo di istanza swap che permette di scambiare i valori contenuti nell'array della classe example data in input con quelli
     * dell’array dell'oggetto corrente
     * @param e Classe esempio
     */
    public void swap(Example e) {
        if (this.example.size() != e.example.size()) {
            throw new ExampleSizeException("La dimensione dei due array di esempi è diversa! ");
        }

        Object appoggio;

        for(int i = 0; i < example.size(); i++) {
            appoggio = e.example.get(i);
            e.example.set(i,this.example.get(i));
            this.example.set(i,appoggio);
        }
    }

    /**
     * Metodo di istanza distance che permette il calcolo della distanza tra il vettore dell'esempio corrente con quello della classe data in input.
     * @param e classe example da cui confrontare il vettore
     * @return double distanza
     */
    public double distance(Example e) {
        if (this.example.size() != e.example.size()) {
            throw new ExampleSizeException("Dimensione vettori diversa");
        }

        double distance;

        distance = 0;

        for(int i = 0; i < this.example.size(); i++) {
            if(e.get(i) instanceof String) {
                if (!e.get(i).equals(this.example.get(i))) {
                    distance++;
                }
            }
            else if(e.get(i) instanceof Double){
                distance = distance + Math.abs((Double) this.get(i) - (Double) e.get(i));
            }
        }

        return distance;
    }
}

