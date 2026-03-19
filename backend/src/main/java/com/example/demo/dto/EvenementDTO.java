package com.example.demo.dto;

import java.util.Date;

public class EvenementDTO {
    private Long idEvent;
    private String titreEvent;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String image;
    private Integer nbPlace;
    private TypeEventDTO typeEvent;
    private AdresseDTO adresse;
    private TarifDTO tarif;
    private UserDTO organisateur;

    public EvenementDTO() {}

    public EvenementDTO(Long idEvent, String titreEvent, String description, Date dateDebut, Date dateFin, String image, Integer nbPlace, TypeEventDTO typeEvent, AdresseDTO adresse, TarifDTO tarif, UserDTO organisateur) {
        this.idEvent = idEvent;
        this.titreEvent = titreEvent;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.image = image;
        this.nbPlace = nbPlace;
        this.typeEvent = typeEvent;
        this.adresse = adresse;
        this.tarif = tarif;
        this.organisateur = organisateur;
    }

    // Getters and setters
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

    public TypeEventDTO getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(TypeEventDTO typeEvent) {
        this.typeEvent = typeEvent;
    }

    public AdresseDTO getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }

    public TarifDTO getTarif() {
        return tarif;
    }

    public void setTarif(TarifDTO tarif) {
        this.tarif = tarif;
    }

    public UserDTO getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(UserDTO organisateur) {
        this.organisateur = organisateur;
    }

    @Override
    public String toString() {
        return "EvenementDTO [idEvent=" + idEvent + ", titreEvent=" + titreEvent + ", description=" + description + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", image=" + image + ", nbPlace=" + nbPlace + ", typeEvent=" + typeEvent + ", adresse=" + adresse + ", tarif=" + tarif + ", organisateur=" + organisateur + "]";
    }

	public void setTitre_Event(String string) {
		// TODO Auto-generated method stub
		
	}
}
