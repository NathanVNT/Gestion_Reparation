package org.nathanvernet.gestion_reparation;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

public class BDDConfigurationWindow {

    public static void showConfigurationWindow(Stage primaryStage, GestionBDD gestionBDD) {
        Stage configStage = new Stage();
        configStage.initModality(Modality.APPLICATION_MODAL);
        configStage.setTitle("Configurer la Base de Données");

        // Création du formulaire de configuration
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Champ pour l'IP de la base de données
        Label ipLabel = new Label("IP de la BDD :");
        TextField ipField = new TextField();
        ipField.setPromptText("localhost");
        grid.add(ipLabel, 0, 0);
        grid.add(ipField, 1, 0);

        // Champ pour le port de la base de données
        Label portLabel = new Label("Port de la BDD :");
        TextField portField = new TextField();
        portField.setPromptText("3306");
        grid.add(portLabel, 0, 1);
        grid.add(portField, 1, 1);

        // Champ pour le nom de la base de données
        Label dbNameLabel = new Label("Nom de la BDD :");
        TextField dbNameField = new TextField();
        dbNameField.setPromptText("votre_bdd");
        grid.add(dbNameLabel, 0, 2);
        grid.add(dbNameField, 1, 2);

        // Champ pour le nom d'utilisateur
        Label usernameLabel = new Label("Nom d'utilisateur :");
        TextField usernameField = new TextField();
        usernameField.setPromptText("root");
        grid.add(usernameLabel, 0, 3);
        grid.add(usernameField, 1, 3);

        // Champ pour le mot de passe
        Label passwordLabel = new Label("Mot de passe :");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("mot de passe");
        grid.add(passwordLabel, 0, 4);
        grid.add(passwordField, 1, 4);

        // Bouton pour sauvegarder la configuration
        Button saveButton = new Button("Sauvegarder et Connecter");
        grid.add(saveButton, 1, 5);

        saveButton.setOnAction(event -> {
            String ip = ipField.getText();
            String port = portField.getText();
            String dbName = dbNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (ip.isEmpty() || port.isEmpty() || dbName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs Manquants");
                alert.setHeaderText("Tous les champs doivent être remplis");
                alert.setContentText("Veuillez remplir tous les champs pour configurer la base de données.");
                alert.showAndWait();
            } else {
                try {
                    // Construire l'URL de la base de données
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;

                    // Mise à jour du fichier config.properties
                    gestionBDD.getProperties().setProperty("db.url", url);
                    gestionBDD.getProperties().setProperty("db.username", username);
                    gestionBDD.getProperties().setProperty("db.password", password);

                    try (FileWriter writer = new FileWriter("config.properties")) {
                        gestionBDD.getProperties().store(writer, null);
                    }

                    // Tentative de connexion à la BDD avec les nouvelles informations
                    gestionBDD.ConnexionBDD();
                    configStage.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès de la connexion");
                    alert.setHeaderText("Connexion réussie");
                    alert.setContentText("Connexion à la base de données réussie.");
                    alert.showAndWait();

                    // Relancer l'application
                    relaunchApplication();

                } catch (SQLException | IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de connexion");
                    alert.setHeaderText("Connexion échouée");
                    alert.setContentText("Impossible de se connecter à la base de données : " + e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        configStage.setScene(scene);
        configStage.initOwner(primaryStage);
        configStage.showAndWait();
    }

    private static void relaunchApplication() {
        String java = System.getProperty("java.home") + "/bin/java";
        String jar = Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try {
            // Relancer le JAR
            ProcessBuilder processBuilder = new ProcessBuilder(java, "-jar", jar);
            processBuilder.start();

            // Fermer l'application actuelle
            Platform.exit();
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du redémarrage de l'application : " + e.getMessage());
        }
    }
}
