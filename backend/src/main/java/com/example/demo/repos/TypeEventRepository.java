package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.TypeEvent;

public interface TypeEventRepository extends JpaRepository<TypeEvent, Long> {
    // Méthodes spécifiques si nécessaires
}
