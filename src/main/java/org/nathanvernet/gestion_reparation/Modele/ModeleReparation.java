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
    private String prenomReparateur;
    private BigDecimal tarif;

    public ModeleReparation(int id, Date date, String numeroReparation, String nomClient, String prenomClient, String details, String reparationEffectuee, String etat, String prenomReparateur, BigDecimal tarif) {
        this.id = id;
        this.date = date;
        this.numeroReparation = numeroReparation;
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.details = details;
        this.reparationEffectuee = reparationEffectuee;
        this.etat = etat;
        this.prenomReparateur = prenomReparateur;
        this.tarif = tarif;
    }

    // Getters et Setters pour chaque champ
    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getNumeroReparation() {
        return numeroReparation;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public String getDetails() {
        return details;
    }

    public String getReparationEffectuee() {
        return reparationEffectuee;
    }

    public String getEtat() {
        return etat;
    }

    public String getPrenomReparateur() {
        return prenomReparateur;
    }

    public BigDecimal getTarif() {
        return tarif;
    }
}