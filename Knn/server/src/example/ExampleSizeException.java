package src.example;

/** Classe che modella l'eccezione del ExampleSizeException che viene lanciata
 * quando c'è un errore di dimensione tra gli esempi*/
public class ExampleSizeException extends RuntimeException {
    /**
     * Costruttore della classe ExampleSizeException che invoca il costruttore della classe padre e
     * permette di stampare a schermo la stringa data in input.
     * @param output Stringa da stampare
     */
    public ExampleSizeException(String output){
        super(output);
    }
}


