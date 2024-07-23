package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public MenuItem close;
    public MenuItem fichier_nouvelle_reparation;
    public MenuItem fichier_nouveau_client;
    public MenuItem paneau_Admin;
    public Button nouvelleReparation;
    public TextField textFieldRechercheReparation;
    public Button rechercheReparation;
    public Button boutonActualiser;
    public Button pageListeClients;
    private Scene currentScene;
    private GestionBDD gestionBDD = new GestionBDD();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        close.setOnAction(event -> System.exit(0));
        nouvelleReparation.setOnAction(event -> {
            try {
                openNouvelleReparation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        fichier_nouvelle_reparation.setOnAction(event -> {
            try {
                openNouvelleReparation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        pageListeClients.setOnAction(event -> {
            try {
                currentScene = nouvelleReparation.getScene();
                loadListeClients();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
    private void openNouvelleReparation() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("add-reparation.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Nouvelle Reparation");
        stage.show();
    }
    private void loadListeClients() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("clients-page.fxml"));
        Parent root = fxmlLoader.load();
        currentScene.setRoot(root);
    }
}