package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;
import org.nathanvernet.gestion_reparation.Modele.ModeleClient;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateClientController implements Initializable {

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
    private ModeleClient client;

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

    public void setClient(ModeleClient client) {
        this.client = client;
        clientType.setValue(client.getType());
        clientNom.setText(client.getNom());
        clientPrenom.setText(client.getPrenom());
        clientTel.setText(client.getTelephone());
        clientEmail.setText(client.getEmail());
        clientAdresse.setText(client.getAdresse());
        clientCodePostal.setText(client.getCodePostal());
        clientVille.setText(client.getVille());
        clientSociete.setText(client.getSociete());
        clientCommentaire.setText(client.getCommentaire());
    }

    @FXML
    private void updateClient() {
        try {
            client.setNom(clientNom.getText());
            client.setPrenom(clientPrenom.getText());
            client.setTelephone(clientTel.getText());
            client.setEmail(clientEmail.getText());
            client.setAdresse(clientAdresse.getText());
            client.setCodePostal(clientCodePostal.getText());
            client.setVille(clientVille.getText());
            client.setType(clientType.getValue());
            client.setSociete(clientSociete.getText());
            client.setCommentaire(clientCommentaire.getText());

            gestionBDD.updateClient(client);
            showAlert("Succès", "Client mis à jour avec succès.", Alert.AlertType.INFORMATION);
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la mise à jour du client : " + e.getMessage(), Alert.AlertType.ERROR);
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