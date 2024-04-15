package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;
import org.nathanvernet.gestion_reparation.QRCodeGenerator;

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
    private ArrayList etat = new ArrayList();
    private QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            try {

                qrCodeGenerator.saveQRCode(generateReference(), "/Users/nathan/DEV/qr.png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stageClose();

        });
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

    //TEST
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

            return reference.toString();        }
    }

