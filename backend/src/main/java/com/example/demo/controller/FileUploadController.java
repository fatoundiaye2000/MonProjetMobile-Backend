package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.demo.service.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    // Chemin du dossier uploads
    private final Path uploadPath = Paths.get("uploads/images");

    /**
     * ENDPOINT CRITIQUE : Récupérer une image via /files/{filename}
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        System.out.println("📥 GET /files/" + filename);
        
        try {
            Path filePath = uploadPath.resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                // Déterminer le Content-Type
                String contentType = "application/octet-stream";
                String lowerFilename = filename.toLowerCase();
                
                if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                } else if (lowerFilename.endsWith(".png")) {
                    contentType = "image/png";
                } else if (lowerFilename.endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (lowerFilename.endsWith(".webp")) {
                    contentType = "image/webp";
                } else if (lowerFilename.endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else if (lowerFilename.endsWith(".txt")) {
                    contentType = "text/plain";
                }
                
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .body(resource);
            } else {
                System.out.println("❌ Fichier non trouvé: " + filename);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur récupération fichier: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * NOUVEL ENDPOINT : Route alternative /api/files/{filename}
     */
    @GetMapping("/api/files/{filename:.+}")
    public ResponseEntity<Resource> getFileApi(@PathVariable String filename) {
        System.out.println("📥 GET /api/files/" + filename);
        return getFile(filename); // Réutiliser la même logique
    }

    /**
     * ENDPOINT : Lister toutes les images
     */
    @GetMapping("/list")
    public ResponseEntity<?> listFilesSimple() {
        System.out.println("📄 GET /files/list");
        
        try {
            List<String> filenames = new ArrayList<>();
            
            // Créer le dossier s'il n'existe pas
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("✅ Dossier créé: " + uploadPath.toAbsolutePath());
            }
            
            Files.list(uploadPath)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    filenames.add(path.getFileName().toString());
                });
            
            Map<String, Object> response = new HashMap<>();
            response.put("files", filenames);
            response.put("count", filenames.size());
            response.put("folder", uploadPath.toAbsolutePath().toString());
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("✅ " + filenames.size() + " fichiers trouvés");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Erreur liste: " + e.getMessage());
            return ResponseEntity.status(500)
                .body(createError("Erreur liste: " + e.getMessage()));
        }
    }

    /**
     * ENDPOINT : Upload simple
     */
    @PostMapping("/upload-simple")
    public ResponseEntity<?> uploadSimple(@RequestParam("file") MultipartFile file) {
        System.out.println("=== UPLOAD SIMPLE ===");
        
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(createError("Fichier vide"));
            }
            
            System.out.println("✅ Fichier reçu: " + file.getOriginalFilename());
            
            // Sauvegarder
            String filename = fileStorageService.storeFile(file);
            
            Map<String, String> response = new HashMap<>();
            response.put("filename", filename);
            response.put("url", "http://localhost:8081/files/" + filename);
            response.put("message", "Upload réussi!");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createError("Erreur: " + e.getMessage()));
        }
    }

    /**
     * ENDPOINT : Tester l'accès aux fichiers
     */
    @GetMapping("/test-access")
    public ResponseEntity<?> testAccess() {
        System.out.println("🧪 GET /files/test-access");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Vérifier le dossier
            response.put("uploadPath", uploadPath.toAbsolutePath().toString());
            response.put("exists", Files.exists(uploadPath));
            response.put("readable", Files.isReadable(uploadPath));
            response.put("writable", Files.isWritable(uploadPath));
            
            // Lister les fichiers de test
            List<String> testImages = Arrays.asList(
                "event_1767731725433_f04f6f9c.jpg",
                "event_1767732256076_7594c16a.jpg", 
                "event_1767732304324_ee1f3d49.jpg",
                "event_1767732541267_a1d12c20.png",
                "event_1767732568405_8b853f8f.jpg"
            );
            
            List<Map<String, Object>> fileTests = new ArrayList<>();
            
            for (String filename : testImages) {
                Map<String, Object> test = new HashMap<>();
                test.put("filename", filename);
                
                Path filePath = uploadPath.resolve(filename);
                boolean exists = Files.exists(filePath);
                test.put("exists", exists);
                
                if (exists) {
                    test.put("size", Files.size(filePath));
                    test.put("readable", Files.isReadable(filePath));
                    test.put("url", "http://localhost:8081/files/" + filename);
                }
                
                fileTests.add(test);
            }
            
            response.put("testFiles", fileTests);
            response.put("status", "SUCCESS");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * ENDPOINT : Créer des images de test si elles n'existent pas
     */
    @PostMapping("/create-missing-images")
    public ResponseEntity<?> createMissingImages() {
        System.out.println("🎨 POST /files/create-missing-images");
        
        try {
            // Créer le dossier
            Files.createDirectories(uploadPath);
            
            List<String> created = new ArrayList<>();
            
            // Vos images manquantes
            String[] missingImages = {
                "event_1767731725433_f04f6f9c.jpg",
                "event_1767732256076_7594c16a.jpg", 
                "event_1767732304324_ee1f3d49.jpg",
                "event_1767732541267_a1d12c20.png",
                "event_1767732568405_8b853f8f.jpg"
            };
            
            for (String filename : missingImages) {
                Path filePath = uploadPath.resolve(filename);
                
                if (!Files.exists(filePath)) {
                    // Créer un fichier texte simple
                    String content = "=== IMAGE DE TEST ===\n";
                    content += "Nom: " + filename + "\n";
                    content += "Projet: BTS SIO SLAM\n";
                    content += "Date: " + new Date() + "\n";
                    content += "Ceci est une image simulée pour test\n";
                    
                    Files.writeString(filePath, content);
                    created.add(filename);
                    System.out.println("✅ Créé: " + filename);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", created.size() + " images créées");
            response.put("created", created);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(createError("Erreur création: " + e.getMessage()));
        }
    }

    /**
     * ENDPOINT : Information sur le serveur
     */
    @GetMapping("/server-info")
    public ResponseEntity<?> serverInfo() {
        System.out.println("🖥️ GET /files/server-info");
        
        Map<String, Object> info = new HashMap<>();
        info.put("server", "Spring Boot");
        info.put("port", "8081");
        info.put("timestamp", System.currentTimeMillis());
        info.put("imageBaseUrl", "http://localhost:8081/files/");
        info.put("apiBaseUrl", "http://localhost:8081/api");
        info.put("status", "ACTIVE");
        
        return ResponseEntity.ok(info);
    }

    /**
     * ENDPOINT : Vérifier une image spécifique
     */
    @GetMapping("/check/{filename:.+}")
    public ResponseEntity<?> checkImage(@PathVariable String filename) {
        System.out.println("🔍 GET /files/check/" + filename);
        
        try {
            Path filePath = uploadPath.resolve(filename);
            
            Map<String, Object> response = new HashMap<>();
            response.put("filename", filename);
            response.put("exists", Files.exists(filePath));
            response.put("path", filePath.toAbsolutePath().toString());
            
            if (Files.exists(filePath)) {
                response.put("size", Files.size(filePath));
                response.put("readable", Files.isReadable(filePath));
                response.put("writable", Files.isWritable(filePath));
                response.put("url", "http://localhost:8081/files/" + filename);
                response.put("directUrl", "http://localhost:8081/files/" + filename);
                response.put("apiUrl", "http://localhost:8081/api/files/" + filename);
                response.put("status", "AVAILABLE");
            } else {
                response.put("status", "NOT_FOUND");
                response.put("suggestion", "Utilisez POST /files/create-missing-images pour créer les images manquantes");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(createError("Erreur vérification: " + e.getMessage()));
        }
    }

    /**
     * Méthode utilitaire pour créer une réponse d'erreur
     */
    private Map<String, String> createError(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return error;
    }
}