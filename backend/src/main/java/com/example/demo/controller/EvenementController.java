package com.example.demo.controller;

import com.example.demo.dto.EvenementDTO;
import com.example.demo.service.EvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evenements")
@CrossOrigin(origins = "*")
public class EvenementController {
    
    @Autowired
    private EvenementService evenementService;
    
    // ⭐⭐⭐ MÉTHODE DE TEST PUBLIQUE ⭐⭐⭐
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("✅✅✅ EvenementController.test() APPELÉ !");
        System.out.println("🔧 evenementService: " + (evenementService != null ? "INJECTÉ" : "NULL"));
        return ResponseEntity.ok("EvenementController fonctionne!");
    }
    
    // ⭐⭐⭐ MÉTHODE PUBLIQUE POUR VÉRIFICATION ⭐⭐⭐
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        System.out.println("🔍 EvenementController.check() - Vérification");
        try {
            if (evenementService == null) {
                return ResponseEntity.ok("evenementService est NULL - Problème d'injection!");
            }
            return ResponseEntity.ok("EvenementController OK - Service injecté: " + evenementService.getClass().getSimpleName());
        } catch (Exception e) {
            return ResponseEntity.ok("Erreur: " + e.getMessage());
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<EvenementDTO>> getAllEvenements() {
        System.out.println("✅✅✅ GET /api/evenements/all APPELÉ - EvenementController");
        try {
            List<EvenementDTO> events = evenementService.findAll();
            System.out.println("✅ " + events.size() + " événements trouvés");
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            System.err.println("❌ Erreur dans getAllEvenements: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/getById/{id}")
    public ResponseEntity<EvenementDTO> getEvenementById(@PathVariable Long id) {
        System.out.println("🔵 GET /api/evenements/getById/" + id);
        try {
            EvenementDTO event = evenementService.findById(id);
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/save")
    public ResponseEntity<EvenementDTO> createEvenement(@RequestBody EvenementDTO evenementDTO) {
        System.out.println("🔵 POST /api/evenements/save");
        System.out.println("📝 Données reçues: " + evenementDTO);
        try {
            EvenementDTO saved = evenementService.save(evenementDTO);
            System.out.println("✅ Événement créé avec ID: " + saved.getIdEvent());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<EvenementDTO> updateEvenement(@RequestBody EvenementDTO evenementDTO) {
        System.out.println("🔵 PUT /api/evenements/update");
        System.out.println("📝 Données reçues: " + evenementDTO);
        try {
            EvenementDTO updated = evenementService.update(evenementDTO);
            System.out.println("✅ Événement mis à jour: " + updated.getIdEvent());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        System.out.println("🔵 DELETE /api/evenements/delete/" + id);
        try {
            evenementService.deleteById(id);
            System.out.println("✅ Événement supprimé: " + id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}