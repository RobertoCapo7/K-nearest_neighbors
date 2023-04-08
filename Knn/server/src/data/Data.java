package src.data;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import src.example.Example;
import src.database.*;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Classe Data che modella i dati che servono per lavorare alla predizione e al corretto funzionamento del KNN.
 */
public class Data implements Serializable {
    /** Variabile di istanza di tipo LinkedList che conterrà il trainingSet su cui lavorare */
    private final LinkedList<Example> data;

    /** Variabile di istanza di tipo ArrayList che conterrà un array di target */
    private final ArrayList<Double> target;

    /** Variabile di istanza di tipo intero che conterrà il numero degli esempi totali acquisiti */
    private final int numberOfExamples;

    /** Variabile di istanza di tipo ArrayList che conterrà gli attributi discreti e continui */
    private final ArrayList<Attribute> explanatorySet;

    /** Variabile di istanza di tipo ContinuousAttribute*/
    private ContinuousAttribute classAttribute;

    /**
     * Costruttore della classe Data responsabile nell'acquisizione del trainingSet da un file .dat
     * @param fileName path del file dal quale acquisire il trainingSet
     * @throws TrainingDataException Eccezione lanciata nel caso ci sia qualche problema nell'acquisizione da file
     * @throws FileNotFoundException Eccezione lanciata dallo scanner
     */
    public Data(String fileName) throws TrainingDataException, FileNotFoundException {
        File inFile = new File (fileName);

        // Controllo se il percorso del file effettivamente si riferisce ad un file
        if(!inFile.exists()) {
            throw new TrainingDataException("Il percorso fa riferimento ad un file inesistente!");
        }

        // Controllo se il percorso del file si riferisce ad una directory
        if(inFile.isDirectory()){
            throw new TrainingDataException("Il percorso fa riferimento a una directory e non ad un file!");
        }

        // Controllo se l'estensione del file sia .dat
        String estensione = fileName.substring(fileName.lastIndexOf("."));
        if(!estensione.equals(".dat")){
            throw new TrainingDataException("L'estensione del file non è .dat");
        }

        Scanner sc = new Scanner(inFile);

        // Controllo se esiste la prossima riga
        if(!sc.hasNextLine()) {
            throw new TrainingDataException("Errore nel file informazioni previste mancanti!");
        }

        String line = sc.nextLine();

        // Controllo se la riga è vuota
        if (line.equals("")) {
            throw new TrainingDataException("Errore di formattazione del file!");
        }

        // Controllo se esiste lo schema
        if(!line.contains("@schema")) {
            throw new TrainingDataException("@schema mancante!");
        }

        String[] s = line.split(" ");

        // Controllo il parametro di schema
        if(s.length > 2) {
            throw new TrainingDataException("@schema contiene più parametri del previsto!");
        }
        else if(s.length == 1) {
            throw new TrainingDataException("@schema parametro mancante!");
        }
        else if(!(line.split(" ")[1].matches("\\d+"))){
            throw new TrainingDataException("@schema il parametro non è numerico!");
        }

        int nSchema = Integer.parseInt(s[1]);
        explanatorySet = new ArrayList<>(nSchema);

        short iAttribute = 0;
        int cDesc = 0;
        boolean targetTrovato = false;

        // Controllo se esiste la prossima riga
        if(!sc.hasNextLine()) {
            throw new TrainingDataException("Errore nel file informazioni previste mancanti!");
        }

        line = sc.nextLine();

        // Controllo se la riga è vuota
        if (line.equals("")) {
            throw new TrainingDataException("Errore di formattazione del file!");
        }

        // Controllo se la riga contiene @data
        if(line.contains("@data")) {
            throw new TrainingDataException("Errore nel file informazioni previste mancanti!");
        }

        while(!line.contains("@data")) {
            s = line.split(" ");

            // Controllo se nella riga splittata esiste @data
            if(s[0].equals("@desc"))
            {
                // Controllo che @data si trovi nella posizione esatta (posizioni consecutive)
                if(iAttribute > nSchema -1) {
                    throw new TrainingDataException("Errore nel file, ordine dei @desc errato o parametro @schema non rispettato!");
                }

                // Controllo che il numero di parametri sia esatto
                if(s.length > 3) {
                    throw new TrainingDataException("@desc contiene più parametri del previsto!");
                }
                else if(s.length == 1 || s.length == 2){
                    throw new TrainingDataException("@desc parametri mancanti!");
                }
                else if(s.length == 3){
                    // Controllo che il secondo parametro sia discreto o continuo
                    if (!(s[2].equals("discrete") || s[2].equals("continuous"))) {
                        throw new TrainingDataException("@desc diverso da discrete o continuous!");
                    }

                    if (s[2].equals("discrete")) {
                        explanatorySet.add(iAttribute, new DiscreteAttribute(s[1], iAttribute));
                    } else {
                        explanatorySet.add(iAttribute, new ContinuousAttribute(s[1], iAttribute));
                    }
                }
                cDesc++;
            }

            // Controllo se nella riga splittata esiste @target
            else if(s[0].equals("@target")) {

                // Controllo che esiste il parametro di target
                if(s.length < 2){
                    throw new TrainingDataException("@target parametro mancante!");
                }
                else if(s.length > 2){
                    throw new TrainingDataException("@target contiene più parametri del previsto!");
                }
                // Controllo che il parametro di @target sia class
                else if(s.length == 2 && s[1].equals("class")) {
                    classAttribute = new ContinuousAttribute(s[1], iAttribute);
                }
                else if(s.length == 2 && !s[1].equals("class")) {
                    throw new TrainingDataException("@target parametro diverso da class!");
                }
                targetTrovato = true;
            }

            // Controllo che non ci siano errori nel file
            else if (!s[0].equals("@data")) {
                throw new TrainingDataException("Errore nel file, @data mancante oppure in posizione errata!");
            }

            iAttribute++;

            // Controllo se esiste la prossima riga
            if(!sc.hasNextLine()) {
                throw new TrainingDataException("Errore nel file informazioni mancanti!");
            }

            line = sc.nextLine();

            // Controllo se la riga è vuota
            if (line.equals("")) {
                throw new TrainingDataException("Errore di formattazione del file!");
            }
        }

        // Controllo che il parametro di data sia corretto
        if(line.split(" ").length > 2) {
            throw new TrainingDataException("@data contiene più parametri del previsto!");

        }
        else if ((line.split(" ").length == 1) ){
            throw new TrainingDataException("@data parametro mancante!");
        }
        else if(!(line.split(" ")[1].matches("\\d+"))){
            throw new TrainingDataException("@data il parametro inserito in non è numerico!");
        }

        // Controllo se in precedenza target è stato trovato nel file
        if (!targetTrovato) {
            throw new TrainingDataException("@target mancante!");
        }

        // Controllo se non sono stati trovati @desc
        if (cDesc == 0) {
            throw new TrainingDataException("@desc mancante!");
        }

        // Controllo se il numero di @desc trovati è diverso dal parametro di @schema
        if(nSchema != cDesc){
            throw new TrainingDataException("@schema parametro non rispettato!");
        }

        //avvalorare numero di esempi
        numberOfExamples = Integer.parseInt(line.split(" ")[1]);

        //popolare data e target
        data = new LinkedList<>();
        target = new ArrayList<>(numberOfExamples);

        short iRow = 0;

        // Controllo se esiste la prossima riga
        if(!sc.hasNextLine()) {
            throw new TrainingDataException("Non ci sono esempi!");
        }

        while (sc.hasNextLine()) {
            Example e = new Example(getNumberOfExplanatoryAttributes());

            line = sc.nextLine();

            // Controllo se la riga è vuota
            if (line.equals("")) {
                throw new TrainingDataException("Errore di formattazione del file!");
            }

            // Assumo che attributi siano tutti discreti
            s = line.split(","); //E,E,5,4, 0.28125095

            // Controllo che ci siano valori per ciascun @desc corrispondente
            if (s.length - 1 != cDesc) {
                throw new TrainingDataException("Errore di formattazione negli esempi!");
            }

            int i = 0;
            for (Attribute attribute : explanatorySet) {
                if (attribute instanceof DiscreteAttribute) {
                    // Controllo se nell'esempio acquisito manchi l'attributo discreto
                    if(s[0].equals("")){
                        throw new TrainingDataException("Attributo Discreto mancante!");
                    }
                }
                else if (attribute instanceof ContinuousAttribute){
                    // Controllo se nell'esempio acquisito l'attributo continuo sia numerico
                    if (!s[i].matches("-?\\d+(\\.\\d+)?")) {
                        throw new TrainingDataException("Attributo Continuo non valido! ");
                    }
                }
                i++;
            }

            // Controllo se nell'esempio acquisito la variabile numerica sia numerica
            if (!s[cDesc].matches("-?\\d+(\\.\\d+)?")) {
                throw new TrainingDataException("Sintassi numerica dell'esempio non valida! ");
            }

            for (short jColumn = 0; jColumn < s.length - 1; jColumn++) {
                e.set(s[jColumn],jColumn);
            }

            // Controllo se il numero di esempi acquisiti da file sia maggiore al numero di esempi aspettato
            if (iRow > numberOfExamples - 1) {
                throw new TrainingDataException("Numero di esempi maggiori, numero di esempi previsti: " + numberOfExamples + " ! ");
            }

            data.add(iRow, e);
            target.add(iRow, Double.valueOf(s[s.length - 1]));

            i = 0;
            for (Attribute attribute : explanatorySet) {
                if (attribute instanceof ContinuousAttribute) {
                    ((ContinuousAttribute) attribute).setMin((Double.valueOf(s[i])));
                    ((ContinuousAttribute) attribute).setMax((Double.valueOf(s[i])));
                }
                i++;
            }

            iRow++;
        }

        // Controllo se il numero di esempi acquisiti da file sia minore al numero di esempi aspettato
        if (iRow < numberOfExamples - 1) {
            throw new TrainingDataException("Numero di esempi minori, numero di esempi previsti: " + numberOfExamples + " ! ");
        }

        sc.close();
    }

