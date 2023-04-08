package src.database;

/** Classe che modella l'eccezione NoValueException che viene lanciata
 * quando mancano le tuple del DB non sono sufficienti (minori di 2) */
public class NoValueException extends Exception {
	/**
	 * Costruttore della classe NoValueException che invoca il costruttore della classe padre e
	 * permette di stampare a schermo la stringa data in input.
	 * @param msg Stringa da stampare
	 */
	public NoValueException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
	}
}
