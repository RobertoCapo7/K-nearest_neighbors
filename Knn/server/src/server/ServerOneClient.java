package src.server;

import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import src.data.Data;
import src.data.TrainingDataException;
import src.database.DatabaseConnectionException;
import src.database.DbAccess;
import src.database.InsufficientColumnNumberException;
import src.database.NoValueException;
import src.mining.KNN;
import src.utility.Keyboard;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

/**
 * gestisce la comunicazione con un client
 */
public class ServerOneClient extends Thread {

    /**
     * socket per la comunicazione con il client
     */
    private final Socket socket;
    /**
     * attributo per ricevere messaggi dal client
     */
    private final ObjectInputStream in;
    /**
     * attributo per inviare messaggi al client
     */
    private final ObjectOutputStream out;

    /**
     * costruttore per l'inizializzazione della comunicazione
     * @param skt socket per la comunicazione con il client
     * @throws IOException eccezione generata in caso di errore di connessione con il client
     */
    public ServerOneClient(Socket skt) throws IOException {
        this.socket = skt;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        start();
    }

    /**
     * metodo principale per la comuncazione con il client
     **/
    public void run() {

        KNN knn = null;

        do {
            int sceltaClient = 0;
            try {
                sceltaClient = (Integer) in.readObject();
                switch (sceltaClient) {
                    case 1 -> {
                        Data trainingSet = null;
                        String file = "";

                        try {
                            file = (String) in.readObject();

                            trainingSet = new Data(file);
                            System.out.println(trainingSet);

                            out.writeObject("@OK");
                            knn = new KNN(trainingSet);
                            knn.salva(file + ".dmp");
                        } catch (TrainingDataException exc) {
                            System.out.println("[ServerOneClient_run_TrainingDataException] : " + exc.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(exc.getMessage());
                        } catch (IOException exc) {
                            out.writeObject("@ERROR");
                            System.out.println(exc.getMessage());
                        }
                    }
                    case 2 -> {
                        String file;

                        try {
                            file = (String) in.readObject();
                            knn = KNN.carica(file);
                            System.out.println(knn);

                            out.writeObject("@OK");
                        } catch (FileNotFoundException e) {
                            System.out.println(e.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(e.getMessage());
                        } catch (IOException exc) {
                            System.out.println("[ClassNotFoundException]" + exc.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(exc.getMessage());
                        }
                        catch (ClassNotFoundException exc2){
                            System.out.println("[ClassNotFoundException]" + exc2.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(exc2.getMessage());
                        }
                    }
                    case 3 -> {
                        Data trainingSet = null;
                        String table = "";
                        DbAccess db = null;

                        try {
                            System.out.print("[Server]: Connecting to DB...");
                            db = new DbAccess();
                            System.out.println("done!");

                            table = (String) in.readObject();

                            trainingSet = new Data(db, table);
                            System.out.println(trainingSet);

                            knn = new KNN(trainingSet);

                            knn.salva(table + "DB.dmp");
                            out.writeObject("@OK");
                        } catch (InsufficientColumnNumberException exc1) {
                            System.out.println("ServerOneClient_run_InsufficientColumnNumberException: " + exc1.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(exc1.getMessage());
                        } catch (DatabaseConnectionException exc2) {
                            System.out.println("ServerOneClient_run_DatabaseConnectionException: " + exc2.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(exc2.getMessage());
                        } catch (SQLException exc3) {
                            System.out.println("ServerOneClient_run_SQLException: " + exc3.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(exc3.getMessage());
                        } catch (NoValueException e) {
                            System.out.println("NoValueException" + e.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(e.getMessage());
                        } catch (IOException exc) {
                            System.out.println("ServerOneClient_run_IOException: " + exc.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(exc.getMessage());
                        } finally {
                            if (db != null) {
                                System.out.print("[Server]: Chiusura connessione database...");
                                db.closeConnection();
                                System.out.println("done");
                            }
                        }
                    }
                    case 4 -> {
                        try {
                            out.writeObject(knn.predict(out, in));
                        }
                        catch (NullPointerException e1) {
                            System.out.println("[Server]: Errore lettura attributi dal client!");
                        }
                        catch (TrainingDataException e) {
                            System.out.println(e.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(e.getMessage());
                        }
                        catch (Exception e3) {
                            System.out.println(e3.getMessage());
                            out.writeObject("@ERROR");
                            out.writeObject(e3.getMessage());
                        }
                    }
                }
            }
            catch (EOFException e) {
                try {
                    socket.close();
                    System.out.println("[Server]: Connessione chiusa con il client!");
                    System.out.println("[Server]: In attesa di connessione da parte di un client...");
                    break;
                } catch (IOException e2) {
                    System.out.println("[ServerOneClient_run_IOException] Socket not closed : " + e.getMessage());
                }
            }
            catch (IOException e2) {
                System.out.println("[ServerOneClient_run_IOException] Socket closed : " + e2.getMessage());
                break;
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            catch (Exception e){
                System.out.println("Errore Connessione DB!");
            }
        } while (true);
    }
}
