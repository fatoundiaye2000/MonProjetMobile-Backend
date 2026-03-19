package com.example.demo.controller;

import com.example.demo.dto.TypeEventDTO;
import com.example.demo.service.TypeEventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/typeEvents")
@CrossOrigin(origins = "http://localhost:4200")
public class TypeEventController {
    
    @Autowired
    private TypeEventServiceImpl typeEventService;

    @GetMapping("/all")
    public List<TypeEventDTO> getAllTypeEvents() {
        return typeEventService.findAll();
    }

    @GetMapping("/getById/{id}")
    public TypeEventDTO getTypeEventById(@PathVariable Long id) {
        return typeEventService.findById(id);
    }

    @PostMapping("/save")
    public void createTypeEvent(@RequestBody TypeEventDTO typeEventDTO) {
        typeEventService.save(typeEventDTO);
    }

    @PutMapping("/update")
    public void updateTypeEvent(@RequestBody TypeEventDTO typeEventDTO) {
        typeEventService.update(typeEventDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTypeEvent(@PathVariable Long id) {
        typeEventService.deleteById(id);
    }
}
