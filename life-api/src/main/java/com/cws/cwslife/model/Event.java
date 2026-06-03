package com.cws.cwslife.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private LocalDate eventDate;
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    // UPCOMING or PAST
    private String status = "UPCOMING";

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters
    public Long getId()                  { return id; }
    public String getTitle()             { return title; }
    public String getCategory()          { return category; }
    public LocalDate getEventDate()      { return eventDate; }
    public String getDescription()       { return description; }
    public String getImageUrl()          { return imageUrl; }
    public String getStatus()            { return status; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  {return updatedAt;}
    // Setters
    public void setTitle(String v)           { this.title = v; }
    public void setCategory(String v)        { this.category = v; }
    public void setEventDate(LocalDate v)    { this.eventDate = v; }
    public void setDescription(String v)     { this.description = v; }
    public void setImageUrl(String v)        { this.imageUrl = v; }
    public void setStatus(String v)          { this.status = v; }
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
    
    
}