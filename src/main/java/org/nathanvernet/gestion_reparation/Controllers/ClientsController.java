package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public Button listeReparationsPage;
    private Scene currentScene;

    public ClientsController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listeReparationsPage.setOnAction(event -> {
            currentScene = listeReparationsPage.getScene();
            try {
                loadListeReparations();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadListeReparations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home-page.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = listeReparationsPage.getScene();
        scene.setRoot(root);
    }
}