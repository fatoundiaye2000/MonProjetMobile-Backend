package com.example.demo.dto;

public class TypeEventDTO {
    private Long idTypeEvent;
    private String nomType;

    public TypeEventDTO() {}

    public TypeEventDTO(Long idTypeEvent, String nomType) {
        this.idTypeEvent = idTypeEvent;
        this.nomType = nomType;
    }

    // Getters and setters
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

    @Override
    public String toString() {
        return "TypeEventDTO [idTypeEvent=" + idTypeEvent + ", nomType=" + nomType + "]";
    }
}