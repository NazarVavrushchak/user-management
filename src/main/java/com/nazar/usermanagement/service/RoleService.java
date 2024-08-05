package com.nazar.usermanagement.service;

import com.nazar.usermanagement.DTO.RoleDTO;
import com.nazar.usermanagement.entity.Role;
import com.nazar.usermanagement.entity.User;
import com.nazar.usermanagement.repository.RoleRepository;
import com.nazar.usermanagement.repository.UserRepository;
import com.nazar.usermanagement.utils.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public RoleService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void addRole(Long userId, RoleDTO roleDTO) {
        var user = userRepository.findById(userId)
                .orElseThrow();
        Role role = RoleMapper.toEntity(roleDTO);
        user.addRole(role);
        userRepository.save(user);
    }

    public void createCustomRole(Long userId, String customRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = new Role();
        role.setRole(Role.RoleType.CUSTOM);
        role.setCustomRole(customRole);
        user.addRole(role);
        roleRepository.save(role);
        userRepository.save(user);
    }

    public Optional<Role> getRole(Long roleId) {
        return roleRepository.findById(roleId);
    }

    public void deleteRole(Long roleId){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        roleRepository.delete(role);
    }

    public void updateRole(Long roleId , RoleDTO roleDTO){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        Role updateRole = RoleMapper.toEntity(roleDTO);
        updateRole.setRoleId(roleId);
        roleRepository.save(updateRole);
    }
}