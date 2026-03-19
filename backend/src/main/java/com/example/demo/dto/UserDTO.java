package com.example.demo.dto;

import java.util.List;

public class UserDTO {
   
    public UserDTO() {}

    public UserDTO(Long idUser, String nom, String prenom, String email, String password, List<RoleDTO> roles) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.roles = roles; // ✅ CORRECTION
    }
 private Long idUser;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private List<RoleDTO> roles; // ✅ CORRECTION: role → roles (liste)

    // Getters and setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ✅ CORRECTION: getRoles() et setRoles()
    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDTO [idUser=" + idUser + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", password=" + password + ", roles=" + roles + "]";
    }
}