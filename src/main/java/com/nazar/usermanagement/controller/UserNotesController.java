package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.entity.Note;
import com.nazar.usermanagement.service.UserNotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping
    public void deleteNoteById(@PathVariable Long userId){
        userNotesService.deleteNoteById(userId);
    }

    @GetMapping
    public ResponseEntity<List<Note>> getUserNotes(@PathVariable Long userId){
        List<Note> notes = userNotesService.getUsersNote(userId);
        return ResponseEntity.ok(notes);
    }
}