    /**
     * Costruttore della classe Data responsabile nell'acquisizione del trainingSet da database
     * @param db per gestire il collegamento con il database
     * @param table stringa contenente il nome della tabella del databse
     * @throws InsufficientColumnNumberException Eccezione per insufficiente numero di colonne
     * @throws SQLException Eccezione per errori SQL
     * @throws NoValueException Eccezione sulle tuple del database
     */
    public Data(DbAccess db, String table) throws InsufficientColumnNumberException, SQLException, NoValueException {
        TableSchema tableSchema = new TableSchema(table, db);
        TableData tableData = new TableData(db, tableSchema);
        Statement st = db.getConn().createStatement();
        ResultSet rs = st.executeQuery("select COUNT(*) from " + table);
        int row;

        explanatorySet = new ArrayList<>();
        data = new LinkedList<>();
        row = 0;

        while (rs.next()) {
            row = rs.getInt(1);
        }

        // Controllo se le righe sono < di 2
        if(row < 2)
            throw new NoValueException("La tabella deve avere almeno due tuple!");


        Iterator<Column> itSchema = tableSchema.iterator();
        int counter = 0;
        while (itSchema.hasNext()) {
            Column col = itSchema.next();

            if (!col.isNumber()) {
                explanatorySet.add(counter, new DiscreteAttribute(col.getColumnName(), counter));
            } else {
                explanatorySet.add(counter, new ContinuousAttribute(col.getColumnName(), counter));
                ((ContinuousAttribute) explanatorySet.get(counter)).setMin((Double) tableData.getAggregateColumnValue(col, QUERY_TYPE.MIN));
                ((ContinuousAttribute) explanatorySet.get(counter)).setMax((Double) tableData.getAggregateColumnValue(col, QUERY_TYPE.MAX));
            }

            counter++;
        }

        numberOfExamples = tableData.getExamples().size();
        target = new ArrayList<>(numberOfExamples);

        Iterator<Example> itData = tableData.getExamples().iterator();
        Iterator<?> itTarget = tableData.getTargetValues().iterator();
        int i;
        int cont;

        i = 0;
        while (itData.hasNext()) {
            Example e = itData.next();
            Double d = (Double) itTarget.next();

            cont = 0;
            while(cont < tableSchema.getNumberOfAttributes()){
                if(e.get(cont) == null)
                    throw new NoValueException("Attributo discreto NULL nel database!");
                cont++;
            }
            data.add(i, e);
            target.add(i, d);
            i++;
        }
    }

