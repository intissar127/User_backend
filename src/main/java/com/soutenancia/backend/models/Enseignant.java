package com.soutenancia.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "enseignants", schema = "public")
@PrimaryKeyJoinColumn(name = "id")
public class Enseignant extends User {

    private String specialite;

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}