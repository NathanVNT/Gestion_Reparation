package org.nathanvernet.gestion_reparation.BDD;

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
    public ArrayList RecupReparateur() throws SQLException {
        ArrayList reparateur = new ArrayList<>();
        ConnexionBDD();
        resultSet = statement.executeQuery("SELECT prenom FROM `reparateur`;");
        while (resultSet.next()) {
            reparateur.add(resultSet.getString("prenom"));
        }
        return reparateur;
    }
}
