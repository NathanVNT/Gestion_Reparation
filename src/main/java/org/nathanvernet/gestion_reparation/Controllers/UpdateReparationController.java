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
import org.nathanvernet.gestion_reparation.QRCodeGenerator;
import org.nathanvernet.gestion_reparation.Modele.ModeleReparation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateReparationController implements Initializable {
    public DatePicker dateReparation;
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
    private static Date date;
    private static String prenomClient;
    private static String telClient;
    private static String emailClient;
    private static String societeClient;
    private static int idClient;
    @FXML
    private Button selectClient;
    private QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private String dateFormat;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                    saveUpdateToDatabase();
                    stageClose();
                } else if (result.get() == buttonTypeNePasEnregistrer) {
                    stageClose();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveUpdateToDatabase() {
        try {
            String query = "UPDATE reparation SET id_client = ?, details = ?, reparation_effectuee = ?, etat = ?, id_reparateur = ?, tarif = ?, date = ? WHERE numero_reparation = ?";
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

            preparedStatement.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            preparedStatement.setString(8, refReparation.getText());
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

    private void remplirChoiceBoxReparateur(List<String> choix) {
        choiceBoxReparateur.getItems().clear();
        choiceBoxReparateur.getItems().addAll(choix);
    }

    private List<String> recupReparateur() throws SQLException {
        return gestionBDD.RecupReparateur();
    }

    public void setReparation(ModeleReparation reparation) {
        refReparation.setText(reparation.getNumeroReparation());
        date = reparation.getDate();
        dateReparation.setValue(date.toLocalDate());
        dateFormat = format.format(date);
        nomClient = reparation.getNomClient();
        prenomClient = reparation.getPrenomClient();
        telClient = reparation.getTelClient();
        emailClient = reparation.getEmailClient();
        societeClient = reparation.getSocieteClient();
        idClient = reparation.getClientId();

        clientNom.setText(nomClient);
        clientPrenom.setText(prenomClient);
        clientTel.setText(telClient);
        clientMail.setText(emailClient);
        clientSociete.setText(societeClient);

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

    public void setClient(String nom, String prenom, String tel, String email, String societe, int id) {
        nomClient = nom;
        prenomClient = prenom;
        telClient = tel;
        emailClient = email;
        societeClient = societe;
        idClient = id;
        clientNom.setText(nomClient);
        clientPrenom.setText(prenomClient);
        clientTel.setText(telClient);
        clientMail.setText(emailClient);
        clientSociete.setText(societeClient);
    }

    private void print() throws IOException {
        // Configuration de l'impression
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            VBox vbox = createPrintContent();
            if (job.printPage(vbox)) {
                job.endJob();
            }
        }
    }

    private VBox createPrintContent() throws IOException {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-padding: 20px; -fx-spacing: 10px;");
        // Ajouter les informations légales
        Label legalInfo = new Label("Informations légales de l'entreprise: NV Informatique - Entrepreneur Individuel, N° SIRET: 91745027200013");
        legalInfo.setStyle("-fx-font-size: 10px;");
        vbox.getChildren().add(legalInfo);
        // Ajout du logo
        Image logo = new Image(String.valueOf(getClass().getResource("/org/nathanvernet/gestion_reparation/logo.png")));
        ImageView logoImageView = new ImageView(logo);
        logoImageView.setFitWidth(100);
        logoImageView.setPreserveRatio(true);
        vbox.getChildren().add(logoImageView);

        Label titleLabel = new Label("NV Informatique");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vbox.getChildren().add(titleLabel);

        Label refLabel = new Label("Fiche réparation (N° " + refReparation.getText() + ")");
        refLabel.setStyle("-fx-font-weight: bold;");
        vbox.getChildren().add(refLabel);

        Label clientLabel = new Label("Client: ");
        clientLabel.setStyle("-fx-font-weight: bold;");
        vbox.getChildren().add(clientLabel);

        Label clientDetails = new Label("Le: " + dateFormat+"\nNom: " + nomClient + "\nPrénom: " + prenomClient + "\nTel: " + telClient + "\nE-Mail: " + emailClient + "\nSociété: " + societeClient);
        vbox.getChildren().add(clientDetails);

        Label panneLabel = new Label("Détail de la panne: ");
        vbox.getChildren().add(panneLabel);
        Label panneDetails = new Label(detailsPanne.getText());
        vbox.getChildren().add(panneDetails);

        Label reparationLabel = new Label("Réparations effectuées: ");
        vbox.getChildren().add(reparationLabel);
        Label reparationDetails = new Label(reparationEffectuee.getText());
        vbox.getChildren().add(reparationDetails);

        Label reparateurLabel = new Label("Réparateur: " + choiceBoxReparateur.getValue());
        vbox.getChildren().add(reparateurLabel);

        BigDecimal tarifHT = tarif.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(tarif.getText());
        Label tarifLabel = new Label("Tarif: " + tarifHT + " €");
        vbox.getChildren().add(tarifLabel);

        // Générer et ajouter le QR code
        byte[] qrCodeBytes = qrCodeGenerator.generateQRCode(refReparation.getText());
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));
        WritableImage qrCodeWritableImage = SwingFXUtils.toFXImage(bufferedImage, null);
        ImageView qrCodeImageView = new ImageView(qrCodeWritableImage);
        qrCodeImageView.setFitWidth(100);
        qrCodeImageView.setPreserveRatio(true);
        vbox.getChildren().add(qrCodeImageView);

        return vbox;
    }

    public void setUpdateReparationController(UpdateReparationController controller) {
        // Implementation pour le SelectClientController
    }
}