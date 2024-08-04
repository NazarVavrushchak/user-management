package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.DTO.RoleDTO;
import com.nazar.usermanagement.entity.Role;
import org.springframework.web.bind.annotation.*;
import com.nazar.usermanagement.service.RoleService;

import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public void addRole(@PathVariable Long userId, @RequestBody RoleDTO roleDTO) {
        roleService.addRole(userId, roleDTO);
    }

    @PostMapping("/custom")
    public void createCustomRole(@PathVariable Long userId, @RequestParam String customRole) {
        roleService.createCustomRole(userId, customRole);
    }

    @GetMapping("/{roleId}")
    public Optional<Role> getRole(@PathVariable Long roleId) {
        return roleService.getRole(roleId);
    }

    @DeleteMapping("/{roleId}")
    public void deleteRole(@PathVariable Long roleId){
        roleService.deleteRole(roleId);
    }

    @PutMapping("/{roleId}")
    public void updateRole(@PathVariable Long roleId , @RequestBody RoleDTO roleDTO){
        roleService.updateRole(roleId, roleDTO);
    }
}