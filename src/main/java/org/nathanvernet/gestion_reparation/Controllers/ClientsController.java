package org.nathanvernet.gestion_reparation.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.Modele.ModeleClient;
import org.nathanvernet.gestion_reparation.Modele.ModeleReparation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.nathanvernet.gestion_reparation.Application.gestionBDD;

public class ClientsController implements Initializable {
    public Button nouveauClient;
    public MenuItem fichier_nouvelle_reparation;
    public MenuItem fichier_nouveau_client;
    public MenuItem close;
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
    private TableColumn<ModeleClient, String> colCommentaire;
    @FXML
    private TableColumn<ModeleClient, String> colSociete;
    @FXML
    private Button boutonActualiser;
    @FXML
    private TextField textFieldRechercheClient;
    @FXML
    private Button rechercheClient;
    @FXML
    private Button listeReparationsPage;

    private ObservableList<ModeleClient> clientList;
    private FilteredList<ModeleClient> filteredClientList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colCodePostal.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        colSociete.setCellValueFactory(new PropertyValueFactory<>("societe"));

        loadClients();
        close.setOnAction(event -> {
            System.exit(0);
        });
        fichier_nouvelle_reparation.setOnAction(event -> {
            try {
                openNouvelleReparation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        fichier_nouveau_client.setOnAction(event -> {
            try {
                openAddClientPage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        tableViewClients.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableViewClients.getSelectionModel().getSelectedItem() != null) {
                ModeleClient selectedClient = tableViewClients.getSelectionModel().getSelectedItem();
                try {
                    openUpdateClient(selectedClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        boutonActualiser.setOnAction(event -> loadClients());

        rechercheClient.setOnAction(event -> searchClients());

        listeReparationsPage.setOnAction(event -> {
            try {
                openListeReparations();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        nouveauClient.setOnAction(event -> {
            try {
                openAddClientPage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }
    private void openAddClientPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("add-client.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Nouveau Client");
        stage.show();
    }
    private void loadClients() {
        try {
            clientList = FXCollections.observableArrayList(gestionBDD.getClients());
            filteredClientList = new FilteredList<>(clientList, p -> true);
            tableViewClients.setItems(filteredClientList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des clients : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void searchClients() {
        String searchText = textFieldRechercheClient.getText().toLowerCase();
        filteredClientList.setPredicate(client -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            return client.getNom().toLowerCase().contains(searchText) ||
                    client.getPrenom().toLowerCase().contains(searchText) ||
                    client.getTelephone().toLowerCase().contains(searchText) ||
                    client.getEmail().toLowerCase().contains(searchText) ||
                    client.getAdresse().toLowerCase().contains(searchText) ||
                    client.getCodePostal().toLowerCase().contains(searchText) ||
                    client.getVille().toLowerCase().contains(searchText) ||
                    client.getType().toLowerCase().contains(searchText) ||
                    (client.getSociete() != null && client.getSociete().toLowerCase().contains(searchText)) ||
                    (client.getCommentaire() != null && client.getCommentaire().toLowerCase().contains(searchText));
        });
    }

    private void openUpdateClient(ModeleClient client) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("update-client.fxml"));
        Parent root = fxmlLoader.load();
        UpdateClientController controller = fxmlLoader.getController();
        controller.setClient(client);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Modifier Client");
        stage.show();
        stage.setOnHidden(event -> loadClients());
    }

    private void openListeReparations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home-page.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = nouveauClient.getScene();
        scene.setRoot(root);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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