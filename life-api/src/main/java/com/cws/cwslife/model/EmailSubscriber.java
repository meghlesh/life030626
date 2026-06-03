package com.cws.cwslife.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_subscribers")
public class EmailSubscriber {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "subscribed_date")
    private LocalDateTime subscribedDate;
    
    // Constructors
    public EmailSubscriber() {}
    
    public EmailSubscriber(String email, LocalDateTime subscribedDate) {
        this.email = email;
        this.subscribedDate = subscribedDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getSubscribedDate() { return subscribedDate; }
    public void setSubscribedDate(LocalDateTime subscribedDate) { this.subscribedDate = subscribedDate; }
}