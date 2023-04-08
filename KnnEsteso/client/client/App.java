package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;
import java.net.URISyntaxException;

/**
 * Classe principale del software.
 */
public class App extends Application {

    /**
     * Metodo che avvia l'interfaccia grafica.
     * @param stage finestra per contenere il pannello dell'applicazione
     */
    @Override
    public void start(Stage stage) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/benvenuto.fxml"));
            ControllerBenvenuto controllerBenvenuto = new ControllerBenvenuto();
            loader.setController(controllerBenvenuto);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setResizable(true);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setMinHeight(400);
            stage.setMinWidth(500);
            Image icon = new Image(getClass().getResourceAsStream("resources/image/download.png"));

            stage.setTitle("K-nearest neighbors");
            stage.getIcons().add(icon);
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
     * Programma principale.
     * @param args args per avviare correttamente il programma
     */
    public static void main(String[] args) {
        launch(args);
    }
}
