package com.nazar.usermanagement.repository;

import com.nazar.usermanagement.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    void deleteById(Long id);
    List<Note> findByTitle(String title);
    List<Note> findByUserIdAndGroupId(Long userId, Long groupId);
    List<Note> findByUserIdOrderByTitleAsc(Long userId);
    List<Note> findByMembersId(Long membersId);
    List<Note> findByUserIdOrderByModifiedAtDesc(Long userId);
}