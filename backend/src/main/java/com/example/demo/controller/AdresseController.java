package com.example.demo.controller;


import com.example.demo.dto.AdresseDTO;
import com.example.demo.service.AdresseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/adresses")
@CrossOrigin(origins = "http://localhost:4200")
public class AdresseController {
    
    @Autowired
    private AdresseServiceImpl adresseService;

    @GetMapping("/all")
    public List<AdresseDTO> getAllAdresses() {
        return adresseService.findAll();
    }

    @GetMapping("/getById/{id}")
    public AdresseDTO getAdresseById(@PathVariable Long id) {
        return adresseService.findById(id);
    }

    @PostMapping("/save")
    public void createAdresse(@RequestBody AdresseDTO adresseDTO) {
        adresseService.save(adresseDTO);
    }

    @PutMapping("/update")
    public void updateAdresse(@RequestBody AdresseDTO adresseDTO) {
        adresseService.update(adresseDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAdresse(@PathVariable Long id) {
        adresseService.deleteById(id);
    }
}