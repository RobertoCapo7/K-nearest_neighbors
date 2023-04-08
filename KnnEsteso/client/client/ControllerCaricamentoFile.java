package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Classe controller delle seguenti interfacce: caricaFile, caricaFileBinario
 **/
public class ControllerCaricamentoFile implements Initializable {
    /** Variabile di istanza di tipo campo di testo che conterrà l'esempio dato in input dall'utente **/
    @FXML
    private TextField esempio;

    /** Variabile di istanza di tipo campo di testo che conterrà il valore K dato in input dall'utente **/
    @FXML
    private TextField valoreK;

    /** Variabile di istanza di tipo campo di testo che conterrà il percorso del file dato in input dall'utente **/
    @FXML
    private TextField path;

    /** Variabile di istanza di tipo campo di testo che conterrà la predizione data in output dal server **/
    @FXML
    private TextField predizione;

    /** Variabile di istanza client **/
    private Client c;

    /** Metodo invocato quando viene cliccato il pulsante calcola dell'interfaccia caricaFile
     *
     * @param event
     */
    @FXML
    private void load(ActionEvent event){
        try {
            Media sound2 = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
            MediaPlayer player2 = new MediaPlayer(sound2);
            player2.play();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            String percorso = path.getText();
            String esempioUtente = esempio.getText();
            String valorek = valoreK.getText();

            if (percorso.equals("")) {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();

                alert.setTitle("Errore!");
                alert.setHeaderText("Non è stato inserito il percorso del file!");
                alert.showAndWait();

                return;
            }
            if (esempioUtente.equals("")) {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Non è stato inserito l'esempio!");
                alert.showAndWait();
                return;
            }

            if (!valorek.matches("\\d+")) {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Valore K non accettato!");
                alert.showAndWait();
                return;
            }
            int k = Integer.parseInt(valorek);
            if (k < 1) {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Valore K non accettato!");
                alert.showAndWait();
                return;
            }
            String[] splitEsempio = esempioUtente.split(",");
            int cont;
            cont = 0;
            c.getOut().writeObject(1);
            String risposta;

            c.getOut().writeObject(percorso); // lettura nome file - tabella ;
            risposta = (String) c.getIn().readObject();
            if (risposta.contains("@ERROR")) {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText((String) c.getIn().readObject());
                alert.showAndWait();
                return;
            }
            c.getOut().writeObject(4);

            boolean flag = true; //reading example
            do {
                risposta = (String) (c.getIn().readObject());

                if (!risposta.contains("@ENDEXAMPLE")) {
                    // sto leggendo l'esempio

                    if (risposta.equals("@READSTRING")) { //leggo una stringa
                        if (cont > splitEsempio.length - 1) {
                            Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                            MediaPlayer player = new MediaPlayer(sound);
                            player.play();
                            alert.setTitle("Errore!");
                            alert.setHeaderText("Attributi mancanti!");
                            alert.showAndWait();
                            c.getOut().writeObject(null);
                            c.getOut().writeObject(1);
                            return;
                        } else {
                            c.getOut().writeObject(splitEsempio[cont]);
                            cont++;
                        }
                    } else if (risposta.equals("@READDOUBLE")) { //leggo un numero
                        if (cont > splitEsempio.length - 1) {
                            Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                            MediaPlayer player = new MediaPlayer(sound);
                            player.play();
                            alert.setTitle("Errore!");
                            alert.setHeaderText("Attributi minori rispetto agli aspettati!");
                            alert.showAndWait();
                            c.getOut().writeObject(null);
                            return;
                        } else {
                            if (!splitEsempio[cont].matches("-?\\d+(\\.\\d+)?")) {
                                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                                MediaPlayer player = new MediaPlayer(sound);
                                player.play();
                                alert.setTitle("Errore!");
                                alert.setHeaderText("Attributo continuo non numerico!");
                                alert.showAndWait();
                                c.getOut().writeObject(null);
                                return;
                            } else {
                                double x = Double.parseDouble(splitEsempio[cont]);
                                cont++;
                                c.getOut().writeObject(x);
                            }
                        }
                    }
                } else
                    flag = false;
            } while (flag);

            risposta = (String) (c.getIn().readObject());

            if (cont < splitEsempio.length) {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Esempi con attributi maggiori!");
                alert.showAndWait();
                c.getOut().writeObject(null);
                return;
            }

            c.getOut().writeObject(k);

            risposta = c.getIn().readObject().toString();
            if (risposta.contains("@ERROR")) {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText((String) c.getIn().readObject());
                alert.showAndWait();
                return;
            }
            predizione.setText(risposta);
        }catch (MediaException ec){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore Fatale!");
            alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
            alert2.showAndWait();
            System.exit(1);
        } catch (Exception e){
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
            alert2.setHeaderText("Connessione al server persa!");
            alert2.showAndWait();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/connessioneServer.fxml"));
                ControllerServer controllerCaricamentoFile = new ControllerServer();
                loader.setController(controllerCaricamentoFile);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setResizable(false);
                stage.setTitle("K-Nearest Neighbors");
                stage.setScene(scene);
                stage.sizeToScene();
                stage.setMinHeight(400);
                stage.setMinWidth(500);
                stage.centerOnScreen();
                stage.show();
            }catch (Exception ec){
                try {
                    Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                    MediaPlayer player = new MediaPlayer(sound);
                    player.play();
                }catch (MediaException ec2) {
                    Alert alert3 = new Alert(Alert.AlertType.ERROR);
                    alert3.setTitle("Errore Fatale!");
                    alert3.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
                    alert3.showAndWait();
                    System.exit(1);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Errore Fatale!");
                alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
                alert2.showAndWait();
                System.exit(1);
            }
        }
    }

