package src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Classe che gestisce l'accesso al DB per la lettura dei dati di training
 *
 */
public class DbAccess {
	/** Variabile di istanza di tipo String che contiene la stringa del driver*/
	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

	/** Variabile di istanza di tipo String che contiene la stringa del DBMS*/
	private final String DBMS = "jdbc:mysql";

	/** Variabile di istanza di tipo String che contiene l'indirizzo del server*/
	private final String SERVER = "localhost";

	/** Variabile di istanza di tipo String che contiene la porta del server*/
	private final int PORT = 3306;

	/** Variabile di istanza di tipo String che contiene il nome del database*/
	private final String DATABASE = "Map";

	/** Variabile di istanza di tipo String che contiene il nome dell'utente*/
	private final String USER_ID = "Student";

	/** Variabile di istanza di tipo String che contiene la password dell'utente*/
	private final String PASSWORD = "map";

	/** Variabile di istanza di tipo Connection responsabile della connessione del DB*/
	private final Connection conn;

	/**
	 * Costruttore della classe DbAccess. Inizializza una connessione al DB
	 * @throws DatabaseConnectionException Eccezione lanciata in caso di errore di connessione al db
	 */
	public DbAccess() throws DatabaseConnectionException{
		String connectionString =  DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
				+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

		try {
			conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
		}
		catch (SQLException e) {
			System.out.println("Impossibile connettersi al DB");
			throw new DatabaseConnectionException(e.toString());
		}
	}

	/**
	 * Metodo di istanza closeConnection che permette la chiusura della connessione con il DB
	 */
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Impossibile chiudere la connessione");
		}
	}

	/**
	 * Metodo di istanza che restituisce l'attuale istanza di Connection.
	 * @return Connection connessione al database
	 */
	public Connection getConn() {
		return conn;
	}
}
