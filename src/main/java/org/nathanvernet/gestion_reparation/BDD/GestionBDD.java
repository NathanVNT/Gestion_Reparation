package org.nathanvernet.gestion_reparation.BDD;

import org.nathanvernet.gestion_reparation.Modele.ModeleClient;
import org.nathanvernet.gestion_reparation.Modele.ModeleReparation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class GestionBDD {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private Properties properties;

    public GestionBDD() throws IOException {
        properties = new Properties();
        File file = new File("config.properties");

        if (file.exists()) {
            try (FileInputStream input = new FileInputStream(file)) {
                properties.load(input);
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement du fichier config.properties : " + e.getMessage());
                throw e;
            }
        } else {
            file.createNewFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("db.url=\n");
                writer.write("db.username=\n");
                writer.write("db.password=\n");
                System.out.println("Fichier config.properties créé. Veuillez remplir les informations de connexion.");
            } catch (IOException e) {
                System.err.println("Erreur lors de la création du fichier config.properties : " + e.getMessage());
                throw e;
            }
        }

        // Recharger les propriétés après la création du fichier
        try (FileInputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Erreur lors du rechargement des propriétés : " + e.getMessage());
            throw e;
        }
    }

    public void ConnexionBDD() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
                statement = connection.createStatement();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            throw e;
        }
    }

    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        if (url == null || username == null || password == null || url.isEmpty() || username.isEmpty() || password.isEmpty()) {
            throw new SQLException("Les informations de connexion à la base de données sont incomplètes dans config.properties");
        }

        return DriverManager.getConnection(url, username, password);
    }

    public ArrayList<String> RecupReparateur() throws SQLException {
        ConnexionBDD();
        ArrayList<String> reparateur = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery("SELECT prenom FROM reparateur;")) {
            while (resultSet.next()) {
                reparateur.add(resultSet.getString("prenom"));
            }
        } finally {
            closeStatement();
        }
        return reparateur;
    }

    public ArrayList<ModeleClient> getClients() throws SQLException {
        ConnexionBDD();
        ArrayList<ModeleClient> clients = new ArrayList<>();
        String query = "SELECT * FROM client";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String telephone = resultSet.getString("telephone");
                String email = resultSet.getString("mail");
                String adresse = resultSet.getString("adresse");
                String codePostal = resultSet.getString("code_postal");
                String ville = resultSet.getString("ville");
                String type = resultSet.getString("type");
                String societe = resultSet.getString("societe");
                String commentaire = resultSet.getString("commentaire");

                ModeleClient client = new ModeleClient(id, nom, prenom, codePostal, adresse, ville, type, societe, commentaire, telephone, email);
                clients.add(client);
            }
        }
        return clients;
    }

    public ArrayList<ModeleReparation> getReparations() throws SQLException {
        ConnexionBDD();
        ArrayList<ModeleReparation> reparations = new ArrayList<>();
        String query = "SELECT reparation.*, client.nom AS nomClient, client.prenom AS prenomClient, client.telephone AS telClient, client.mail AS emailClient, client.societe AS societeClient, reparateur.prenom AS prenomReparateur FROM reparation INNER JOIN client ON reparation.id_client = client.id INNER JOIN reparateur ON reparation.id_reparateur = reparateur.id";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date date = resultSet.getDate("date");
                String numeroReparation = resultSet.getString("numero_reparation");
                String nomClient = resultSet.getString("nomClient");
                String prenomClient = resultSet.getString("prenomClient");
                String telClient = resultSet.getString("telClient");
                String emailClient = resultSet.getString("emailClient");
                String societeClient = resultSet.getString("societeClient");
                String details = resultSet.getString("details");
                String reparationEffectuee = resultSet.getString("reparation_effectuee");
                String etat = resultSet.getString("etat");
                String prenomReparateur = resultSet.getString("prenomReparateur");
                BigDecimal tarif = resultSet.getBigDecimal("tarif");
                int idClient = resultSet.getInt("id_client");

                ModeleReparation reparation = new ModeleReparation(id, date, numeroReparation, nomClient, prenomClient, details, reparationEffectuee, etat, prenomReparateur, tarif, idClient, telClient, emailClient, societeClient);
                reparations.add(reparation);
            }
        } finally {
            closeStatement();
        }
        return reparations;
    }

    public ArrayList<ModeleReparation> searchReparations(String searchTerm) throws SQLException {
        ConnexionBDD();
        ArrayList<ModeleReparation> reparations = new ArrayList<>();
        String query = "SELECT reparation.*, client.nom AS nomClient, client.prenom AS prenomClient, client.telephone AS telClient, client.mail AS emailClient, client.societe AS societeClient, reparateur.prenom AS prenomReparateur FROM reparation INNER JOIN client ON reparation.id_client = client.id INNER JOIN reparateur ON reparation.id_reparateur = reparateur.id WHERE client.nom LIKE ? OR client.prenom LIKE ? OR reparation.numero_reparation LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchTerm + "%");
            preparedStatement.setString(2, "%" + searchTerm + "%");
            preparedStatement.setString(3, "%" + searchTerm + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Date date = resultSet.getDate("date");
                    String numeroReparation = resultSet.getString("numero_reparation");
                    String nomClient = resultSet.getString("nomClient");
                    String prenomClient = resultSet.getString("prenomClient");
                    String telClient = resultSet.getString("telClient");
                    String emailClient = resultSet.getString("emailClient");
                    String societeClient = resultSet.getString("societeClient");
                    String details = resultSet.getString("details");
                    String reparationEffectuee = resultSet.getString("reparation_effectuee");
                    String etat = resultSet.getString("etat");
                    String prenomReparateur = resultSet.getString("prenomReparateur");
                    BigDecimal tarif = resultSet.getBigDecimal("tarif");
                    int idClient = resultSet.getInt("id_client");

                    ModeleReparation reparation = new ModeleReparation(id, date, numeroReparation, nomClient, prenomClient, details, reparationEffectuee, etat, prenomReparateur, tarif, idClient, telClient, emailClient, societeClient);
                    reparations.add(reparation);
                }
            }
        }
        return reparations;
    }

    public void addClient(ModeleClient client) throws SQLException {
        ConnexionBDD();
        String query = "INSERT INTO client(nom, prenom, telephone, mail, adresse, code_postal, ville, type, commentaire, societe) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, client.getNom());
            preparedStatement.setString(2, client.getPrenom());
            preparedStatement.setString(3, client.getTelephone());
            preparedStatement.setString(4, client.getEmail());
            preparedStatement.setString(5, client.getAdresse());
            preparedStatement.setString(6, client.getCodePostal());
            preparedStatement.setString(7, client.getVille());
            preparedStatement.setString(8, client.getType());
            preparedStatement.setString(9, client.getCommentaire());
            preparedStatement.setString(10, client.getSociete());
            preparedStatement.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private void closeStatement() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture du statement : " + e.getMessage());
        }
    }
    public Properties getProperties() {
        return properties;
    }
    public void updateClient(ModeleClient client) throws SQLException {
        ConnexionBDD();
        String query = "UPDATE client SET nom = ?, prenom = ?, telephone = ?, mail = ?, adresse = ?, code_postal = ?, ville = ?, type = ?, societe = ?, commentaire = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, client.getNom());
            preparedStatement.setString(2, client.getPrenom());
            preparedStatement.setString(3, client.getTelephone());
            preparedStatement.setString(4, client.getEmail());
            preparedStatement.setString(5, client.getAdresse());
            preparedStatement.setString(6, client.getCodePostal());
            preparedStatement.setString(7, client.getVille());
            preparedStatement.setString(8, client.getType());
            preparedStatement.setString(9, client.getSociete());
            preparedStatement.setString(10, client.getCommentaire());
            preparedStatement.setInt(11, client.getId()); // Utilisation de l'ID du client pour la mise à jour

            preparedStatement.executeUpdate();
        }
    }

}
