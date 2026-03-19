package com.example.demo.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String role;
    
    private String type;

    // RELATION INVERSE MANYTOMANY
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    // CONSTRUCTEURS
    public Role() {
    }

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
        this.type = role;
    }

    public Role(Long id, String role, String type) {
        this.id = id;
        this.role = role;
        this.type = type;
    }

    // GETTERS ET SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        this.type = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.role = type;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}