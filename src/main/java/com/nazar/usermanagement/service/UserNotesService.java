package com.nazar.usermanagement.service;

import com.nazar.usermanagement.entity.Note;
import com.nazar.usermanagement.repository.NoteRepository;
import com.nazar.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserNotesService {
    private final UserRepository userRepository;

    private final NoteRepository noteRepository;

    public UserNotesService(UserRepository userRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public void addNote(Long userId, Note note) {
        var user = userRepository.findById(userId)
                .orElseThrow();
        user.addNote(note);
    }

    public void deleteNoteById(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    public List<Note> getUsersNote(Long userId){
        var user = userRepository.findById(userId)
                .orElseThrow();
        return user.getNotes();
    }
}