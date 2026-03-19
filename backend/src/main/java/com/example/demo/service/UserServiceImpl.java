package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.RoleDTO;
import com.example.demo.dto.Request.RegistrationRequestDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repos.UserRepository;
import com.example.demo.repos.RoleRepository;
import com.example.demo.exception.EmailAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // MÉTHODES EXISTANTES DTO
    @Override
    public User convertDtoToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @Override
    public UserDTO convertEntityToDto(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        
        // ✅ CORRECTION SIMPLIFIÉE: Votre Role a getRole()
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<RoleDTO> roleDTOs = user.getRoles().stream()
                .map(role -> {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setId(role.getId());
                    
                    // ✅ SIMPLE: Votre Role a getRole() qui fonctionne
                    roleDTO.setRole(role.getRole());
                    roleDTO.setType(role.getType());
                    
                    return roleDTO;
                })
                .collect(Collectors.toList());
            userDTO.setRoles(roleDTOs);
        }
        
        return userDTO;
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return convertEntityToDto(user);
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        return convertEntityToDto(userRepository.save(convertDtoToEntity(userDTO)));
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        return convertEntityToDto(userRepository.save(convertDtoToEntity(userDTO)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // NOUVELLES MÉTHODES JWT
    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String username) {
        List<User> users = userRepository.findByEmail(username);
        if(users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User addRoleToUser(String username, String rolename) {
        User user = findUserByEmail(username);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé: " + username);
        }
        
        Role role = roleRepository.findByRole(rolename);
        
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
        }
        
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequestDTO request) {
        List<User> existingUsers = userRepository.findByEmail(request.getEmail());
        if(!existingUsers.isEmpty())
            throw new EmailAlreadyExistsException("email déjà existant!");
        
        User newUser = new User();
        newUser.setNom(request.getNom());
        newUser.setPrenom(request.getPrenom());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        newUser.setEnabled(true);
        
        // Ajouter le rôle par défaut USER
        Role r = roleRepository.findByRole("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }

}