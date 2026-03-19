package com.example.demo.entities;

import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.OneToMany;

@Entity
public class TypeEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeEvent;
    
    private String nomType;
    
    @OneToMany(mappedBy = "typeEvent")
    private List<Evenement> evenements;
    
    // Getters et Setters
    public Long getIdTypeEvent() {
        return idTypeEvent;
    }

    public void setIdTypeEvent(Long idTypeEvent) {
        this.idTypeEvent = idTypeEvent;
    }

    public String getNomType() {
        return nomType;
    }

    public void setNomType(String nomType) {
        this.nomType = nomType;
    }

    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }
}