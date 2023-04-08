package src.database;

/** Classe che modella l'eccezione InsufficientColumnNumberException che viene lanciata
 * quando non ci sono colonne sufficienti nella tabella (minori di 2) */
public class InsufficientColumnNumberException extends Exception {
	/**
	 * Costruttore della classe InsufficientColumnNumberException che invoca il costruttore della classe padre e
	 * permette di stampare a schermo la stringa data in input.
	 * @param msg Stringa da stampare
	 */
	 InsufficientColumnNumberException(String msg) {super(msg);}
}
