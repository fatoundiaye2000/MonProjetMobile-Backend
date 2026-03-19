package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.ReservationDTO;
import com.example.demo.entities.Reservation;

public interface ReservationService {
    ReservationDTO findById(Long id);
    List<ReservationDTO> findAll();
    ReservationDTO save(ReservationDTO reservationDTO);
    ReservationDTO update(ReservationDTO reservationDTO);
    void deleteById(Long id);
    Reservation convertDtoToEntity(ReservationDTO reservationDTO);
    ReservationDTO convertEntityToDto(Reservation reservation);
    
    
}
