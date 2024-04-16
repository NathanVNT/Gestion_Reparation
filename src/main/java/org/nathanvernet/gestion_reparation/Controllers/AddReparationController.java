package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    public ChoiceBox choiceBoxReparateur;
    public GestionBDD gestionBDD = new GestionBDD();
    public Button exitButton;
    public ChoiceBox choiceBoxEtat;
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
    private ArrayList etat = new ArrayList();
    private String getReference;
    private QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getReference = generateReference();
        refReparation.setText(getReference);
        try {
            qrCodeGenerator.saveQRCode(getReference, "/Users/nathan/DEV/"+ getReference+".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setClient("Michel", "truc", "0606060606", "example@example.com", "");
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
        exitButton.setOnAction(event -> {
            stageClose();
        });
        printButton.setOnAction(event -> print());
    }

    /**
     * Méthode qui permet la fermeture du stage
     */
    private void stageClose() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
    * @param choix
     * Méthode qui rempli une choiceBox (ici la choicebox état de réparation)
    * */
    private void remplirChoiceBoxReparateur(ArrayList choix){
        choiceBoxReparateur.getItems().clear();
        choiceBoxReparateur.getItems().addAll(choix);
    }
    /**
     * @param choix
     * Méthode qui rempli une choiceBox (ici la choicebox réparateur)
     * */
    private void remplirChoiceBoxEtat(ArrayList choix){
        choiceBoxEtat.getItems().clear();
        choiceBoxEtat.getItems().addAll(choix);
    }

    /**
     * Méthode qui permet de récuperer les réparateurs depuis la classe gestion bdd qui fait le lien entre le code et la base de données.
     * @return ArrayList
     * @throws SQLException
     */
    private ArrayList recupReparateur() throws SQLException {
        return gestionBDD.RecupReparateur();
    }
     public String generateReference() {
            // Générer une chaîne aléatoire de 6 caractères
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder reference = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                reference.append(chars.charAt(random.nextInt(chars.length())));
            }

            // Ajouter la date et l'heure actuelles à la référence
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            reference.append(dateFormat.format(new Date()));

            return reference.toString();
    }
    //TODO Ne fonctionne pas, à revoir
    private void print(){
        // Créer une instance de PrinterJob
        PrinterJob job = PrinterJob.getPrinterJob();

        // Définir le contenu à imprimer
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            // Définir la police pour le texte
            graphics.setFont(new Font("Arial", Font.PLAIN, 12));

            // Dessiner le texte
            graphics.drawString("Exemple de texte à imprimer", 100, 100);
            graphics.drawString("Client: " + nomClient + " " + prenomClient , 100, 120);
            graphics.drawString("Société: " + societeClient, 100, 140);
            graphics.drawString("Tel: " + telClient, 100, 160);
            graphics.drawString("E-Mail: " + emailClient, 100, 180);

            // Charger et dessiner une image
            Image image = Toolkit.getDefaultToolkit().getImage("/Users/nathan/DEV/" + getReference +".png");
            graphics.drawImage(image, 100, 200, 200, 200, null);

            // Renvoyer que la page a été imprimée
            return Printable.PAGE_EXISTS;
        });

        // Afficher la boîte de dialogue d'impression et imprimer si l'utilisateur le confirme
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                System.err.println("Erreur lors de l'impression : " + e);
            }
        }
    }
    public static void setClient(String nom, String prenom, String tel, String email, String societe){
        nomClient = nom;
        prenomClient = prenom;
        telClient = tel;
        emailClient = email;
        societeClient = societe;
    }
    private void setClientTextField(){
        clientNom.setText(nomClient);
        clientPrenom.setText(prenomClient);
        clientTel.setText(telClient);
        clientMail.setText(emailClient);
        clientSociete.setText(societeClient);
    }
}

