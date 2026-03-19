package com.example.demo.dto;

public class TarifDTO {
    private Long idTarif;
    private Boolean isPromotion;
    private Double prix;

    public TarifDTO() {}

    public TarifDTO(Long idTarif, Boolean isPromotion, Double prix) {
        this.idTarif = idTarif;
        this.isPromotion = isPromotion;
        this.prix = prix;
    }

    // Getters and setters
    public Long getIdTarif() {
        return idTarif;
    }

    public void setIdTarif(Long idTarif) {
        this.idTarif = idTarif;
    }

    public Boolean getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "TarifDTO [idTarif=" + idTarif + ", isPromotion=" + isPromotion + ", prix=" + prix + "]";
    }
}
