package com.nazar.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nazar.usermanagement.DTO.UserLoginDTO;
import com.nazar.usermanagement.DTO.UserRegistrationDTO;
import com.nazar.usermanagement.entity.Role;
import com.nazar.usermanagement.entity.User;
import com.nazar.usermanagement.repository.RoleRepository;
import com.nazar.usermanagement.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3");

    @DynamicPropertySource
    static void postgreSqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment environment;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    private void createUserInDb(String firstName, String lastName, String email, String password, int age) {
        Role role = roleRepository.findByName("USER").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setRole(Role.RoleType.USER);
            newRole.setName("USER");
            return roleRepository.save(newRole);
        });

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setAge(age);
        user.setRole(role);
        userRepository.save(user);
    }

    @Test
    @SneakyThrows
    void registerUser() {
        assertEquals("org.postgresql.Driver", environment.getProperty("spring.datasource.driver-class-name"));

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setFirstName("Mykola");
        userRegistrationDTO.setLastName("Subur");
        userRegistrationDTO.setEmail("mykola@gmail.com");
        userRegistrationDTO.setPassword("qwerty");
        userRegistrationDTO.setAge(18);

        String registrationDtoToJson = objectMapper.writeValueAsString(userRegistrationDTO);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationDtoToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mykola"))
                .andExpect(jsonPath("$.lastName").value("Subur"))
                .andExpect(jsonPath("$.email").value("mykola@gmail.com"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    @SneakyThrows
    void loginUser() {
        assertEquals("org.postgresql.Driver", environment.getProperty("spring.datasource.driver-class-name"));

        createUserInDb("Mykola", "Subur", "mykola@gmail.com", "qwerty1111", 18);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("mykola@gmail.com");
        userLoginDTO.setPassword("qwerty");

        String loginDtoToJson = objectMapper.writeValueAsString(userLoginDTO);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDtoToJson))
                .andExpect(status().isOk());
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

        mockMvc.perform(post("/users/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": " + user.getId() + "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Nazar"));
    }

    @Test
    void createUser() throws Exception {
        Role userRole = new Role();
        userRole.setRole(Role.RoleType.STUDENT);
        roleRepository.save(userRole);

        String userDtoJson = "{ \"firstName\": \"Nazar\", \"lastName\": \"Vavrushchak\", \"email\": \"test@gmail.com\", \"age\": 18, \"roleId\": " + userRole.getRoleId() + " }";

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Nazar"));

        List<User> userList = userRepository.findAll();
        assertFalse(userList.isEmpty());
        assertEquals("Nazar", userList.get(0).getFirstName());
    }

    @Test
    void getAllUser() throws Exception {
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

        mockMvc.perform(post("/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Nazar"))
                .andExpect(jsonPath("$[1].firstName").value("Volodymyr"));
    }

    @Test
    @SneakyThrows
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

        String updatedUserDtoJson = "{ \"firstName\": \"UpdatedFirstName\", \"lastName\": \"UpdatedLastName\", \"email\": \"updated.email@gmail.com\", \"age\": 20, \"roleId\": " + userRole.getRoleId() + " }";

        mockMvc.perform(post("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedFirstName"))
                .andExpect(jsonPath("$.lastName").value("UpdatedLastName"))
                .andExpect(jsonPath("$.email").value("updated.email@gmail.com"));

        Optional<User> updatedUserFromDb = userRepository.findById(user.getId());
        assertTrue(updatedUserFromDb.isPresent());
        assertEquals("UpdatedFirstName", updatedUserFromDb.get().getFirstName());
        assertEquals("UpdatedLastName", updatedUserFromDb.get().getLastName());
        assertEquals("updated.email@gmail.com", updatedUserFromDb.get().getEmail());
        assertEquals(20, updatedUserFromDb.get().getAge());
    }
}