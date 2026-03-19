package com.example.demo.config; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.JWTAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173", 
            "http://localhost:3000",
            "http://127.0.0.1:5173"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .csrf(csrf -> csrf.disable())
            
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            .authorizeHttpRequests(requests -> 
                requests
                    // =========================================================
                    // PERMETTRE OPTIONS POUR CORS
                    // =========================================================
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    
                    // =========================================================
                    // SWAGGER - ACCES PUBLIC (DEJA PRESENT, RIEN A CHANGER ici)
                    // =========================================================
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/v3/api-docs.json",
                        "/swagger-resources/**",
                        "/swagger-resources",
                        "/webjars/**"
                    ).permitAll()
                    
                    // =========================================================
                    // ACCES AUX IMAGES SANS AUTH
                    // =========================================================
                    .requestMatchers("/files/**").permitAll()
                    .requestMatchers("/uploads/**").permitAll()
                    .requestMatchers("/api/files/**").permitAll()
                    
                    // =========================================================
                    // ENDPOINTS PUBLICS (SANS AUTHENTIFICATION)
                    // =========================================================
                    .requestMatchers("/test").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/favicon.ico").permitAll()
                    
                    // =========================================================
                    // API PUBLIQUE (SANS AUTH)
                    // =========================================================
                    .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                    .requestMatchers("/api/test/**").permitAll()
                    
                    // =========================================================
                    // POUR LE DEVELOPPEMENT - PERMETTRE L'ACCES AUX DONNEES
                    // =========================================================
                    .requestMatchers(HttpMethod.GET, "/api/users/all").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/evenements/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/typeevents/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/adresses/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/tarifs/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/reservations/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/roles/**").permitAll()
                    
                    // =========================================================
                    // REGLES METIER - POST / PUT / DELETE necessite ADMIN
                    // =========================================================
                    .requestMatchers(HttpMethod.POST, "/api/users/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("ADMIN")
                    
                    .requestMatchers(HttpMethod.POST, "/api/roles/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/roles/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/roles/**").hasAuthority("ADMIN")
                    
                    .requestMatchers(HttpMethod.POST, "/api/evenements/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/evenements/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/evenements/**").hasAuthority("ADMIN")
                    
                    .requestMatchers(HttpMethod.POST, "/api/reservations/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/reservations/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasAuthority("ADMIN")
                    
                    .requestMatchers(HttpMethod.POST, "/api/typeevents/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/typeevents/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/typeevents/**").hasAuthority("ADMIN")
                    
                    .requestMatchers(HttpMethod.POST, "/api/adresses/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/adresses/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/adresses/**").hasAuthority("ADMIN")
                    
                    .requestMatchers(HttpMethod.POST, "/api/tarifs/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/tarifs/**").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/tarifs/**").hasAuthority("ADMIN")
                    
                    // =========================================================
                    // TOUTES LES AUTRES REQUETES NECESSITENT UNE AUTHENTIFICATION
                    // =========================================================
                    .anyRequest().authenticated()
            )
            
            .authenticationProvider(authenticationProvider())
            
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(userDetailsService);
    }
}