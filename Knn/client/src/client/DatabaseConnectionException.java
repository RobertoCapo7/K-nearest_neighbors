package client;

/** Classe che modella l'eccezione del DatabaseConnectionException che viene lanciata
 * quando c'è un errore di connessione al DB*/
public class DatabaseConnectionException extends Exception {
	/**
	 * Costruttore della classe DatabaseConnectionException che invoca il costruttore della classe padre e
	 * permette di stampare a schermo la stringa data in input.
	 * @param msg Stringa da stampare
	 */
	DatabaseConnectionException(String msg){
		super(msg);
	}
}
