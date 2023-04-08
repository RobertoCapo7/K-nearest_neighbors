package src.data;

/** Classe che modella l'eccezione del trainingDataException che viene lanciata
 * quando c'è un errore nell'acquisizione del trainingSet da file*/
public class TrainingDataException extends Exception {
    /**
     * Costruttore della classe TrainingDataException che invoca il costruttore della classe padre e
     * permette di stampare a schermo la stringa data in input.
     * @param output Stringa da stampare
     */
    TrainingDataException(String output) {
        super(output);
    }
}
