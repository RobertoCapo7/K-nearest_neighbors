package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import src.utility.Keyboard;

/**
 * Classe che modella il Client
 **/

public class Client {
    /** Variabile di istanza socket */
    private Socket socket;

    /** Variabile di istanza che serve per mandare un messaggio al server */
    private ObjectOutputStream out;

    /** Variabile di istanza che serve per ricevere un messaggio dal server */
    private ObjectInputStream in;

    /** Variabile statica che serve per contenere l'indirizzo di default */
    private final static String defaultAddress = "localhost";

    /** Variabile statica che serve per contenere la porta di default */
    private final static String defaultPort = "8080";

    /**
     * Costruttore della classe: connessione del client al server
     * @param address Indirizzo del server.
     * @param port Porta del server.
     * @throws IOException Eccezioni per l'I/O verso il server.
     * @throws ClassNotFoundException Eccezione per la serializzazione degli oggetti sugli stream.
     */
    Client (InetAddress address, int port) throws IOException, ClassNotFoundException{
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByAddress(address.getAddress()),port);
        socket.connect(socketAddress,5000);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        talking();
    }

    /** Metodo di istanza talking che ritorna la variabile di istanza in della classe
     * @throws IOException Eccezioni per l'I/O verso il server.
     * @throws ClassNotFoundException Eccezione per la serializzazione degli oggetti sugli stream.
     */
    private void talking() throws IOException, ClassNotFoundException {
        int decision;
        String menu;
        try {
            do {
                do {
                    System.out.println("Caricare KNN da file [1]");
                    System.out.println("Caricare KNN da file binario [2]");
                    System.out.println("Caricare KNN da database [3]");
                    decision = Keyboard.readInt();
                } while (decision < 0 || decision > 3);
            try{
                String risposta = null;
                do {
                    out.writeObject(decision);

                    if (decision == 1) {
                        System.out.println("Inserire il path di un file contenente un training set valido:");
                    } else if (decision == 2) {
                        System.out.println("Inserire il path di un file binario");
                    } else if (decision == 3) {
                        risposta = (String) in.readObject();
                        if (risposta.contains("@ERROR")) {
                            System.out.println((String) in.readObject());
                            throw new DatabaseConnectionException((String) in.readObject());
                        }
                        System.out.println("Inserire il nome della tabella presente nel database");
                    }

                    out.writeObject(Keyboard.readString()); // lettura nome file - tabella ;
                    if (decision != 3) {
                        risposta = (String) in.readObject();
                    }
                    if (risposta.contains("@ERROR")) {
                        System.out.println((String) in.readObject());
                    }
                } while (risposta.contains("@ERROR"));

                System.out.println("KNN loaded on the server");
                // predict
                String c;
                do {
                    out.writeObject(4);

                    boolean flag = true; //reading example
                    do {
                        risposta = (String) (in.readObject());

                        if (!risposta.contains("@ENDEXAMPLE")) {
                            // sto leggendo l'esempio
                            String msg = (String) (in.readObject());

                            if (risposta.equals("@READSTRING")) { //leggo una stringa
                                System.out.println(msg);
                                out.writeObject(Keyboard.readString());
                            } else if (risposta.equals("@READDOUBLE")) { //leggo un numero
                                double x;
                                do {
                                    System.out.println(msg);
                                    x = Keyboard.readDouble();
                                } while (new Double(x).equals(Double.NaN));
                                out.writeObject(x);
                            }
                        } else flag = false;
                    } while (flag);

                    //sto leggendo k
                    risposta = (String) (in.readObject());
                    int k;
                    do {
                        System.out.print(risposta);
                        k = Keyboard.readInt();
                    } while (k < 1);
                    out.writeObject(k);
                    //aspetto la predizione

                    risposta = (in.readObject()).toString();
                    if (risposta.contains("@ERROR")) {
                        System.out.println((String) in.readObject());
                    }
                    System.out.println("Prediction:" + risposta);

                    do {
                        System.out.println("Vuoi ripetere predizione? Y/N");
                        c = Keyboard.readString();
                    } while (!c.equalsIgnoreCase("y") && !c.equalsIgnoreCase("n"));
                } while (c.equalsIgnoreCase("y"));
            } catch(Exception e){
                System.out.println(e.getMessage());
            }
                do {
                    System.out.println("Vuoi ripetere una nuova esecuzione con un nuovo oggetto KNN? (Y/N)");
                    menu = Keyboard.readString();
                } while (!menu.equalsIgnoreCase("y") && !menu.equalsIgnoreCase("n"));
            }
            while (menu.equalsIgnoreCase("y"));

        } catch (Exception e) {
            System.out.println("Connessione al server persa! Il client verrà chiuso!");
            System.exit(1);
        }
    }

    /**
     * Metodo statico main
     * @param args si aspetta che in args[0] ci sia l'ip e che in args[1] la porta
     */
    public static void main(String[] args) {
        String addressSupp = defaultAddress;
        String portSupp = defaultPort;
        String ans;
        System.out.println("Client KNN");

        if (args.length == 1) {
            if (args[0].matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$") || args[0].matches("[a-z]*]")) {
                addressSupp = args[0];
            } else if(args[0].matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {
                portSupp = args[0];
            } else {
                System.out.println("Indirizzo non valido!");
            }
        } else if (args.length == 2) {

            if (args[0].matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$") || args[0].matches("[a-z]*]")) {
                addressSupp = args[0];
            } else System.out.println("Indirizzo non valido");

            if(args[1].matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {
                portSupp = args[1];
            } else System.out.println("Porta non valida");
        }

        boolean flag;
        do {
            flag = true;

            do {
                System.out.println("Parametri di connessione impostati a -> " + addressSupp + ":" + portSupp);
                System.out.println("Vuoi impostare una nuova connessione? [y/n]");
                ans = Keyboard.readString();
            } while (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n"));

            if (ans.equalsIgnoreCase("y")) {
                System.out.println("Inserire l'indirizzo del server: ");
                ans = Keyboard.readString();

                if (!ans.matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$") && ans.matches("[A-za-z]*")) {
                    System.out.println("L'indirizzo del server non è valido");
                    flag = false;
                } else {
                    addressSupp = ans;
                }

                if (flag) {
                    System.out.println("Inserire la porta");
                    ans = Keyboard.readString();

                    if (!ans.matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {
                        System.out.println("La porta inserita non è valida!");

                        addressSupp = defaultAddress;
                        flag = false;
                    } else {
                        portSupp = ans;
                    }
                }
            }

        } while (!flag);

        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(addressSupp);
        } catch (UnknownHostException e) {
            System.out.println("Indirizzo ip non valido. Il client verrà chiuso!");
            System.exit(1);
        }

        try {
            System.out.println("Connessione in corso al seguente indirizzo: " + addr + "...");
            InetAddress address = InetAddress.getByName(addressSupp);
            Client c = new Client(address, Integer.valueOf(portSupp));
        }catch (SocketTimeoutException e) {
            System.out.println("Non è stato possibile contattare il server. Il client verrà chiuso!");
        }
        catch (ConnectException e){
            System.out.println("Qualcosa è andato storto controlla l'indirizzo ip o la porta! Il client verrà chiuso!");
        }
        catch (IOException e) {
            System.out.println("[client_main_IOException] : " + e.getMessage() + " Il client verrà chiuso!");
        } catch (ClassNotFoundException e) {
            System.out.println("[client_main_ClassNotFoundException] : " + e.getMessage() + " Il client verrà chiuso!");
        }
    }
}
