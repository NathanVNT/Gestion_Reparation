package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.Modele.ModeleClient;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SelectClientController implements Initializable {
    @FXML
    private TableView<ModeleClient> tableViewClients;
    @FXML
    private TableColumn<ModeleClient, String> colNom;
    @FXML
    private TableColumn<ModeleClient, String> colPrenom;
    @FXML
    private TableColumn<ModeleClient, String> colSociete;
    @FXML
    private TableColumn<ModeleClient, String> colType;
    @FXML
    private TableColumn<ModeleClient, String> colEmail;
    @FXML
    private TableColumn<ModeleClient, String> colTel;
    @FXML
    private TableColumn<ModeleClient, String> colCommentaire;

    private AddReparationController addReparationController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colSociete.setCellValueFactory(new PropertyValueFactory<>("societe"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));

        try {
            remplirTableView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAddReparationController(AddReparationController addReparationController) {
        this.addReparationController = addReparationController;
    }

    private void remplirTableView() throws SQLException {
        ArrayList<ModeleClient> clients = Application.gestionBDD.getClients();
        tableViewClients.getItems().setAll(clients);
    }

    @FXML
    private void handleSelectButtonAction() {
        ModeleClient selectedClient = tableViewClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            addReparationController.setClient(
                    selectedClient.getNom(),
                    selectedClient.getPrenom(),
                    selectedClient.getTelephone(),
                    selectedClient.getEmail(),
                    selectedClient.getSociete(),
                    selectedClient.getId() // Assurez-vous que cette méthode renvoie l'ID correct
            );
            closeStage();
        }
    }

    private void closeStage() {
        Stage stage = (Stage) tableViewClients.getScene().getWindow();
        stage.close();
    }
}