package com.nazar.usermanagement.utils;

import com.nazar.usermanagement.DTO.RoleDTO;
import com.nazar.usermanagement.entity.Role;

public class RoleMapper {
    public static RoleDTO toDTO(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(role.getRoleId());
        roleDTO.setRole(role.getRole());
        return roleDTO;
    }

    public static Role toEntity(RoleDTO roleDTO){
        Role role = new Role();
        role.setRole(roleDTO.getRole());
        role.setRoleId(roleDTO.getRoleId());
        return role;
    }
}
