package src.server;

import src.utility.Keyboard;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe Multiserver che gestisce piu' client per volta
 */
public class MultiServer {

    /**
     * Variabile di istanza di tipo int contenente la porta del server per la connessione
     */
    private final int port;

    /**
     * Costruttore della classe MultiServer. Inizializza la variabile di istanza port con quella in input e avvia il server
     * @param port porta del server
     * @throws IOException eccezione generata in caso di errori in fase di operazioni di I/O
     */
    public MultiServer(int port) throws IOException {
        this.port = port;
        System.out.println("Server started");
        run();
    }

    /**
     * Metodo di istanza della classe e ha la responsabilità di avviare il server e di connettersi
     * nel caso di richiesta di connessione con il client
     * @throws IOException eccezione generata in caso di errori in fase di operazioni di I/O
     */
    private void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        try (serverSocket) {
            System.out.println("[Server]: Started: " + serverSocket);
            System.out.println("[Server]: In attesa di connessione da parte di un client...");
            while (true) {
                Socket socket = serverSocket.accept();
                InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("[Server]: Connessione avvenuta con successo! Address: " + socketAddress);

                try {
                    new ServerOneClient(socket);
                } catch (IOException e) {
                    System.out.println("[MultiServer_run_IOexception] : " + e.getMessage());
                }
            }

        }

    }

    /**
     * metodo main
     * @param args argomenti passati al lancio del programma - non sono utili
     */

    public static void main(String[] args) {
        int portaServer = 1024;
        System.out.println("Server Knn");
        do {
            if (portaServer < 1024 || portaServer > 65353) {
                System.out.println("Porta inserita non valida");
            }
            System.out.print("Inserire la porta per attivare il server [1024/65353]: ");
            portaServer = Keyboard.readInt();
        } while (portaServer < 1024 || portaServer > 65353);
        try {
            new MultiServer(portaServer);
        } catch (IOException e) {
            System.out.println("[MultiServer_main_IOException] : " + e.getMessage());
        }
    }
}
