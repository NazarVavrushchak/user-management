package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.DTO.UserDTO;
import org.springframework.web.bind.annotation.*;
import com.nazar.usermanagement.service.UserService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")//path
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void createUser(@RequestBody UserDTO userDTO) {
        //save user to the db
        userService.createUser(userDTO);
    }

    @GetMapping("/{id}")
    public Optional<UserDTO> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Long id) {
        userService.removeUser(id);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public Optional<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }
}