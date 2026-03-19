package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdresseDTO;
import com.example.demo.entities.Adresse;
import com.example.demo.repos.AdresseRepository;



@Service
public class AdresseServiceImpl implements AdresseService {

    @Autowired
    private ModelMapper modelMapper ;
    
    @Autowired
    private AdresseRepository adresseRepository;

    @Override
    public Adresse convertDtoToEntity(AdresseDTO adresseDTO) {
        return modelMapper.map(adresseDTO, Adresse.class);
    }

    @Override
    public AdresseDTO convertEntityToDto(Adresse adresse) {
        return modelMapper.map(adresse, AdresseDTO.class);
    }

    @Override
    public AdresseDTO findById(Long id) {
        return convertEntityToDto(adresseRepository.getReferenceById(id));
    }

    @Override
    public List<AdresseDTO> findAll() {
        return adresseRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdresseDTO save(AdresseDTO adresseDTO) {
        return convertEntityToDto(adresseRepository.save(convertDtoToEntity(adresseDTO)));
    }

    @Override
    public AdresseDTO update(AdresseDTO adresseDTO) {
        return convertEntityToDto(adresseRepository.save(convertDtoToEntity(adresseDTO)));
    }

    @Override
    public void deleteById(Long id) {
        adresseRepository.deleteById(id);
    }
}
