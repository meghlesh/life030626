package com.cws.cwslife.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_inquiries")
public class ContactInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters
    public Long getId()                 { return id; }
    public String getFirstName()        { return firstName; }
    public String getLastName()         { return lastName; }
    public String getEmail()            { return email; }
    public String getPhone()            { return phone; }
    public String getMessage()          { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setFirstName(String v)  { this.firstName = v; }
    public void setLastName(String v)   { this.lastName = v; }
    public void setEmail(String v)      { this.email = v; }
    public void setPhone(String v)      { this.phone = v; }
    public void setMessage(String v)    { this.message = v; }
}