package com.example.demo.controller;

import com.example.demo.dto.EvenementDTO;
import com.example.demo.service.EvenementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EvenementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvenementService evenementService;

    @Autowired
    private ObjectMapper objectMapper;

    // =====================================================
    // TEST 1 : GET /api/evenements/all avec USER connecté
    // Dans ton SecurityConfig : GET /api/evenements/** = permitAll
    // Donc accessible même sans token, a fortiori avec USER
    // =====================================================
    @Test
    @WithMockUser(username = "user@test.com", authorities = {"USER"})
    @DisplayName("Test 1 - GET /api/evenements/all retourne 200 pour USER connecté")
    void testGetAllEvenements_avecUser_retourne200() throws Exception {

        // ARRANGE
        EvenementDTO e1 = new EvenementDTO();
        when(evenementService.findAll()).thenReturn(Arrays.asList(e1));

        // ACT + ASSERT
        mockMvc.perform(get("/api/evenements/all"))
               .andExpect(status().isOk());
    }

    // =====================================================
    // TEST 2 : GET /api/evenements/all SANS authentification
    // Dans ton SecurityConfig : GET /api/evenements/** = permitAll
    // Donc accessible sans token (accès public)
    // =====================================================
    @Test
    @DisplayName("Test 2 - GET /api/evenements/all retourne 200 sans token (public)")
    void testGetAllEvenements_sansAuth_retourne200() throws Exception {

        // ARRANGE
        when(evenementService.findAll()).thenReturn(Arrays.asList());

        // ACT + ASSERT
        mockMvc.perform(get("/api/evenements/all"))
               .andExpect(status().isOk());
    }

    // =====================================================
    // TEST 3 : POST /api/evenements/save SANS authentification
    // Dans ton SecurityConfig : POST /api/evenements/** = hasAuthority("ADMIN")
    // Donc doit être refusé (401 ou 403)
    // =====================================================
    @Test
    @DisplayName("Test 3 - POST /api/evenements/save sans token retourne 401 ou 403")
    void testCreateEvenement_sansAuth_retourneRefus() throws Exception {

        // ACT + ASSERT : pas de token = refusé par Spring Security
        mockMvc.perform(post("/api/evenements/save")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{}"))
               .andExpect(status().is4xxClientError());
    }

    // =====================================================
    // TEST 4 : POST /api/evenements/save AVEC autorité ADMIN
    // authorities = {"ADMIN"} correspond à hasAuthority("ADMIN")
    // dans ton SecurityConfig (pas roles = {"ADMIN"} !)
    // =====================================================
    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ADMIN"})
    @DisplayName("Test 4 - POST /api/evenements/save avec ADMIN retourne 200")
    void testCreateEvenement_avecAdmin_retourne200() throws Exception {

        // ARRANGE
        EvenementDTO dto = new EvenementDTO();
        when(evenementService.save(any(EvenementDTO.class))).thenReturn(dto);

        // ACT + ASSERT
        mockMvc.perform(post("/api/evenements/save")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk());
    }

    // =====================================================
    // TEST 5 : DELETE /api/evenements/delete/{id} avec ADMIN
    // authorities = {"ADMIN"} correspond à hasAuthority("ADMIN")
    // dans ton SecurityConfig (pas roles = {"ADMIN"} !)
    // =====================================================
    @Test
    @WithMockUser(username = "admin@test.com", authorities = {"ADMIN"})
    @DisplayName("Test 5 - DELETE /api/evenements/delete/1 avec ADMIN retourne 200")
    void testDeleteEvenement_avecAdmin_retourne200() throws Exception {

        // ARRANGE
        doNothing().when(evenementService).deleteById(1L);

        // ACT + ASSERT
        mockMvc.perform(delete("/api/evenements/delete/1"))
               .andExpect(status().isOk());
    }
}      