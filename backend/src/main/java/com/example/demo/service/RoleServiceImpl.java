package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RoleDTO;
import com.example.demo.entities.Role;
import com.example.demo.repos.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role convertDtoToEntity(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }

    @Override
    public RoleDTO convertEntityToDto(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public RoleDTO findById(Long id) {
        return convertEntityToDto(roleRepository.getReferenceById(id));
    }

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        return convertEntityToDto(roleRepository.save(convertDtoToEntity(roleDTO)));
    }

    @Override
    public RoleDTO update(RoleDTO roleDTO) {
        return convertEntityToDto(roleRepository.save(convertDtoToEntity(roleDTO)));
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
