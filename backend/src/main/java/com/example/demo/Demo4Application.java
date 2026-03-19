 package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repos.RoleRepository;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.demo")
public class Demo4Application implements CommandLineRunner {

    @Autowired
    UserService userService;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    FileStorageService fileStorageService;
    
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Demo4Application.class, args);
        
        System.out.println("==========================================");
        System.out.println("🔍 BEANS CHARGÉS PAR SPRING :");
        System.out.println("==========================================");
        
        String[] beanNames = ctx.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.toLowerCase().contains("controller") || 
                beanName.toLowerCase().contains("user") ||
                beanName.toLowerCase().contains("security")) {
                System.out.println("✅ " + beanName + " -> " + ctx.getBean(beanName).getClass().getName());
            }
        }
        
        System.out.println("==========================================");
        System.out.println("🌐 ENDPOINTS DISPONIBLES:");
        System.out.println("==========================================");
        System.out.println("GET  http://localhost:8081/test");
        System.out.println("GET  http://localhost:8081/login");
        System.out.println("POST http://localhost:8081/login");
        System.out.println("GET  http://localhost:8081/api/users/all");
        System.out.println("POST http://localhost:8081/api/users/register");
        System.out.println("==========================================");
    }
    
    @Override
    public void run(String... args) throws Exception {
        fileStorageService.init();
        System.out.println("🚀 Application démarrée avec succès!");
    }
    
    @Bean
    CommandLineRunner init_users(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return args -> {
            try {
                System.out.println("=== DÉBUT INITIALISATION ===");
                
                // CRÉER LES RÔLES
                if (roleRepository.findByRole("ADMIN") == null) {
                    userService.addRole(new Role(null, "ADMIN"));
                    System.out.println("✔ Rôle ADMIN créé");
                }
                
                if (roleRepository.findByRole("USER") == null) {
                    userService.addRole(new Role(null, "USER"));
                    System.out.println("✔ Rôle USER créé");
                }
                
                // CRÉER LES UTILISATEURS
                if (userService.findUserByEmail("admin@example.com") == null) {
                    User admin = new User();
                    admin.setEmail("admin@example.com");
                    admin.setPassword(bCryptPasswordEncoder.encode("123"));
                    admin.setEnabled(true);
                    admin.setNom("Admin");
                    admin.setPrenom("System");
                    userService.saveUser(admin);
                    System.out.println("✔ Utilisateur admin créé");
                }
                
                if (userService.findUserByEmail("user@example.com") == null) {
                    User user = new User();
                    user.setEmail("user@example.com");
                    user.setPassword(bCryptPasswordEncoder.encode("123"));
                    user.setEnabled(true);
                    user.setNom("User");
                    user.setPrenom("Normal");
                    userService.saveUser(user);
                    System.out.println("✔ Utilisateur user créé");
                }
                
                // ASSIGNER LES RÔLES
                userService.addRoleToUser("admin@example.com", "ADMIN");
                userService.addRoleToUser("admin@example.com", "USER");
                userService.addRoleToUser("user@example.com", "USER");
                System.out.println("✔ Rôles assignés");
                
                System.out.println("=== INITIALISATION TERMINÉE ===");
                
            } catch (Exception e) {
                System.out.println("❌ Erreur lors de l'initialisation: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
