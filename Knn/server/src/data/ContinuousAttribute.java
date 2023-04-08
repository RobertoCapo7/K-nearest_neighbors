package src.data;

/** Classe che modella l'attributo continuo ed estende la classe attribute*/
public class ContinuousAttribute extends Attribute {

    /** Variabile di istanza di tipo double che conterrà il minimo
     *  valore degli attributi continui di un training set
     */
    private double min;

    /** Variabile di istanza di tipo double che conterrà il massimo
     *  valore degli attributi continui di un training set
     */
    private double max;

    /**
     * Costruttore della classe ContinuousAttribute.
     * Invoca il costruttore della classe padre che inizializza le variabili di istanza name e index con quelle in input
     * Inizializza le variabili di istanza min e max rispettivamente a + infinito e - infinito
     * @param name Nome dell'attributo.
     * @param index Indice dell'attributo.
     */
    ContinuousAttribute(String name, int index) {
        super(name, index);
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
    }

    /**
     * Metodo di istanza setMin che permette di settare la variabile di istanza min della classe
     * con quella v data in input
     * @param v
     */
    void setMin(Double v) {
        if(v < min) {
            min = v;
        }
    }

    /**
     * Metodo di istanza setMax che permette di settare la variabile di istanza max della classe
     * con quella v data in input
     * @param v
     */
    void setMax(Double v) {
        if(v > max) {
            max = v;
        }
    }

    /**
     * Metodo di istanza scale che permette di scalare il valore dato in input nell'intervallo [0,1]
     * attraverso l'uso delle variabili di istanza max e min infine restituisce il valore scalato
     * @param value
     * @return value scalato
     * @throws TrainingDataException Eccezione lanciata nel caso in cui non sia possibile scalare gli esempi
     */
    double scale(Double value) throws TrainingDataException {
        if(max == min)
            throw new TrainingDataException("Non è possibile scalare gli esempi in quanto max e min coincidono!");

        return (value - min) / (max - min);
    }
}
