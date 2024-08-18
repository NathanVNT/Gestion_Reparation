package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;
import org.nathanvernet.gestion_reparation.Modele.ModeleReparation;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Button nouveauClient;
    @FXML
    private MenuItem close;
    @FXML
    private MenuItem fichier_nouvelle_reparation;
    @FXML
    private MenuItem fichier_nouveau_client;
    @FXML
    private MenuItem paneau_Admin;
    @FXML
    private Button nouvelleReparation;
    @FXML
    private TextField textFieldRechercheReparation;
    @FXML
    private Button rechercheReparation;
    @FXML
    private Button boutonActualiser;
    @FXML
    private Button pageListeClients;
    @FXML
    private TableView<ModeleReparation> tableViewReparations;
    @FXML
    private TableColumn<ModeleReparation, Date> colDate;
    @FXML
    private TableColumn<ModeleReparation, String> colNumeroReparation;
    @FXML
    private TableColumn<ModeleReparation, String> colNomClient;
    @FXML
    private TableColumn<ModeleReparation, String> colPrenomClient;
    @FXML
    private TableColumn<ModeleReparation, String> colDetails;
    @FXML
    private TableColumn<ModeleReparation, String> colReparationEffectuee;
    @FXML
    private TableColumn<ModeleReparation, String> colEtat;
    @FXML
    private TableColumn<ModeleReparation, String> colReparateur;
    @FXML
    private TableColumn<ModeleReparation, BigDecimal> colTarif;

    private Scene currentScene;
    private GestionBDD gestionBDD = new GestionBDD();

    public HomeController() throws IOException {
    }

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
        fichier_nouveau_client.setOnAction(event -> {
            try {
                openAddClientPage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        nouveauClient.setOnAction(event -> {
            try {
                openAddClientPage();
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
                openListeClients();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        boutonActualiser.setOnAction(event -> {
            try {
                loadReparations();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        rechercheReparation.setOnAction(event -> {
            try {
                searchReparations();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colNumeroReparation.setCellValueFactory(new PropertyValueFactory<>("numeroReparation"));
        colNomClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        colPrenomClient.setCellValueFactory(new PropertyValueFactory<>("prenomClient"));
        colDetails.setCellValueFactory(new PropertyValueFactory<>("details"));
        colReparationEffectuee.setCellValueFactory(new PropertyValueFactory<>("reparationEffectuee"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colReparateur.setCellValueFactory(new PropertyValueFactory<>("reparateur"));
        colTarif.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        colTarif.setCellFactory(column -> new TableCell<ModeleReparation, BigDecimal>() {
            private final DecimalFormat df = createDecimalFormat();

            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(df.format(item) + " €");
                }
            }
        });

        try {
            loadReparations();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableViewReparations.setRowFactory(tv -> {
            TableRow<ModeleReparation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ModeleReparation rowData = row.getItem();
                    try {
                        openModifierReparation(rowData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    private DecimalFormat createDecimalFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(',');
        return new DecimalFormat("#,##0.00", symbols);
    }

    private void openAddClientPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("add-client.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Nouveau Client");
        stage.show();
    }

    private void loadReparations() throws SQLException {
        ArrayList<ModeleReparation> reparations = gestionBDD.getReparations();
        tableViewReparations.getItems().setAll(reparations);
    }

    private void searchReparations() throws SQLException {
        String searchTerm = textFieldRechercheReparation.getText();
        ArrayList<ModeleReparation> reparations = gestionBDD.searchReparations(searchTerm);
        tableViewReparations.getItems().setAll(reparations);
    }

    private void openNouvelleReparation() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("add-reparation.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Nouvelle Reparation");
        stage.show();

        stage.setOnHidden(event -> {
            try {
                loadReparations();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void openModifierReparation(ModeleReparation reparation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("update-reparation.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Modifier Reparation");

        UpdateReparationController controller = fxmlLoader.getController();
        controller.setReparation(reparation);

        stage.show();

        stage.setOnHidden(event -> {
            try {
                loadReparations();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void openListeClients() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("clients-page.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = nouvelleReparation.getScene();
        scene.setRoot(root);
    }

}