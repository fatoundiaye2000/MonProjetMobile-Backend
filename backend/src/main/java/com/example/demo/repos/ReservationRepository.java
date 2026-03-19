package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Méthodes spécifiques si nécessaires

}