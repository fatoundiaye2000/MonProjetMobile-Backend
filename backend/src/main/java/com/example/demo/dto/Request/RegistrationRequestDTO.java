package com.example.demo.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDTO {
    private String nom;  // À noter : votre User a nom/prenom, pas username
    private String prenom;  // À noter : votre User a nom/prenom, pas username
    private String password;
    private String email;
}
