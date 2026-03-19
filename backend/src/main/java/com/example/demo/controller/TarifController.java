package com.example.demo.controller;

import com.example.demo.dto.TarifDTO;
import com.example.demo.service.TarifServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tarifs")
@CrossOrigin(origins = "http://localhost:4200")
public class TarifController {
    
    @Autowired
    private TarifServiceImpl tarifService;

    @GetMapping("/all")
    public List<TarifDTO> getAllTarifs() {
        return tarifService.findAll();
    }

    @GetMapping("/getById/{id}")
    public TarifDTO getTarifById(@PathVariable Long id) {
        return tarifService.findById(id);
    }

    @PostMapping("/save")
    public void createTarif(@RequestBody TarifDTO tarifDTO) {
        tarifService.save(tarifDTO);
    }

    @PutMapping("/update")
    public void updateTarif(@RequestBody TarifDTO tarifDTO) {
        tarifService.update(tarifDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTarif(@PathVariable Long id) {
        tarifService.deleteById(id);
    }
}
