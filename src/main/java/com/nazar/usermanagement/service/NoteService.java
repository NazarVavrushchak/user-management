package com.nazar.usermanagement.service;

import com.nazar.usermanagement.entity.Group;
import com.nazar.usermanagement.entity.Note;
import com.nazar.usermanagement.entity.User;
import com.nazar.usermanagement.repository.GroupRepository;
import com.nazar.usermanagement.repository.NoteRepository;
import com.nazar.usermanagement.repository.UserRepository;
import com.nazar.usermanagement.utils.FileValidator;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class NoteService {
    private final UserRepository userRepository;

    private final NoteRepository noteRepository;

    private final GroupRepository groupRepository;

    public NoteService(UserRepository userRepository, NoteRepository noteRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.groupRepository = groupRepository;
    }

    public void addNote(Long userId, Note note) {
        userRepository.findById(userId)
                .ifPresent(user -> user.addNote(note));
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    public List<Note> findByTitle(Long userId, String title) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return noteRepository.findByTitle(title);
    }

    public void deleteNoteById(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    public List<Note> getUsersNote(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow();
        return user.getNotes();
    }

    public void addNoteToGroup(Long groupId, Long noteId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        note.setGroup(group);
        group.getNotes().add(note);
    }

    public List<Note> getNotesByGroup(Long userId, Long groupId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        return noteRepository.findByUserIdAndGroupId(userId, groupId);
    }

    public Note changeNoteInfo(Long noteId, String newBody, String newTitle) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        if (newTitle != null && !newTitle.equals(note.getTitle())) {
            note.setTitle(newTitle);
        }

        if (newBody != null && !newBody.equals(note.getBody())) {
            note.setBody(newBody);
        }

        return noteRepository.save(note);
    }

    @SneakyThrows
    public void addNoteWithFile(Long userId, Note note, MultipartFile file) {
        if (!FileValidator.isValidFileType(file)) {
            throw new IllegalArgumentException("Invalid file type");
        }
        if (!FileValidator.isValidFileSize(file)) {
            throw new IllegalArgumentException("Size of  file is too big");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        note.setUser(user);
        note.setFileContext(file.getBytes());
        note.setFileName(file.getOriginalFilename());
        note.setFileType(file.getContentType());
        noteRepository.save(note);
    }

    @SneakyThrows
    public void addNoteWithPhoto(Long userId, Note note, MultipartFile photo) {
        if (!FileValidator.isValidPhotoType(photo)) {
            throw new IllegalArgumentException("Invalid photo type");
        }
        if (!FileValidator.isValidFileSize(photo)) {
            throw new IllegalArgumentException("Size of  file is too big");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        note.setUser(user);
        note.setPhoto(photo.getBytes());
        noteRepository.save(note);

    }

    public List<Note> getNotesByAdditionOrUpdate(Long userId) {
        return noteRepository.findByUserIdOrderByModifiedAtDesc(userId);
    }

    public List<Note> sortByName(Long userId) {
        return noteRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public Note createTemplateForNote(Long userId, String title, String body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Note templateNote = new Note();
        templateNote.setTitle(title);
        templateNote.setBody(body);
        templateNote.setUser(user);
        templateNote.setTemplate(true);

        return noteRepository.save(templateNote);
    }

    public Note createNoteFromTemplate(Long userId, Long templateNoteId, Map<String, String> placeholders) {
        Note templateNote = noteRepository.findById(templateNoteId)
                .orElseThrow(() -> new IllegalArgumentException("Template note not found"));

        if (!templateNote.isTemplate()) {
            throw new IllegalArgumentException("Note is not a template");
        }

        Note newNote = new Note();
        newNote.setTitle(replacePlaceholders(templateNote.getTitle(), placeholders));
        newNote.setBody(replacePlaceholders(templateNote.getBody(), placeholders));
        newNote.setUser(templateNote.getUser());

        return noteRepository.save(newNote);
    }

    private String replacePlaceholders(String content, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return content;
    }

    public Note createNoteWithOtherMembers(User owner, String title, String body, List<Long> memberIds, String access) {
        Note note = new Note();
        note.setTitle(title);
        note.setBody(body);
        note.setOwner(owner);
        note.setAccess(access);
        note.setUser(owner); // Встановлюємо власника як автора

        List<User> members = new ArrayList<>();
        for (Long memberId : memberIds) {
            User member = userRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + memberId));
            members.add(member);
        }
        note.setMembers(members);

        return noteRepository.save(note);
    }

    public void addUserToNote(Long userId , Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with ID: " + noteId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        note.getMembers().add(user);
        noteRepository.save(note);
    }

    public List<Note> getSharedNotes(Long userId) {
        return noteRepository.findByMembersId(userId);
    }
}