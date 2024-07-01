package com.nazar.usermanagement.repository;

import com.nazar.usermanagement.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    void deleteById(Long id);
}
