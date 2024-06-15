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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:16.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    @Transactional
    public void clearTable() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getUser() throws Exception {
        Role userRole = roleRepository.findByRole(Role.RoleType.WORKER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole(Role.RoleType.WORKER);
                    return roleRepository.save(newRole);
                });
        User user = new User();
        user.setFirstName("Nazar");
        user.setLastName("Vavrushchak");
        user.setEmail("test@gmail.com");
        user.setAge(18);
        user.setRole(userRole);

        userRepository.save(user);

        Optional<UserDTO> result = userService.getUser(user.getId());
        assertTrue(result.isPresent());
        assertEquals("Nazar", result.get().getFirstName());
    }

    @Test
    void createUser() throws Exception {
        Role userRole = new Role();
        userRole.setRole(Role.RoleType.STUDENT);
        roleRepository.save(userRole);

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Nazar");
        userDTO.setLastName("Vavrushchak");
        userDTO.setEmail("test@gmail.com");
        userDTO.setAge(18);
        userDTO.setRoleId(userRole.getRoleId());

        userService.createUser(userDTO);

        List<User> userList = userRepository.findAll();
        assertFalse(userList.isEmpty());
        assertEquals("Nazar", userList.get(0).getFirstName());

    }

    @Test
    void getAllUser() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        Role userRole = roleRepository.findByRole(Role.RoleType.WORKER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole(Role.RoleType.WORKER);
                    return roleRepository.save(newRole);
                });
        User user = new User();
        user.setFirstName("Nazar");
        user.setLastName("Vavrushchak");
        user.setEmail("test@gmail.com");
        user.setAge(18);
        user.setRole(userRole);

        userRepository.save(user);

        User user1 = new User();
        user1.setFirstName("Volodymyr");
        user1.setLastName("Dmytryk");
        user1.setEmail("test1@gmail.com");
        user1.setAge(25);
        user1.setRole(userRole);
        userRepository.save(user1);
        List<User> userList = Arrays.asList(user, user1);

        assertFalse(userList.isEmpty());
    }

    @Test
    void updateUser() {
        Role userRole = new Role();
        userRole.setRole(Role.RoleType.STUDENT);
        roleRepository.save(userRole);

        User user = new User();
        user.setFirstName("Ostap");
        user.setLastName("Kvitka");
        user.setEmail("test3@gmail.com");
        user.setAge(20);
        user.setRole(userRole);
        userRepository.save(user);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setFirstName("UpdatedFirstName");
        updatedUserDTO.setLastName("UpdatedLastName");
        updatedUserDTO.setEmail("updated.email@gmail.com");
        updatedUserDTO.setAge(20);
        updatedUserDTO.setRoleId(userRole.getRoleId());

        Optional<UserDTO> updatedUser = userService.updateUser(user.getId(), updatedUserDTO);

        assertTrue(updatedUser.isPresent());
        assertEquals("UpdatedFirstName", updatedUser.get().getFirstName());
        assertEquals("UpdatedLastName", updatedUser.get().getLastName());
        assertEquals("updated.email@gmail.com", updatedUser.get().getEmail());
        assertEquals(20, updatedUser.get().getAge());

        Optional<User> updatedUserFromDb = userRepository.findById(user.getId());
        assertTrue(updatedUserFromDb.isPresent());
        assertEquals("UpdatedFirstName", updatedUserFromDb.get().getFirstName());
        assertEquals("UpdatedLastName", updatedUserFromDb.get().getLastName());
        assertEquals("updated.email@gmail.com", updatedUserFromDb.get().getEmail());
        assertEquals(20, updatedUserFromDb.get().getAge());
    }
}