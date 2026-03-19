package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.TypeEventDTO;
import com.example.demo.entities.TypeEvent;
import com.example.demo.repos.TypeEventRepository;
import com.example.demo.service.TypeEventService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeEventServiceImpl implements TypeEventService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private TypeEventRepository typeEventRepository;

    @Override
    public TypeEvent convertDtoToEntity(TypeEventDTO typeEventDTO) {
        return modelMapper.map(typeEventDTO, TypeEvent.class);
    }

    @Override
    public TypeEventDTO convertEntityToDto(TypeEvent typeEvent) {
        return modelMapper.map(typeEvent, TypeEventDTO.class);
    }

    @Override
    public TypeEventDTO findById(Long id) {
        return convertEntityToDto(typeEventRepository.getReferenceById(id));
    }

    @Override
    public List<TypeEventDTO> findAll() {
        return typeEventRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TypeEventDTO save(TypeEventDTO typeEventDTO) {
        return convertEntityToDto(typeEventRepository.save(convertDtoToEntity(typeEventDTO)));
    }

    @Override
    public TypeEventDTO update(TypeEventDTO typeEventDTO) {
        return convertEntityToDto(typeEventRepository.save(convertDtoToEntity(typeEventDTO)));
    }

    @Override
    public void deleteById(Long id) {
        typeEventRepository.deleteById(id);
    }
}
