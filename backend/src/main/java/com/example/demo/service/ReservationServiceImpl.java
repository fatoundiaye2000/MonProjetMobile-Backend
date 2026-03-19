package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.ReservationDTO;
import com.example.demo.entities.Reservation;
import com.example.demo.repos.ReservationRepository;
import com.example.demo.service.ReservationService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation convertDtoToEntity(ReservationDTO reservationDTO) {
        return modelMapper.map(reservationDTO, Reservation.class);
    }

    @Override
    public ReservationDTO convertEntityToDto(Reservation reservation) {
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    @Override
    public ReservationDTO findById(Long id) {
        return convertEntityToDto(reservationRepository.getReferenceById(id));
    }

    @Override
    public List<ReservationDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDTO save(ReservationDTO reservationDTO) {
        return convertEntityToDto(reservationRepository.save(convertDtoToEntity(reservationDTO)));
    }

    @Override
    public ReservationDTO update(ReservationDTO reservationDTO) {
        return convertEntityToDto(reservationRepository.save(convertDtoToEntity(reservationDTO)));
    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
    
}
