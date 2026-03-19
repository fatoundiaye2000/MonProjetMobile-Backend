package com.example.demo.controller;

import com.example.demo.dto.ReservationDTO;
import com.example.demo.service.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {
    
    @Autowired
    private ReservationServiceImpl reservationService;

    @GetMapping("/all")
    public List<ReservationDTO> getAllReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/getById/{id}")
    public ReservationDTO getReservationById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping("/save")
    public void createReservation(@RequestBody ReservationDTO reservationDTO) {
        reservationService.save(reservationDTO);
    }

    @PutMapping("/update")
    public void updateReservation(@RequestBody ReservationDTO reservationDTO) {
        reservationService.update(reservationDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}
