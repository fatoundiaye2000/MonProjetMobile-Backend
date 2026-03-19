package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path imagesUploadPath;

    /**
     * Initialiser le dossier de stockage au démarrage
     */
    @PostConstruct
    public void init() {
        try {
            // Créer le chemin pour uploads/images/
            imagesUploadPath = Paths.get(uploadDir, "images");
            
            if (!Files.exists(imagesUploadPath)) {
                Files.createDirectories(imagesUploadPath);
                System.out.println("✅ Dossier créé : " + imagesUploadPath.toAbsolutePath());
            } else {
                System.out.println("✅ Dossier existe déjà : " + imagesUploadPath.toAbsolutePath());
            }
            
            // Lister les fichiers existants
            System.out.println("📁 Fichiers dans " + imagesUploadPath + ":");
            try (Stream<Path> paths = Files.list(imagesUploadPath)) {
                long count = paths.filter(Files::isRegularFile).count();
                System.out.println("  " + count + " fichiers trouvés");
            } catch (IOException e) {
                System.out.println("  (Aucun fichier trouvé)");
            }
            
        } catch (IOException e) {
            System.err.println("❌ ERREUR : Impossible de créer le dossier d'upload");
            throw new RuntimeException("Impossible de créer le dossier d'upload", e);
        }
    }

    /**
     * Sauvegarder un fichier image
     */
    public String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }
        
        // Générer un nom unique
        String originalFilename = file.getOriginalFilename();
        String extension = ".jpg";
        
        if (originalFilename != null && originalFilename.contains(".")) {
            int lastDot = originalFilename.lastIndexOf(".");
            if (lastDot > 0) {
                String ext = originalFilename.substring(lastDot + 1).toLowerCase();
                if (ext.matches("(jpg|jpeg|png|gif|webp|bmp)")) {
                    extension = "." + ext;
                }
            }
        }
        
        String filename = "event_" + System.currentTimeMillis() + "_" + 
                         UUID.randomUUID().toString().substring(0, 8) + extension;
        
        try {
            // Sauvegarder le fichier
            Path filePath = imagesUploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return filename;
            
        } catch (IOException e) {
            throw new IOException("Erreur sauvegarde: " + e.getMessage(), e);
        }
    }

    /**
     * Obtenir l'URL complète d'une image
     */
    public String getImageUrl(String filename, HttpServletRequest request) {
        if (filename == null || filename.trim().isEmpty()) {
            return "";
        }
        
        // Construire l'URL absolue
        String baseUrl = "http://localhost:8081";
        
        // Si c'est déjà une URL complète, retourner telle quelle
        if (filename.startsWith("http")) {
            return filename;
        }
        
        // Retourner l'URL complète
        return baseUrl + "/files/" + filename;
    }

    /**
     * Vérifier si un fichier existe
     */
    public boolean fileExists(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        return Files.exists(imagesUploadPath.resolve(filename));
    }

    /**
     * Obtenir le chemin physique
     */
    public Path getFilePath(String filename) {
        return imagesUploadPath.resolve(filename);
    }

    /**
     * Lister tous les fichiers
     */
    public List<String> listAllFiles() throws IOException {
        if (!Files.exists(imagesUploadPath)) {
            return new ArrayList<>();
        }
        
        try (Stream<Path> paths = Files.list(imagesUploadPath)) {
            return paths
                .filter(Files::isRegularFile)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
        }
    }

    /**
     * Créer une image de test
     */
    public String createTestImage(String baseName) throws IOException {
        String filename = baseName + "_" + System.currentTimeMillis() + ".jpg";
        Path filePath = imagesUploadPath.resolve(filename);
        
        String content = "=== IMAGE DE TEST ===\n";
        content += "Nom: " + filename + "\n";
        content += "Date: " + new Date() + "\n";
        
        Files.writeString(filePath, content);
        
        return filename;
    }
}