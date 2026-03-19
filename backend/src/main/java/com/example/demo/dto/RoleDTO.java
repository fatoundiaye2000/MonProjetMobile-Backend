package com.example.demo.dto;

public class RoleDTO {
    private Long id;
    private String role;  // ✅ AJOUT: propriété manquante
    private String type;

    public RoleDTO() {}

    public RoleDTO(Long id, String role, String type) {  // ✅ CORRECTION: ajouter role
        this.id = id;
        this.role = role;
        this.type = type;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ✅ AJOUT: Getters et setters pour 'role'
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RoleDTO [id=" + id + ", role=" + role + ", type=" + type + "]";
    }
}