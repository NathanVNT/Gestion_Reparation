package org.nathanvernet.gestion_reparation.Modele;

import java.math.BigDecimal;
import java.sql.Date;

public class ModeleReparation {
    private int id;
    private Date date;
    private String numeroReparation;
    private String nomClient;
    private String prenomClient;
    private String details;
    private String reparationEffectuee;
    private String etat;
    private String reparateur;
    private BigDecimal tarif;
    private int clientId;  // Ajouter cette variable
    private String telClient;
    private String emailClient;
    private String societeClient;

    // Constructeur
    public ModeleReparation(int id, Date date, String numeroReparation, String nomClient, String prenomClient,
                            String details, String reparationEffectuee, String etat, String reparateur, BigDecimal tarif,
                            int clientId, String telClient, String emailClient, String societeClient) {
        this.id = id;
        this.date = date;
        this.numeroReparation = numeroReparation;
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.details = details;
        this.reparationEffectuee = reparationEffectuee;
        this.etat = etat;
        this.reparateur = reparateur;
        this.tarif = tarif;
        this.clientId = clientId;  // Initialiser cette variable
        this.telClient = telClient;
        this.emailClient = emailClient;
        this.societeClient = societeClient;
    }

    // Ajoutez un constructeur sans paramètres si nécessaire
    public ModeleReparation() {
    }

    // Getters et setters

    // Ajoutez ce getter pour clientId
    public int getClientId() {
        return clientId;
    }

    // Ajoutez ce setter pour clientId
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNumeroReparation() {
        return numeroReparation;
    }

    public void setNumeroReparation(String numeroReparation) {
        this.numeroReparation = numeroReparation;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getReparationEffectuee() {
        return reparationEffectuee;
    }

    public void setReparationEffectuee(String reparationEffectuee) {
        this.reparationEffectuee = reparationEffectuee;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getReparateur() {
        return reparateur;
    }

    public void setReparateur(String reparateur) {
        this.reparateur = reparateur;
    }

    public BigDecimal getTarif() {
        return tarif;
    }

    public void setTarif(BigDecimal tarif) {
        this.tarif = tarif;
    }

    public String getTelClient() {
        return telClient;
    }

    public void setTelClient(String telClient) {
        this.telClient = telClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getSocieteClient() {
        return societeClient;
    }

    public void setSocieteClient(String societeClient) {
        this.societeClient = societeClient;
    }
}