package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Adresse;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    // Méthodes spécifiques si nécessaires
}
