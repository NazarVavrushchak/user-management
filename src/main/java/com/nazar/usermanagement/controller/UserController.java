package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.DTO.UserDTO;
import com.nazar.usermanagement.DTO.UserLoginDTO;
import com.nazar.usermanagement.DTO.UserRegistrationDTO;
import com.nazar.usermanagement.entity.Note;
import com.nazar.usermanagement.entity.User;
import com.nazar.usermanagement.service.UserNotesService;
import com.nazar.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserNotesService userNotesService;

    public UserController(UserService userService, UserNotesService userNotesService) {
        this.userService = userService;
        this.userNotesService = userNotesService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            User registeredUser = userService.registerUser(userRegistrationDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            User authenticatedUser = userService.authenticateUser(userLoginDTO);
            List<Note> notes = userNotesService.getUsersNote(authenticatedUser.getId());
            return ResponseEntity.ok(authenticatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping
    public void createUser(@RequestBody UserDTO userDTO) {
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