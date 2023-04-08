package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;

/**
 * Classe controller delle seguenti interfacce: benvenuto
 **/

public class ControllerBenvenuto {
    /** Metodo invocato quando viene cliccato il pulsante entra dell'interfaccia benvenuto
     *
     * @param event
     */
    @FXML
    private void enter(ActionEvent event) {
        try {
            Media sound = new Media(getClass().getResource("resources/sound/suono.mp3").toURI().toString());;;;;;;
            MediaPlayer player = new MediaPlayer(sound);
            player.play();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/connessioneServer.fxml"));
            ControllerServer controllerCaricamentoFile = new ControllerServer();
            loader.setController(controllerCaricamentoFile);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("K-Nearest Neighbors");
            stage.sizeToScene();
            stage.setMinHeight(450);
            stage.setMinWidth(500);
            stage.centerOnScreen();
            stage.show();
        }catch (Exception ec) {
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
}
