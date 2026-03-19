package com.example.demo.dto;

public class AdresseDTO {
    private Long idAdresse;
    private String rue;
    private String ville;
    private String codePostal;

    public AdresseDTO() {}

    public AdresseDTO(Long idAdresse, String rue, String ville, String codePostal) {
        this.idAdresse = idAdresse;
        this.rue = rue;
        this.ville = ville;
        this.codePostal = codePostal;
    }

    // Getters and setters
    public Long getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(Long idAdresse) {
        this.idAdresse = idAdresse;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    @Override
    public String toString() {
        return "AdresseDTO [idAdresse=" + idAdresse + ", rue=" + rue + ", ville=" + ville + ", codePostal=" + codePostal + "]";
    }
}
