package com.example.demo.entities;

import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Tarif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarif;
    
    private boolean isPromotion;
    private double prix;
    
    @OneToMany(mappedBy = "tarif")
    private List<Evenement> evenements;
    
    // Getters et Setters
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

    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }
}
