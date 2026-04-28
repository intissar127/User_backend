package com.soutenancia.backend.DTO;

public class EtudiantRequest {
    private String name;
    private String email;
    private String passwd;
    private String specialite;

    private String encadreur;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

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
