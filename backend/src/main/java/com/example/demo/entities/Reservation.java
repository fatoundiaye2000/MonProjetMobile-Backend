package com.example.demo.entities;

import java.util.Date;
import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;
    
    private Date dateReservation;
    private Integer nbrPersonnes;
    
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Evenement evenement;
    
    // Getters et Setters
    public Long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Integer getNbrPersonnes() {
        return nbrPersonnes;
    }

    public void setNbrPersonnes(Integer nbrPersonnes) {
        this.nbrPersonnes = nbrPersonnes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }
}