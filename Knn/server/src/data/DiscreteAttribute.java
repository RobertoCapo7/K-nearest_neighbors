package src.data;

/** Classe che modella l'attributo discreto ed estende la classe attribute*/
public class DiscreteAttribute extends Attribute {

    /**
     * Costruttore della classe DiscreteAttribute.
     * Invoca il costruttore della classe padre che inizializza le variabili di istanza name e index con quelle in input
     * @param name Nome dell'attributo.
     * @param index Indice dell'attributo.
     */
    DiscreteAttribute(String name, int index){
        super(name,index);
    }
}
