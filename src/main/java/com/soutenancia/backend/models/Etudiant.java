package com.soutenancia.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "etudiants", schema = "public")
@PrimaryKeyJoinColumn(name = "id")
public class Etudiant extends User {

    private String specialite;
    private String encadreur;

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getEncadreur() {
        return encadreur;
    }

    public void setEncadreur(String encadreur) {
        this.encadreur = encadreur;
    }
}