package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Tarif;

public interface TarifRepository extends JpaRepository<Tarif, Long> {
    // Méthodes spécifiques si nécessaires
}
