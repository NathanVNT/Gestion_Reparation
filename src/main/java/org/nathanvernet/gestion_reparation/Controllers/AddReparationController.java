package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.Application;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;
import org.nathanvernet.gestion_reparation.QRCodeGenerator;

import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddReparationController implements Initializable {
    public ChoiceBox<String> choiceBoxReparateur;
    public GestionBDD gestionBDD = new GestionBDD();
    public Button exitButton;
    public ChoiceBox<String> choiceBoxEtat;
    public TextField refReparation;
    public Button printButton;
    public TextField clientNom;
    public TextField clientPrenom;
    public TextField clientTel;
    public TextField clientMail;
    public TextField clientSociete;
    private static String nomClient;
    private static String prenomClient;
    private static String telClient;
    private static String emailClient;
    private static String societeClient;
    public Button selectClient;
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
        try {
            remplirChoiceBoxReparateur(recupReparateur());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        exitButton.setOnAction(event -> stageClose());
        printButton.setOnAction(event -> print());
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

    private void print() {
        PrinterJob job = PrinterJob.getPrinterJob();

        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            graphics.setFont(new Font("Arial", Font.PLAIN, 12));

            graphics.drawString("Exemple de texte à imprimer", 100, 100);
            graphics.drawString("Client: " + nomClient + " " + prenomClient, 100, 120);
            graphics.drawString("Société: " + societeClient, 100, 140);
            graphics.drawString("Tel: " + telClient, 100, 160);
            graphics.drawString("E-Mail: " + emailClient, 100, 180);

            Image image = Toolkit.getDefaultToolkit().getImage("/Users/nathan/DEV/" + getReference + ".png");
            graphics.drawImage(image, 100, 200, 200, 200, null);

            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    public void setClient(String nom, String prenom, String tel, String email, String societe) {
        this.nomClient = nom;
        this.prenomClient = prenom;
        this.telClient = tel;
        this.emailClient = email;
        this.societeClient = societe;
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