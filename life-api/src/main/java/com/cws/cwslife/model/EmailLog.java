package com.cws.cwslife.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
public class EmailLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "recipient_email")
    private String recipientEmail;
    
    private String subject;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    private String status;
    
    @Column(name = "sent_date")
    private LocalDateTime sentDate;
    
    // Constructors
    public EmailLog() {}
    
    public EmailLog(String recipientEmail, String subject, String message, String status, LocalDateTime sentDate) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.sentDate = sentDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }
}
