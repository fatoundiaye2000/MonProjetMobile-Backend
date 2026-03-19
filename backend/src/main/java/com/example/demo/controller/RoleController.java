package com.example.demo.controller;

import com.example.demo.dto.RoleDTO;
import com.example.demo.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RoleController {
    
    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping("/all")
    public List<RoleDTO> getAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/getById/{id}")
    public RoleDTO getRoleById(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @PostMapping("/save")
    public void createRole(@RequestBody RoleDTO roleDTO) {
        roleService.save(roleDTO);
    }

    @PutMapping("/update")
    public void updateRole(@RequestBody RoleDTO roleDTO) {
        roleService.update(roleDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
    }
}
