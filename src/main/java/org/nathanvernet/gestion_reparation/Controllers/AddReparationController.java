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
import java.util.Random;
import java.util.ResourceBundle;

public class AddReparationController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getReference = generateReference();
        refReparation.setText(getReference);
        try {
            qrCodeGenerator.saveQRCode(getReference, "/Users/nathan/DEV/" + getReference + ".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setClientTextField();
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
        controller.setAddReparationController(this);
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
                    saveDataToDatabase();
                    stageClose();
                } else if (result.get() == buttonTypeNePasEnregistrer) {
                    stageClose();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDataToDatabase() {
        try {
            String query = "INSERT INTO reparation (numero_reparation, id_client, details, reparation_effectuee, etat, id_reparateur, tarif, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE details=VALUES(details), reparation_effectuee=VALUES(reparation_effectuee), etat=VALUES(etat), id_reparateur=VALUES(id_reparateur), tarif=VALUES(tarif), date=VALUES(date)";
            PreparedStatement preparedStatement = gestionBDD.getConnection().prepareStatement(query);
            preparedStatement.setString(1, getReference);
            preparedStatement.setInt(2, getClientId());
            preparedStatement.setString(3, detailsPanne.getText());
            preparedStatement.setString(4, reparationEffectuee.getText());
            preparedStatement.setString(5, choiceBoxEtat.getValue());
            preparedStatement.setInt(6, getReparateurId());

            // Handle optional tarif
            if (tarif.getText().isEmpty()) {
                preparedStatement.setNull(7, java.sql.Types.DECIMAL);
            } else {
                preparedStatement.setBigDecimal(7, new BigDecimal(tarif.getText()));
            }

            preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis()));
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

    public String generateReference() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder reference = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            reference.append(chars.charAt(random.nextInt(chars.length())));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        reference.append(dateFormat.format(new Date()));

        return reference.toString();
    }

    private void print() throws IOException {
        Stage printStage = new Stage();
        VBox printVBox = new VBox(10);
        printVBox.setStyle("-fx-padding: 20px;");

        // Load the logo image
        ImageView logoImageView = new ImageView(new Image(String.valueOf(getClass().getResource("/org/nathanvernet/gestion_reparation/logo.png"))));
        logoImageView.setFitWidth(100);
        logoImageView.setPreserveRatio(true);

        Label titleLabel = new Label("NV Informatique");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label refLabel = new Label("Fiche réparation (N° " + getReference + ")");
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
        File qrCodeFile = new File("/Users/nathan/DEV/" + getReference + ".png");
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

    public void setReparation(ModeleReparation reparation) {
        getReference = reparation.getNumeroReparation();
        refReparation.setText(getReference);

        nomClient = reparation.getNomClient();
        prenomClient = reparation.getPrenomClient();
        telClient = reparation.getTelClient();
        emailClient = reparation.getEmailClient();
        societeClient = reparation.getSocieteClient();
        idClient = reparation.getId();

        setClientTextField();

        detailsPanne.setText(reparation.getDetails());
        reparationEffectuee.setText(reparation.getReparationEffectuee());
        choiceBoxEtat.setValue(reparation.getEtat());
        choiceBoxReparateur.setValue(reparation.getReparateur());

        // Vérifiez si le tarif est null avant de l'utiliser
        if (reparation.getTarif() != null) {
            tarif.setText(reparation.getTarif().toString());
        } else {
            tarif.setText("");
        }
    }
}