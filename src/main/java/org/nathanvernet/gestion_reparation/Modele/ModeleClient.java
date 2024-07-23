package org.nathanvernet.gestion_reparation.Modele;

public class ModeleClient {
    private int id;
    private String nom;
    private String prenom;
    private String codePostal;
    private String adresse;
    private String ville;
    private String type;
    private String societe;
    private String commentaire;
    private String telephone;
    private String email;

    public ModeleClient(int id, String nom, String prenom, String codePostal, String adresse, String ville, String type, String societe, String commentaire, String telephone, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.codePostal = codePostal;
        this.adresse = adresse;
        this.ville = ville;
        this.type = type;
        this.societe = societe;
        this.commentaire = commentaire;
        this.telephone = telephone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getType() {
        return type;
    }

    public String getSociete() {
        return societe;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }
}