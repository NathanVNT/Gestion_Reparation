package org.nathanvernet.gestion_reparation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;

import java.io.IOException;
import java.sql.SQLException;

public class Application extends javafx.application.Application {
    public static GestionBDD gestionBDD;

    static {
        try {
            gestionBDD = new GestionBDD();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Gestion Réparation by Nathan VERNET");
        stage.setScene(scene);

        // Set the application icon
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        // Maximize the window
        stage.setMaximized(true);

        stage.show();
        getBDD(stage);
    }

    private void getBDD(Stage primaryStage) {
        try {
            gestionBDD.ConnexionBDD();
        } catch (SQLException e) {
            BDDConfigurationWindow.showConfigurationWindow(primaryStage, gestionBDD);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
