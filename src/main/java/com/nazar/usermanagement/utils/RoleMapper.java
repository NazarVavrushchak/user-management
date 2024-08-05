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
    private static RoleDTO.RoleType toRoleTypeDTO(Role.RoleType roleType) {
        return RoleDTO.RoleType.valueOf(roleType.name());
    }

    private static Role.RoleType toRoleTypeEntity(RoleDTO.RoleType roleType) {
        return Role.RoleType.valueOf(roleType.name());
    }

}
