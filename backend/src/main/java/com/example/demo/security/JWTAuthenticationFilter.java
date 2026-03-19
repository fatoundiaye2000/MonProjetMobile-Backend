package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    
    private UserDetailsService userDetailsService;
    
    public JWTAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String method = request.getMethod();
        
        // Ne pas filtrer les endpoints publics
        return path.equals("/login") || 
               path.equals("/test") ||
               path.startsWith("/api/users/register") ||
               path.startsWith("/api/test") ||
               method.equals("OPTIONS") ||
               // ✅ SWAGGER - ne pas filtrer ces chemins
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-resources") ||
               path.startsWith("/webjars") ||
               path.equals("/swagger-ui.html");
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        // Si pas de token, continuer (Spring Security bloquera si nécessaire)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // Extraire et vérifier le token
            String jwt = authHeader.substring(7);
            System.out.println("🔐 Vérification du token pour: " + request.getServletPath());
            
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("exampleb@yahoo.com")).build();
            DecodedJWT decodedJWT = verifier.verify(jwt);
            
            // Récupérer les infos du token
            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
            
            System.out.println("👤 Utilisateur: " + username);
            System.out.println("🎭 Rôles: " + roles);
            
            // Charger l'utilisateur depuis la base de données
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // Créer les authorities
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            if (roles != null) {
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            }
            
            // Créer l'authentication
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("✅ Token validé - Utilisateur authentifié");
            
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            System.out.println("❌ Token invalide: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token invalide: " + e.getMessage() + "\"}");
        }
    }
}