package src.database;

/**
 * Classe che modella una colonna della tabella del database.
 */
public class Column{
	/** Variabile di istanza di tipo String che contiene il nome della colonna */
	private final String name;

	/** Variabile di istanza di tipo String che contiene il tipo della colonna */
	private final String type;

	/**
	 * Costruttore della classe Column. Inizializza le variabili di istanza della classe con quelle
	 * date in input
	 * @param name Nome della colonna.
	 * @param type Tipo dei dati contenuti nella colonna.
	 */
	Column(String name,String type){
		this.name=name;
		this.type=type;
	}

	/**
	 * Metodo di istanza getColumnName che ritorna il nome della colonna
	 * @return String nome della colonna
	 */
	public String getColumnName(){
		return name;
	}

	/**
	 * Metodo di istanza isNumber che controllare se i valori della colonna sono numerici
	 * @return boolean che indica se true un numero false se non lo è
	 */
	public boolean isNumber(){
		return type.equals("number");
	}

	/**
	 * Converte in stringa un'istanza di Column.
	 */
	public String toString(){
		return name+":"+type;
	}
}