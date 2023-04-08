package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

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

    /**
     * Costruttore della classe: connessione del client al server
     * @param address Indirizzo del server.
     * @param port Porta del server.
     * @throws IOException Eccezioni per l'I/O verso il server.
     * @throws ClassNotFoundException Eccezione per la serializzazione degli oggetti sugli stream.
     */
    public Client(InetAddress address, int port) throws IOException, ClassNotFoundException {
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByAddress(address.getAddress()),port);
        socket.connect(socketAddress,5000);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /** Metodo get che ritorna la variabile di istanza in della classe
     * @return in input del socket
     */
    public ObjectInputStream getIn() {
        return in;
    }

    /**
     * Metodo get che ritorna la variabile di istanza out della classe
     * @return out output del socket
     */
    public ObjectOutputStream getOut() {
        return out;
    }

    /***
     *
     * Metodo get che ritorna la variabile di istanza socket della classe
     *
     * @return socket
     */
    public Socket getSocket() {
        return socket;
    }
}
