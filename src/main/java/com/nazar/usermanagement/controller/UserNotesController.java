package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.entity.Note;
import com.nazar.usermanagement.service.UserNotesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/notes")
public class UserNotesController {
    private final UserNotesService userNotesService;

    public UserNotesController(UserNotesService userNotesService) {
        this.userNotesService = userNotesService;
    }

    @PostMapping
    public void addNote(@PathVariable Long userId, @RequestBody Note note) {
        //save note related to a person by id
        userNotesService.addNote(userId, note);
    }
}