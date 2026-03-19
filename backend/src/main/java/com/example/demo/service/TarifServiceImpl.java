package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.TarifDTO;
import com.example.demo.entities.Tarif;
import com.example.demo.repos.TarifRepository;
import com.example.demo.service.TarifService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarifServiceImpl implements TarifService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private TarifRepository tarifRepository;

    @Override
    public Tarif convertDtoToEntity(TarifDTO tarifDTO) {
        return modelMapper.map(tarifDTO, Tarif.class);
    }

    @Override
    public TarifDTO convertEntityToDto(Tarif tarif) {
        return modelMapper.map(tarif, TarifDTO.class);
    }

    @Override
    public TarifDTO findById(Long id) {
        return convertEntityToDto(tarifRepository.getReferenceById(id));
    }

    @Override
    public List<TarifDTO> findAll() {
        return tarifRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TarifDTO save(TarifDTO tarifDTO) {
        return convertEntityToDto(tarifRepository.save(convertDtoToEntity(tarifDTO)));
    }

    @Override
    public TarifDTO update(TarifDTO tarifDTO) {
        return convertEntityToDto(tarifRepository.save(convertDtoToEntity(tarifDTO)));
    }

    @Override
    public void deleteById(Long id) {
        tarifRepository.deleteById(id);
    }
}
