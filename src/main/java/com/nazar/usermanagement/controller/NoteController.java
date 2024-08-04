package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.entity.Note;
import com.nazar.usermanagement.entity.User;
import com.nazar.usermanagement.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public void addNote(@PathVariable Long userId, @RequestBody Note note) {
        noteService.addNote(userId, note);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNoteById(@PathVariable Long noteId) {
        noteService.deleteNoteById(noteId);
    }

    @GetMapping
    public List<Note> getUserNotes(@PathVariable Long userId) {
        return noteService.getUsersNote(userId);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Note> findById(@PathVariable Long noteId) {
        Optional<Note> note = noteService.findById(noteId);
        return note.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/search")
    public List<Note> findByTitle(@PathVariable Long userId, @RequestParam String title) {
        return noteService.findByTitle(userId, title);
    }

    @GetMapping("/groups/{groupId}")
    public List<Note> getNotesByGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return noteService.getNotesByGroup(groupId, userId);
    }

    @PostMapping("/groups/{groupId}/add/{noteId}")
    public void addNoteToGroup(@PathVariable Long groupId, @PathVariable Long noteId) {
        noteService.addNoteToGroup(groupId, noteId);
    }

    @PutMapping("/{noteId}")
    public Note changeNoteInfo(@PathVariable Long noteId, @RequestParam(required = false) String newBody
            , @RequestParam(required = false) String newTitle) {
        return noteService.changeNoteInfo(noteId, newBody, newTitle);
    }

    @PostMapping("/{noteWithFile}")
    public void addNoteWithFile(@PathVariable Long userId, @RequestPart("note") Note note
            , @RequestPart("file") MultipartFile file) {
        noteService.addNoteWithFile(userId, note, file);
    }

    @PostMapping("/{noteWithPhoto}")
    public void addNoteWithPhoto(@PathVariable Long userId, @RequestPart("note") Note note
            , @RequestPart("photo") MultipartFile photo) {
        noteService.addNoteWithPhoto(userId, note, photo);
    }

    @GetMapping("/byAdditionOrUpdate")
    public List<Note> getNotesByAdditionOrUpdate(@PathVariable Long userId) {
        return noteService.getNotesByAdditionOrUpdate(userId);
    }

    @GetMapping("/sortByName")
    public List<Note> sortByName(@PathVariable Long userId) {
        return noteService.sortByName(userId);
    }

    @PostMapping("/template")
    public ResponseEntity<Note> createTemplateForNote(@PathVariable Long userId, @RequestBody Map<String, String> noteData) {
        String title = noteData.get("title");
        String body = noteData.get("body");
        Note templateNote = noteService.createTemplateForNote(userId, title, body);
        return ResponseEntity.ok(templateNote);
    }

    @PostMapping("/from-template/{templateNoteId}")
    public ResponseEntity<Note> createNoteFromTemplate(@PathVariable Long userId, @PathVariable Long templateNoteId
            , @RequestBody Map<String, String> placeholders) {
        Note newNote = noteService.createNoteFromTemplate(userId, templateNoteId, placeholders);
        return ResponseEntity.ok(newNote);
    }

    @PostMapping("/createWithMembers")
    public Note createNoteWithOtherMembers(@RequestParam User ownerId
            , @RequestParam String title
            , @RequestParam String body
            , @RequestParam List<Long> memberIds
            , @RequestParam String access) {
        return noteService.createNoteWithOtherMembers(ownerId, title, body, memberIds, access);
    }

    @PostMapping("/addUserToNote")
    public void addUserToNote(@PathVariable Long userId, @PathVariable Long noteId) {
        noteService.addUserToNote(userId, noteId);
    }

    @PostMapping("/getSharedNotes")
    public List<Note> getSharedNotes(@PathVariable Long userId) {
        return noteService.getSharedNotes(userId);
    }
}