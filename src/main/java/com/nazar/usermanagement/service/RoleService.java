package com.nazar.usermanagement.service;

import com.nazar.usermanagement.DTO.RoleDTO;
import com.nazar.usermanagement.utils.RoleMapper;
import org.springframework.stereotype.Service;

import com.nazar.usermanagement.entity.Role;
import com.nazar.usermanagement.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {
    private final UserRepository userRepository;

    public RoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addRole(Long userId, RoleDTO roleDTO) {
        //add role by users id
        var user = userRepository.findById(userId)
                .orElseThrow();
        Role role = RoleMapper.toEntity(roleDTO);
        user.addRole(role);
        userRepository.save(user);
    }
}