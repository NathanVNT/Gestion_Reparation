package org.nathanvernet.gestion_reparation;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    }
    private void openNouvelleReparation() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("add-reparation.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Nouvelle Reparation");
        stage.show();
    }
}