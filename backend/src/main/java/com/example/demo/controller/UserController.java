package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.Request.RegistrationRequestDTO;
import com.example.demo.entities.User;
import com.example.demo.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    // ========== ENDPOINTS DE TEST ==========
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("✅ Endpoint /test appelé");
        return ResponseEntity.ok("Le contrôleur fonctionne !");
    }
    
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> testLoginGet() {
        System.out.println("✅ GET /login appelé");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Utilisez POST pour vous connecter");
        response.put("method", "POST");
        response.put("endpoint", "/login");
        return ResponseEntity.ok(response);
    }

    // ========== ENDPOINT LOGIN ==========
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("==========================================");
        System.out.println("🚀 POST /login APPELÉ");
        System.out.println("==========================================");
        
        try {
            System.out.println("📧 Email reçu: " + loginRequest.getEmail());
            System.out.println("🔑 Password présent: " + (loginRequest.getPassword() != null));
            
            // Vérifier les champs
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                System.out.println("❌ Email ou password manquant");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email et mot de passe requis");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Authentifier
            System.out.println("🔐 Tentative d'authentification...");
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                );
            
            Authentication authentication = authenticationManager.authenticate(authToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            System.out.println("✅ Authentification réussie pour: " + userDetails.getUsername());
            
            // Récupérer les rôles
            List<String> roles = new ArrayList<>();
            userDetails.getAuthorities().forEach(auth -> {
                roles.add(auth.getAuthority());
            });
            
            System.out.println("🎭 Rôles: " + roles);
            
            // Créer le token JWT
            String jwt = JWT.create()
                .withSubject(userDetails.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256("exampleb@yahoo.com"));
            
            System.out.println("🎫 Token JWT généré");
            
            // Réponse
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", userDetails.getUsername());
            response.put("roles", roles);
            
            System.out.println("📤 Réponse envoyée avec succès");
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationException e) {
            System.out.println("❌ Erreur d'authentification: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Email ou mot de passe incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            
        } catch (Exception e) {
            System.out.println("💥 Erreur générale: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur serveur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ========== ENDPOINTS USERS ==========
    @GetMapping("/api/users/all")
    public List<UserDTO> getAllUsers() {
        System.out.println("✅ Endpoint /api/users/all appelé");
        return userService.findAll();
    }

    @GetMapping("/api/users/getById/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/api/users/save")
    public void createUser(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
    }

    @PutMapping("/api/users/update")
    public void updateUser(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
    }

    @DeleteMapping("/api/users/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
    
    @PostMapping("/api/users/register")
    public User register(@RequestBody RegistrationRequestDTO request) {
        return userService.registerUser(request);
    }
}

// Classe pour recevoir les credentials
class LoginRequest {
    private String email;
    private String password;
    
    public LoginRequest() {}
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
