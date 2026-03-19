package com.example.demo.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.entities.User;
import com.example.demo.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 MyUserDetailsService appelé avec email: " + email);
        
        // Recherche par email
        User user = userService.findUserByEmail(email);
        
        if (user == null) {
            System.out.println("❌ Utilisateur introuvable avec email: " + email);
            throw new UsernameNotFoundException("Utilisateur introuvable avec email: " + email);
        }
        
        System.out.println("✅ Utilisateur trouvé: " + user.getEmail());
        System.out.println("🔑 Mot de passe (hashé): " + user.getPassword());
        
        List<GrantedAuthority> auths = new ArrayList<>();
        
        // Parcourir tous les rôles
        user.getRoles().forEach(role -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
            auths.add(authority);
            System.out.println("🎭 Rôle ajouté: " + role.getRole());
        });
        
        System.out.println("📋 Création de UserDetails pour Spring Security");
        
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(), 
            auths);
    }
}
