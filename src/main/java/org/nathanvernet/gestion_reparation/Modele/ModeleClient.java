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

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getCodePostal() { return codePostal; }
    public String getAdresse() { return adresse; }
    public String getVille() { return ville; }
    public String getType() { return type; }
    public String getSociete() { return societe; }
    public String getCommentaire() { return commentaire; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setVille(String ville) { this.ville = ville; }
    public void setType(String type) { this.type = type; }
    public void setSociete(String societe) { this.societe = societe; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }
}