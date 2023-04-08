package src.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe che legge e modella la struttura della tabella del database.
 */
public class TableSchema implements Iterable<Column>{
	/** Variabile di istanza di tipo array che contengono le colonne della tabella */
	private ArrayList<Column> tableSchema;

	/** Variabile di istanza di tipo Column corrispondente all'attributo target. */
	private Column target;

	/** Variabile di istanza di tipo String che contiene il nome della tabella */
	private String tableName;

	/**
	 * Costruttore della classe TableScheme: legge lo schema della tabella per costruire il training set.
	 * @param tableName Nome della tabella dal quale leggere lo schema.
	 * @param db Nome del DB dal quale leggere lo schema della tabella.
	 * @throws SQLException Eccezioni SQL.
	 * @throws InsufficientColumnNumberException Numero insufficiente di colonne (minore di 2).
	 */
	public TableSchema(String tableName, DbAccess db) throws SQLException,InsufficientColumnNumberException{
		this.tableName=tableName;
		tableSchema=new ArrayList<Column>();

		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");

		DatabaseMetaData meta = db.getConn().getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);


		while (res.next()) {

			if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				if(res.isLast())
					target=new Column(
							res.getString("COLUMN_NAME"),
							mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
							;
				else
					tableSchema.add(new Column(
							res.getString("COLUMN_NAME"),
							mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
					);



		}

		res.close();
		//pensarci o rimettere come ha messo la prof.
		if (target == null && tableSchema.size() == 0) {
			throw new SQLException("La tabella " + tableName + " non è stata trovata!");
		}
		else if(tableSchema.size() < 1){
			throw new InsufficientColumnNumberException("La tabella selezionata contiene meno di due colonne.");
		}

	}

	/**
	 * Metodo di istanza che restituisce la colonna target.
	 * @return target
	 */
	Column target(){
		return target;
	}

	/**
	 * Metodo di istanza che restituisce il numero di colonne lette nello schema.
	 * @return int
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * Metodo di istanza che restituisce il nome della tabella.
	 * @return String
	 */
	String getTableName() {
		return tableName;
	}

	/**
	 * Metodo di istanza iterator che restituisce l'iteratore per il vettore di tipo Column.
	 * @return iteratore
	 */
	@Override
	public Iterator<Column> iterator() {
		return tableSchema.iterator();
	}

}

		     