    /**
     * Metodo di istanza getNumberOfExplanatoryAttributes che ritorna la dimensione dell'array della variabile
     * di istanza explanatorySet
     * @return intero -> dimensione dell'array
     */
    private int getNumberOfExplanatoryAttributes() {
        return explanatorySet.size();
    }

    /**
     * Metodo di istanza partition che partiziona data rispetto all'elemento x di key e restituisce il punto di separazione.
     * @param inf Estremo inferiore.
     * @param sup Estremo superiore.
     * @return int
     */
    private int partition(ArrayList<Double> key, int inf, int sup){
        int i,j;

        i = inf;
        j = sup;
        int	med = (inf+sup)/2;

        Double x = key.get(med);

        data.get(inf).swap(data.get(med));

        double temp = target.get(inf);
        target.set(inf,target.get(med));
        target.set(med,temp);

        temp = key.get(inf);
        key.set(inf,key.get(med));
        key.set(med,temp);

        while (true)
        {
            while(i <= sup && key.get(i) <= x){
                i++;
            }

            while(key.get(j) > x){
                j--;
            }

            if(i < j) {
                data.get(i).swap(data.get(j));
                temp = target.get(i);
                target.set(i,target.get(j));
                target.set(j,temp);

                temp = key.get(i);
                key.set(i,key.get(j));
                key.set(j,temp);
            }
            else break;
        }

        data.get(inf).swap(data.get(j));
        temp = target.get(inf);
        target.set(inf,target.get(j));
        target.set(j,temp);

        temp = key.get(inf);
        key.set(inf,key.get(j));
        key.set(j,temp);

        return j;
    }

