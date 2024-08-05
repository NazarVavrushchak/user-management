package com.nazar.usermanagement.controller;

import com.nazar.usermanagement.entity.Event;
import com.nazar.usermanagement.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/notes/{noteId}/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PutMapping
    public void saveEventToNoteById(@PathVariable Long noteId, @RequestBody Event event) {
        eventService.saveEventToNoteById(noteId, event);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Optional<Event>> changeEventData(@PathVariable Long eventId, @RequestBody Event updatedEvent) {
        return ResponseEntity.ok(eventService.changeEventData(eventId, updatedEvent));
    }

    @PostMapping("/notifications")
    public void sendNotifications() {
        eventService.sendNotifications();
    }
}