package src.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import src.example.Example;

/**
 * Classe responsabile della lettura da database del trainingSet e del vettore target
 */
public class TableData {
	/** Variabile di istanza di tipo DbAccess per la connessione al database.*/
	private final DbAccess db;

	/** Variabile di istanza di tipo String che contiene il nome della tabella.*/
	private final String table;

	/** Variabile di istanza di tipo TableSchema che contiene lo schema della tabella.*/
	private final TableSchema tSchema;

	/** Variabile di istanza di tipo ArrayList che contiene il trainingSet letto da DB.*/
	private final ArrayList<Example> transSet;

	/** Variabile di istanza di tipo ArrayList che contiene i target letti da DB.*/
	private final ArrayList target;

	/**
	 * Costruttore della classe TableData
	 * @param db Istanza di DbAccess per la connessione al database.
	 * @param tSchema Schema della tabella del DB dal quale leggere.
	 * @throws SQLException Eccezioni di SQL.
	 * @throws InsufficientColumnNumberException Eccezione per numero di colonne insufficiente.
	 */
	public TableData(DbAccess db, TableSchema tSchema) throws SQLException,InsufficientColumnNumberException{
		this.db=db;
		this.tSchema=tSchema;
		this.table=tSchema.getTableName();
		transSet = new ArrayList<>();
		target= new ArrayList<>();
		init();
	}

	/**
	 * Metodo di istanza init responsabile della lettura del training set da DB.
	 * @throws SQLException Eccezioni di SQL.
	 */
	private void init() throws SQLException{
		String query="select ";
		
		Iterator<Column> it=tSchema.iterator();
		for(Column c:tSchema){			
			query += c.getColumnName();
			query+=",";
		}
		query +=tSchema.target().getColumnName();
		query += (" FROM "+table);
		
		Statement statement = db.getConn().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			Example currentTuple=new Example(tSchema.getNumberOfAttributes());
			int i=0;
			for(Column c:tSchema) {
				if(c.isNumber())
					currentTuple.set(rs.getDouble(i+1),i);
				else
					currentTuple.set(rs.getString(i+1),i);
				i++;
			}
			transSet.add(currentTuple);
			
			if(tSchema.target().isNumber())
				target.add(rs.getDouble(tSchema.target().getColumnName()));
			else
				target.add(rs.getString(tSchema.target().getColumnName()));
		}
		rs.close();
		statement.close();	
	}

	/**
	 * Metodo di istanza getExamples che permette di ritornare l'array transSet
	 * @return tranSet
	 */
	public List<Example> getExamples(){
		return transSet; 
	}

	/**
	 * Metodo di istanza getTargetValues che permette di ritornare l'array target
	 * @return tranSet
	 */
	public List getTargetValues(){
		return target; 
	}

	/**
	 * Metodo di istanza getAggregateColumnValue che permette di effettuare la query del min e del max attraverso
	 * la classe enum aggregate
	 * @param column Colonna presa in cosiderazione
	 * @param aggregate Classe enum per l'ottenimento della query
	 * @return value risultato della query
	 * @throws SQLException eccezione di SQL
	 */
	public Object getAggregateColumnValue(Column column,QUERY_TYPE aggregate) throws SQLException {
		double value = new Double(0);
		Statement stmt = db.getConn().createStatement();
		ResultSet rs = stmt.executeQuery("SELECT " + aggregate + "(" + column.getColumnName() + ") " + "FROM " + table);

		while (rs.next()){
			value = rs.getDouble(1);
		}
		stmt.close();
		rs.close();

		return value;
	}
	
}
