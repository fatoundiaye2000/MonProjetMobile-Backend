package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.Request.RegistrationRequestDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.exception.EmailAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // Méthodes existantes pour DTO
    User convertDtoToEntity(UserDTO userDTO);
    UserDTO convertEntityToDto(User user);
    UserDTO findById(Long id);
    List<UserDTO> findAll();
    UserDTO save(UserDTO userDTO);
    UserDTO update(UserDTO userDTO);
    void deleteById(Long id);
    
    // Nouvelles méthodes pour JWT
    User saveUser(User user);
    User findUserByEmail(String username);
    Role addRole(Role role);
    User addRoleToUser(String username, String rolename);
    
    // ⭐⭐⭐ AJOUTER CETTE MÉTHODE POUR LE TP2 ⭐⭐⭐
    List<User> findAllUsers();
    
    // ⭐⭐⭐ AJOUTER CETTE MÉTHODE POUR LE TP4 ⭐⭐⭐
    User registerUser(RegistrationRequestDTO request);
    
}