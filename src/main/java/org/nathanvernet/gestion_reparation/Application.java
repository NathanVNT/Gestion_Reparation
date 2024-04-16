package org.nathanvernet.gestion_reparation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;

import java.io.IOException;
import java.sql.SQLException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Gestion Réparation by Nathan VERNET");
        stage.setScene(scene);
        stage.show();
        GestionBDD gestionBDD = new GestionBDD();
        try {
            gestionBDD.ConnexionBDD();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Erreur de connexion");
            alert.setContentText("Erreur de connexion à la base de données veuillez vérifier la configuration.");
            alert.showAndWait();
            stage.close();

        }
    }

    public static void main(String[] args) {
        launch();
    }
}