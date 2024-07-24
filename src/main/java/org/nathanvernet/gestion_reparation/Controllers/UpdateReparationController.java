package org.nathanvernet.gestion_reparation.Controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;
import org.nathanvernet.gestion_reparation.Modele.ModeleReparation;
import org.nathanvernet.gestion_reparation.QRCodeGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateReparationController implements Initializable {
    @FXML
    private ChoiceBox<String> choiceBoxReparateur;
    @FXML
    private GestionBDD gestionBDD = new GestionBDD();
    @FXML
    private Button exitButton;
    @FXML
    private ChoiceBox<String> choiceBoxEtat;
    @FXML
    private TextField refReparation;
    @FXML
    private Button printButton;
    @FXML
    private TextField clientNom;
    @FXML
    private TextField clientPrenom;
    @FXML
    private TextField clientTel;
    @FXML
    private TextField clientMail;
    @FXML
    private TextField clientSociete;
    @FXML
    private TextArea detailsPanne;
    @FXML
    private TextArea reparationEffectuee;
    @FXML
    private TextField tarif;

    private static String nomClient;
    private static String prenomClient;
    private static String telClient;
    private static String emailClient;
    private static String societeClient;
    private static int idClient;
    @FXML
    private Button selectClient;
    private ArrayList<String> etat = new ArrayList<>();
    private String getReference;
    private QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
    private ModeleReparation reparation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        etat.add("Non traité");
        etat.add("En cours");
        etat.add("Traité");
        remplirChoiceBoxEtat(etat);
        choiceBoxEtat.setValue("Non traité"); // Set default value to "Non traité"
        try {
            remplirChoiceBoxReparateur(recupReparateur());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        exitButton.setOnAction(event -> handleExit());
        printButton.setOnAction(event -> {
            try {
                print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        selectClient.setOnAction(event -> {
            try {
                openSelectClient();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void openSelectClient() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("select-client.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        SelectClientController controller = fxmlLoader.getController();
        controller.setUpdateReparationController(this);
        stage.setScene(scene);
        stage.setTitle("Sélection de client");
        stage.show();
    }

    private void handleExit() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Enregistrer");
            alert.setHeaderText("Voulez-vous enregistrer les modifications avant de quitter ?");
            alert.setContentText("Choisissez une option.");

            ButtonType buttonTypeEnregistrer = new ButtonType("Enregistrer");
            ButtonType buttonTypeAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType buttonTypeNePasEnregistrer = new ButtonType("Ne pas enregistrer");

            alert.getButtonTypes().setAll(buttonTypeEnregistrer, buttonTypeNePasEnregistrer, buttonTypeAnnuler);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == buttonTypeEnregistrer) {
                    updateDataInDatabase();
                    stageClose();
                } else if (result.get() == buttonTypeNePasEnregistrer) {
                    stageClose();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDataInDatabase() {
        try {
            String query = "UPDATE reparation SET id_client = ?, details =?, reparation_effectuee = ?, etat = ?, id_reparateur = ?, tarif = ? WHERE id = ?";
            PreparedStatement preparedStatement = gestionBDD.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, getClientId());
            preparedStatement.setString(2, detailsPanne.getText());
            preparedStatement.setString(3, reparationEffectuee.getText());
            preparedStatement.setString(4, choiceBoxEtat.getValue());
            preparedStatement.setInt(5, getReparateurId());
            // Handle optional tarif
            if (tarif.getText().isEmpty()) {
                preparedStatement.setNull(6, java.sql.Types.DECIMAL);
            } else {
                preparedStatement.setBigDecimal(6, new BigDecimal(tarif.getText()));
            }

            preparedStatement.setInt(7, reparation.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getClientId() {
        return idClient;
    }

    private int getReparateurId() {
        String selectedReparateur = choiceBoxReparateur.getValue();
        int reparateurId = 0;
        try {
            String query = "SELECT id FROM reparateur WHERE prenom = ?";
            PreparedStatement preparedStatement = gestionBDD.getConnection().prepareStatement(query);
            preparedStatement.setString(1, selectedReparateur);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                reparateurId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reparateurId;
    }

    private void stageClose() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    private void remplirChoiceBoxReparateur(ArrayList<String> choix) {
        choiceBoxReparateur.getItems().clear();
        choiceBoxReparateur.getItems().addAll(choix);
    }

    private void remplirChoiceBoxEtat(ArrayList<String> choix) {
        choiceBoxEtat.getItems().clear();
        choiceBoxEtat.getItems().addAll(choix);
    }

    private ArrayList<String> recupReparateur() throws SQLException {
        return gestionBDD.RecupReparateur();
    }

    private void print() throws IOException {
        Stage printStage = new Stage();
        VBox printVBox = new VBox(10);
        printVBox.setStyle("-fx-padding: 20px;");

        // Load the logo image
        ImageView logoImageView = new ImageView(new Image(String.valueOf(getClass().getResource("./logo.png"))));
        logoImageView.setFitWidth(100);
        logoImageView.setPreserveRatio(true);

        Label titleLabel = new Label("NV Informatique");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label refLabel = new Label("Fiche réparation (N° " + reparation.getNumeroReparation() + ")");
        refLabel.setStyle("-fx-font-weight: bold;");

        Label clientLabel = new Label("Client: ");
        Label clientDetails = new Label("Nom: " + nomClient + "\nPrénom: " + prenomClient + "\nTel: " + telClient + "\nE-Mail: " + emailClient + "\nSociété: " + societeClient);

        Label panneLabel = new Label("Détail de la panne: ");
        Label panneDetails = new Label(detailsPanne.getText());

        Label reparationLabel = new Label("Réparations effectuées: ");
        Label reparationDetails = new Label(reparationEffectuee.getText());
        Label reparateurLabel = new Label("Réparateur: " + choiceBoxReparateur.getValue());

        BigDecimal tarifHT = tarif.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(tarif.getText());
        Label tarifLabel = new Label("Tarif HT: " + tarifHT + " €");

        // Load the QR code image
        ImageView qrCodeImageView = new ImageView();
        File qrCodeFile = new File("/Users/nathan/DEV/" + reparation.getNumeroReparation() + ".png");
        BufferedImage bufferedImage = ImageIO.read(qrCodeFile);
        WritableImage qrCodeWritableImage = SwingFXUtils.toFXImage(bufferedImage, null);
        qrCodeImageView.setImage(qrCodeWritableImage);

        // Add elements to the VBox
        printVBox.getChildren().addAll(logoImageView, titleLabel, refLabel, clientLabel, clientDetails, panneLabel, panneDetails, reparationLabel, reparationDetails, reparateurLabel, tarifLabel, qrCodeImageView);

        Scene printScene = new Scene(printVBox);
        printStage.setScene(printScene);

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(printStage)) {
            boolean success = job.printPage(printVBox);
            if (success) {
                job.endJob();
            }
        }
    }

    public void setReparation(ModeleReparation reparation) {
        this.reparation = reparation;
        refReparation.setText(reparation.getNumeroReparation());
        detailsPanne.setText(reparation.getDetails());
        reparationEffectuee.setText(reparation.getReparationEffectuee());
        choiceBoxEtat.setValue(reparation.getEtat());
        tarif.setText(reparation.getTarif() != null ? reparation.getTarif().toString() : "");
        choiceBoxReparateur.setValue(reparation.getReparateur());

        // Set client details
        nomClient = reparation.getNomClient();
        prenomClient = reparation.getPrenomClient();
        telClient = reparation.getTelClient();
        emailClient = reparation.getEmailClient();
        societeClient = reparation.getSocieteClient();
        idClient = reparation.getClientId();

        setClientTextField();
    }

    public void setClient(String nom, String prenom, String tel, String email, String societe, int id) {
        nomClient = nom;
        prenomClient = prenom;
        telClient = tel;
        emailClient = email;
        societeClient = societe;
        idClient = id;
        setClientTextField();
    }

    private void setClientTextField() {
        clientNom.setText(nomClient);
        clientPrenom.setText(prenomClient);
        clientTel.setText(telClient);
        clientMail.setText(emailClient);
        clientSociete.setText(societeClient);
    }
}