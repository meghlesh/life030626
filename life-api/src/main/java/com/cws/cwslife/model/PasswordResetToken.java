package com.cws.cwslife.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String token;
    private LocalDateTime expiresAt;
    private Boolean isUsed = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters
    public Long getId()                  { return id; }
    public String getEmail()             { return email; }
    public String getToken()             { return token; }
    public LocalDateTime getExpiresAt()  { return expiresAt; }
    public Boolean getIsUsed()           { return isUsed; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    // Setters
    public void setEmail(String v)              { this.email = v; }
    public void setToken(String v)              { this.token = v; }
    public void setExpiresAt(LocalDateTime v)   { this.expiresAt = v; }
    public void setIsUsed(Boolean v)            { this.isUsed = v; }
}