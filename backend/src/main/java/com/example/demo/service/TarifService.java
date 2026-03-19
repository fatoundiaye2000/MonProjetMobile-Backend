package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.TarifDTO;
import com.example.demo.entities.Tarif;

public interface TarifService {
    TarifDTO findById(Long id);
    List<TarifDTO> findAll();
    TarifDTO save(TarifDTO tarifDTO);
    TarifDTO update(TarifDTO tarifDTO);
    void deleteById(Long id);
    Tarif convertDtoToEntity(TarifDTO tarifDTO);
    TarifDTO convertEntityToDto(Tarif tarif);
}