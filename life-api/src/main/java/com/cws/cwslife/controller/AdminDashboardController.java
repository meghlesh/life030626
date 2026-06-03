package com.cws.cwslife.controller;

import com.cws.cwslife.model.AdminDashboard;
import com.cws.cwslife.model.Event;
import com.cws.cwslife.model.RecentActivity;
import com.cws.cwslife.repository.EventRepository;
import com.cws.cwslife.repository.GalleryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/admin/dashboard")
@CrossOrigin("*")
public class AdminDashboardController {

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/media-count")
    public AdminDashboard getMediaUploadsCount() {

        long mediaCount = galleryRepository.count();

        return new AdminDashboard(mediaCount);
    }

    // =============================
    // Recent Activity API
    // =============================
    @GetMapping("/recent-activities")
    public List<RecentActivity> getRecentActivities() {

        List<Event> events = eventRepository.findAll();

        List<RecentActivity> activities = new ArrayList<>();

        // latest activity first
        events.sort((a, b) -> {

            LocalDateTime timeA =
                    a.getUpdatedAt() != null ? a.getUpdatedAt() : a.getCreatedAt();

            LocalDateTime timeB =
                    b.getUpdatedAt() != null ? b.getUpdatedAt() : b.getCreatedAt();

            return timeB.compareTo(timeA);
        });

        for (Event event : events) {

            String message;
            LocalDateTime activityTime;

            // Removed event
            if ("PAST".equalsIgnoreCase(event.getStatus())) {

                message = "Event Removed";
                activityTime = event.getUpdatedAt() != null
                        ? event.getUpdatedAt()
                        : event.getCreatedAt();

            }

            // Updated event
            else if (event.getUpdatedAt() != null &&
                    !event.getUpdatedAt().equals(event.getCreatedAt())) {

                message = "Event Updated";
                activityTime = event.getUpdatedAt();

            }

            // Added event
            else {

                message = "New Event Added";
                activityTime = event.getCreatedAt();
            }

            String timeAgo = formatTimeAgo(activityTime);

            activities.add(new RecentActivity(
                    message,
                    timeAgo,
                    "Admin"
            ));
        }

        return activities;
    }
    // =============================
    private String formatTimeAgo(LocalDateTime createdAt) {

        Duration duration = Duration.between(createdAt, LocalDateTime.now());

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 60) {
            return minutes + " mins ago";
        }

        if (hours < 24) {
            return hours + " hours ago";
        }

        return days + " days ago";
    }
}