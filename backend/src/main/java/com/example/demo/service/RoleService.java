package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.RoleDTO;
import com.example.demo.entities.Role;

public interface RoleService {
    RoleDTO findById(Long id);
    List<RoleDTO> findAll();
    RoleDTO save(RoleDTO roleDTO);
    RoleDTO update(RoleDTO roleDTO);
    void deleteById(Long id);
    Role convertDtoToEntity(RoleDTO roleDTO);
    RoleDTO convertEntityToDto(Role role);
}
