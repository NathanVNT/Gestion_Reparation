package org.nathanvernet.gestion_reparation.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import org.nathanvernet.gestion_reparation.BDD.GestionBDD;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddReparationController implements Initializable {
    public ChoiceBox choiceBoxReparateur;
    public GestionBDD gestionBDD = new GestionBDD();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            remplirChoiceBox(recupReparateur());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
    * @param choix
     * Méthode qui rempli une choiceBox (ici la choicebox réparateur)
    * */
    private void remplirChoiceBox(ArrayList choix){
        choiceBoxReparateur.getItems().clear();
        choiceBoxReparateur.getItems().addAll(choix);
    }

    /**
     * Méthode qui permet de récuperer les réparateurs depuis la classe gestion bdd qui fait le lien entre le code et la base de données.
     * @return ArrayList
     * @throws SQLException
     */
    private ArrayList recupReparateur() throws SQLException {
        return gestionBDD.RecupReparateur();
    }
}
