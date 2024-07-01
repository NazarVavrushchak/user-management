package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.DTO.UserDTO;
import com.nazar.usermanagement.entity.Role;
import com.nazar.usermanagement.entity.User;
import com.nazar.usermanagement.repository.RoleRepository;
import com.nazar.usermanagement.repository.UserRepository;
import com.nazar.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class RoleControllerTest {
    @Container
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.2");

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    @Transactional
    public void clearTable() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void addRole() {
        Role userRole = new Role();
        userRole.setRole(Role.RoleType.STUDENT);
        userRole = roleRepository.save(userRole);

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Stepan");
        userDTO.setLastName("Silkovych");
        userDTO.setEmail("test9@gmail.com");
        userDTO.setAge(45);
        userDTO.setRoleId(userRole.getRoleId());

        userService.createUser(userDTO);

        Optional<User> savedUserOptional = userRepository.findByEmail(userDTO.getEmail());
        assertTrue(savedUserOptional.isPresent());
        User savedUser = savedUserOptional.get();

        assertNotNull(savedUser);
        assertEquals("Stepan", savedUser.getFirstName());
        assertEquals("Silkovych", savedUser.getLastName());
        assertEquals("test9@gmail.com", savedUser.getEmail());
        assertEquals(45, savedUser.getAge());
        assertEquals(userRole.getRoleId(), savedUser.getRole().getRoleId());
    }
}