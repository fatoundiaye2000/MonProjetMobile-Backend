package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Evenement;
import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    // Méthodes spécifiques au besoin
    List<Evenement> findByTitreEventContaining(String titre); // Exemple de recherche
    
}