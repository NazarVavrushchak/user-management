package com.nazar.usermanagement.service;

import com.nazar.usermanagement.DTO.RoleDTO;
import org.springframework.stereotype.Service;

import com.nazar.usermanagement.entity.Role;
import com.nazar.usermanagement.repository.PersonRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // щоб не порушити атомарність (операція виконується або не виконується)
public class RoleService {
    private final PersonRepository personRepository;

    public RoleService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void addRole(Long personId, RoleDTO roleDTO) {
        //add role by persons id
        var person = personRepository.findById(personId)
                .orElseThrow();
        Role role = new Role();
        role.setRole(Role.RoleType.valueOf(roleDTO.getRole().name()));

        person.addRole(role);
    }
}