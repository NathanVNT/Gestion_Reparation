package org.nathanvernet.gestion_reparation.BDD;

import org.nathanvernet.gestion_reparation.Modele.ModeleClient;

import java.sql.*;
import java.util.*;

public class GestionBDD {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    public void ConnexionBDD() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestionReparation", "root", "root"); //TODO Ajout de la config via le config.json
        statement = connection.createStatement();
    }
    public ArrayList RecupReparateur() throws SQLException {
        ConnexionBDD();
        ArrayList reparateur = new ArrayList<>();
        resultSet = statement.executeQuery("SELECT prenom FROM `reparateur`;");
        while (resultSet.next()) {
            reparateur.add(resultSet.getString("prenom"));
        }
        return reparateur;
    }

    public ArrayList getClients() throws SQLException {
        ConnexionBDD();
        ArrayList<ModeleClient> clients = new ArrayList<>();
        resultSet = statement.executeQuery("SELECT * FROM client");
        while (resultSet.next()) {
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
            ModeleClient client = new ModeleClient(nom, prenom, codePostal, adresse, ville, type, societe, commentaire, telephone, email);
            clients.add(client);
            }
        return clients;
    }
}
