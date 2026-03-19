package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.EvenementDTO;
import com.example.demo.entities.Evenement;

public interface EvenementService {
    EvenementDTO findById(Long id);
    List<EvenementDTO> findAll();
    EvenementDTO save(EvenementDTO evenementDTO);
    EvenementDTO update(EvenementDTO evenementDTO);
    void deleteById(Long id);
    Evenement convertDtoToEntity(EvenementDTO evenementDTO);
    EvenementDTO convertEntityToDto(Evenement evenement);
}