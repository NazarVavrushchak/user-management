package com.nazar.usermanagement.service;

import com.nazar.usermanagement.entity.Note;
import com.nazar.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserNotesService {
    private final UserRepository userRepository;

    public UserNotesService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNote(Long userId, Note note) {
        var user = userRepository.findById(userId)
                .orElseThrow();
        user.addNote(note);
    }
}