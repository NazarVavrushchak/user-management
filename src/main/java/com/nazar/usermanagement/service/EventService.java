package com.nazar.usermanagement.service;

import com.nazar.usermanagement.entity.Event;
import com.nazar.usermanagement.entity.Note;
import com.nazar.usermanagement.repository.EventRepository;
import com.nazar.usermanagement.repository.NoteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {
    private final EventRepository eventRepository;

    private final NoteRepository noteRepository;

    private final FcmService fcmService;

    public EventService(EventRepository eventRepository, NoteRepository noteRepository, FcmService fcmService) {
        this.eventRepository = eventRepository;
        this.noteRepository = noteRepository;
        this.fcmService = fcmService;
    }

    public void saveEventToNoteById(Long noteId, Event event) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        event.setUser(note.getUser());
        Event savedEvent = eventRepository.save(event);
        note.getEvents().add(savedEvent);
        noteRepository.save(note);
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    public Optional<Event> changeEventData(Long eventId, Event updatedEvent) {
        return eventRepository.findById(eventId)
                .map(event -> {
                    event.setTaskTime(updatedEvent.getTaskTime());
                    event.setTitle(updatedEvent.getTitle());
                    event.setTaskDescription(updatedEvent.getTaskDescription());
                    event.setTimeBeforeNotification(updatedEvent.getTimeBeforeNotification());
                    return eventRepository.save(event);
                });
    }

    @Scheduled(fixedRate = 60000)
    public void sendNotifications() {
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {
            Date now = new Date();
            Date notificationTime = new Date(event.getTaskTime().getTime() - event.getTimeBeforeNotification() * 60 * 1000);
            if (now.after(notificationTime) && now.before(event.getTaskTime())) {
                sendPushNotification(event);
            }
        }
    }

    private void sendPushNotification(Event event) {
        String token = event.getUser().getFcmToken();
        String title = "Reminder: " + event.getTitle();
        String body = "This is a reminder for your event: " + event.getTitle() + " starting at " + event.getTaskTime();

        fcmService.sendNotification(token, title, body);
    }
}