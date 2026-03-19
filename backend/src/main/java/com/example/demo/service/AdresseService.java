package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.AdresseDTO;
import com.example.demo.entities.Adresse;

public interface AdresseService {
    AdresseDTO findById(Long id);
    List<AdresseDTO> findAll();
    AdresseDTO save(AdresseDTO adresseDTO);
    AdresseDTO update(AdresseDTO adresseDTO);
    void deleteById(Long id);
    Adresse convertDtoToEntity(AdresseDTO adresseDTO);
    AdresseDTO convertEntityToDto(Adresse adresse);
}