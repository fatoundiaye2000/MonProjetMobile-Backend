package com.example.demo.dto;

import java.time.LocalDate;
import java.util.Date;

public class ReservationDTO {
    private Long idReservation;
    private LocalDate dateReservation;
    private int nbrPersonnes;
    private UserDTO user;
    private EvenementDTO evenement;

    public ReservationDTO() {}

    public ReservationDTO(Long idReservation, LocalDate dateReservation, int nbrPersonnes, UserDTO user, EvenementDTO evenement) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.nbrPersonnes = nbrPersonnes;
        this.user = user;
        this.evenement = evenement;
    }

    // Getters and setters
    public Long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Integer getNbrPersonnes() {
        return nbrPersonnes;
    }

    public void setNbrPersonnes(Integer nbrPersonnes) {
        this.nbrPersonnes = nbrPersonnes;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public EvenementDTO getEvenement() {
        return evenement;
    }

    public void setEvenement(EvenementDTO evenement) {
        this.evenement = evenement;
    }

    @Override
    public String toString() {
        return "ReservationDTO [idReservation=" + idReservation + ", dateReservation=" + dateReservation + ", nbrPersonnes=" + nbrPersonnes + ", user=" + user + ", evenement=" + evenement + "]";
    }
}