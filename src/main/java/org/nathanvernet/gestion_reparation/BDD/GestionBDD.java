package org.nathanvernet.gestion_reparation.BDD;

import org.nathanvernet.gestion_reparation.Modele.ModeleClient;
import org.nathanvernet.gestion_reparation.Modele.ModeleReparation;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class GestionBDD {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void ConnexionBDD() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestionReparation", "root", "root"); //TODO Ajout de la config via le config.json
        statement = connection.createStatement();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/GestionReparation", "root", "root");
    }

    public ArrayList<String> RecupReparateur() throws SQLException {
        ConnexionBDD();
        ArrayList<String> reparateur = new ArrayList<>();
        resultSet = statement.executeQuery("SELECT prenom FROM `reparateur`;");
        while (resultSet.next()) {
            reparateur.add(resultSet.getString("prenom"));
        }
        return reparateur;
    }

    public ArrayList<ModeleClient> getClients() throws SQLException {
        ConnexionBDD();
        ArrayList<ModeleClient> clients = new ArrayList<>();
        resultSet = statement.executeQuery("SELECT * FROM client");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nom = resultSet.getString("nom");
            String prenom = resultSet.getString("prenom");
            String codePostal = resultSet.getString("code_postal");
            String adresse = resultSet.getString("adresse");
            String ville = resultSet.getString("ville");
            String type = resultSet.getString("type");
            String societe = resultSet.getString("societe");
            String commentaire = resultSet.getString("commentaire");
            String telephone = resultSet.getString("telephone");
            String email = resultSet.getString("mail");
            ModeleClient client = new ModeleClient(id, nom, prenom, codePostal, adresse, ville, type, societe, commentaire, telephone, email);
            clients.add(client);
        }
        return clients;
    }

    // Ajout de la méthode getReparations()
    public ArrayList<ModeleReparation> getReparations() throws SQLException {
        ConnexionBDD();
        ArrayList<ModeleReparation> reparations = new ArrayList<>();
        resultSet = statement.executeQuery("SELECT reparation.*, client.nom AS nomClient, client.prenom AS prenomClient, reparateur.prenom AS prenomReparateur FROM reparation INNER JOIN client ON reparation.id_client = client.id INNER JOIN reparateur ON reparation.id_reparateur = reparateur.id");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Date date = resultSet.getDate("date");
            String numeroReparation = resultSet.getString("numero_reparation");
            String nomClient = resultSet.getString("nomClient");
            String prenomClient = resultSet.getString("prenomClient");
            String details = resultSet.getString("details");
            String reparationEffectuee = resultSet.getString("reparation_effectuee");
            String etat = resultSet.getString("etat");
            String prenomReparateur = resultSet.getString("prenomReparateur");
            BigDecimal tarif = resultSet.getBigDecimal("tarif");
            ModeleReparation reparation = new ModeleReparation(id, date, numeroReparation, nomClient, prenomClient, details, reparationEffectuee, etat, prenomReparateur, tarif);
            reparations.add(reparation);
        }
        return reparations;
    }

    // Ajout de la méthode searchReparations()
    public ArrayList<ModeleReparation> searchReparations(String searchTerm) throws SQLException {
        ConnexionBDD();
        ArrayList<ModeleReparation> reparations = new ArrayList<>();
        String query = "SELECT reparation.*, client.nom AS nomClient, client.prenom AS prenomClient, reparateur.prenom AS prenomReparateur FROM reparation INNER JOIN client ON reparation.id_client = client.id INNER JOIN reparateur ON reparation.id_reparateur = reparateur.id WHERE client.nom LIKE ? OR client.prenom LIKE ? OR reparation.numero_reparation LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, "%" + searchTerm + "%");
        preparedStatement.setString(2, "%" + searchTerm + "%");
        preparedStatement.setString(3, "%" + searchTerm + "%");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Date date = resultSet.getDate("date");
            String numeroReparation = resultSet.getString("numero_reparation");
            String nomClient = resultSet.getString("nomClient");
            String prenomClient = resultSet.getString("prenomClient");
            String details = resultSet.getString("details");
            String reparationEffectuee = resultSet.getString("reparation_effectuee");
            String etat = resultSet.getString("etat");
            String prenomReparateur = resultSet.getString("prenomReparateur");
            BigDecimal tarif = resultSet.getBigDecimal("tarif");
            ModeleReparation reparation = new ModeleReparation(id, date, numeroReparation, nomClient, prenomClient, details, reparationEffectuee, etat, prenomReparateur, tarif);
            reparations.add(reparation);
        }
        return reparations;
    }
}