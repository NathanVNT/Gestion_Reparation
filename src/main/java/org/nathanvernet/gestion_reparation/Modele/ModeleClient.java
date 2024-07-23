package org.nathanvernet.gestion_reparation.Modele;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ModeleClient {
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty codePostal = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();
    private final StringProperty ville = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty societe = new SimpleStringProperty();
    private final StringProperty commentaire = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();

    public ModeleClient() {
    }

    public ModeleClient(String nom, String prenom, String codePostal, String adresse, String ville, String type, String societe, String commentaire, String telephone, String email) {
        setNom(nom);
        setPrenom(prenom);
        setCodePostal(codePostal);
        setAdresse(adresse);
        setVille(ville);
        setType(type);
        setSociete(societe);
        setCommentaire(commentaire);
        setTelephone(telephone);
        setEmail(email);
    }

    // Méthodes getters et setters pour les propriétés

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPrenom() {
        return prenom.get();
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public String getCodePostal() {
        return codePostal.get();
    }

    public StringProperty codePostalProperty() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal.set(codePostal);
    }

    public String getAdresse() {
        return adresse.get();
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public String getVille() {
        return ville.get();
    }

    public StringProperty villeProperty() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville.set(ville);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getSociete() {
        return societe.get();
    }

    public StringProperty societeProperty() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe.set(societe);
    }

    public String getCommentaire() {
        return commentaire.get();
    }

    public StringProperty commentaireProperty() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire.set(commentaire);
    }

    public String getTelephone() {
        return telephone.get();
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
