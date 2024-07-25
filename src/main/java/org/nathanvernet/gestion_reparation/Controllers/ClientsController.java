package org.nathanvernet.gestion_reparation.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;
import org.nathanvernet.gestion_reparation.Modele.ModeleClient;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    @FXML
    private TableView<ModeleClient> tableViewClients;
    @FXML
    private TableColumn<ModeleClient, String> colNom;
    @FXML
    private TableColumn<ModeleClient, String> colPrenom;
    @FXML
    private TableColumn<ModeleClient, String> colTelephone;
    @FXML
    private TableColumn<ModeleClient, String> colMail;
    @FXML
    private TableColumn<ModeleClient, String> colAdresse;
    @FXML
    private TableColumn<ModeleClient, String> colCodePostal;
    @FXML
    private TableColumn<ModeleClient, String> colVille;
    @FXML
    private TableColumn<ModeleClient, String> colType;
    @FXML
    private TableColumn<ModeleClient, String> colSociete;
    @FXML
    private TableColumn<ModeleClient, String> colCommentaire;
    @FXML
    private Button listeReparationsPage;

    public ClientsController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listeReparationsPage.setOnAction(event -> {
            try {
                loadListeReparations();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        configureTableView();
        try {
            loadClients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void configureTableView() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colCodePostal.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colSociete.setCellValueFactory(new PropertyValueFactory<>("societe"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
    }

    private void loadClients() throws SQLException {
        ArrayList<ModeleClient> clients = Application.gestionBDD.getClients();
        ObservableList<ModeleClient> clientList = FXCollections.observableArrayList(clients);
        tableViewClients.setItems(clientList);
    }

    private void loadListeReparations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) listeReparationsPage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}