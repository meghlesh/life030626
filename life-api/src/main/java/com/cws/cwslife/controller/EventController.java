package com.cws.cwslife.controller;

import com.cws.cwslife.model.Event;
import com.cws.cwslife.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    // ── GET ALL EVENTS ──
    // GET /api/events/all
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    // ── GET UPCOMING EVENTS ──
    // GET /api/events/upcoming
    // Public page upcoming events
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingEvents() {
        return ResponseEntity.ok(
            eventRepository.findByStatus("UPCOMING")
        );
    }

    // ── GET PAST EVENTS ──
    // GET /api/events/past
    // Public page past events
    @GetMapping("/past")
    public ResponseEntity<?> getPastEvents() {
        return ResponseEntity.ok(
            eventRepository.findByStatus("PAST")
        );
    }

    // ── GET COUNT ──
    // GET /api/events/count
    // Dashboard stats card 
    @GetMapping("/count")
    public ResponseEntity<?> getCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("total",    eventRepository.count());
        response.put("upcoming", eventRepository.countByStatus("UPCOMING"));
        response.put("past",     eventRepository.countByStatus("PAST"));
        return ResponseEntity.ok(response);
    }

    // ── GET ONE EVENT ──
    // GET /api/events/{id}
    // detail one event
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            return ResponseEntity.ok(event.get());
        }
        Map<String, String> error = new HashMap<>();
        error.put("message", "Event not found");
        return ResponseEntity.status(404).body(error);
    }

    // ── ADD NEW EVENT ──
    // POST /api/events/add
    //Add new event from admin panel
    @PostMapping("/add")
    public ResponseEntity<?> addEvent(
            @RequestBody Map<String, String> request) {

        String title       = request.get("title");
        String category    = request.get("category");
        String eventDate   = request.get("eventDate");
        String description = request.get("description");
        String imageUrl    = request.get("imageUrl");
        String status      = request.get("status");

        // Required fields check
        if (title == null || title.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Event title is required");
            return ResponseEntity.badRequest().body(error);
        }

        if (eventDate == null || eventDate.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Event date is required");
            return ResponseEntity.badRequest().body(error);
        }

        // Event object
        Event event = new Event();
        event.setTitle(title);
        event.setCategory(category);
        event.setEventDate(LocalDate.parse(eventDate));
        event.setDescription(description);
        event.setImageUrl(imageUrl);
        event.setStatus(status != null ? status : "UPCOMING");

        // Saved in Database 
        eventRepository.save(event);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Event added successfully!");
        return ResponseEntity.ok(response);
    }

    // ── UPDATE EVENT ──
    // PUT /api/events/update/{id}
    // Update event from admin panel
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        Optional<Event> eventOpt = eventRepository.findById(id);

        if (!eventOpt.isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Event not found");
            return ResponseEntity.status(404).body(error);
        }

        Event event = eventOpt.get();

        // Update fields
        if (request.get("title") != null)
            event.setTitle(request.get("title"));
        if (request.get("category") != null)
            event.setCategory(request.get("category"));
        if (request.get("eventDate") != null)
            event.setEventDate(LocalDate.parse(request.get("eventDate")));
        if (request.get("description") != null)
            event.setDescription(request.get("description"));
        if (request.get("imageUrl") != null)
            event.setImageUrl(request.get("imageUrl"));
        if (request.get("status") != null)
            event.setStatus(request.get("status"));

        // Save updated event
        // IMPORTANT
        event.setUpdatedAt(LocalDateTime.now());

        // Save updated event
        eventRepository.save(event);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Event updated successfully!");
        return ResponseEntity.ok(response);
    }

		 // ── DELETE EVENT ──
		 // DELETE /api/events/delete/{id}
		 // Delete event from admin panel
		 @DeleteMapping("/delete/{id}")
		 public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
		
		     Optional<Event> eventOpt = eventRepository.findById(id);
		
		     if (!eventOpt.isPresent()) {
		         Map<String, String> error = new HashMap<>();
		         error.put("message", "Event not found");
		         return ResponseEntity.status(404).body(error);
		     }
		
		     // delete from database permentally 
		     eventRepository.deleteById(id);
		
		     Map<String, String> response = new HashMap<>();
		     response.put("message", "Event deleted successfully!");
		     return ResponseEntity.ok(response);
		 }
    
		 // ── GET MEDIA COUNT ──
		 // GET /api/events/media-count
		 // uploads/events/ folder 
		 @GetMapping("/media-count")
		 public ResponseEntity<?> getMediaCount() {
		     try {
		         java.io.File folder = new java.io.File("uploads/events");
		         
		         long count = 0;
		         if (folder.exists() && folder.isDirectory()) {
		             count = java.util.Arrays.stream(folder.listFiles())
		                 .filter(java.io.File::isFile)
		                 .count();
		         }
		
		         Map<String, Long> response = new HashMap<>();
		         response.put("mediaCount", count);
		         return ResponseEntity.ok(response);
		
		     } catch (Exception e) {
		         Map<String, Long> response = new HashMap<>();
		         response.put("mediaCount", 0L);
		         return ResponseEntity.ok(response);
		     }
		 }
}