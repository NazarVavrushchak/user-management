package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.DTO.RoleDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nazar.usermanagement.service.RoleService;

@RestController
@RequestMapping("/users/{userId}/roles")
public class RoleController {
	
	private final RoleService roleService;
	
	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}
	
	@PostMapping
	public void addRole(@PathVariable Long userId , @RequestBody RoleDTO roleDTO) {
		//add role by persons id
		roleService.addRole(userId, roleDTO);
	}
}