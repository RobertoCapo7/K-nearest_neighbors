package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.URISyntaxException;

/**
 * Classe controller delle seguenti interfacce: connessioneServer, opzioni
 */

public class ControllerServer  {
    /** Variabile di istanza di tipo campo di testo che conterrà l'indirizzo ip del server dato in input dall'utente **/
    @FXML
    private TextField ipAddress;

    /** Variabile di istanza di tipo campo di testo che conterrà la porta del server dato in input dall'utente **/
    @FXML
    private TextField numberOfPort;

    /** Variabile di istanza di tipo casella di controllo che sarà spuntata nel caso in cui l'utente scelga
     * la predizione da file binario */
    @FXML
    private CheckBox fileBinario;

    /** Variabile di istanza di tipo casella di controllo che sarà spuntata nel caso in cui l'utente scelga
     * la predizione da database */
    @FXML
    private CheckBox database;

    /** Variabile di istanza di tipo casella di controllo che sarà spuntata nel caso in cui l'utente scelga
     * la predizione da file dat*/
    @FXML
    private CheckBox fileDat;

    /** Variabile di istanza client **/
    private Client c;

    /** Metodo invocato quando viene cliccato il pulsante connetti dell'interfaccia connessioneServer
     *
     * @param event
     */
    @FXML
    private void connect(ActionEvent event) {
        InetAddress addr;
        Alert alert = new Alert(Alert.AlertType.ERROR);

        try {
            Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
            MediaPlayer player = new MediaPlayer(sound);
            player.play();

            String address = ipAddress.getText();
            String port = numberOfPort.getText();

            // Controllo che siano stati inseriti tutti e quattro gli input della finestra
            if (address.equals("")) {
                sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                player = new MediaPlayer(sound);
                player.play();

                alert.setTitle("Errore!");
                alert.setHeaderText("Non è stato inserito l'indirizzo IP del server!");
                alert.showAndWait();
                return;
            }
            if (port.equals("")) {
                sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Non è stata inserita la porta del server!");
                alert.showAndWait();
                return;
            }

            if (!address.matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$") && address.equals("localhost") == false) {
                sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Sintassi dell'indirizzo IP non valida! Un indirizzo ip deve essere composto da 4 numeri separati da un '.' -> Es: 0.0.0.0");
                alert.showAndWait();
            } else if (!port.matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {
                sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Sintassi della porta non valida! Una porta deve essere compresa tra 1024 e 65535.");
                alert.showAndWait();
            } else {
                try {
                    addr = InetAddress.getByName(address);
                    c = new Client(addr, Integer.valueOf(port));
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/opzioni.fxml"));

                    ControllerServer controllerCaricamentoFile = new ControllerServer();
                    controllerCaricamentoFile.setC(this.c);
                    loader.setController(controllerCaricamentoFile);

                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setResizable(true);
                    stage.setTitle("K-Nearest Neighbors");
                    stage.setMinHeight(450);
                    stage.setMinWidth(500);
                    stage.setScene(scene);
                    stage.show();

                } catch (ConnectException e4) {
                    sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                    player = new MediaPlayer(sound);
                    player.play();
                    alert.setTitle("Errore!");
                    alert.setHeaderText("Porta non corretta!");
                    alert.showAndWait();
                } catch (IOException e) {
                    sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                    player = new MediaPlayer(sound);
                    player.play();
                    alert.setTitle("Errore!");
                    alert.setHeaderText("Connessione al server non riuscita!");
                    alert.showAndWait();
                } catch (NumberFormatException e2) {
                    sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                    player = new MediaPlayer(sound);
                    player.play();
                    alert.setTitle("Errore!");
                    alert.setHeaderText("Impossibile connettersi al server!");
                    alert.showAndWait();
                } catch (ClassNotFoundException e3) {
                    sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                    player = new MediaPlayer(sound);
                    player.play();
                    alert.setTitle("Errore!");
                    alert.setHeaderText("Errore nell'indirizzo o nella porta!");
                    alert.showAndWait();
                }
            }
        }catch (MediaException ec){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore Fatale!");
            alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
            alert2.showAndWait();
            System.exit(1);
        } catch (Exception e) {
            try {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
            } catch (MediaException ec) {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Errore Fatale!");
                alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
                alert2.showAndWait();
                System.exit(1);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore!");
            alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
            alert2.showAndWait();
            System.exit(1);
        }
    }

    /** Metodo invocato quando viene cliccato il pulsante avanti dell'interfaccia opzioni
     *
     * @param event
     */
    @FXML
    private void avanti(ActionEvent event) {
        try {
            if (fileDat.isSelected() && !database.isSelected() && !fileBinario.isSelected()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/caricaFile.fxml"));

                ControllerCaricamentoFile controllerCaricamentoFile = new ControllerCaricamentoFile();
                controllerCaricamentoFile.setC(this.c);
                loader.setController(controllerCaricamentoFile);

                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("K-Nearest Neighbors");
                stage.setResizable(true);
                stage.setScene(scene);
                stage.setMinHeight(450);
                stage.setMinWidth(510);
                stage.show();
            } else if (!fileDat.isSelected() && !database.isSelected() && fileBinario.isSelected()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/caricaFileBinario.fxml"));

                ControllerCaricamentoFile controllerCaricamentoFile = new ControllerCaricamentoFile();
                controllerCaricamentoFile.setC(this.c);
                loader.setController(controllerCaricamentoFile);

                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("K-Nearest Neighbors");
                stage.setResizable(true);
                stage.setScene(scene);
                stage.setMinHeight(450);
                stage.setMinWidth(510);
                stage.show();
            } else if (!fileDat.isSelected() && database.isSelected() && !fileBinario.isSelected()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/caricaDatabase.fxml"));

                ControllerDatabase controllerDatabase = new ControllerDatabase();
                controllerDatabase.setC(this.c);
                loader.setController(controllerDatabase);

                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("K-Nearest Neighbors");
                stage.setResizable(true);
                stage.setScene(scene);
                stage.setMinHeight(450);
                stage.setMinWidth(510);
                stage.show();
            } else {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore!");
                if (!fileDat.isSelected() && !database.isSelected() && !fileBinario.isSelected()) {
                    alert.setHeaderText("Nessuna opzione è stata selezionata!");
                } else {
                    alert.setHeaderText("Solo un opzione deve essere selezionata!");
                }
                alert.showAndWait();
            }
        } catch (MediaException ec) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore Fatale!");
            alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
            alert2.showAndWait();
            System.exit(1);
        } catch (Exception e) {
            try {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
            } catch (MediaException ec) {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Errore Fatale!");
                alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
                alert2.showAndWait();
                System.exit(1);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore!");
            alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
            alert2.showAndWait();
            System.exit(1);
        }
    }

    /**
     *  Metodo set per settare il riferimento della classe Client data in input alla variabile d'istanza Client della classe
     * @param c
     */
    void setC(Client c) {
        this.c = c;
    }
}