    /** Metodo invocato quando viene cliccato il pulsante calcola dell'interfaccia caricaFileBinario
     *
     * @param event
     */
    @FXML
    private void loadBinary(ActionEvent event) {
        try{
            Media sound2 = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
            MediaPlayer player2 = new MediaPlayer(sound2);
            player2.play();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            String percorso = path.getText();
            String esempioUtente = esempio.getText();
            String valorek = valoreK.getText();

            if (percorso.equals("")) {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();

                alert.setTitle("Errore!");
                alert.setHeaderText("Non è stato inserito il percorso del file!");
                alert.showAndWait();

                return;
            }
            if(esempioUtente.equals("")){
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Non è stato inserito l'esempio!");
                alert.showAndWait();
                return;
            }

            if(!valorek.matches("\\d+")){
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Valore K non accettato!");
                alert.showAndWait();
                return;
            }
            int k=Integer.parseInt(valorek);
            if(k < 1){
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Valore K non accettato!");
                alert.showAndWait();
                return;
            }
            String[] splitEsempio = esempioUtente.split(",");
            int cont;
            cont = 0;
            c.getOut().writeObject(2);
            String risposta;

            c.getOut().writeObject(percorso); // lettura nome file - tabella
            risposta = (String)c.getIn().readObject();
            if(risposta.contains("@ERROR")){
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText((String)c.getIn().readObject());
                alert.showAndWait();
                return;
            }

            c.getOut().writeObject(4);

            boolean flag = true; //reading example
            do {
                risposta = (String)(c.getIn().readObject());

                if(!risposta.contains("@ENDEXAMPLE")) {
                    // sto leggendo l'esempio

                    if(risposta.equals("@READSTRING")) { //leggo una stringa
                        if(cont > splitEsempio.length -1){
                            Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                            MediaPlayer player = new MediaPlayer(sound);
                            player.play();
                            alert.setTitle("Errore!");
                            alert.setHeaderText("Attributi mancanti!");
                            alert.showAndWait();
                            c.getOut().writeObject(null);
                            c.getOut().writeObject(1);
                            return;
                        }
                        else {
                            c.getOut().writeObject(splitEsempio[cont]);
                            cont++;
                        }
                    } else if(risposta.equals("@READDOUBLE")) { //leggo un numero
                        if(cont > splitEsempio.length -1) {
                            Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                            MediaPlayer player = new MediaPlayer(sound);
                            player.play();
                            alert.setTitle("Errore!");
                            alert.setHeaderText("Attributi minori rispetto agli aspettati!");
                            alert.showAndWait();
                            c.getOut().writeObject(null);
                            return;
                        }
                        else {
                            if(!splitEsempio[cont].matches("-?\\d+(\\.\\d+)?")){
                                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                                MediaPlayer player = new MediaPlayer(sound);
                                player.play();
                                alert.setTitle("Errore!");
                                alert.setHeaderText("Attributo continuo non numerico!");
                                alert.showAndWait();
                                c.getOut().writeObject(null);
                                return;
                            }
                            else {
                                double x = Double.parseDouble(splitEsempio[cont]);
                                cont++;
                                c.getOut().writeObject(x);
                            }
                        }
                    }
                }
                else
                    flag = false;
            } while(flag);

            risposta = (String)(c.getIn().readObject());

            if(cont < splitEsempio.length){
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText("Esempi con attributi maggiori!");
                alert.showAndWait();
                c.getOut().writeObject(null);
                return;
            }

            c.getOut().writeObject(k);
            risposta = c.getIn().readObject().toString();
            if(risposta.contains("@ERROR")){
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
                alert.setTitle("Errore!");
                alert.setHeaderText((String)c.getIn().readObject());
                alert.showAndWait();
                return;
            }
            predizione.setText(risposta);
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
            alert2.setHeaderText("Connessione al server persa!");
            alert2.showAndWait();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/connessioneServer.fxml"));
                ControllerServer controllerCaricamentoFile = new ControllerServer();
                loader.setController(controllerCaricamentoFile);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setResizable(false);
                stage.setTitle("K-Nearest Neighbors");
                stage.setScene(scene);
                stage.sizeToScene();
                stage.setMinHeight(400);
                stage.setMinWidth(500);
                stage.centerOnScreen();
                stage.show();
            } catch (Exception ec) {
                try {
                    Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                    MediaPlayer player = new MediaPlayer(sound);
                    player.play();
                } catch (MediaException ec2) {
                    Alert alert3 = new Alert(Alert.AlertType.ERROR);
                    alert3.setTitle("Errore Fatale!");
                    alert3.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
                    alert3.showAndWait();
                    System.exit(1);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Errore Fatale!");
                alert2.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
                alert2.showAndWait();
                System.exit(1);
            }
        }
    }

    /** Metodo invocato quando viene cliccata l'immagine indietro dell'interfaccia caricaFile o caricaFileBinario
     *
     * @param event
     */
    @FXML
    private void back(MouseEvent event) {
        try{
            c.getOut().writeObject(null);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/opzioni.fxml"));

            ControllerServer controllerCaricamentoFile = new ControllerServer();
            controllerCaricamentoFile.setC(this.c);
            loader.setController(controllerCaricamentoFile);

            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ec) {
            try {
                Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                player.play();
            } catch (MediaException ec2) {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Errore Fatale!");
                alert3.setHeaderText("File mancanti per il corretto funzionamento dell'App! L'app verrà chiusa");
                alert3.showAndWait();
                System.exit(1);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore Fatale!");
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

    /**
     *  Override del metodo initialize della interfaccia Inizializable
     *  Metodo usato per inizializzare la casella di testo della predizione a non editabile,
     *  ovvero in modo che non sia possibile scriverci dentro
     * @param url Url
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        predizione.setEditable(false);
    }
}
