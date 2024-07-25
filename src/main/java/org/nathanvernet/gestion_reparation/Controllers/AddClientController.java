package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;
import org.nathanvernet.gestion_reparation.Modele.ModeleClient;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddClientController implements Initializable {

    @FXML
    private ChoiceBox<String> clientType;

    @FXML
    private TextField clientNom;

    @FXML
    private TextField clientPrenom;

    @FXML
    private TextField clientTel;

    @FXML
    private TextField clientEmail;

    @FXML
    private TextField clientAdresse;

    @FXML
    private TextField clientCodePostal;

    @FXML
    private TextField clientVille;

    @FXML
    private TextField clientSociete;

    @FXML
    private TextArea clientCommentaire;

    private GestionBDD gestionBDD;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionBDD = Application.gestionBDD;
        clientType.getItems().addAll("Professionnel", "Particulier");
        clientType.setOnAction(event -> handleClientTypeChange());
    }

    private void handleClientTypeChange() {
        if (clientType.getValue().equals("Particulier")) {
            clientSociete.setDisable(true);
            clientSociete.clear();
        } else {
            clientSociete.setDisable(false);
        }
    }

    @FXML
    private void saveClient() {
        try {
            ModeleClient client = new ModeleClient(
                    0,  // L'ID sera auto-incrémenté
                    clientNom.getText(),
                    clientPrenom.getText(),
                    clientCodePostal.getText(),
                    clientAdresse.getText(),
                    clientVille.getText(),
                    clientType.getValue(),
                    clientSociete.getText(),
                    clientCommentaire.getText(),
                    clientTel.getText(),
                    clientEmail.getText()
            );
            gestionBDD.addClient(client);
            showAlert("Succès", "Client ajouté avec succès.", Alert.AlertType.INFORMATION);
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout du client : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) clientNom.getScene().getWindow();
        stage.close();
    }
}