    /**
     * Metodo di istanza quicksort che permette l'ordinamento per i vettori data, target e key in base ai valori di key.
     * @param inf Indice inferiore.
     * @param sup Indice superiore.
     */
    private void quicksort(ArrayList<Double> key, int inf, int sup){
        if(sup >= inf){

            int pos;

            pos = partition(key, inf, sup);

            if ((pos-inf) < (sup-pos + 1)) {
                quicksort(key, inf, pos - 1);
                quicksort(key, pos + 1,sup);
            }
            else
            {
                quicksort(key, pos + 1, sup);
                quicksort(key, inf, pos - 1);
            }

        }
    }

    /**
     * Metodo di istanza avgClosest che permette di selezionare le distanze minori e di calcolare la predizione.
     * @param e Esempio da confrontare per la predizione
     * @param k Numero di distanze piu' piccole da considerare.
     * @return double predizione
     * @throws TrainingDataException Eccezione lanciata nel caso in cui gli attributi continui non possono essere scalati
     */
    public double avgClosest(Example e, int k) throws TrainingDataException {
        ArrayList<Double> key = new ArrayList<>(data.size());

        e=scaledExample(e);
        // Calcolo della Distanza di Hamming tra istanza 'e' e istanza 'data'
        for (Example ex : data) {
            key.add(scaledExample(ex).distance(e));
        }

        quicksort(key,0,key.size() - 1);

        int conteggioValori;
        double somma;

        conteggioValori = 0;
        somma = 0;

        int numeroDistanzeDaPrendere = k;
        double minimaDistanza = key.get(0);

        for (int i = 0; i < target.size() && numeroDistanzeDaPrendere != 0; i++) {
            if(key.get(i) == minimaDistanza){
                somma = somma + target.get(i);
                conteggioValori++;
            }
            else{
                minimaDistanza = key.get(i);
                numeroDistanzeDaPrendere--;
                if(numeroDistanzeDaPrendere != 0){
                    somma = somma + target.get(i);
                    conteggioValori++;
                }
            }
        }

        return somma / conteggioValori;
    }

    /**
     * Metodo di istanza readExample che permette la lettura dell'esempio da tastiera da parte del client e
     * la comunica al server.
     * @param out ObjectOutputStream
     * @param in ObjectInputStream
     * @return Example
     * @throws IOException Eccezione lanciata dai metodi writeObject e readObject
     * @throws ClassNotFoundException Eccezione lanciata nel caso sia impossibile trovare la classe di un oggetto serializzato.
     */
    public Example readExample(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        Example e = new Example(numberOfExamples);

        int i = 0;
        for (Attribute a : explanatorySet) {
            if (a instanceof DiscreteAttribute) {
                out.writeObject("@READSTRING"); //tag
                out.writeObject("Inserisci valore discreto X[" + i + "]:");

                e.set(in.readObject(), i);
            } else {
                double x;
                do {
                    out.writeObject("@READDOUBLE"); //tag
                    out.writeObject("Inserisci valore continuo X[" + i + "]:");

                    x = (Double) in.readObject();
                } while (new Double(x).equals(Double.NaN));
                e.set(x, i);
            }
            i++;
        }

        out.writeObject("@ENDEXAMPLE");
        return e;
    }

    /**
     * Metodo di istanza scaledExample che permette di scalare gli elementi, di un array di attributi,
     * che siano istanza della classe ContinuousAttribute
     * @param e Classe example
     * @return Classe example con array di attributi scalati
     */
    private Example scaledExample(Example e) throws TrainingDataException {
        Example e1 = new Example(getNumberOfExplanatoryAttributes());
        int i;

        i = 0;
        for (Attribute attribute : explanatorySet) {
            if (attribute instanceof DiscreteAttribute){
                e1.set(e.get(i),i);
            }
            else if (attribute instanceof ContinuousAttribute){
                e1.set(((ContinuousAttribute) attribute).scale(Double.valueOf(e.get(i).toString())),i);
            }
            i++;
        }
        return e1;
    }
}


