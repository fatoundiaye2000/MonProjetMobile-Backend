package com.example.demo.service;

import com.example.demo.entities.User;
import com.example.demo.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // =====================================================
    // @Mock = faux objets, ne touchent JAMAIS la vraie BDD
    // =====================================================
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    // =====================================================
    // @InjectMocks = ta vraie classe UserServiceImpl
    // Mockito lui injecte automatiquement les @Mock
    // =====================================================
    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    // =====================================================
    // @BeforeEach = s'exécute AVANT chaque test
    // =====================================================
    @BeforeEach
    void setUp() {
        user = new User();
        user.setIdUser(1L);
        user.setNom("Ndiaye");
        user.setPrenom("Fatou");
        user.setEmail("fatou@example.com");
        user.setPassword("motdepasse123");
        user.setEnabled(true);
    }

    // =====================================================
    // TEST 1 : saveUser(User user)
    // Vérifie que BCrypt encode le mot de passe
    // avant la sauvegarde en base de données
    // =====================================================
    @Test
    @DisplayName("Test 1 - saveUser() encode le mot de passe avant sauvegarde")
    void testSaveUser_motDePasseEncode() {

        // ARRANGE
        when(passwordEncoder.encode("motdepasse123")).thenReturn("$2a$10$encodedHash");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // ACT
        User result = userService.saveUser(user);

        // ASSERT
        assertThat(result).isNotNull();
        verify(passwordEncoder, times(1)).encode("motdepasse123");
        verify(userRepository, times(1)).save(user);
    }

    // =====================================================
    // TEST 2 : saveUser() - vérifie que save() est appelé
    // =====================================================
    @Test
    @DisplayName("Test 2 - saveUser() appelle bien userRepository.save()")
    void testSaveUser_appelleRepository() {

        // ARRANGE
        when(userRepository.save(any(User.class))).thenReturn(user);

        // ACT
        userService.saveUser(user);

        // ASSERT : save() doit être appelé exactement 1 fois
        verify(userRepository, times(1)).save(any(User.class));
    }

    // =====================================================
    // TEST 3 : findAllUsers()
    // Vérifie que la liste de tous les utilisateurs
    // est bien retournée depuis le repository
    // =====================================================
    @Test
    @DisplayName("Test 3 - findAllUsers() retourne la liste de tous les utilisateurs")
    void testFindAllUsers_retourneListe() {

        // ARRANGE : 2 utilisateurs fictifs
        User user2 = new User();
        user2.setIdUser(2L);
        user2.setNom("Dupont");
        user2.setPrenom("Jean");
        user2.setEmail("jean@example.com");
        user2.setEnabled(true);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        // ACT
        List<User> result = userService.findAllUsers();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmail()).isEqualTo("fatou@example.com");
        assertThat(result.get(1).getEmail()).isEqualTo("jean@example.com");
    }
}