package src.mining;
import src.data.*;
import src.example.Example;
import java.io.*;

/**
 * Classe che modella il KNN
 */
public class KNN implements Serializable {
    /** Variabile di istanza di tipo Data che contiene il trainingSet */
    private final Data data;

    /**
     * Costruttore della classe KNN. Inizializza la variabile di istanza data con quella data in input
     * @param trainingSet Training set del KNN.
     */
    public KNN(Data trainingSet){
        data = trainingSet;
    }

    /**
     * Metodo di istanza predict che chiama il metodo readExample della classe Data e comunica con il client
     * per acquisire k. Restituisce infine la predizione dell’esempio e con numero di vicini k.
     * @param out Output dal server
     * @param in Input dal client
     * @return double predizione
     * @throws IOException Eccezione lanciata dai metodi writeObject e readObject
     * @throws ClassNotFoundException Eccezione lanciata nel caso sia impossibile trovare la classe di un oggetto serializzato.
     * @throws ClassCastException Eccezione lanciata nel caso ci sia un errore di cast
     * @throws TrainingDataException Lanciata nel caso in cui min e max coincidono e non è possibile fare la predizione
     */
    public double predict (ObjectOutputStream out, ObjectInputStream in) throws
            IOException, ClassNotFoundException, ClassCastException, TrainingDataException {
        Example e = data.readExample(out,in);
        int k=0;
        out.writeObject("Inserisci valore k>=1:");
        k=(Integer)(in.readObject());
        return data.avgClosest(e, k);
    }

    /**
     * Metodo di istanza per la serializzazione dell'istanza KNN.
     * @param nomeFile Nome del file nel quale memorizzare.
     * @throws IOException Eccezione per l'input/output da file.
     */
    public void salva(String nomeFile) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(nomeFile);
        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
        out.writeObject(this);
        out.close();
        fileOutputStream.close();
    }

    /**
     * Metodo statico per il caricamento di un'istanza di KNN serializzato.
     * @param nomeFile Nome del file contenente una serializzazione di KNN.
     * @return classe KNN caricata dal file
     * @throws IOException Eccezione per l'input/output da file.
     * @throws ClassNotFoundException Eccezione per input di oggetti serializzati.
     * @throws FileNotFoundException Eccezione per il file non trovato.
     */
    public static KNN carica(String nomeFile) throws IOException,ClassNotFoundException {
        File file = new File(nomeFile);

        if(!file.exists()){
            throw new FileNotFoundException("Il percorso fa riferimento ad un file inesistente!");
        }
        if(file.isDirectory()){
            throw new FileNotFoundException("Il percorso fa riferimento a una directory e non ad un file!");
        }

        String estensione = nomeFile.substring(nomeFile.lastIndexOf("."));
        if(!estensione.equals(".dmp")){
            throw new FileNotFoundException("L'estensione del file non è .dmp");
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        KNN knn=(KNN)in.readObject();
        in.close();
        fileInputStream.close();

        return knn;
    }
}
