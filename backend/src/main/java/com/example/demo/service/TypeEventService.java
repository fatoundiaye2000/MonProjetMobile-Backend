package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.TypeEventDTO;
import com.example.demo.entities.TypeEvent;

public interface TypeEventService {
    TypeEventDTO findById(Long id);
    List<TypeEventDTO> findAll();
    TypeEventDTO save(TypeEventDTO typeEventDTO);
    TypeEventDTO update(TypeEventDTO typeEventDTO);
    void deleteById(Long id);
    TypeEvent convertDtoToEntity(TypeEventDTO typeEventDTO);
    TypeEventDTO convertEntityToDto(TypeEvent typeEvent);
}
