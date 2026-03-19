package com.example.demo.entities;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Evenement<Sponsor> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;
    
    private String titreEvent;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String image;
    private Integer nbPlace;
    
    @ManyToOne
    private TypeEvent typeEvent;
    
    @ManyToOne
    private Adresse adresse;
    
    @ManyToOne
    private Tarif tarif;
    
    @ManyToOne
    private User organisateur;
    
    @OneToMany(mappedBy = "evenement")
    private List<Reservation> reservations;
    
    @ManyToMany
    @JoinTable(
        name = "sponsoriser",
        joinColumns = @JoinColumn(name = "idEvent"),
        inverseJoinColumns = @JoinColumn(name = "idSponsor"))
    private List<User> sponsors;
    
    // Getters et Setters
    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitreEvent() {
        return titreEvent;
    }

    public void setTitreEvent(String titreEvent) {
        this.titreEvent = titreEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNbPlace() {
        return nbPlace;
    }

    public void setNbPlace(Integer nbPlace) {
        this.nbPlace = nbPlace;
    }

    public TypeEvent getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(TypeEvent typeEvent) {
        this.typeEvent = typeEvent;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Tarif getTarif() {
        return tarif;
    }

    public void setTarif(Tarif tarif) {
        this.tarif = tarif;
    }

    public User getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(User organisateur) {
        this.organisateur = organisateur;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<User> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<User> sponsors) {
        this.sponsors = sponsors;
    }
}
